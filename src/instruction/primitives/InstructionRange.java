package instruction.primitives;

import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */

public class InstructionRange {
    public final Word start;
    public final Word endInclusive;

    @Override
    public String toString() {
        return "InstructionDiapason{" +
                "start=" + start +
                ", endInclusive=" + endInclusive +
                '}';
    }

    public InstructionRange(Word start, Word endInclusive) {
        assert start.value <= endInclusive.value;
        this.start = start;
        this.endInclusive = endInclusive;
    }

    /**
     * @param code
     * @param expSize - size of the diapason
     */
    public InstructionRange(Word code, int expSize) {
        assert 0 <= expSize && expSize <= 16;
        int mask = 0xFFFF << expSize;
        this.start = new Word(code.value & mask);
        this.endInclusive = new Word(start.value | ~mask);
    }

    public boolean contains(Word w) {
        return start.value <= w.value && w.value <= endInclusive.value;
    }
}
