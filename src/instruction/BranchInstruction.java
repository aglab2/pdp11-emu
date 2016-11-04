package instruction;

import com.sun.istack.internal.Nullable;
import memory.primitives.Offset;
import memory.primitives.Word;

import java.lang.reflect.InvocationTargetException;

public abstract class BranchInstruction extends Instruction {
    public final Offset offset;

    public BranchInstruction(Word code, Offset offset) {
        super(code, 8);
        this.offset = offset;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | (offset.toSignedByte() & 0xFF));
    }

    @Override
    public String getAssembler() {
        byte signed = offset.toSignedByte();
        int abs = Math.abs(signed);
        return name + " " + (signed < 0 ? "-" : "") + Integer.toOctalString(abs);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if(!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        int value = word.value;

        Object[] params = {new Offset(value & 0xFF)};

        try {
            return this.getClass().getConstructor(Offset.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
