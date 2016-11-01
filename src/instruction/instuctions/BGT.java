package instruction.instuctions;

import instruction.BranchInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BGT extends BranchInstruction {

    public BGT(Offset offset) {
        super(new Word(0b0000_0110_00000000), offset);
    }

    @Override
    public void apply(MemoryModel memory) {
        if (memory.flags.Z == (memory.flags.N ^ memory.flags.V))
            memory.registers.add(RegAddr.PC.offset, 2 * offset.toSigned());
    }
}
