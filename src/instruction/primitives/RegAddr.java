package instruction.primitives;

import memory.primitives.Offset;

/**
 * Created by voddan on 12/10/16.
 */


public enum RegAddr {
    R0, R1, R2, R3, R4, R5, R6, R7;

    public final int value = ordinal();

    public final Offset offset = new Offset(value);

    /** has no other register after itself */
    public final boolean isLast() {
        return this == R7;
    }

    public static RegAddr parse(int v) {
        return RegAddr.values()[v & 0b111];
    }

//    public Word fetch(MemoryModel memory) {
//        return memory.registers.fetch(this.offset);
//    }
//
//    public void load(MemoryModel memory, Word value) {
//        memory.registers.load(this.offset, value);
//    }

    public static final RegAddr SP = R6;
    public static final RegAddr PC = R7;
}
