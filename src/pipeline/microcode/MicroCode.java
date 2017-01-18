package pipeline.microcode;

import bus.BusAddr;
import pipeline.microcode.instruction.MicroDecode;
import pipeline.microcode.instruction.MicroExecute;
import pipeline.microcode.instruction.MicroFetch;
import pipeline.microcode.instruction.MicroMemory;

import java.util.Set;

/**
 * Created by denis on 1/18/2017.
 */
public class MicroCode {
    public final MicroFetch fetch;
    public final MicroDecode decode;
    public final MicroMemory load;
    public final MicroExecute execute;
    public final MicroMemory store;

    public Set<BusAddr> read;
    public Set<BusAddr> write;

    public MicroCode(MicroFetch fetch, MicroDecode decode, MicroMemory load, MicroExecute execute, MicroMemory store,
                     Set<BusAddr> read, Set<BusAddr> write) {
        this.fetch = fetch;
        this.decode = decode;
        this.load = load;
        this.execute = execute;
        this.store = store;

        this.read = read;
        this.write = write;
    }

    public int getTotalCost() {
        return fetch.cost + decode.cost + load.cost + execute.cost + store.cost;
    }
}
