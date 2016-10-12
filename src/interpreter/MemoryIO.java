package interpreter;

import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */
public class MemoryIO {
    private final MemoryModel memory;

    public MemoryIO(MemoryModel memory) {
        this.memory = memory;
    }


    public Addr getStackPointer() {
        return memory.registers[MemoryModel.STACK_POINTER_INDEX].toAddr();
    }

    /* should it be? Public? */
    private void setStackPointer(Addr value) {
        memory.registers[MemoryModel.STACK_POINTER_INDEX] = value;
    }



    public Addr getProgramCounter() {
        return memory.registers[MemoryModel.PROGRAM_COUNTER_INDEX].toAddr();
    }

    /* should it be? Public? */
    private void setProgramCounter(Addr value) {
        memory.registers[MemoryModel.PROGRAM_COUNTER_INDEX] = value;
    }



    public void stackPut(Word value) {
        memory.ram.load(getStackPointer(), value);
        setStackPointer(getStackPointer().inc());
    }

    public Word stackPop() {
        Word value = memory.ram.fetch(getStackPointer());
        setStackPointer(getStackPointer().dec());
        return value;
    }
}
