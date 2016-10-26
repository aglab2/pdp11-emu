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
public class DIV extends RegisterMemoryInstruction {

    public DIV(RegAddr reg, RegMode srcMode, RegAddr srcAddr, @Nullable Word nextWord) {
        super(new Word(0b0_111001_000_0000000), reg, srcMode, srcAddr, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        int num1 = memory.registers.fetch(reg.address).value;

        int num2;
        if (reg.ordinal() != RegAddr.values().length - 1)  // not the last register
            num2 = memory.registers.fetch(reg.address.inc()).value;
        else
            num2 = 0;


        BusAddr src = sodMode.apply(memory, sodAddr, nextWord);
        int srcValue = src.fetch(memory).value;
        int regValue = num1 | num2 << 16;

        int div = regValue / srcValue;
        int mod = regValue % srcValue;

        memory.registers.load(reg.address, new Word(div));
        if (reg.ordinal() != RegAddr.values().length - 1) // not the last register
            memory.registers.load(reg.address.inc(), new Word(mod));

        memory.flags.setZN(div);
        memory.flags.V = (srcValue == 0 || regValue > Math.abs(srcValue));
        memory.flags.C = (srcValue == 0);
    }
}
