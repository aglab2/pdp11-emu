package instruction;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.microcode.MicroCode;
import pipeline.microcode.instruction.MicroDecode;
import pipeline.microcode.instruction.MicroExecute;
import pipeline.microcode.instruction.MicroFetch;
import pipeline.microcode.instruction.MicroMemory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class SingleOperandInstruction extends Instruction {
    public final RegMode dstMode;
    public final RegAddr dstAddr;
    public final @Nullable Word index;

    public SingleOperandInstruction(Word code,
                                    RegMode dstMode, RegAddr dstAddr,
                                    @Nullable Word index, int cost) {
        super(code, 10, cost);
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
        this.index = index;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | dstMode.value << 3 | dstAddr.ordinal());
    }

    @Override
    public String getAssembler() {
        return name + " " + dstMode.getAssembler(dstAddr, index);
    }

    //TODO: Separate store from load
    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch fetch = new MicroFetch(pc);
        List<BusAddr> indexes = new ArrayList<>();

        int pc_value = pc.value;
        if (index == null) {
            pc_value += 2;
            indexes.add(new BusAddr(pc_value));
        }
        MicroDecode decode = new MicroDecode(indexes);
        MicroMemory load = new MicroMemory(dstMode.getAddresses(memory, dstAddr, index));
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(dstMode.getAddresses(memory, dstAddr, index));

        return new MicroCode(fetch, decode, load, execute, store);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        int value = word.value;

        Object[] params = {RegMode.parse(value >> 3), RegAddr.parse(value), index1};

        try {
            return this.getClass().getConstructor(RegMode.class, RegAddr.class, Word.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return dstMode.needsIndex() ? 1 : 0;
    }
}
