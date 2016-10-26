package memory;


import bus.MemoryBus;
import memory.primitives.Addr;
import memory.primitives.MemSize;
import memory.primitives.Word;

public class MemoryModel {
    public static final MemSize NUMBER_OF_REGISTERS = new MemSize(8);
    public static final Addr STACK_POINTER_INDEX = new Addr(6);
    public static final Addr PROGRAM_COUNTER_INDEX = new Addr(7);

    public final int ramOffset;
    public final int vramOffset;
    public final int romOffset;
    public final int regOffset = 0x8000;
    public final int flagsOffset = 0xFFFF;

    /* TODO: Should this be private as we can access to them from bus? */
    public final RWMemory ram;
    public final Memory rom;
    public final MemoryStorage vram;
    public final RWMemory registers = new MemoryStorage(NUMBER_OF_REGISTERS);
    public final FlagsStorage flags = new FlagsStorage();

    public final MemoryBus bus;

    public MemoryModel(MemSize ramSize, MemSize vramSize, MemoryStorage rom) {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);
        this.rom = rom;
        this.bus = new MemoryBus();

        this.ramOffset = 0;
        this.bus.addRegion(ramOffset, this.ram);

        this.vramOffset = this.ramOffset + this.ram.size.value;
        this.bus.addRegion(vramOffset, this.vram);

        this.romOffset = this.vramOffset + this.vram.size.value;
        this.bus.addRegion(romOffset, (MemoryStorage) this.rom);  /*TODO: security breach*/

        this.bus.addRegion(regOffset, this.registers);
        this.bus.addRegion(flagsOffset, this.flags);
    }

    public Addr getStackPointer() {
        return registers.fetch(MemoryModel.STACK_POINTER_INDEX).toAddr();
    }

    public void setStackPointer(Addr value) {
        registers.load(MemoryModel.STACK_POINTER_INDEX, value);
    }


    public Addr getProgramCounter() {
        return registers.fetch(MemoryModel.PROGRAM_COUNTER_INDEX).toAddr();
    }

    public void setProgramCounter(Addr value) {
        registers.load(MemoryModel.PROGRAM_COUNTER_INDEX, value);
    }


    public void stackPut(Word value) {
        ram.load(getStackPointer(), value);
        setStackPointer(getStackPointer().inc());
    }

    public Word stackPop() {
        Word value = ram.fetch(getStackPointer());
        setStackPointer(getStackPointer().dec());
        return value;
    }
}
