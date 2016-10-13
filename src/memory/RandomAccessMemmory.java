package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import java.util.Arrays;

public class RandomAccessMemmory {
    public final MemSize size;
    private final Word[] data;

    public RandomAccessMemmory(MemSize size) {
        this.size = size;
        this.data = new Word[size.value];
        Arrays.fill(data, Word.ZERO);
    }

    public Word fetch(Addr address) {
        assert contains(address);
        return data[address.value];
    }

    public void load(Addr address, Word value) {
        assert contains(address);
        data[address.value] = value;
    }

    public final boolean contains(Addr address) {
        return address.value < size.value;
    }
}
