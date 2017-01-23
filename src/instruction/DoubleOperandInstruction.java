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

public abstract class DoubleOperandInstruction extends Instruction {
    public final RegMode srcMode;
    public final RegAddr srcAddr;
    public final ArgumentType srcType;
    public final RegMode dstMode;
    public final RegAddr dstAddr;
    public final ArgumentType dstType;

    public final @Nullable Word srcIndex;
    public final @Nullable Word dstIndex;


    /**
     * @param index1 the first `Word` after the instruction
     * @param index2 the second `Word` after the instruction
     */
    public DoubleOperandInstruction(Word code,
                                    RegMode srcMode, RegAddr srcAddr, ArgumentType srcType,
                                    RegMode dstMode, RegAddr dstAddr, ArgumentType dstType,
                                    @Nullable Word index1, @Nullable Word index2, int cost) {
        super(code, 4, cost);
        this.srcMode = srcMode;
        this.srcAddr = srcAddr;
        this.srcType = srcType;
        this.dstMode = dstMode;
        this.dstAddr = dstAddr;
        this.dstType = dstType;

        this.srcIndex = index1;
        this.dstIndex = (srcMode.needsIndex()) ? index2 : index1;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value |
                srcMode.value << 9 | srcAddr.value << 6 |
                dstMode.value << 3 | dstAddr.value);
    }

    @Override
    public String getAssembler() {
        return name + " " + srcMode.getAssembler(srcAddr, srcIndex) + "," + dstMode.getAssembler(dstAddr, dstIndex);
    }

    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch      fetch = new MicroFetch(pc, memory.cache);
        List<BusAddr> indexes = new ArrayList<>();

        int pc_value = pc.value;
        if (srcIndex == null) {
            pc_value += 2;
            indexes.add(new BusAddr(pc_value));
        }
        if (dstIndex == null) {
            pc_value += 2;
            indexes.add(new BusAddr(pc_value));
        }

        List<BusAddr> srcAddresses = srcMode.getAddresses(memory, srcAddr, srcIndex);
        List<BusAddr> dstAddresses = dstMode.getAddresses(memory, dstAddr, dstIndex);

        List<BusAddr> readAddresses = new ArrayList<>();
        if (srcType == ArgumentType.READ || srcType == ArgumentType.READWRITE) readAddresses.addAll(srcAddresses);
        if (dstType == ArgumentType.READ || dstType == ArgumentType.READWRITE) readAddresses.addAll(dstAddresses);
        List<BusAddr> writeAddresses = new ArrayList<>();
        if (srcType == ArgumentType.WRITE || srcType == ArgumentType.READWRITE) writeAddresses.addAll(srcAddresses);
        if (dstType == ArgumentType.WRITE || dstType == ArgumentType.READWRITE) writeAddresses.addAll(dstAddresses);

        MicroDecode     decode = new MicroDecode(indexes, memory.cache);
        MicroMemory     load = new MicroMemory(readAddresses, memory.cache);
        MicroExecute    execute = new MicroExecute(cost);
        MicroMemory     store = new MicroMemory(writeAddresses, memory.cache);

        Set<Integer> readSet = readAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());
        Set<Integer> writeSet = writeAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());

        return new MicroCode(fetch, decode, load, execute, store, readSet, writeSet);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        int value = word.value;

        Object[] params = {
                RegMode.parse(value >> 9), RegAddr.parse(value >> 6),
                RegMode.parse(value >> 3), RegAddr.parse(value),
                index1, index2};

        try {
            return this.getClass().getConstructor(
                    RegMode.class, RegAddr.class,
                    RegMode.class, RegAddr.class,
                    Word.class, Word.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return (srcMode.needsIndex() ? 1 : 0) + (dstMode.needsIndex() ? 1 : 0);
    }
}
