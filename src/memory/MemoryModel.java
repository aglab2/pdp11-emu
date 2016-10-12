package memory;


import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

public class MemoryModel {
    public static final int NUMBER_OF_REGISTERS = 8;
    public static final int STACK_POINTER_INDEX = 6;
    public static final int PROGRAM_COUNTER_INDEX = 7;


    public final RandomAccessMemmory ram;
    public final RandomAccessMemmory vram;
    public final Word[] registers = new Word[NUMBER_OF_REGISTERS];


    public MemoryModel(MemSize ramSize, MemSize vramSize) {
        this.ram = new RandomAccessMemmory(ramSize);
        this.vram = new RandomAccessMemmory(vramSize);
    }


}
