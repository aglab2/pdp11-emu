package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class DoubleOperandInstruction extends Instruction {
    public final RegMode srcMode;
    public final RegAddr srcAddr;
    public final RegMode dstMode;
    public final RegAddr dstAddr;

    public DoubleOperandInstruction(Word code,
                                    RegMode srcMode, RegAddr srcAddr,
                                    RegMode dstMode, RegAddr dstAddr,
                                    @Nullable Word nextWord) {
        super(code, 4, nextWord);
        this.srcMode = srcMode;
        this.srcAddr = srcAddr;
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value +
                dstMode.value << 9 + dstAddr.value << 6 +
                dstMode.value << 3 + dstAddr.value);
    }

    @Override
    public boolean needsNextWord() {
        return srcMode.needsNextWord() || dstMode.needsNextWord();
    }
}
