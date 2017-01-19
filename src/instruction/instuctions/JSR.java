package instruction.instuctions;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import instruction.ArgumentType;
import instruction.RegisterMemoryInstruction;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.MemoryModel;
import memory.primitives.Word;

/**
 * Created by voddan on 23/10/16.
 */

public class JSR extends RegisterMemoryInstruction {

    public JSR(RegAddr reg, RegMode sodMode, RegAddr sodAddr, @Nullable Word index) {
        super(new Word(0b0_000100_000_000000), reg, ArgumentType.READWRITE ,sodMode, sodAddr, ArgumentType.READWRITE, index, 2);
    }

    @Override
    public void execute(MemoryModel memory) {  //TODO: write the test!
//        Word tmp = sodMode.apply(memory, sodAddr, index).fetch(memory);
        BusAddr tmp = sodMode.apply(memory, sodAddr, index);
        Word rrr = memory.registers.fetch(reg.offset);
        Word pc = memory.registers.fetch(RegAddr.PC.offset);

        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, rrr);
        memory.registers.load(reg.offset, pc);
//        memory.registers.load(RegAddr.PC.offset, tmp);
        memory.registers.load(RegAddr.PC.offset, new Word(tmp.value));
    }
}
