package util;

/**
 * Created by voddan on 04/11/16.
 */
public class IndexedValue<T> {
    public final int index;
    public final T value;

    public IndexedValue(int index, T value) {
        this.index = index;
        this.value = value;
    }
}
