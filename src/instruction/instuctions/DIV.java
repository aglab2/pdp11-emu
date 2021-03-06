package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.ArgumentType;
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

    public DIV(RegAddr reg, RegMode srcMode, RegAddr srcAddr, @Nullable Word index) {
        super(new Word(0b0_111001_000_000000), reg, ArgumentType.READWRITE, srcMode, srcAddr, ArgumentType.READWRITE, index, 10);
    }

    @Override
    public void execute(MemoryModel memory) {
        int num1 = memory.registers.fetch(reg.offset).value;
        int num2 = !reg.isLast() ? memory.registers.fetch(reg.offset.inc()).value : 0;

        BusAddr src = sodMode.apply(memory, sodAddr, index);
        int srcValue = src.fetch(memory).toSigned();
        int regValue = num1 | num2 << 16;

        int div = regValue / srcValue;
        int mod = regValue % srcValue;

        memory.registers.load(reg.offset, new Word(div));
        if (!reg.isLast())
            memory.registers.load(reg.offset.inc(), new Word(mod));

        memory.flags.setZN(div);
        memory.flags.V.set(srcValue == 0 || regValue > Math.abs(srcValue));
        memory.flags.C.set(srcValue == 0);
    }
}
