package instruction.instuctions;

import instruction.BranchInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BR extends BranchInstruction {


    public BR(Offset offset) {
        super(new Word(0b0000_0001_00000000), offset, 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        memory.registers.add(RegAddr.PC.offset, 2 * offset.toSignedByte());
    }
}
