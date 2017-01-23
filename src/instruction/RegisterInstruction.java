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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class RegisterInstruction extends Instruction {
    public final RegAddr reg;

    public RegisterInstruction(Word code, RegAddr reg, int cost) {
        super(code, 13, cost);
        this.reg = reg;
    }

    @Override
    public Word getBinary() {
        return new Word(range.start.value | reg.value);
    }

    @Override
    public String getAssembler() {
        return name + " " + reg.name();
    }

    //TODO: This is used for only one function: RTS that uses more SP -_-
    @Override
    public MicroCode getMicrocode(BusAddr pc, MemoryModel memory) {
        MicroFetch      fetch = new MicroFetch(pc, memory.cache);
        List<BusAddr> indexes = new ArrayList<>();

        List<BusAddr> regAddresses = RegMode.Register.getAddresses(memory, reg, null);
        List<BusAddr> spAddresses = RegMode.Register.getAddresses(memory, RegAddr.SP, null);

        MicroDecode decode = new MicroDecode(indexes, memory.cache);
        MicroMemory load = new MicroMemory(regAddresses, memory.cache);
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(spAddresses, memory.cache);

        Set<Integer> srcSet = regAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());
        Set<Integer> dstSet = spAddresses.stream().map(addr -> addr.value).collect(Collectors.toSet());

        return new MicroCode(fetch, decode, load, execute, store, srcSet, dstSet);
    }

    @Override
    public Instruction parse(Word word, @Nullable Word index1, @Nullable Word index2) {
        if (!range.contains(word))
            throw new UnsupportedOperationException("Word " + word + " is not in range");

        Object[] params = {RegAddr.parse(word.value)};

        try {
            return this.getClass().getConstructor(RegAddr.class).newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexСapacity() {
        return 0;
    }
}
