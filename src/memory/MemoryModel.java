package memory;


import memory.primitives.MemSize;
import memory.primitives.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MemoryModel {
    public static final int NUMBER_OF_REGISTERS = 8;
    public static final int STACK_POINTER_INDEX = 6;
    public static final int PROGRAM_COUNTER_INDEX = 7;


    public final ReadWriteMemory ram;
    public final ReadOnlyMemory rom;

    /* TODO: same addresation ar `ram` */
    public final RandomAccessMemory vram;

    /* TODO: encapsulate */
    public final Word[] registers = new Word[NUMBER_OF_REGISTERS];


    public MemoryModel(MemSize ramSize, MemSize vramSize, Path romFile) throws IOException {
        this.ram = new RandomAccessMemory(ramSize);
        this.vram = new RandomAccessMemory(vramSize);
        Arrays.fill(registers, Word.ZERO);

        // must be in constructor because we need to know the rom size before initing it
        byte[] bytes = Files.readAllBytes(romFile);
        if (bytes.length % 2 != 0) throw new IOException("A rom file must contain shorts (2n bytes)");

//        short[] shorts = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().array();
        int num  = bytes.length / 2;
        short[] shorts = new short[num];
        for(int i = 0; i < num; i++)
            shorts[i] = (short) (bytes[2 * i] | bytes[2 * i + 1] << 8); // Little Endian

        this.rom = new RandomAccessMemory(shorts);
    }

}
