package bus;

import com.sun.istack.internal.Nullable;
import memory.RWMemory;
import memory.primitives.Offset;
import memory.primitives.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aglab2 on 10/25/16.
 */



public class MemoryBus {
    private Map<Integer, MemoryRegion> regions= new HashMap();
    private List<MemoryRegion> regionList = new ArrayList<>();


    public void addRegion(String name, int dstart, RWMemory storage) {
        int start = dstart / 2;
        int size = storage.size.value;

        MemoryRegion memoryRegion = new MemoryRegion(name, start, storage);
        regionList.add(memoryRegion);

        for (int offset = start; offset < start + size; offset++) {
            regions.put(offset, memoryRegion);
        }
    }

    public int getBusAddr(RWMemory storage, Offset offset) {  //TODO: return BusAddr
        for(MemoryRegion mr: regionList) {
            if(mr.storage == storage)
                return mr.start + offset.value;
        }
        throw new RuntimeException(); //TODO: return `null`
    }

    public @Nullable MemoryRegion getRegion(int daddr) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        return regions.get(addr);
    }

    public @Nullable Word fetch(int daddr) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        MemoryRegion region = regions.get(addr);
        if(region == null) return null;
        Offset offset = new Offset(addr - region.start);
        return region.storage.fetch(offset);
    }

    public void load(int daddr, Word value) {
        assert daddr % 2 == 0;
        int addr = daddr / 2;
        MemoryRegion region = regions.get(addr);
        if(region == null) throw new IndexOutOfBoundsException();
        Offset offset = new Offset(addr - region.start);
        region.storage.load(offset, value);
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
