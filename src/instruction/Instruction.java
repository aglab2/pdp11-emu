package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.InstructionRange;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
public abstract class Instruction {
    public final InstructionRange range;

    public Instruction(Word code, int bitSize) {
        this.range = new InstructionRange(code, 16 - bitSize);
    }

    public abstract Word getBinary();

    public abstract void apply(MemoryModel memory);

    public abstract Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2);

    /** returns the number {0, 1, 2} of words it needs as indexes */
    public abstract int indexСapacity();
}

