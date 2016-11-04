package instruction.instructions;

import instruction.Instruction;
import instruction.instuctions.*;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Offset;
import memory.primitives.Word;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by voddan on 26/10/16.
 */
public class InstructionAssemblerTest {

    @Test
    public void examples_from_3_3() {
        assertInstructionAssembler("INC R3", new INC(RegMode.Register, RegAddr.R3, null));
        assertInstructionAssembler("ADD R2,R4", new ADD(RegMode.Register, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstructionAssembler("CLR (R5)+", new CLR(RegMode.AutoInc, RegAddr.R5, null));
        assertInstructionAssembler("ADD (R2)+,R4", new ADD(RegMode.AutoInc, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstructionAssembler("INC -(R0)", new INC(RegMode.AutoDec, RegAddr.R0, null));
        assertInstructionAssembler("ADD -(R3),R0", new ADD(RegMode.AutoDec, RegAddr.R3, RegMode.Register, RegAddr.R0, null, null));

        assertInstructionAssembler("CLR 200(R4)", new CLR(RegMode.Index, RegAddr.R4, new Word(000200)));
        assertInstructionAssembler("CLR -200(R4)", new CLR(RegMode.Index, RegAddr.R4, new Word(-0200)));
        assertInstructionAssembler("ADD 30(R2),20(R5)",
                new ADD(RegMode.Index, RegAddr.R2, RegMode.Index, RegAddr.R5, new Word(000030), new Word(000020)));

        assertInstructionAssembler("CLR (R5)", new CLR(RegMode.DRegister, RegAddr.R5, null));
        assertInstructionAssembler("INC @(R2)+", new INC(RegMode.DAutoInc, RegAddr.R2, null));
        assertInstructionAssembler("ADD @1000(R2),R1",
                new ADD(RegMode.DIndex, RegAddr.R2, RegMode.Register, RegAddr.R1, new Word(001000), null));
    }

    @Test
    public void other_instructions() throws Exception {
        assertInstructionAssembler("MUL R0,R1", new MUL(RegAddr.R0, RegMode.Register, RegAddr.R1, null));
        assertInstructionAssembler("DIV R5,@-(R1)", new DIV(RegAddr.R5, RegMode.DAutoDec, RegAddr.R1, null));
        assertInstructionAssembler("ASH R0,200(R3)", new ASH(RegAddr.R0, RegMode.Index, RegAddr.R3, new Word(0200)));

        assertInstructionAssembler("BNE 20", new BNE(new Offset(020)));
        assertInstructionAssembler("BR -11", new BR(new Offset(-9)));
    }

    void assertInstructionAssembler(String asm, Instruction instruction) {
        assertEquals(asm, instruction.getAssembler());
    }

    static String binary(int value) {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}
