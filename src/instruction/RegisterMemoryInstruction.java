package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class RegisterMemoryInstruction extends Instruction {
    public final RegAddr reg;
    public final RegMode sodMode;
    public final RegAddr sodAddr;
    public final @Nullable Word index;

    /**
     * @param sodMode is a srcMode OR a dstMode
     * @param sodAddr is a srcAddr OR a dstAddr
     */
    public RegisterMemoryInstruction(Word code, RegAddr reg,
                                     RegMode sodMode, RegAddr sodAddr,
                                     @Nullable Word index) {
        super(code, 7);
        this.reg = reg;
        this.sodMode = sodMode;
        this.sodAddr = sodAddr;
        this.index = index;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value | reg.value << 6 |
                sodMode.value << 3 | sodAddr.ordinal());
    }

    @Override
    public int indexСapacity() {
        return sodMode.needsIndex() ? 1 : 0;
    }
}
