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
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            return regAddr;
        }
    },
    AutoInc(2) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new RamAddr(addr);
        }
    },
    AutoDec(4) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).toAddr().dec();
            memory.registers.load(regAddr.address, newAddr);
            return new RamAddr(newAddr);
        }
    },
    Index(6) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Addr addr = new Addr(memory.registers.fetch(regAddr.address).value + nextWord.value);
            return new RamAddr(addr);
        }
    },
    DRegister(1) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            return new RamAddr(addr);
        }
    },
    DAutoInc(3) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new RamAddr(memory.ram.fetch(addr).toAddr());
        }
    },
    DAutoDec(5) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).dec().toAddr();
            memory.registers.load(regAddr.address, newAddr);
            return new RamAddr(memory.ram.fetch(newAddr).toAddr());
        }
    },
    DIndex(7) {
        @Override
        public GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Word reg = memory.registers.fetch(regAddr.address);
            Addr addr = new Addr(reg.value + nextWord.value);
            return new RamAddr(memory.ram.fetch(addr).toAddr());
        }
    };

    public final int value;

    RegMode(int value) {
        this.value = value;
    }

    public abstract GenAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord);

    public final boolean needsNextWord() {
        return this == Index || this == DIndex;
    }
}