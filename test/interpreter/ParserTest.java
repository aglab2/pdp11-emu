package interpreter;

import instruction.Instruction;
import instruction.instuctions.*;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Addr;
import memory.primitives.Word;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by voddan on 01/11/16.
 */
public class ParserTest {
    Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = new Parser();
    }

    @Test
    public void examples_from_3_4() throws Exception {
        assertInstruction("INC R3", 005203, null, null,
                new INC(RegMode.Register, RegAddr.R3, null));
        assertInstruction("ADD R2,R4", 060204, null, null,
                new ADD(RegMode.Register, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstruction("CLR (R5)+", 005025, null, null,
                new CLR(RegMode.AutoInc, RegAddr.R5, null));
        assertInstruction("ADD (R2)+,R4", 062204, null, null,
                new ADD(RegMode.AutoInc, RegAddr.R2, RegMode.Register, RegAddr.R4, null, null));

        assertInstruction("INC-(RO)", 005240, null, null,
                new INC(RegMode.AutoDec, RegAddr.R0, null));
        assertInstruction("ADD-(R3),RO", 064300, null, null,
                new ADD(RegMode.AutoDec, RegAddr.R3, RegMode.Register, RegAddr.R0, null, null));

        assertInstruction("CLR 200(R4)", 005064, new Word(000200), null,
                new CLR(RegMode.Index, RegAddr.R4, new Word(000200)));
        assertInstruction("ADD 30(R2),20(R5)", 066265, new Word(000020), new Word(000030),
                new ADD(RegMode.Index, RegAddr.R2, RegMode.Index, RegAddr.R5, new Word(000030), new Word(000020)));

        assertInstruction("CLR @R5", 005015, null, null,
                new CLR(RegMode.DRegister, RegAddr.R5, null));
        assertInstruction("INC@(R2)+", 005232, null, null,
                new INC(RegMode.DAutoInc, RegAddr.R2, null));
        assertInstruction("ADD @1000(R2),R1", 067201, null, null,
                new ADD(RegMode.DIndex, RegAddr.R2, RegMode.Register, RegAddr.R1, new Word(001000), null));
    }

    @Test
    public void other_instructions() throws Exception {
        assertInstruction("MUL R0,R1", 0b0111000_000_000001, null, null,
                new MUL(RegAddr.R0, RegMode.Register, RegAddr.R1, null));
        assertInstruction("DIV R0,R1", 0b0111001_101_101001, null, null,
                new DIV(RegAddr.R5, RegMode.DAutoDec, RegAddr.R1, null));
        assertInstruction("ASH R0,R1", 0b0111010_000_110011, null, null,
                new ASH(RegAddr.R0, RegMode.Index, RegAddr.R3, null));

        assertInstruction("BNE 20", 0b00000010_00010000, null, null,
                new BNE(new Addr(020)));
    }

    @Test @Ignore
    public void parse_an_array() throws Exception {
        int[] arr = {0xC0, 0x15, 0x00, 0x90, 0xC1, 0x15, 0x00, 0x40, 0x02, 0x14,
                     0xC2, 0x0B, 0x07, 0x03, 0x03, 0x14, 0xC2, 0x0B, 0x03, 0x03,
                     0xD1, 0x10, 0xC2, 0x0A, 0xFB, 0x01, 0xF6, 0x01};

        Word[] words = new Word[arr.length / 2];
        for (int i = 0; i < arr.length / 2; i ++) {
            words[i] = new Word((byte) arr[2 * i], (byte) arr[2 * i + 1]);
        }

        System.out.println(words);

//        Instruction[] instructions = parser.parse(words);
//
//        System.out.println(instructions);
    }

    void assertInstruction(String asm, int code, Word index1, Word index2, Instruction instruction) {
        Instruction instr = parser.parseInstruction(new Word(code), index1, index2);
        assertEquals(asm + " [" + binary(code) + "] was expected", instruction.getBinary(), instr.getBinary());
    }

    static String binary(int value) {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}