package memory;

import memory.primitives.Addr;
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
    public Word fetch(Addr address) {
        int value = 0b0000_0000_000_00000;
        if(T) value |= 1 << 4;
        if(N) value |= 1 << 3;
        if(Z) value |= 1 << 2;
        if(V) value |= 1 << 1;
        if(C) value |= 1;
        return new Word(value);
    }

    public void setZN(Word result) {
        setZN(result.value);
    }

    public void setZN(int result) {
        Z = (result == 0);
        N = (result < 0);
    }

    @Override
    public void load(Addr address, Word value) {
        throw new UnsupportedOperationException();
    }
}
