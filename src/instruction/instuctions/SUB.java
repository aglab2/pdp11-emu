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
public class SUB extends DoubleOperandInstruction {

    public SUB(RegMode srcMode, RegAddr srcAddr, RegMode dstMode, RegAddr dstAddr, @Nullable Word nextWord) {
        super(new Word(0b1_110_000000_000000), srcMode, srcAddr, dstMode, dstAddr, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr src = srcMode.apply(memory, srcAddr, nextWord);
        BusAddr dst = dstMode.apply(memory, dstAddr, nextWord);

        int srcValue = src.fetch(memory).value;
        int dstValue = dst.fetch(memory).value;

        int res = srcValue - dstValue;
        memory.flags.setZN(res);
        memory.flags.V = srcValue * dstValue < 0 && dstValue * res > 0;
        memory.flags.C = (res >> 16 != 0);

        dst.load(memory, new Word(res));
    }
}
