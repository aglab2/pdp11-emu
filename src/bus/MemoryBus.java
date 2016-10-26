package bus;

import memory.Memory;
import memory.MemoryStorage;
import memory.RWMemory;
import memory.primitives.Addr;
import memory.primitives.Word;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aglab2 on 10/25/16.
 */

//This bus DON'T use Word because it should be able to map more than 64kB

class MemoryRegion {
    int start;
    RWMemory storage;

    MemoryRegion(int start, RWMemory storage) {
        this.start = start;
        this.storage = storage;
    }
}

public class MemoryBus {
    private Map<Integer, MemoryRegion> regions; //start -> region

    public MemoryBus() {
        this.regions = new HashMap();
    }

    public void addRegion(int start, RWMemory storage) {
        int size = storage.size.value;
        MemoryRegion region = new MemoryRegion(start, storage);
        for (int offset = start; offset < start + size; offset++) {
            regions.put(offset, region);
        }
    }

    public Word fetch(int addr) {
        MemoryRegion region = regions.get(addr);
        Addr realAddress = new Addr(addr - region.start);
        return region.storage.fetch(realAddress);
    }

    public void load(int addr, Word value) {
        MemoryRegion region = regions.get(addr);
        Addr realAddress = new Addr(addr - region.start);
        region.storage.load(realAddress, value);
    }
}
