package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

public abstract class ReadWriteMemory extends ReadOnlyMemory {
    public ReadWriteMemory(MemSize size) {
        super(size);
    }

    abstract public void load(Addr address, Word value);
}
