package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class SingleOperandInstruction extends Instruction {
    public final RegMode dstMode;
    public final RegAddr dstAddr;

    public SingleOperandInstruction(Word code,
                                    RegMode dstMode, RegAddr dstAddr,
                                    @Nullable Word nextWord) {
        super(code, 10, nextWord);
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + dstMode.value << 3 + dstAddr.ordinal());
    }

    @Override
    public boolean needsNextWord() {
        return dstMode.needsNextWord();
    }
}
