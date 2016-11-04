package instruction.instuctions;

import instruction.BranchInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BMI extends BranchInstruction {

    public BMI(Offset offset) {
        super(new Word(0b1000_0001_00000000), offset);
    }

    @Override
    public void execute(MemoryModel memory) {
        if (memory.flags.N.get())
            memory.registers.add(RegAddr.PC.offset, 2 * offset.toSignedByte());
    }
}
