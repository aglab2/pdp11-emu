package pipeline.microcode;

import pipeline.microcode.instruction.MicroDecode;
import pipeline.microcode.instruction.MicroExecute;
import pipeline.microcode.instruction.MicroFetch;
import pipeline.microcode.instruction.MicroMemory;

/**
 * Created by denis on 1/18/2017.
 */
public class MicroCode {
    public final MicroFetch fetch;
    public final MicroDecode decode;
    public final MicroMemory load;
    public final MicroExecute execute;
    public final MicroMemory store;

    public MicroCode(MicroFetch fetch, MicroDecode decode, MicroMemory load, MicroExecute execute, MicroMemory store) {
        this.fetch = fetch;
        this.decode = decode;
        this.load = load;
        this.execute = execute;
        this.store = store;
    }

    public int getTotalCost() {
        return fetch.cost + decode.cost + load.cost + execute.cost + store.cost;
    }
}
