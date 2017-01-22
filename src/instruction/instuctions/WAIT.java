package instruction.instuctions;

import instruction.ZeroOperandInstruction;
import instruction.primitives.RegAddr;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by denis on 1/22/2017.
 */
public class WAIT extends ZeroOperandInstruction {
    public WAIT() {
        super(new Word(1), 1);
    }

    @Override
    public void execute(MemoryModel memory) {
        System.out.println("Waiting at " + memory.registers.fetch(RegAddr.PC.offset).fmtOctal());
    }
}
