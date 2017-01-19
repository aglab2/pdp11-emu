package instruction;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.microcode.MicroCode;

/**
 * Created by voddan on 01/11/16.
 */
public class Data extends Instruction {
    private final Word data;

    public Data(Word data) {
        super(Word.ZERO, 0, 0);
        this.data = data;
    }

    @Override
    public Word getBinary() {
        return data;
    }

    @Override
    public String getAssembler() {
        return data.fmtOctal();
    }

    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        return null;
    }


    @Override
    public void execute(MemoryModel memory) {
        throw new UnsupportedOperationException("operation code " + data.fmtOctal() +
                " is not recognised: " + data.fmtBinary());
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        return new Data(word);
    }

    @Override
    public int indexСapacity() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Data) && super.equals(obj);
    }
}
