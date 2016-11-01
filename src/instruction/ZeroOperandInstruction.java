package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

import java.lang.reflect.InvocationTargetException;

public abstract class ZeroOperandInstruction extends Instruction {
    public ZeroOperandInstruction(Word code) {
        super(code, 16);
    }

    @Override
    public Word getBinary() {
        return range.start;
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        try {
            return this.getClass().getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
