package instruction;

import instruction.primitives.InstructionDiapason;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
public abstract class Instruction {
    public final InstructionDiapason diapason;

    public Instruction(Word code, int bitSize) {
        this.diapason = new InstructionDiapason(code, bitSize);
    }

    public abstract Word getCode();
}

