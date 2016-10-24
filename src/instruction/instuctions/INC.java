package instruction.instuctions;

import instruction.SingleOperandInstruction;
import instruction.primitives.GenAddr;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class INC extends SingleOperandInstruction {
    public INC(RegMode dstMode, RegAddr dstIndex, Word nextWord) {
        super(new Word(0b0_000_101_010_000000), dstMode, dstIndex, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        GenAddr dst = dstMode.apply(memory, dstAddr, nextWord);
        dst.load(memory, dst.fetch(memory).inc());
    }
}
