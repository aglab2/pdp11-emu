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
public class ASH extends RegisterMemoryInstruction {

    public ASH(RegAddr reg, RegMode srcMode, RegAddr srcAddr, @Nullable Word nextWord) {
        super(new Word(0b0_111010_000_0000000), reg, srcMode, srcAddr, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr src = sodMode.apply(memory, sodAddr, nextWord);
        int num = src.fetch(memory).value;
        int data = memory.registers.fetch(reg.address).value;

        if(num >= 0)
            memory.registers.load(reg.address, new Word(data << num));
        else
            memory.registers.load(reg.address, new Word(data >> -num));
    }
}
