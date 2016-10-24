package instruction.primitives;

import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 24/10/16.
 */
public class RamAddr implements GenAddr {
    private final Addr address;

    public RamAddr(Addr address) {
        this.address = address;
    }

    @Override
    public Word fetch(MemoryModel memory) {
        return memory.ram.fetch(this.address);
    }

    @Override
    public void load(MemoryModel memory, Word value) {
        memory.ram.load(this.address, value);
    }
}
