package instruction.instuctions;

import bus.BusAddr;
import instruction.SingleOperandInstruction;
import instruction.ZeroOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */
public class HALT extends ZeroOperandInstruction {
    public HALT() {
        super(Word.ZERO);
    }

    @Override
    public void apply(MemoryModel memory) {
        memory.registers.load(RegAddr.PC.offset, Word.NaN);
    }
}
