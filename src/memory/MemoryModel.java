package memory;


import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MemoryModel {
    public static final MemSize NUMBER_OF_REGISTERS = new MemSize(8);
    public static final Addr STACK_POINTER_INDEX = new Addr(6);
    public static final Addr PROGRAM_COUNTER_INDEX = new Addr(7);


    public final ReadWriteMemory ram;
    public final ReadOnlyMemory rom;
    public final ReadWriteMemory registers = new RandomAccessMemory(NUMBER_OF_REGISTERS);

    /* TODO: same addresation ar `ram` */
    public final RandomAccessMemory vram;


    public MemoryModel(MemSize ramSize, MemSize vramSize, Path romFile) throws IOException {
        this.ram = new RandomAccessMemory(ramSize);
        this.vram = new RandomAccessMemory(vramSize);

        // must be in constructor because we need to know the rom size before initing it
        byte[] bytes = Files.readAllBytes(romFile);
        if (bytes.length % 2 != 0) throw new IOException("A rom file must contain shorts (2n bytes)");

        /*TODO: encapsulate*/
//        short[] shorts = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().array();
        int num  = bytes.length / 2;
        short[] shorts = new short[num];
        for(int i = 0; i < num; i++)
            shorts[i] = (short) (bytes[2 * i] | bytes[2 * i + 1] << 8); // Little Endian

        this.rom = new RandomAccessMemory(shorts);
    }

}
