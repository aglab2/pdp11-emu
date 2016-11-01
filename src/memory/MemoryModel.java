package memory;


import bus.MemoryBus;
import instruction.primitives.RegAddr;
import memory.primitives.Offset;
import memory.primitives.MemSize;
import memory.primitives.Word;

public class MemoryModel {
    public final int ramOffset;
    public final int vramOffset;
    public final int romOffset;
    public final int regOffset = 0x10000;
    public final int flagsOffset = 0xFFFF;

    /* TODO: Should this be private as we can access to them from bus? */
    public final RWMemory ram;
    public final Memory rom;
    public final MemoryStorage vram;
    public final RWMemory registers = new MemoryStorage(new MemSize(8));
    public final FlagsStorage flags = new FlagsStorage();

    public final MemoryBus bus;

    public MemoryModel(MemSize ramSize, MemSize vramSize, MemoryStorage rom) {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);
        this.rom = rom;
        this.bus = new MemoryBus();

        //All addresses are multiplied by 2 in real asm
        this.ramOffset = 0;
        this.bus.addRegion("ram", ramOffset, this.ram);

        this.vramOffset = this.ramOffset + this.ram.size.value * 2;
        this.bus.addRegion("vram", vramOffset, this.vram);

        this.romOffset = this.vramOffset + this.vram.size.value * 2;
        this.bus.addRegion("romList", romOffset, (MemoryStorage) this.rom);  /*TODO: security breach*/

        this.bus.addRegion("reg", regOffset, this.registers);
        this.bus.addRegion("flags", flagsOffset, this.flags);
    }
}
