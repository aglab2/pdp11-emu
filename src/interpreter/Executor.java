package interpreter;

import instruction.Instruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Word;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by voddan on 01/11/16.
 */
public class Executor {
    public final MemoryModel memory;
    private final Parser parser = new Parser();

    public long sleepMillisDelay = 0;
    private volatile boolean play = false;

    public Executor(MemoryModel memory) {
        this.memory = memory;
    }

    public boolean executeStep() {
        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        if(pc == Word.NaN) return false;

        Word word0 = memory.bus.fetch(pc.value);
        if(word0 == null) return false;

        Word word1 = memory.bus.fetch(pc.value + 1);
        Word word2 = memory.bus.fetch(pc.value + 2);

        Instruction instruction = parser.parseInstruction(word0, word1, word2);

        memory.registers.add(RegAddr.PC.offset, 2 * (1 + instruction.indexСapacity()));

        instruction.apply(memory);
        return true;
    }

    public void startExecution() {
        play = true;
        while (executeStep())
            try {
                if(!play) break;
                Thread.sleep(sleepMillisDelay);
                if(!play) break;
            } catch (InterruptedException e) { }
    }

    public void stopExecution() {
        play = false;
    }
}
