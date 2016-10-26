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
    public final @Nullable Word srcIndex;
    public final @Nullable Word dstIndex;


    /**
     * @param index1    the first `Word` after the instruction
     * @param index2    the second `Word` after the instruction
     */
    public DoubleOperandInstruction(Word code,
                                    RegMode srcMode, RegAddr srcAddr,
                                    RegMode dstMode, RegAddr dstAddr,
                                    @Nullable Word index1, @Nullable Word index2) {
        super(code, 4);
        this.srcMode = srcMode;
        this.srcAddr = srcAddr;
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;

        this.srcIndex = index1;
        this.dstIndex = (srcMode.needsIndex()) ? index2 : index1;
    }

    @Override
    public Word getCode() {
        return new Word(diapason.start.value |
                srcMode.value << 9 | srcAddr.value << 6 |
                dstMode.value << 3 | dstAddr.value);
    }

    @Override
    public int indexСapacity() {
        return (srcMode.needsIndex() ? 1 : 0) + (dstMode.needsIndex() ? 1 : 0);
    }
}
