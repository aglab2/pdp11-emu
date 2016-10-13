package memory;


import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;
import java.util.Arrays;

public class MemoryModel {
    public static final int NUMBER_OF_REGISTERS = 8;
    public static final int STACK_POINTER_INDEX = 6;
    public static final int PROGRAM_COUNTER_INDEX = 7;


    public final RandomAccessMemmory ram;

    /* TODO: same addresation ar `ram` */
    public final RandomAccessMemmory vram;

    /* TODO: encapsulate */
    public final Word[] registers = new Word[NUMBER_OF_REGISTERS];


    public MemoryModel(MemSize ramSize, MemSize vramSize) {
        this.ram = new RandomAccessMemmory(ramSize);
        this.vram = new RandomAccessMemmory(vramSize);
        Arrays.fill(registers, Word.ZERO);
    }


}
