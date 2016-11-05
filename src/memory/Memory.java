package memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import memory.primitives.MemSize;
import memory.primitives.Offset;
import memory.primitives.Word;

import javax.xml.bind.ValidationException;

public abstract class Memory {
    public final MemSize size;
    public final ObservableList<Word> dataObservableList;

    public Memory(MemSize size) {
        this.size = size;
        this.dataObservableList = FXCollections.observableArrayList();
    }

    abstract public Word fetch(Offset address);

    abstract public void reload(byte[] arr) throws ValidationException;

    public final boolean containsAddr(Offset address) {
        return address.value < size.value;
    }
}
