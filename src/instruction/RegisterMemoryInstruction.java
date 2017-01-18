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
import java.util.Collections;
import java.util.List;

public abstract class RegisterMemoryInstruction extends Instruction {
    public final RegAddr reg;
    public final RegMode sodMode;
    public final RegAddr sodAddr;
    public final @Nullable Word index;

    /**
     * @param sodMode is a srcMode OR a dstMode
     * @param sodAddr is a srcAddr OR a dstAddr
     */
    public RegisterMemoryInstruction(Word code, RegAddr reg,
                                     RegMode sodMode, RegAddr sodAddr,
                                     @Nullable Word index, int cost) {
        super(code, 7, cost);
        this.reg = reg;
        this.sodMode = sodMode;
        this.sodAddr = sodAddr;
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

    //TODO: Separate store and load - JSR!!!
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
        MicroMemory load = new MicroMemory(sodMode.getAddresses(memory, sodAddr, index));
        MicroExecute execute = new MicroExecute(cost);
        MicroMemory store = new MicroMemory(Collections.emptyList());

        return new MicroCode(fetch, decode, load, execute, store);
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
