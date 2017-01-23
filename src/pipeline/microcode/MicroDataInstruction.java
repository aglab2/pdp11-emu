package pipeline.microcode;

import bus.BusAddr;
import memory.Cache;

import java.util.List;

/**
 * Created by aglab2 on 1/18/2017.
 */
public abstract class MicroDataInstruction extends MicroInstruction {
    public MicroDataInstruction(int basicCost, List<BusAddr> addresses, Cache cache){
        this.cost = basicCost;
        for (BusAddr addr : addresses) {
            this.cost += cache.getCost(addr.value);
        }
    }
}
