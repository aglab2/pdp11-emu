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
public class SWAP extends SingleOperandInstruction {
    public SWAP(RegMode dstMode, RegAddr dstIndex, Word index) {
        super(new Word(0b0_000_000_011_000000), dstMode, dstIndex, index, 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr dst = dstMode.apply(memory, dstAddr, index);
        Word word = dst.fetch(memory);
        Word res = new Word(word.highByte(), word.lowByte());
        dst.load(memory, res);
        memory.flags.clearArithm();
        memory.flags.setZN(res);
    }
}
