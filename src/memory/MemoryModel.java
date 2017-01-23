package memory;


import bus.MemoryBus;
import instruction.primitives.RegAddr;
import memory.primitives.MemSize;
import memory.primitives.Word;

public class MemoryModel {
    public final int ramOffset          = 0x0000;
    public final int vramOffset         = 0x4000;
    public final int romOffset          = 0x8000;
    public final int regOffset          = 0x10000;
    public final int flagsOffset        = 0xFFFF;
    public final int interruptOffset    = 0xF800;

    /* TODO: Should this be private as we can access to them from bus? */
    public final RWMemory ram;
    public final Memory rom;
    public final MemoryStorage vram;
    public final RWMemory registers = new MemoryStorage(new MemSize(8));
    public final FlagsStorage flags = new FlagsStorage();
    public final RWMemory interruptTable = new MemoryStorage(new MemSize(1));

    public final MemoryBus bus;
    public final Cache cache;

    public MemoryModel(MemSize ramSize, MemSize vramSize, MemoryStorage rom) {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);
        this.rom = rom;
        this.bus = new MemoryBus();
        this.cache = new Cache(4, 1024);

        //All addresses are multiplied by 2 in real asm
        this.bus.addRegion("ram", ramOffset, this.ram);
        this.bus.addRegion("vram", vramOffset, this.vram);
        this.bus.addRegion("romList", romOffset, (MemoryStorage) this.rom);  /*TODO: security breach*/
        this.bus.addRegion("reg", regOffset, this.registers);
        this.bus.addRegion("flags", flagsOffset, this.flags);
        this.bus.addRegion("IDT", interruptOffset, this.interruptTable);

        System.out.println("Vram: " + this.vramOffset);
        System.out.println("Rom : " + this.ramOffset);
        System.out.println("Bus : " + this.romOffset);

        resetRegisters();
    }

    public void reset() {
        ram.clean();
        vram.clean();
        registers.clean();
        flags.clean();
        resetRegisters();
    }

    private void resetRegisters() {
        registers.load(RegAddr.PC.offset, new Word(romOffset));
        registers.load(RegAddr.SP.offset, new Word(ramOffset + ram.size.value * 2));
    }
}
