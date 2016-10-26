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
public class ASR extends SingleOperandInstruction {
    public ASR(RegMode dstMode, RegAddr dstIndex, Word nextWord) {
        super(new Word(0b0_000_110_010_000000), dstMode, dstIndex, nextWord);
    }

    @Override
    public void apply(MemoryModel memory) {
        BusAddr dst = dstMode.apply(memory, dstAddr, nextWord);
        int res = dst.fetch(memory).value >> 1;

        memory.flags.clearArithm();
        memory.flags.setZN(res);
        memory.flags.C = (dst.fetch(memory).value % 2 == 1);
        memory.flags.V = (memory.flags.N ^ memory.flags.C);

        dst.load(memory, new Word(res));
    }
}
