package instruction.primitives;

import junit.framework.TestCase;
import memory.primitives.Word;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by voddan on 22/10/16.
 */
public class InstructionDiapasonTest {
    Word word0 = new Word(0x10FF);
    Word word1 = new Word(0x1100);
    Word word2 = new Word(0x11A8);
    Word word3 = new Word(0x11FF);
    Word word4 = new Word(0x1200);
    Word word5 = new Word(0x1201);

    @Test
    public void test_diapason_from_boundaries() throws Exception {
        InstructionDiapason diapason = new InstructionDiapason(word1, word4);

        assertFalse(diapason.contains(word0));
        assertTrue(diapason.contains(word1));
        assertTrue(diapason.contains(word2));
        assertTrue(diapason.contains(word3));
        assertFalse(diapason.contains(word4));
        assertFalse(diapason.contains(word5));
    }

    @Test
    public void test_diapason_with_expSize() throws Exception {
        Word word = new Word(0b1111_1111_1111_1111);

        assertEquals(new InstructionDiapason(word, 0),
                new InstructionDiapason(new Word(0b1111_1111_1111_1111), new Word(0b1111_1111_1111_1111)));
        assertEquals(new InstructionDiapason(word, 1),
                new InstructionDiapason(new Word(0b1111_1111_1111_1100), new Word(0b1111_1111_1111_1111)));
        assertEquals(new InstructionDiapason(word, 2),
                new InstructionDiapason(new Word(0b1111_1111_1111_1100), new Word(0b1111_1111_1111_1110)));
        assertEquals(new InstructionDiapason(word, 3),
                new InstructionDiapason(new Word(0b1111_1111_1111_1000), new Word(0b1111_1111_1111_1100)));
        assertEquals(new InstructionDiapason(word, 4),
                new InstructionDiapason(new Word(0b1111_1111_1111_0000), new Word(0b1111_1111_1111_1000)));

    }

    void assertEquals(InstructionDiapason diap1, InstructionDiapason diap2) {
        Assert.assertEquals(diap1.start.value, diap2.start.value);
        Assert.assertEquals(diap1.endExclusive.value, diap2.endExclusive.value);
    }
}