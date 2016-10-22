package instruction;

import instruction.primitives.RegIndex;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public class DoubleOperandInstruction extends Instruction {
    public final RegMode srcMode;
    public final RegIndex srcIndex;
    public final RegMode dstMode;
    public final RegIndex dstIndex;

    public DoubleOperandInstruction(Word code,
                                    RegMode srcMode, RegIndex srcIndex,
                                    RegMode dstMode, RegIndex dstIndex) {
        super(code, 4);
        this.srcMode = srcMode;
        this.srcIndex = srcIndex;
        this.dstMode = dstMode;
        this.dstIndex = dstIndex;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value +
                dstMode.value << 9 + dstIndex.ordinal() << 6 +
                dstMode.value << 3 + dstIndex.ordinal());
    }
}
