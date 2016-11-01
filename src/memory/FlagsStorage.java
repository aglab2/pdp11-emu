package memory;

import memory.primitives.Offset;
import memory.primitives.MemSize;
import memory.primitives.Word;

/**
 * Created by voddan on 26/10/16.
 */
public class FlagsStorage extends RWMemory {
    public boolean T = false;
    public boolean N = false;
    public boolean Z = false;
    public boolean V = false;
    public boolean C = false;

    public FlagsStorage() {
        super(MemSize.ONE);
    }

    @Override
    public Word fetch(Offset address) {
        int value = 0b0000_0000_000_00000;
        if (T) value |= 1 << 4;
        if (N) value |= 1 << 3;
        if (Z) value |= 1 << 2;
        if (V) value |= 1 << 1;
        if (C) value |= 1;
        return new Word(value);
    }

    public void setZN(Word result) {
        setZN(result.value);
    }

    public void setZN(int result) {
        Z = (result == 0);
        N = (result < 0);
    }

    public void clearArithm() {
        N = Z = V = C = false;
    }

    /**
     * N, Z, V, C may me 0 or 1
     */
    public void setArithm(int N, int Z, int V, int C) {
        this.N = (N == 1);
        this.Z = (Z == 1);
        this.V = (V == 1);
        this.C = (C == 1);
    }

    @Override
    public void load(Offset address, Word value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(Offset address, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clean() {
        T = N = Z = V = C = false;
    }
}
