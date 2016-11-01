package memory.primitives;

public class Offset extends Word {
    public static final Offset ZERO = new Offset(0);

    public Offset(int value) {
        super(value);
    }

    @Override
    public Offset inc() {
        return new Offset(value + 1);
    }

    @Override
    public Offset dec() {
        return new Offset(value - 1);
    }
}
