package memory.primitives;


public class Word {
    public static final Word ZERO = new Word(0);

    public final int value;

    public Word(int value) {
        this.value = value & 0xFFFF;
    }

    public Word(byte lowByte, byte highByte) {
        this((lowByte & 0xFF) | ((int) highByte) << 8);   // Little Endian
    }

    public Word inc() {
        return new Word(value + 1);
    }

    public Word dec() {
        return new Word(value - 1);
    }

    public Word inc2() {
        return new Word(value + 2);
    }

    public Word dec2() {
        return new Word(value - 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        return value == ((Word) o).value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Word{" + value + '}';
    }

    public byte highByte() {
        return (byte) (value & 0xFF);
    }

    public byte lowByte() {
        return (byte) ((value & 0xFF00) >> 8);
    }

    public short toSigned() {
        return (short) ((value <= Short.MAX_VALUE) ? value : value - (1 << 16));
    }

    public byte toSignedByte() {
        return (byte) ((value <= Byte.MAX_VALUE) ? value : value - (1 << 8));
    }

    public String fmtBinary() {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}
