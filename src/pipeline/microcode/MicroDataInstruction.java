package pipeline.microcode;

import bus.BusAddr;

import java.util.List;

/**
 * Created by aglab2 on 1/18/2017.
 */
public abstract class MicroDataInstruction extends MicroInstruction {
    public MicroDataInstruction(int basicCost, List<BusAddr> addresses /*insert cache here*/){
        this.cost = basicCost;
        for (BusAddr addr : addresses) {
            this.cost += 1; //TODO: Code me :)
        }
    }
}
