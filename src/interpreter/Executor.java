package interpreter;

import bus.BusAddr;
import com.sun.javafx.application.PlatformImpl;
import instruction.Instruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.LinearPipeline;
import pipeline.ParallelPipeline;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by voddan on 01/11/16.
 */
public class Executor {
    public final MemoryModel memory;
    private final Parser parser;

    public final Set<Integer> breakpointsAddresses = new HashSet<>();
    private boolean isFirstStep = true;

    private LinearPipeline linearPipeline = new LinearPipeline();
    private ParallelPipeline parallelPipeline = new ParallelPipeline();

    public Executor(MemoryModel memory, Parser parser) {
        this.memory = memory;
        this.parser = parser;
    }

    public void clearCache() {
        parser.clearCache();
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

        Instruction instruction = parser.parseInstructionCached(pc.value, word0, word1, word2);

        memory.registers.add(RegAddr.PC.offset, 2 * (1 + instruction.indexСapacity()));
        linearPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        parallelPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        //if (parallelPipeline.clock != 0) System.out.format("%.4f\n", (double) linearPipeline.clock / (double) parallelPipeline.clock);

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
            final int stepsInBunch = 200;

            return new Task<Void>() {
                @Override
                protected Void call() {
                    while (true) {
                        final boolean[] keepGoing = new boolean[1];

                        PlatformImpl.runAndWait(() -> {
                            for (int i = 0; i < stepsInBunch; i++) {
                                boolean res = executeStep();
                                if(!res) {
                                    keepGoing[0] = res;
                                    return;
                                }
                            }
                            keepGoing[0] = true;
                        });

                        if(!keepGoing[0] || isCancelled())
                            return null;
                    }
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
        boolean stepRunning = stepService.isRunning();
        boolean execRunning = executeService.isRunning();

        if (!stepRunning && !execRunning)
            return;

        Word interruptHandlerPtr = memory.bus.fetch(memory.interruptOffset + interruptCode * 2);
        if (interruptHandlerPtr.value == 0) return;

        try {
            if (stepRunning) stepService.wait();
            if (execRunning) executeService.wait();
        }catch(Exception e){ }
        cancelAll();

        System.out.println("Interrupt " + interruptCode + ": " + errorCode);

        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        Word ps = memory.bus.fetch(0xFFFE);

        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, ps);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, pc);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, errorCode);

        memory.registers.load(RegAddr.PC.offset, interruptHandlerPtr);

        if (execRunning) executeService.restart();
        if (stepRunning) stepService.restart();
    }
}
