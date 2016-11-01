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
    Register() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            return new BusAddr(regAddr.address.value * 2 + memory.regOffset); //Registers also live on Bus
        }
    },
    DRegister() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            return new BusAddr(addr.value);
        }
    },
    AutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new BusAddr(addr.value);
        }
    },
    DAutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr addr = memory.registers.fetch(regAddr.address).toAddr();
            memory.registers.load(regAddr.address, addr.inc());
            return new BusAddr(memory.ram.fetch(addr).toAddr().value);
        }
    },
    AutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).toAddr().dec();
            memory.registers.load(regAddr.address, newAddr);
            return new BusAddr(newAddr.value);
        }
    },
    DAutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Addr newAddr = memory.registers.fetch(regAddr.address).dec().toAddr();
            memory.registers.load(regAddr.address, newAddr);
            return new BusAddr(memory.ram.fetch(newAddr).toAddr().value);
        }
    },
    Index() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Addr addr = new Addr(memory.registers.fetch(regAddr.address).value + nextWord.value);
            return new BusAddr(addr.value);
        }
    },
    DIndex() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Word reg = memory.registers.fetch(regAddr.address);
            Addr addr = new Addr(reg.value + nextWord.value);
            return new BusAddr(memory.ram.fetch(addr).toAddr().value);
        }
    };

    public final int value = ordinal();

    public abstract BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord);

    public final boolean needsIndex() {
        return this == Index || this == DIndex;
    }

    public static RegMode parse(int v) {
        return RegMode.values()[v & 0b111];
    }
}