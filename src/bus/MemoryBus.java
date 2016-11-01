package bus;

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

    public void addRegion(int dstart, RWMemory storage) {
        int start = dstart / 2;
        int size = storage.size.value;
        MemoryRegion region = new MemoryRegion(start, storage);
        for (int offset = start; offset < start + size; offset++) {
            regions.put(offset, region);
        }
    }

    public Word fetch(int daddr) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        MemoryRegion region = regions.get(addr);
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
        int start;
        RWMemory storage;

        MemoryRegion(int start, RWMemory storage) {
            this.start = start;
            this.storage = storage;
        }
    }
}
