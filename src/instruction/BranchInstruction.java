package instruction;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;
import pipeline.microcode.MicroCode;
import pipeline.microcode.instruction.MicroDecode;
import pipeline.microcode.instruction.MicroExecute;
import pipeline.microcode.instruction.MicroFetch;
import pipeline.microcode.instruction.MicroMemory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

public abstract class BranchInstruction extends Instruction {
    public final Offset offset;

    public BranchInstruction(Word code, Offset offset, int cost) {
        super(code, 8, cost);
        this.offset = offset;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | (offset.toSignedByte() & 0xFF));
    }

    @Override
    public String getAssembler() {
        byte signed = offset.toSignedByte();
        int abs = Math.abs(signed);
        return name + " " + (signed < 0 ? "-" : "") + Integer.toOctalString(abs);
    }

    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch fetch = new MicroFetch(pc);

        List<BusAddr> indexes = Collections.singletonList(new BusAddr(pc.value + 2)); //TODO: This is kinda bad approach
        MicroDecode decode = new MicroDecode(indexes);

        MicroMemory load = new MicroMemory(Collections.emptyList());
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(Collections.emptyList());

        return new MicroCode(fetch, decode, load, execute, store);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if(!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        int value = word.value;

        Object[] params = {new Offset(value & 0xFF)};

        try {
            return this.getClass().getConstructor(Offset.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
