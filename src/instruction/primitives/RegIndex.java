package instruction.primitives;

import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */


public enum RegIndex {
    R0, R1, R2, R3, R4, R5, R6, R7;

    public final Addr address = new Addr(ordinal());
}
