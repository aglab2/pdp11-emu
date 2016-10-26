package instruction.instuctions;

import instruction.BranchInstruction;
import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BNE extends BranchInstruction {

    public BNE(Addr offset) {
        super(new Word(0b0000_0010_00000000), offset);
    }

    @Override
    public void apply(MemoryModel memory) {
        if (!memory.flags.Z)
            memory.setProgramCounter(new Addr(memory.getProgramCounter().value + offset.toShort()));
    }
}
