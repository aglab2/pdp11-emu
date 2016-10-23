package memory;


import memory.primitives.Addr;
import memory.primitives.MemSize;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MemoryModel {
    public static final MemSize NUMBER_OF_REGISTERS = new MemSize(8);
    public static final Addr STACK_POINTER_INDEX = new Addr(6);
    public static final Addr PROGRAM_COUNTER_INDEX = new Addr(7);


    public final ReadWriteMemory ram;
    public final ReadOnlyMemory rom;
    public final ReadWriteMemory registers = new MemoryStorage(NUMBER_OF_REGISTERS);

    /* TODO: same addresation ar `ram` */
    public final MemoryStorage vram;


    public MemoryModel(MemSize ramSize, MemSize vramSize, Path romFile) throws IOException {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);

        // must be in constructor because we need to know the rom size before initialising it
        byte[] bytes = Files.readAllBytes(romFile);

        try {
            this.rom = new MemoryStorage(bytes);
        } catch (ValidationException e) {
            throw new IOException("A rom file must contain shorts (2n bytes)");
        }
    }

}
