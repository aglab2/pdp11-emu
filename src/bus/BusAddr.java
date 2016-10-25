package bus;

import instruction.primitives.GenAddr;
import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by aglab2 on 10/25/16.
 */
public class BusAddr implements GenAddr {
    private final Addr address;

    public BusAddr(Addr address) {
            this.address = address;
        }

    @Override
    public Word fetch(MemoryModel memory) {
            return memory.bus.fetch(this.address.value);
        }

    @Override
    public void load(MemoryModel memory, Word value) {
            memory.bus.load(this.address.value, value);
        }
}
