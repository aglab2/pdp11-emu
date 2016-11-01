package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.RegisterMemoryInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
/* TODO: test arithmetic */
public class ASH extends RegisterMemoryInstruction {

    public ASH(RegAddr reg, RegMode srcMode, RegAddr srcAddr, @Nullable Word index) {
        super(new Word(0b0_111010_000_000000), reg, srcMode, srcAddr, index);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr src = sodMode.apply(memory, sodAddr, index);
        int num = src.fetch(memory).value;
        int data = memory.registers.fetch(reg.offset).value;

        int res = (num >= 0) ? data << num : data >> -num;

        memory.flags.setZN(res);
        memory.flags.V = (res * memory.registers.fetch(reg.offset).value < 0);

        memory.registers.load(reg.offset, new Word(res));
    }
}
