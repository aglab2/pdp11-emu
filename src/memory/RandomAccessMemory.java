package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import java.util.Arrays;

public class RandomAccessMemory extends ReadWriteMemory {
    public final Word[] __data__;

    public RandomAccessMemory(MemSize size) {
        super(size);
        this.__data__ = new Word[size.value];
        Arrays.fill(__data__, Word.ZERO);
    }

    public RandomAccessMemory(short[] arr) {
        super(new MemSize(arr.length));
        this.__data__ = new Word[size.value];

        for(int i = 0; i < size.value; i++)
            __data__[i] = new Word(arr[i] & 0xFFFF);
    }

    @Override
    public Word fetch(Addr address) {
        assert containsAddr(address);
        return __data__[address.value];
    }

    @Override
    public void load(Addr address, Word value) {
        assert containsAddr(address);
        __data__[address.value] = value;
    }
}
