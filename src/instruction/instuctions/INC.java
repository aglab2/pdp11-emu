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
public class INC extends SingleOperandInstruction {
    public INC(RegMode dstMode, RegAddr dstIndex, Word index) {
        super(new Word(0b0_000_101_010_000000), dstMode, dstIndex, ArgumentType.READWRITE, index, 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr dst = dstMode.apply(memory, dstAddr, index);
        Word res = dst.fetch(memory).inc();
        dst.load(memory, res);
        memory.flags.setZN(res);
        memory.flags.V.set(dst.value == 077777);
    }
}
