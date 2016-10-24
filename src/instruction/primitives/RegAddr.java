package instruction.primitives;

import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */


public enum RegAddr implements GenAddr {
    R0, R1, R2, R3, R4, R5, R6, R7;

    public final Addr address = new Addr(ordinal());


    @Override
    public Word fetch(MemoryModel memory) {
        return memory.registers.fetch(this.address);
    }

    @Override
    public void load(MemoryModel memory, Word value) {
        memory.registers.load(this.address, value);
    }
}
