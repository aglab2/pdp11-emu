package instruction;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xpath.internal.Arg;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SingleOperandInstruction extends Instruction {
    public final RegMode dstMode;
    public final RegAddr dstAddr;
    public final ArgumentType dstType;
    public final @Nullable Word index;

    public SingleOperandInstruction(Word code,
                                    RegMode dstMode, RegAddr dstAddr, ArgumentType dstType,
                                    @Nullable Word index, int cost) {
        super(code, 10, cost);
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
        this.dstType = dstType;
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

    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch fetch = new MicroFetch(pc, memory.cache);
        List<BusAddr> indexes = new ArrayList<>();

        int pc_value = pc.value;
        if (index == null) {
            pc_value += 2;
            indexes.add(new BusAddr(pc_value));
        }

        List<BusAddr> dstAddresses = dstMode.getAddresses(memory, dstAddr, index);

        List<BusAddr> readAddresses = new ArrayList<>();
        if (dstType == ArgumentType.READ || dstType == ArgumentType.READWRITE) readAddresses.addAll(dstAddresses);
        List<BusAddr> writeAddresses = new ArrayList<>();
        if (dstType == ArgumentType.WRITE || dstType == ArgumentType.READWRITE) writeAddresses.addAll(dstAddresses);

        MicroDecode decode = new MicroDecode(indexes, memory.cache);
        MicroMemory load = new MicroMemory(readAddresses, memory.cache);
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(writeAddresses, memory.cache);

        Set<Integer> readSet = readAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());
        Set<Integer> writeSet = writeAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());

        return new MicroCode(fetch, decode, load, execute, store, readSet, writeSet);
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
