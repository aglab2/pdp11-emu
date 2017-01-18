package instruction.instuctions;

import com.sun.istack.internal.Nullable;
import instruction.RegisterInstruction;
import instruction.RegisterMemoryInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */

public class RTS extends RegisterInstruction {

    public RTS(RegAddr reg) {
        super(new Word(0b0_000000_010000_000), reg, 2);
    }

    @Override
    public void execute(MemoryModel memory) {
        Word tmp = memory.registers.fetch(reg.offset);
        memory.registers.load(RegAddr.PC.offset, tmp);

        Word sp = RegMode.AutoInc.apply(memory, RegAddr.SP, null).fetch(memory);
        memory.registers.load(reg.offset, sp);
    }
}
