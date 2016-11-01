package instruction.primitives;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
import memory.primitives.Offset;
import memory.primitives.Word;

/**
 * Created by voddan on 12/10/16.
 */

/* pdp11-40.pdf page 44*/

public enum RegMode {
    Register() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            return new BusAddr(memory.regOffset, regAddr.offset);
        }
    },
    DRegister() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            return new BusAddr(addr.value);
        }
    },
    AutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            memory.registers.load(regAddr.offset, addr.inc2());
            return new BusAddr(addr.value);
        }
    },
    DAutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            memory.registers.load(regAddr.offset, addr.inc2());
            return new BusAddr(memory.bus.fetch(addr.value).value);
        }
    },
    AutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word newAddr = memory.registers.fetch(regAddr.offset).dec2();
            memory.registers.load(regAddr.offset, newAddr);
            return new BusAddr(newAddr.value);
        }
    },
    DAutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word newAddr = memory.registers.fetch(regAddr.offset).dec2();
            memory.registers.load(regAddr.offset, newAddr);
            return new BusAddr(memory.bus.fetch(newAddr.value).value);
        }
    },
    Index() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            return new BusAddr(memory.registers.fetch(regAddr.offset).value + nextWord.value);
        }
    },
    DIndex() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Word reg = memory.registers.fetch(regAddr.offset);
            return new BusAddr(memory.bus.fetch(reg.value + nextWord.value).value);
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