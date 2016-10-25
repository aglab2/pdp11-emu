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
    public final int regOffset;

    /* TODO: Should this be private as we can access to them from bus? */
    public final ReadWriteMemory ram;
    public final ReadOnlyMemory rom;
    public final ReadWriteMemory registers = new MemoryStorage(NUMBER_OF_REGISTERS);

    /* TODO: same addresation ar `ram` */
    public final MemoryStorage vram;

    public final MemoryBus bus;

    public MemoryModel(MemSize ramSize, MemSize vramSize, MemoryStorage rom) {
        this.ram = new MemoryStorage(ramSize);
        this.vram = new MemoryStorage(vramSize);
        this.rom = rom;
        this.bus = new MemoryBus();

        this.ramOffset = 0;
        this.bus.addRegion(ramOffset, (MemoryStorage) this.ram);

        this.vramOffset = this.ramOffset + ((MemoryStorage) this.ram).size.value;
        this.bus.addRegion(vramOffset, this.vram);

        this.romOffset = this.vramOffset + this.vram.size.value;
        this.bus.addRegion(romOffset, (MemoryStorage) this.rom);

        this.regOffset = 0x8000;
        this.bus.addRegion(regOffset, (MemoryStorage) this.registers);
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
