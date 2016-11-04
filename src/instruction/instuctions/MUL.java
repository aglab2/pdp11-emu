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
public class MUL extends RegisterMemoryInstruction {

    public MUL(RegAddr reg, RegMode srcMode, RegAddr srcAddr, @Nullable Word index) {
        super(new Word(0b0_111000_000_000000), reg, srcMode, srcAddr, index);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr src = sodMode.apply(memory, sodAddr, index);
        int result = memory.registers.fetch(reg.offset).toSigned() * src.fetch(memory).toSigned();

        memory.registers.load(reg.offset, new Word(result & 0xFFFF));

        if (!reg.isLast())
            memory.registers.load(reg.offset.inc(), new Word(result & 0xFFFF0000));

        memory.flags.setZN(result);
        memory.flags.V.set(false);
        memory.flags.C.set(!(-(1 << 13) <= result && result < (1 << 13)));
    }
}
