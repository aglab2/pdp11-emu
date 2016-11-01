package memory;

import memory.primitives.Offset;
import memory.primitives.MemSize;
import memory.primitives.Word;

public abstract class RWMemory extends Memory {
    public RWMemory(MemSize size) {
        super(size);
    }

    abstract public void load(Offset address, Word value);

    abstract public void add(Offset address, int x);
}
