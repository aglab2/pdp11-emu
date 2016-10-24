package instruction;

import memory.primitives.Addr;
import memory.primitives.Word;

public abstract class BranchInstruction extends Instruction {
    public final Addr offset;

    public BranchInstruction(Word code, Addr offset) {
        super(code, 8, null);
        this.offset = offset;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + offset.value);
    }

    @Override
    public boolean needsNextWord() {
        return false;
    }
}
