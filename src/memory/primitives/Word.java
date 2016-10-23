package memory.primitives;


public class Word {
    public static final Word ZERO = new Word(0);

    public final int value;

    public Word(int value) {
        this.value = value & 0xFFFF;
    }

    public Word(byte b0, byte b1) {
        this((b0 & 0xFF) | ((int) b1) << 8);   // Little Endian
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
}
