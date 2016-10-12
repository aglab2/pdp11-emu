package memory.primitives;

public class Addr extends Word {
    public Addr(int value) {
        super(value);
    }

    @Override
    public Addr inc() {
        return (Addr) super.inc();
    }

    @Override
    public Addr dec() {
        return (Addr) super.dec();
    }
}
