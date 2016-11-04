package instruction.primitives;

import bus.BusAddr;
import com.sun.istack.internal.Nullable;
import memory.MemoryModel;
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

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return regAddr.name();
        }
    },
    DRegister() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            return new BusAddr(addr.value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return "(" + regAddr.name() + ")";
        }
    },
    AutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            memory.registers.load(regAddr.offset, addr.inc2());
            return new BusAddr(addr.value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return "(" + regAddr.name() + ")+";
        }
    },
    DAutoInc() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word addr = memory.registers.fetch(regAddr.offset);
            memory.registers.load(regAddr.offset, addr.inc2());
            return new BusAddr(memory.bus.fetch(addr.value).value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return "@(" + regAddr.name() + ")+";
        }
    },
    AutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word newAddr = memory.registers.fetch(regAddr.offset).dec2();
            memory.registers.load(regAddr.offset, newAddr);
            return new BusAddr(newAddr.value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return "-(" + regAddr.name() + ")";
        }
    },
    DAutoDec() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            Word newAddr = memory.registers.fetch(regAddr.offset).dec2();
            memory.registers.load(regAddr.offset, newAddr);
            return new BusAddr(memory.bus.fetch(newAddr.value).value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            return "@-(" + regAddr.name() + ")";
        }
    },
    Index() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            return new BusAddr(memory.registers.fetch(regAddr.offset).value + nextWord.value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;

            short signed = nextWord.toSigned();
            int abs = Math.abs(signed);
            return (signed < 0 ? "-" : "") + Integer.toOctalString(abs) + "(" + regAddr.name() + ")";
        }
    },
    DIndex() {
        @Override
        public BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            Word reg = memory.registers.fetch(regAddr.offset);
            return new BusAddr(memory.bus.fetch(reg.value + nextWord.value).value);
        }

        @Override
        public String getAssembler(RegAddr regAddr, @Nullable Word nextWord) {
            assert nextWord != null;
            short signed = nextWord.toSigned();
            int abs = Math.abs(signed);
            return "@" + (signed < 0 ? "-" : "") + Integer.toOctalString(abs) + "(" + regAddr.name() + ")";
        }
    };

    public final int value = ordinal();

    public abstract BusAddr apply(MemoryModel memory, RegAddr regAddr, @Nullable Word nextWord);

    public abstract String getAssembler(RegAddr regAddr, @Nullable Word nextWord);


    public final boolean needsIndex() {
        return this == Index || this == DIndex;
    }

    public static RegMode parse(int v) {
        return RegMode.values()[v & 0b111];
    }
}