package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.DoubleOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

import static instruction.ArgumentType.READ;
import static instruction.ArgumentType.READWRITE;

/**
 * Created by voddan on 23/10/16.
 */
public class ADD extends DoubleOperandInstruction {

    public ADD(RegMode srcMode, RegAddr srcAddr, RegMode dstMode, RegAddr dstAddr,
               @Nullable Word index1, @Nullable Word index2) {
        super(new Word(0b0_110_000000_000000), srcMode, srcAddr, READ, dstMode, dstAddr, READWRITE, index1, index2, 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        BusAddr src = srcMode.apply(memory, srcAddr, srcIndex);
        BusAddr dst = dstMode.apply(memory, dstAddr, dstIndex);

        int srcValue = src.fetch(memory).toSigned();
        int dstValue = dst.fetch(memory).toSigned();

        int res = srcValue + dstValue;
        memory.flags.setZN(res);
        memory.flags.V.set(srcValue * dstValue > 0 && dstValue * res < 0);
        memory.flags.C.set(res >> 16 != 0);

        dst.load(memory, new Word(res));
    }
}
