package memory.primitives;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by voddan on 23/10/16.
 */
public class WordTest {

    @Test
    public void init_with_ints() {
        assertEquals(new Word(0xA7B2), new Word(0x1A7B2));
    }

    @Test
    public void init_with_bytes() {
        assertEquals(new Word(0xA7B2), new Word((byte) 0xB2, (byte) 0xA7));
    }

}