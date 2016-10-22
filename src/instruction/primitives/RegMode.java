package instruction.primitives;

import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Addr;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */

/* pdp11-40.pdf page 44*/

public enum RegMode {
    Register(0) {
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            return memory.registers.fetch(index.address).toAddr();
        }
    },
    AutoInc(2){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(index.address).toAddr();
            memory.registers.load(index.address, addr.inc());
            return addr;
        }
    },
    AutoDec(4){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(index.address).toAddr().dec();
            memory.registers.load(index.address,newAddr);
            return newAddr;
        }
    },
    Index(6){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            assert nextWord != null;
            return new Addr(memory.registers.fetch(index.address).value + nextWord.value);
        }
    },
    DRegister(1){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(index.address).toAddr();
            return memory.ram.fetch(addr).toAddr();
        }
    },
    DAutoInc(3){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(index.address).toAddr();
            Word content = memory.ram.fetch(addr);
            memory.ram.load(addr, content.inc());
            return content.toAddr();
        }
    },
    DAutoDec(5){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(index.address).toAddr();
            Word newContent = memory.ram.fetch(addr).dec();
            memory.ram.load(addr, newContent);
            return newContent.toAddr();
        }
    },
    DIndex(7){
        @Override
        Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord) {
            assert nextWord != null;
            Addr addr = memory.registers.fetch(index.address).toAddr();
            return new Addr(memory.ram.fetch(addr).value + nextWord.value);
        }
    };

    public final int value;

    RegMode(int value) {
        this.value = value;
    }

    abstract Addr apply(MemoryModel memory, RegIndex index, @Nullable Word nextWord);

}