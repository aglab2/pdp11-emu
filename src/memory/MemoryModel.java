package memory;


import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MemoryModel {
    public static final int NUMBER_OF_REGISTERS = 8;
    public static final int STACK_POINTER_INDEX = 6;
    public static final int PROGRAM_COUNTER_INDEX = 7;


    public final RandomAccessMemory ram;
    public final RandomAccessMemory rom;

    /* TODO: same addresation ar `ram` */
    public final RandomAccessMemory vram;

    /* TODO: encapsulate */
    public final Word[] registers = new Word[NUMBER_OF_REGISTERS];


    public MemoryModel(MemSize ramSize, MemSize vramSize, Path romFile) throws IOException {
        this.ram = new RandomAccessMemory(ramSize);
        this.vram = new RandomAccessMemory(vramSize);
        Arrays.fill(registers, Word.ZERO);

        // must be in constructor because we need to know the rom size before initing it
        byte[] data = Files.readAllBytes(romFile);
        if (data.length % 2 != 0) {
            throw new IOException("A rom file must contain shorts (2n bytes)");
        }

        int romLength = data.length / 2;
        this.rom = new RandomAccessMemory(new MemSize(romLength));

        DataInputStream romStream = new DataInputStream(new ByteArrayInputStream(data));
        for (int index = 0; index < romLength; index++)
            rom.load(new Addr(index), new Word(romStream.readShort()));  // TODO: implement via `loadAll`
    }

}
