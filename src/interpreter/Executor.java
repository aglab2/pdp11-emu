package interpreter;

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

/**
 * Created by voddan on 01/11/16.
 */
public class Executor {
    public final MemoryModel memory;
    private final Parser parser;

    public Executor(MemoryModel memory, Parser parser) {
        this.memory = memory;
        this.parser = parser;
    }

    public boolean executeStep() {
        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        if(pc == Word.NaN) return false;

        Word word0 = memory.bus.fetch(pc.value);
        if(word0 == null) return false;

        Word word1 = memory.bus.fetch(pc.value + 2);
        Word word2 = memory.bus.fetch(pc.value + 4);

        Instruction instruction = parser.parseInstruction(word0, word1, word2);

        memory.registers.add(RegAddr.PC.offset, 2 * (1 + instruction.indexСapacity()));
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
    }

    public final void interrupt(int interruptCode, Word errorCode) {
        if (!stepService.isRunning() && !executeService.isRunning())
            return;
        this.cancelAll();

        System.out.println("Interrupt " + interruptCode + ": " + errorCode);

        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        Word sp = memory.registers.fetch(RegAddr.SP.offset);
        sp.dec2();
        memory.bus.load(sp.value, pc);
        sp.dec2();
        memory.bus.load(sp.value, errorCode);

        Word interruptHandlerPtr = memory.bus.fetch(memory.interruptOffset + interruptCode * 2);
        memory.registers.load(RegAddr.PC.offset, interruptHandlerPtr);
        memory.registers.load(RegAddr.SP.offset, sp);

        executeService.restart(); //TODO: This does not work in step service!

        Word word1 = memory.bus.fetch(pc.value + 2);
        Word word2 = memory.bus.fetch(pc.value + 4);
    }
}
