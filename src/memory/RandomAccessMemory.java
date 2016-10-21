package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import java.util.Arrays;

public class RandomAccessMemory extends ReadWriteMemory {
    private final Word[] data;

    public RandomAccessMemory(MemSize size) {
        super(size);
        this.data = new Word[size.value];
        Arrays.fill(data, Word.ZERO);
    }

    @Override
    public Word fetch(Addr address) {
        assert containsAddr(address);
        return data[address.value];
    }

    @Override
    public void load(Addr address, Word value) {
        assert containsAddr(address);
        data[address.value] = value;
    }
}
