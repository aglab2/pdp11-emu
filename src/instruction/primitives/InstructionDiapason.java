package instruction.primitives;

import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
/* TODO: the aesthetics needs unit-testing */
public class InstructionDiapason {
    public final Word start;
    public final Word endExclusive;

    public InstructionDiapason(Word start, Word endExclusive) {
        assert start.value < endExclusive.value;
        this.start = start;
        this.endExclusive = endExclusive;
    }

    public InstructionDiapason(Word code, int expSize) {
        assert 0 < expSize && expSize < 16;
        assert code.value > (1 << (expSize - 1));
        this.start = code;
        this.endExclusive = new Word(code.value + (1 << expSize));
    }

    public boolean contains(Word w) {
        return start.value <= w.value && w.value < endExclusive.value;
    }
}
