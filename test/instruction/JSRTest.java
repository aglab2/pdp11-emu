package instruction;

import instruction.instuctions.JSR;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import interpreter.Executor;
import interpreter.Parser;
import memory.Memory;
import memory.MemoryModel;
import memory.MemoryStorage;
import memory.RWMemory;
import memory.primitives.MemSize;
import memory.primitives.Offset;
import memory.primitives.Word;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by voddan on 04/11/16.
 */
public class JSRTest {

    @Test @Ignore
    public void official_example_from_page_4_59() throws Exception {
        MemoryModel memory = new MemoryModel(MemSize.TEN, MemSize.ZERO, new MemoryStorage(new MemSize(2)));
        Executor executor = new Executor(memory, new Parser());

        memory.registers.load(RegAddr.R5.offset, new Word(239));

        int pc = memory.registers.fetch(RegAddr.R7.offset).value;
        int n = memory.registers.fetch(RegAddr.R6.offset).value;
        int data = memory.registers.fetch(RegAddr.R5.offset).value;
        int sbr = 010;


        Word index = new Word(sbr);
        JSR instruction = new JSR(RegAddr.R5, RegMode.Index, RegAddr.PC, index);

        ((RWMemory) memory.rom).load(new Offset(0), instruction.getBinary());
        ((RWMemory) memory.rom).load(new Offset(1), index);

        assertMemory("Regs", memory.registers, new int[]{0, 0, 0, 0, 0, data, n, pc});
        assertMemory("RAM", memory.ram, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertMemory("ROM", memory.rom, new int[]{instruction.getBinary().value, sbr});

        executor.executeStep();

        assertMemory("Regs", memory.registers, new int[]{0, 0, 0, 0, 0, pc + 2, n - 2, sbr});
        assertMemory("RAM", memory.ram, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, data});
    }

    @Test @Ignore
    public void official_example_from_page_4_59_indexed() throws Exception {
        MemoryModel memory = new MemoryModel(MemSize.TEN, MemSize.ZERO, new MemoryStorage(new MemSize(2)));
        Executor executor = new Executor(memory, new Parser());

        memory.registers.load(RegAddr.R5.offset, new Word(239));

        int pc = memory.registers.fetch(RegAddr.R7.offset).value;
        int n = memory.registers.fetch(RegAddr.R6.offset).value;
        int data = memory.registers.fetch(RegAddr.R5.offset).value;
        int sbr = 010;


        Word index = new Word(sbr);
        JSR instruction = new JSR(RegAddr.R5, RegMode.Index, RegAddr.PC, index);

        ((RWMemory) memory.rom).load(new Offset(0), instruction.getBinary());
        ((RWMemory) memory.rom).load(new Offset(1), index);

        assertMemory("Regs", memory.registers, new int[]{0, 0, 0, 0, 0, data, n, pc});
        assertMemory("RAM", memory.ram, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertMemory("ROM", memory.rom, new int[]{instruction.getBinary().value, sbr});

        executor.executeStep();

        assertMemory("Regs", memory.registers, new int[]{0, 0, 0, 0, 0, pc + 4, n - 2, sbr});
        assertMemory("RAM", memory.ram, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, data});
    }

    private void assertMemory(String name, Memory memory, int[] expected) {
        List<Word> list = ((MemoryStorage) memory).dataObservableList;
        int len = Math.min(list.size(), expected.length);

        int i = 0;
        for(; i < len; i++)
            assertEquals("verifying " + name + " at index " + i,
                    expected[i], list.get(i).value);

        for(; i < list.size(); i++)
            assertEquals("verifying " + name + " tailing zeros at index " + i, 0, list.get(i).value);
    }
}