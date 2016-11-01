package instruction.instuctions;

import instruction.BranchInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BPL extends BranchInstruction {

    public BPL(Offset offset) {
        super(new Word(0b1000_0000_00000000), offset);
    }

    @Override
    public void apply(MemoryModel memory) {
        if (!memory.flags.N)
            memory.registers.add(RegAddr.PC.offset, 2 * offset.toSignedByte());
    }
}
