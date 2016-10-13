package memory.primitives;


public class Word{
    public static final Word ZERO = new Word(0);

    public final int value;

    public Word(int value) {
        Utill.assertIs2Bytes(value);
        this.value = value;
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
}
