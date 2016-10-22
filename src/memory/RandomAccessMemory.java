package memory;

import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import javax.xml.bind.ValidationException;
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

    public RandomAccessMemory(byte[] arr) throws ValidationException {
        super(new MemSize(arr.length / 2));
        if(arr.length % 2 != 0) throw new ValidationException("Cannot convert `byte[] arr` to `short[]`");
        int length = arr.length / 2;

        this.__data__ = new Word[length];

        for(int i = 0; i < length; i++)
            __data__[i] = new Word((arr[2 * i] | arr[2 * i + 1] << 8) & 0xFFFF); // Little Endian
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
