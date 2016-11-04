package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.DoubleOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class MOV extends DoubleOperandInstruction {

    public MOV(RegMode srcMode, RegAddr srcAddr, RegMode dstMode, RegAddr dstAddr,
               @Nullable Word index1, @Nullable Word index2) {
        super(new Word(0b0_001_000000_000000), srcMode, srcAddr, dstMode, dstAddr, index1, index2);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr src = srcMode.apply(memory, srcAddr, srcIndex);
        BusAddr dst = dstMode.apply(memory, dstAddr, dstIndex);
        Word res = src.fetch(memory);
        dst.load(memory, res);
        memory.flags.setZN(res);
        memory.flags.V.set(false);
    }
}
