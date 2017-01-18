package memory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import memory.primitives.MemSize;
import memory.primitives.Offset;
import memory.primitives.Word;

import javax.xml.bind.ValidationException;

/**
 * Created by voddan on 26/10/16.
 */
public class FlagsStorage extends RWMemory {
    public final BooleanProperty T = new SimpleBooleanProperty(null, "T", false);
    public final BooleanProperty N = new SimpleBooleanProperty(null, "N", false);
    public final BooleanProperty Z = new SimpleBooleanProperty(null, "Z", false);
    public final BooleanProperty V = new SimpleBooleanProperty(null, "V", false);
    public final BooleanProperty C = new SimpleBooleanProperty(null, "C", false);

    public FlagsStorage() {
        super(MemSize.ONE);
    }

    @Override
    public Word fetch(Offset address) {
        int value = 0b0000_0000_000_00000;
        if (T.get()) value |= 1 << 4;
        if (N.get()) value |= 1 << 3;
        if (Z.get()) value |= 1 << 2;
        if (V.get()) value |= 1 << 1;
        if (C.get()) value |= 1;
        return new Word(value);
    }

    @Override
    public void load(Offset address, Word word) {
        int value = word.value;
        T.set((value & 1 << 4) != 0);
        N.set((value & 1 << 3) != 0);
        Z.set((value & 1 << 2) != 0);
        V.set((value & 1 << 1) != 0);
        C.set((value & 1) != 0);
    }

    public void setZN(Word result) {
        setZN(result.toSigned());
    }

    public void setZN(int result) {
        Z.set(result == 0);
        N.set(result < 0);
    }

    public void clearArithm() {
        N.set(false);
        Z.set(false);
        V.set(false);
        C.set(false);
    }

    /**
     * N, Z, V, C may me 0 or 1
     */
    public void setArithm(int N, int Z, int V, int C) {
        this.N.set(N == 1);
        this.Z.set(Z == 1);
        this.V.set(V == 1);
        this.C.set(C == 1);
    }

    @Override
    public void add(Offset address, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clean() {
        T.set(false);
        N.set(false);
        Z.set(false);
        V.set(false);
        C.set(false);
    }

    @Override
    public void reload(byte[] arr) throws ValidationException {
        throw new UnsupportedOperationException();
    }
}
