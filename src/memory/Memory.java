package memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import memory.primitives.Offset;
import memory.primitives.MemSize;
import memory.primitives.Word;

public abstract class Memory {
    public final MemSize size;
    public final ObservableList<Word> dataObservableList;

    public Memory(MemSize size) {
        this.size = size;
        this.dataObservableList = FXCollections.observableArrayList();
    }

    abstract public Word fetch(Offset address);

    public final boolean containsAddr(Offset address) {
        return address.value < size.value;
    }
}
