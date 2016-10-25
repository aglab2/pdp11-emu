package bus;

import instruction.primitives.GenAddr;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by aglab2 on 10/25/16.
 */
public class BusAddr implements GenAddr {
    private final int address; //Addresses on Bus are bigger then Word!

    public BusAddr(int address) {
            this.address = address;
        }

    @Override
    public Word fetch(MemoryModel memory) { return memory.bus.fetch(this.address); }

    @Override
    public void load(MemoryModel memory, Word value) {
            memory.bus.load(this.address, value);
        }
}
