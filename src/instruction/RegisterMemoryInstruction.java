package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

import java.lang.reflect.InvocationTargetException;

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
    public Word getBinary() {
        return new Word(range.start.value | reg.value << 6 |
                sodMode.value << 3 | sodAddr.ordinal());
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
        return sodMode.needsIndex() ? 1 : 0;
    }
}
