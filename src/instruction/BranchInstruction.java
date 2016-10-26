package instruction;

import memory.primitives.Addr;
import memory.primitives.Word;

public abstract class BranchInstruction extends Instruction {
    public final Addr offset;

    public BranchInstruction(Word code, Addr offset) {
        super(code, 8);
        this.offset = offset;
    }

    @Override
    public Word getCode() {
        return new Word(range.start.value | offset.value);
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
