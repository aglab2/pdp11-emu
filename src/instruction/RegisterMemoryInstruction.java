package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class RegisterMemoryInstruction extends Instruction {
    public final RegAddr reg;
    public final RegMode dstMode;
    public final RegAddr dstAddr;

    public RegisterMemoryInstruction(Word code, RegAddr reg,
                                     RegMode dstMode, RegAddr dstAddr,
                                     @Nullable Word nextWord) {
        super(code, 7, nextWord);
        this.reg = reg;
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + reg.ordinal() << 6 +
                dstMode.value << 3 + dstAddr.ordinal());
    }

    @Override
    public boolean needsNextWord() {
        return dstMode.needsNextWord();
    }
}
