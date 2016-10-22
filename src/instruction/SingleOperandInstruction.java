package instruction;

import instruction.primitives.RegIndex;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public class SingleOperandInstruction extends Instruction {
    public final RegMode dstMode;
    public final RegIndex dstIndex;

    public SingleOperandInstruction(Word code, RegMode dstMode, RegIndex dstIndex) {
        super(code, 10);
        this.dstMode = dstMode;
        this.dstIndex = dstIndex;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + dstMode.value << 3 + dstIndex.ordinal());
    }
}
