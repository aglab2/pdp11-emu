package pipeline.microcode.instruction;

import bus.BusAddr;
import pipeline.microcode.MicroDataInstruction;

import java.util.Arrays;

/**
 * Created by aglab2 on 1/18/2017.
 */
public class MicroFetch extends MicroDataInstruction {
    public MicroFetch(BusAddr pc){
        super(1, Arrays.asList(pc));
    }
}
