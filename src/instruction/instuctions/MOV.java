package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.DoubleOperandInstruction;
import instruction.SingleOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class MOV extends DoubleOperandInstruction {

    public MOV(RegMode srcMode, RegAddr srcAddr, RegMode dstMode, RegAddr dstAddr, @Nullable Word nextWord) {
        super(new Word(0b0_001_000000_000000), srcMode, srcAddr, dstMode, dstAddr, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr src = srcMode.apply(memory, srcAddr, nextWord);
        BusAddr dst = dstMode.apply(memory, dstAddr, nextWord);
        dst.load(memory, src.fetch(memory));
    }
}
