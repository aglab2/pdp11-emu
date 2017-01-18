package instruction.instuctions;

import instruction.ZeroOperandInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by aglab2 on 1/12/2017.
 */
public class RTI extends ZeroOperandInstruction {
    public RTI() {
        super(new Word(0b0_000000_000_000010), 2);
    }

    @Override
    public void execute(MemoryModel memory) {
        Word oldPC = RegMode.AutoInc.apply(memory, RegAddr.SP, null).fetch(memory);
        memory.registers.load(RegAddr.PC.offset, oldPC);
        Word oldPS = RegMode.AutoInc.apply(memory, RegAddr.SP, null).fetch(memory);
        memory.bus.load(0xFFFE, oldPS);
    }
}
