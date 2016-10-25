package instruction.instructions;

import instruction.Instruction;
import instruction.instuctions.INC;
import instruction.instuctions.MOV;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.MemoryStorage;
import memory.ReadOnlyMemory;
import memory.primitives.MemSize;
import memory.primitives.Word;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertEquals;


/**
 * Created by voddan on 26/10/16.
 */
public class InstructionsTest {
    MemoryModel memory;

    @Before
    public void initMemory() {
        memory = new MemoryModel(new MemSize(16), new MemSize(0), new MemoryStorage(new MemSize(0)));
    }

    @Test
    public void incTest() throws Exception {
        Instruction[] code = {
                new INC(RegMode.Register, RegAddr.R0, null),
                new INC(RegMode.DRegister, RegAddr.R0, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
        };

        for(Instruction i : code)
            i.apply(memory);

        assertMemory("REGs", memory.registers, new int[] {1, 3});
        assertMemory("RAM", memory.ram, new int[] {1, 2, 1});
    }

    @Test
    public void incTest2() throws Exception {
        Instruction[] code = {
                new INC(RegMode.Register, RegAddr.R0, null),
                new INC(RegMode.DRegister, RegAddr.R0, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
                new INC(RegMode.AutoInc, RegAddr.R1, null),
                new INC(RegMode.DAutoInc, RegAddr.R1, null),
        };

        for(Instruction i : code)
            i.apply(memory);

        assertMemory("REGs", memory.registers, new int[] {1, 4});
        assertMemory("RAM", memory.ram, new int[] {2, 2, 1, 0});
    }

    private void assertMemory(String name, ReadOnlyMemory memory, int[] expected) {
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
