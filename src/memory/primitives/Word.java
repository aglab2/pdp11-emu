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

    public Addr toAddr() {
        return new Addr(value);
    }

    public Word inc() {
        return new Word(value + 1);
    }

    public Word dec() {
        return new Word(value - 1);
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

    public short toShort() {
        return (short) ((value < 128) ? value : value - 256);
    }

    public String fmtBinary() {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}
