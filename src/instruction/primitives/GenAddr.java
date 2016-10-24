package instruction.primitives;

import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 24/10/16.
 */
public interface GenAddr {
    Word fetch(MemoryModel memory);

    void load(MemoryModel memory, Word value);
}
