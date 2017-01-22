package bus;

import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by aglab2 on 10/25/16.
 */
public class BusAddr {
    public final int value; //Addresses on Bus are bigger then Word!

    public BusAddr(int value) {
        //if(value % 2 != 0) { throw new RuntimeException("Byte addresses must be even"); }
        this.value = value;
    }

    public BusAddr(int regionOffset, Offset offset) {
        //if(regionOffset % 2 != 0) throw new RuntimeException("Byte addresses must be even");
        this.value = regionOffset + offset.value * 2;
    }

    public @Nullable Word fetch(MemoryModel memory) {
        return memory.bus.fetch(this.value);
    }

    public void load(MemoryModel memory, Word value) {
        memory.bus.load(this.value, value);
    }
}
