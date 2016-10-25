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
    public final
    @Nullable
    Word nextWord;

    public Instruction(Word code, int bitSize, @Nullable Word nextWord) {
        this.diapason = new InstructionRange(code, 16 - bitSize);
        this.nextWord = nextWord;
    }

    public abstract Word getCode();

    public abstract void apply(MemoryModel memory);

    public abstract boolean needsNextWord();
}

