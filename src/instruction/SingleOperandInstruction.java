package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

import java.lang.reflect.InvocationTargetException;

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
    public Word getBinary() {
        return new Word(range.start.value | dstMode.value << 3 | dstAddr.ordinal());
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if(!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        int value = word.value;

        Object[] params = {RegMode.parse(value >> 3), RegAddr.parse(value), index1};

        try {
            return this.getClass().getConstructor(RegMode.class, RegAddr.class, Word.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return dstMode.needsIndex() ? 1 : 0;
    }
}
