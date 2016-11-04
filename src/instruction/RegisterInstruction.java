package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

import java.lang.reflect.InvocationTargetException;

public abstract class RegisterInstruction extends Instruction {
    public final RegAddr reg;

    public RegisterInstruction(Word code, RegAddr reg) {
        super(code, 13);
        this.reg = reg;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | reg.value);
    }

    @Override
    public String getAssembler() {
        return name + " " + reg.name();
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        Object[] params = {RegAddr.parse(word.value)};

        try {
            return this.getClass().getConstructor(RegAddr.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
