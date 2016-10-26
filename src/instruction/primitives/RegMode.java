package instruction.primitives;

import bus.BusAddr;
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
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            return new BusAddr(regAddr.address.value * 2 + memory.regOffset); //Registers also live on Bus
        }
    },
    AutoInc(2) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new BusAddr(addr.value);
        }
    },
    AutoDec(4) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).toAddr().dec();
            memory.registers.load(regAddr.address, newAddr);
            return new BusAddr(newAddr.value);
        }
    },
    Index(6) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Addr addr = new Addr(memory.registers.fetch(regAddr.address).value + nextWord.value);
            return new BusAddr(addr.value);
        }
    },
    DRegister(1) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            return new BusAddr(addr.value);
        }
    },
    DAutoInc(3) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new BusAddr(memory.ram.fetch(addr).toAddr().value);
        }
    },
    DAutoDec(5) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).dec().toAddr();
            memory.registers.load(regAddr.address, newAddr);
            return new BusAddr(memory.ram.fetch(newAddr).toAddr().value);
        }
    },
    DIndex(7) {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Word reg = memory.registers.fetch(regAddr.address);
            Addr addr = new Addr(reg.value + nextWord.value);
            return new BusAddr(memory.ram.fetch(addr).toAddr().value);
        }
    };

    public final int value;

    RegMode(int value) {
        this.value = value;
    }

    public abstract BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord);

    public final boolean needsNextWord() {
        return this == Index || this == DIndex;
    }
}