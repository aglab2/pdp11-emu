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
public class InstructionGetCodeTest {

    @Test
    public void examples_from_3_3() {
        assertInstruction("INC R3", 005203, new INC(RegMode.Register, RegAddr.R3, null));
        assertInstruction("ADD R2,R4", 060204, new ADD(RegMode.Register, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstruction("CLR (R5)+", 005025, new CLR(RegMode.AutoInc, RegAddr.R5, null));
        assertInstruction("ADD (R2)+,R4", 062204, new ADD(RegMode.AutoInc, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstruction("INC-(RO)", 005240, new INC(RegMode.AutoDec, RegAddr.R0, null));
        assertInstruction("ADD-(R3),RO", 064300, new ADD(RegMode.AutoDec, RegAddr.R3, RegMode.Register, RegAddr.R0, null, null));

        assertInstruction("CLR 200(R4)", 005064, new CLR(RegMode.Index, RegAddr.R4, new Word(000200)));
        assertInstruction("ADD 30(R2),20(R5)", 066265,
                new ADD(RegMode.Index, RegAddr.R2, RegMode.Index, RegAddr.R5, new Word(000030), new Word(000020)));

        assertInstruction("CLR @R5", 005015, new CLR(RegMode.DRegister, RegAddr.R5, null));
        assertInstruction("INC@(R2)+", 005232, new INC(RegMode.DAutoInc, RegAddr.R2, null));
        assertInstruction("ADD @1000(R2),R1", 067201,
                new ADD(RegMode.DIndex, RegAddr.R2, RegMode.Register, RegAddr.R1, new Word(001000), null));
    }

    @Test
    public void other_instructions() throws Exception {
        assertInstruction("MUL R0,R1", 0b0111000_000_000001, new MUL(RegAddr.R0, RegMode.Register, RegAddr.R1, null));
        assertInstruction("DIV R0,R1", 0b0111001_101_101001, new DIV(RegAddr.R5, RegMode.DAutoDec, RegAddr.R1, null));
        assertInstruction("ASH R0,R1", 0b0111010_000_110011, new ASH(RegAddr.R0, RegMode.Index, RegAddr.R3, null));

        assertInstruction("BNE 20", 0b00000010_00010000, new BNE(new Offset(020)));

    }

    void assertInstruction(String asm, int code, Instruction instruction) {
        int value = instruction.getBinary().value;
        assertEquals(asm + " [" + binary(code) + "] was expected, got " + binary(value), code, value);
    }

    static String binary(int value) {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}
