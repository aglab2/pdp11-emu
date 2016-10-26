package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class RegisterMemoryInstruction extends Instruction {
    public final RegAddr reg;
    public final RegMode sodMode;
    public final RegAddr sodAddr;

    /**
     * @param sodMode is a srcMode OR a dstMode
     * @param sodAddr is a srcAddr OR a dstAddr
     */
    public RegisterMemoryInstruction(Word code, RegAddr reg,
                                     RegMode sodMode, RegAddr sodAddr,
                                     @Nullable Word nextWord) {
        super(code, 7, nextWord);
        this.reg = reg;
        this.sodMode = sodMode;
        this.sodAddr = sodAddr;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value + reg.ordinal() << 6 +
                sodMode.value << 3 + sodAddr.ordinal());
    }

    @Override
    public boolean needsNextWord() {
        return sodMode.needsNextWord();
    }
}
