package memory.primitives;


public class Word {
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
        int v = (value & 0xFF);
        return (byte) ((v <= Byte.MAX_VALUE) ? v : v - (1 << 8));
    }

    public String fmtBinary() {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public String fmtOctal() {
        return String.format("%6s", Integer.toOctalString(value)).replace(' ', '0');
    }

    public String fmtHex() {
        return String.format("%4s", Integer.toHexString(value)).replace(' ', '0');
    }

    public static final Word ZERO = new Word(0);
    public static final Word NaN = new Word(-1) {
        @Override
        public Word inc() {
            return this;
        }

        @Override
        public Word dec() {
            return this;
        }

        @Override
        public Word inc2() {
            return this;
        }

        @Override
        public Word dec2() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return -1;
        }

        @Override
        public String toString() {
            return "NaN";
        }

        @Override
        public byte highByte() {
            throw new UnsupportedOperationException("NaN");
        }

        @Override
        public byte lowByte() {
            throw new UnsupportedOperationException("NaN");
        }

        @Override
        public short toSigned() {
            throw new UnsupportedOperationException("NaN");
        }

        @Override
        public byte toSignedByte() {
            throw new UnsupportedOperationException("NaN");
        }

        @Override
        public String fmtBinary() {
            throw new UnsupportedOperationException("NaN");
        }
    };
}
