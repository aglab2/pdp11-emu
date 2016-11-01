package bus;

import com.sun.istack.internal.Nullable;
import memory.RWMemory;
import memory.primitives.Offset;
import memory.primitives.Word;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aglab2 on 10/25/16.
 */



public class MemoryBus {
    private Map<Integer, MemoryRegion> regions; //start -> region

    public MemoryBus() {
        this.regions = new HashMap();
    }

    public void addRegion(String name, int dstart, RWMemory storage) {
        int start = dstart / 2;
        int size = storage.size.value;
        for (int offset = start; offset < start + size; offset++) {
            regions.put(offset, new MemoryRegion(name, start, storage));
        }
    }

    public @Nullable Word fetch(int daddr) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        MemoryRegion region = regions.get(addr);
        if(region == null) return null;
        Offset realAddress = new Offset(addr - region.start);
        return region.storage.fetch(realAddress);
    }

    public void load(int daddr, Word value) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        MemoryRegion region = regions.get(addr);
        Offset realAddress = new Offset(addr - region.start);
        region.storage.load(realAddress, value);
    }

    class MemoryRegion {
        final String name;
        final int start;
        final RWMemory storage;

        MemoryRegion(String name, int start, RWMemory storage) {
            this.name = name;
            this.start = start;
            this.storage = storage;
        }
    }
}
