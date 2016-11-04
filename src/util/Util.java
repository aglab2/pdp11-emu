package util;

import com.sun.istack.internal.Nullable;
import javafx.collections.ObservableList;

import java.util.function.BiFunction;

/**
 * Created by voddan on 04/11/16.
 */
public class Util {

    public static <T, U> ObservableList<U> mapIndexed(
            ObservableList<? extends T> sourceList,
            BiFunction<Integer, ? super T, ? extends U> f) {
        return new MappedIndexedList<>(sourceList, f);
    }
    
    public static <T> ObservableList<IndexedValue<T>> withIndices(
            ObservableList<? extends T> sourceList) {
        return new MappedIndexedList<IndexedValue<T>, T>(sourceList, IndexedValue::new);
    }

    public static <E> E getOrNull(ObservableList<? extends E> list, int index) {
        if(0 <= index && index < list.size())
            return list.get(index);
        else
            return null;
    }
}
