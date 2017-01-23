package instruction;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.microcode.MicroCode;
import pipeline.microcode.instruction.MicroDecode;
import pipeline.microcode.instruction.MicroExecute;
import pipeline.microcode.instruction.MicroFetch;
import pipeline.microcode.instruction.MicroMemory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class ZeroOperandInstruction extends Instruction {
    public ZeroOperandInstruction(Word code, int cost) {
        super(code, 16, cost);
    }

    @Override
    public Word getBinary() {
        return range.start;
    }

    @Override
    public String getAssembler() {
        return name;
    }

    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch fetch = new MicroFetch(pc, memory.cache);
        List<BusAddr> indexes = new ArrayList<>();

        MicroDecode decode = new MicroDecode(indexes, memory.cache);
        MicroMemory load = new MicroMemory(Collections.emptyList(), memory.cache);
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(Collections.emptyList(), memory.cache);

        return new MicroCode(fetch, decode, load, execute, store,
                Collections.emptySet(), Collections.emptySet());
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        try {
            return this.getClass().getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
