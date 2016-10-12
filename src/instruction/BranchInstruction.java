package instruction;

import instruction.primitives.InstructionDiapason;
import instruction.primitives.RegIndex;
import instruction.primitives.RegMode;
import memory.primitives.Addr;
import memory.primitives.Word;

public class BranchInstruction extends Instruction {
    public final Addr offset;

    public BranchInstruction(Word code, Addr offset) {
        super(code, 8);
        this.offset = offset;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + offset.value);
    }
}
