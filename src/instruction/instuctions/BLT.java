package instruction.instuctions;

import instruction.BranchInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BLT extends BranchInstruction {

    public BLT(Offset offset) {
        super(new Word(0b0000_0101_00000000), offset, 2);
    }

    @Override
    public void execute(MemoryModel memory) {
        if (memory.flags.N.get() != memory.flags.V.get())
            memory.registers.add(RegAddr.PC.offset, 2 * offset.toSignedByte());
    }
}
