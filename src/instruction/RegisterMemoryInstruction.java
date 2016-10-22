package instruction;

import instruction.primitives.RegIndex;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public class RegisterMemoryInstruction extends Instruction {
    public final RegIndex reg;
    public final RegMode dstMode;
    public final RegIndex dstIndex;

    public RegisterMemoryInstruction(Word code, RegIndex reg, RegMode dstMode, RegIndex dstIndex) {
        super(code, 7);
        this.reg = reg;
        this.dstMode = dstMode;
        this.dstIndex = dstIndex;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + reg.ordinal() << 6 +
                dstMode.value << 3 + dstIndex.ordinal());
    }
}
