package memory.primitives;


public class Utill {
    static void assertIs2Bytes(int value) {
        if(!(0 <= value && value < 0xFFFF))
            throw new RuntimeException("value " + value + " is not ushort");
    }
}
