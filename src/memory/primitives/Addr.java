package memory.primitives;

public class Addr extends Word {
    public static final Addr ZERO = new Addr(0);

    public Addr(int value) {
        super(value);
    }

    @Override
    public Addr inc() {
        return new Addr(value + 1);
    }

    @Override
    public Addr dec() {
        return new Addr(value - 1);
    }
}
