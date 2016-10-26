package instruction.instuctions;

import instruction.BranchInstruction;
import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class BPL extends BranchInstruction {

    public BPL(Addr offset) {
        super(new Word(0b1000_0000_00000000), offset);
    }

    @Override
    public void apply(MemoryModel memory) {
        if (!memory.flags.N)
            memory.setProgramCounter(new Addr(memory.getProgramCounter().value + offset.toShort()));
    }
}
