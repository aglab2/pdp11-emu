package memory;

import memory.primitives.Offset;
import memory.primitives.MemSize;
import memory.primitives.Word;

public abstract class Memory {
    public final MemSize size;

    public Memory(MemSize size) {
        this.size = size;
    }

    abstract public Word fetch(Offset address);

    public final boolean containsAddr(Offset address) {
        return address.value < size.value;
    }
}
