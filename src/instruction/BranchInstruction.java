package instruction;

import com.sun.istack.internal.Nullable;
import memory.primitives.Addr;
import memory.primitives.Word;

public abstract class BranchInstruction extends Instruction {
    public final Addr offset;

    public BranchInstruction(Word code, Addr offset) {
        super(code, 8);
        this.offset = offset;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | offset.value);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        return null;
//        if(!range.contains(word))
//            throw new UnsupportedOperationException("Word " + word + " is not in range");
//
//        int value = word.value;
//
//        Object[] params = {RegMode.parse(value >> 3), RegAddr.parse(value), index1};
//
//        try {
//            return this.getClass().getConstructor(RegMode.class, RegAddr.class, Word.class).newInstance(params);
//        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }

    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
