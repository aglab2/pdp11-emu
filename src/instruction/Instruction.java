package instruction;

import com.sun.istack.internal.Nullable;
import instruction.primitives.InstructionRange;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by voddan on 12/10/16.
 */
public abstract class Instruction {
    public final InstructionRange range;

    public Instruction(Word code, int bitSize) {
        this.range = new InstructionRange(code, 16 - bitSize);
    }

    public abstract Word getCode();

    public abstract void apply(MemoryModel memory);

    /** returns the number {0, 1, 2} of words it needs as indexes */
    public abstract int indexСapacity();
}

