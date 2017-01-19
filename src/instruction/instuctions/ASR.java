package instruction.instuctions;

import bus.BusAddr;
import instruction.ArgumentType;
import instruction.SingleOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class ASR extends SingleOperandInstruction {
    public ASR(RegMode dstMode, RegAddr dstIndex, Word index) {
        super(new Word(0b0_000_110_010_000000), dstMode, dstIndex, ArgumentType.READWRITE, index, 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr dst = dstMode.apply(memory, dstAddr, index);
        int res = dst.fetch(memory).value >> 1;

        memory.flags.clearArithm();
        memory.flags.setZN(res);
        memory.flags.C.set(dst.fetch(memory).value % 2 == 1);
        memory.flags.V.set(memory.flags.N.get() ^ memory.flags.C.get());

        dst.load(memory, new Word(res));
    }
}
