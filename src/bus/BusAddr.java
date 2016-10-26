package bus;

import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by aglab2 on 10/25/16.
 */
public class BusAddr {
    public final int value; //Addresses on Bus are bigger then Word!

    public BusAddr(int value) {
        this.value = value;
    }

    public Word fetch(MemoryModel memory) {
        return memory.bus.fetch(this.value);
    }

    public void load(MemoryModel memory, Word value) {
        memory.bus.load(this.value, value);
    }
}
