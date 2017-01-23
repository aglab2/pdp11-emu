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
import java.util.*;
import java.util.stream.Collectors;

public abstract class RegisterMemoryInstruction extends Instruction {
    public final RegAddr reg;
    public final ArgumentType regType;

    public final RegMode sodMode;
    public final RegAddr sodAddr;
    public final ArgumentType sodType;

    public final @Nullable Word index;

    /**
     * @param sodMode is a srcMode OR a dstMode
     * @param sodAddr is a srcAddr OR a dstAddr
     */
    public RegisterMemoryInstruction(Word code, RegAddr reg, ArgumentType regType,
                                     RegMode sodMode, RegAddr sodAddr, ArgumentType sodType,
                                     @Nullable Word index, int cost) {
        super(code, 7, cost);
        this.reg = reg;
        this.regType = regType;
        this.sodMode = sodMode;
        this.sodAddr = sodAddr;
        this.sodType = sodType;
        this.index = index;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | reg.value << 6 |
                sodMode.value << 3 | sodAddr.ordinal());
    }

    @Override
    public String getAssembler() {
        return name + " " + reg.name() + "," + sodMode.getAssembler(sodAddr, index);
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

        List<BusAddr> regAddresses = RegMode.Register.getAddresses(memory, reg, null);
        List<BusAddr> sodAddresses = sodMode.getAddresses(memory, sodAddr, index);

        List<BusAddr> readAddresses = new ArrayList<>();
        if (regType == ArgumentType.READ || regType == ArgumentType.READWRITE) readAddresses.addAll(regAddresses);
        if (sodType == ArgumentType.READ || sodType == ArgumentType.READWRITE) readAddresses.addAll(sodAddresses);
        List<BusAddr> writeAddresses = new ArrayList<>();
        if (regType == ArgumentType.WRITE || regType == ArgumentType.READWRITE) writeAddresses.addAll(regAddresses);
        if (sodType == ArgumentType.WRITE || sodType == ArgumentType.READWRITE) writeAddresses.addAll(sodAddresses);

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

        Object[] params = {RegAddr.parse(value >> 6), RegMode.parse(value >> 3), RegAddr.parse(value), index1};

        try {
            return this.getClass().getConstructor(RegAddr.class, RegMode.class, RegAddr.class, Word.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return sodMode.needsIndex() ? 1 : 0;
    }
}
