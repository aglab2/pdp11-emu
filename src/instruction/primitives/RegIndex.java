package instruction.primitives;

import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
public class RegIndex {
    public final int value;

    public RegIndex(int value) {
        assert 0 <= value && value < MemoryModel.NUMBER_OF_REGISTERS;
        this.value = value;
    }

    /* do we need those? */
    public Word getRegisterValue(MemoryModel memory) {
        return memory.registers[value];
    }

    //TODO: redo this WTF. It should be done in `MemoryModel` or `MemoryIO`
    public void setRegisterValue(MemoryModel memory, Word value) {
        memory.registers[this.value] = value;
    }
}
