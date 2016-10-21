package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

public abstract class ReadOnlyMemory {
    public final MemSize size;

    public ReadOnlyMemory(MemSize size) {
        this.size = size;
    }

    abstract public Word fetch(Addr address);

    public final boolean containsAddr(Addr address) {
        return address.value < size.value;
    }
}
