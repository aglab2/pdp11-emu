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
    public final String name;

    public Instruction(Word code, int bitSize) {
        this.range = new InstructionRange(code, 16 - bitSize);
        this.name = this.getClass().getSimpleName();
    }


    public abstract Word getBinary();

    public abstract String getAssembler();


    public abstract void execute(MemoryModel memory);

    public abstract Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2);

    /** returns the number {0, 1, 2} of words it needs as indexes */
    public abstract int indexСapacity();

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Instruction)) return false;
        if(!obj.getClass().equals(this.getClass())) return false;

        return ((Instruction) obj).getBinary().value == getBinary().value;
    }
}

