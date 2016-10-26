package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

public abstract class SingleOperandInstruction extends Instruction {
    public final RegMode dstMode;
    public final RegAddr dstAddr;
    public final @Nullable Word index;

    public SingleOperandInstruction(Word code,
                                    RegMode dstMode, RegAddr dstAddr,
                                    @Nullable Word index) {
        super(code, 10);
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
        this.index = index;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value | dstMode.value << 3 | dstAddr.ordinal());
    }

    @Override
    public int indexСapacity() {
        return dstMode.needsIndex() ? 1 : 0;
    }
}
