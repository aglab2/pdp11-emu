package memory;


import bus.MemoryBus;
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

    public final int ramOffset;
    public final int vramOffset;
    public final int romOffset;
    public final int regOffset;

    /* TODO: Should this be private as we can access to them from bus? */
    public final ReadWriteMemory ram;
    public final ReadOnlyMemory rom;
    public final ReadWriteMemory registers = new MemoryStorage(NUMBER_OF_REGISTERS);

    /* TODO: same addresation ar `ram` */
    public final MemoryStorage vram;

    public final MemoryBus bus;

    public MemoryModel(MemSize ramSize, MemSize vramSize, Path romFile) throws IOException {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);
        this.bus = new MemoryBus();

        // must be in constructor because we need to know the rom size before initialising it
        byte[] bytes = Files.readAllBytes(romFile);

        try {
            this.rom = new MemoryStorage(bytes);
        } catch (ValidationException e) {
            throw new IOException("A rom file must contain shorts (2n bytes)");
        }


        this.ramOffset = 0;
        this.bus.addRegion(ramOffset, (MemoryStorage) this.ram);

        this.vramOffset = this.ramOffset + ((MemoryStorage) this.ram).size.value;
        this.bus.addRegion(vramOffset, this.vram);

        this.romOffset = this.vramOffset + this.vram.size.value;
        this.bus.addRegion(romOffset, (MemoryStorage) this.rom);

        this.regOffset = 0x8000;
        this.bus.addRegion(regOffset, (MemoryStorage) this.registers);


        System.out.printf("RAM : 0x%08X\n", ramOffset);
        System.out.printf("VRAM: 0x%08X\n", vramOffset);
        System.out.printf("ROM : 0x%08X\n", romOffset);
        System.out.printf("REG : 0x%08X\n", regOffset);
    }

}
