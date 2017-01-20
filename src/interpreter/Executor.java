package interpreter;

import bus.BusAddr;
import com.sun.javafx.application.PlatformImpl;
import instruction.Data;
import instruction.Instruction;
import instruction.instuctions.MOV;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.LinearPipeline;
import pipeline.ParallelPipeline;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by voddan on 01/11/16.
 */
public class Executor {
    public final MemoryModel memory;
    private final Parser parser;

    public final Set<Integer> breakpointsAddresses;
    public boolean isFirstStep;

    private LinearPipeline linearPipeline;
    private ParallelPipeline parallelPipeline;

    public Executor(MemoryModel memory, Parser parser) {
        this.memory = memory;
        this.parser = parser;
        this.parallelPipeline = new ParallelPipeline();
        this.linearPipeline = new LinearPipeline();

        this.breakpointsAddresses = new HashSet<>();
        this.isFirstStep = true;
    }

    public boolean executeStep() {
        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        if(pc == Word.NaN) {
            System.out.format("Finished with: %d parallel, %d linear\n", parallelPipeline.clock, linearPipeline.clock);
            parallelPipeline.finish();
            linearPipeline.finish();
            System.out.format("Finished with: %d parallel, %d linear\n", parallelPipeline.clock, linearPipeline.clock);
            return false;
        }

        if (breakpointsAddresses.contains(pc.value) && !isFirstStep){
            memory.registers.load(RegAddr.PC.offset, Word.NaN);
            memory.registers.load(RegAddr.PC.offset, pc);
            isFirstStep = true;
            return false;
        }

        isFirstStep = false;

        Word word0 = memory.bus.fetch(pc.value);
        if(word0 == null) return false;

        Word word1 = memory.bus.fetch(pc.value + 2);
        Word word2 = memory.bus.fetch(pc.value + 4);

        Instruction instruction = parser.parseInstruction(word0, word1, word2);

        memory.registers.add(RegAddr.PC.offset, 2 * (1 + instruction.indexСapacity()));
        linearPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        parallelPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        if (parallelPipeline.clock != 0) System.out.format("%.4f\n", (double) linearPipeline.clock / (double) parallelPipeline.clock);

        instruction.execute(memory);

        return true;
    }

    public final Service<Boolean> stepService = new Service<Boolean>() {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    final boolean[] result = new boolean[1];
                    PlatformImpl.runAndWait(() -> result[0] = executeStep());
                    return result[0];
                }
            };
        }
    };


    public final Service<Void> executeService = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            boolean graceEnd = false;

            return new Task<Void>() {
                @Override
                protected Void call() {
                    while (true) {
                        final boolean[] result = new boolean[1];
                        PlatformImpl.runAndWait(() -> result[0] = executeStep());
                        if(!result[0] || isCancelled())
                            break;
                    }

                    return null;
                }
            };
        }
    };

    public final void cancelAll() {
        stepService.cancel();
        executeService.cancel();
        linearPipeline = new LinearPipeline();
        parallelPipeline = new ParallelPipeline();

        isFirstStep = true;
    }

    public final void interrupt(int interruptCode, Word errorCode) {
        if (!stepService.isRunning() && !executeService.isRunning())
            return;

        try {
            stepService.wait();
        }catch(Exception e){ }
        this.cancelAll();

        System.out.println("Interrupt " + interruptCode + ": " + errorCode);

        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        Word ps = memory.bus.fetch(0xFFFE);

        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, ps);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, pc);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, errorCode);

        Word interruptHandlerPtr = memory.bus.fetch(memory.interruptOffset + interruptCode * 2);
        memory.registers.load(RegAddr.PC.offset, interruptHandlerPtr);

        executeService.restart(); //TODO: This does not work in step service!
    }
}
