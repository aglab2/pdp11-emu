package bus;

import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by aglab2 on 10/25/16.
 */
public class BusAddr {
    private final int address; //Addresses on Bus are bigger then Word!

    public BusAddr(int address) {
        this.address = address;
    }

    public Word fetch(MemoryModel memory) {
        return memory.bus.fetch(this.address);
    }

    public void load(MemoryModel memory, Word value) {
        memory.bus.load(this.address, value);
    }
}
