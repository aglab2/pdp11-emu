package instruction.instuctions;

import bus.BusAddr;
import instruction.SingleOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class CLR extends SingleOperandInstruction {
    public CLR(RegMode dstMode, RegAddr dstIndex, Word nextWord) {
        super(new Word(0b0_000_101_000_000000), dstMode, dstIndex, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr dst = dstMode.apply(memory, dstAddr, nextWord);
        dst.load(memory, Word.ZERO);
    }
}
