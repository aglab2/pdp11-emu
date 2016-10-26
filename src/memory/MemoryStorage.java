package memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import javax.xml.bind.ValidationException;
import java.util.Arrays;

public class MemoryStorage extends RWMemory {
    public final ObservableList<Word> dataObservableList;

    public MemoryStorage(MemSize size) {
        super(size);
        Word[] __data__ = new Word[size.value];
        Arrays.fill(__data__, Word.ZERO);

        this.dataObservableList = FXCollections.observableArrayList();
        this.dataObservableList.addAll(__data__);
    }

    public MemoryStorage(short[] arr) {
        super(new MemSize(arr.length));
        Word[] __data__ = new Word[size.value];

        for (int i = 0; i < size.value; i++)
            __data__[i] = new Word(arr[i]);

        this.dataObservableList = FXCollections.observableArrayList();
        this.dataObservableList.addAll(__data__);
    }

    public MemoryStorage(byte[] arr) throws ValidationException {
        super(new MemSize(arr.length / 2));
        if (arr.length % 2 != 0) throw new ValidationException("Cannot convert `byte[] arr` to `short[]`");
        int length = arr.length / 2;

        Word[] __data__ = new Word[length];

        for (int i = 0; i < length; i++)
            __data__[i] = new Word(arr[2 * i], arr[2 * i + 1]);

        this.dataObservableList = FXCollections.observableArrayList();
        this.dataObservableList.addAll(__data__);
    }

    @Override
    public Word fetch(Addr address) {
        assert containsAddr(address);
        return dataObservableList.get(address.value);
    }

    @Override
    public void load(Addr address, Word value) {
        assert containsAddr(address);
        //__data__[address.value] = value;
        dataObservableList.set(address.value, value);
    }
}
