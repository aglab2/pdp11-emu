package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.InstructionRange;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
public abstract class Instruction {
    public final InstructionRange diapason;

    public Instruction(Word code, int bitSize) {
        this.diapason = new InstructionRange(code, 16 - bitSize);
    }

    public abstract Word getCode();

    public abstract void apply(MemoryModel memory);

    /** returns the number {0, 1, 2} of words it needs as indexes */
    public abstract int indexСapacity();
}

