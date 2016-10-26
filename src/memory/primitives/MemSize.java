package memory.primitives;


public class MemSize extends Word {
    public static MemSize ZERO = new MemSize(0);
    public static MemSize ONE = new MemSize(1);
    public static MemSize TEN = new MemSize(10);

    public MemSize(int value) {
        super(value);
    }
}
