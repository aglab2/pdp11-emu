package pipeline.microcode.instruction;

import bus.BusAddr;
import pipeline.microcode.MicroDataInstruction;

import java.util.List;

/**
 * Created by denis on 1/18/2017.
 */
public class MicroMemory extends MicroDataInstruction {
    public MicroMemory(List<BusAddr> addresses){
        super(0, addresses);
    }
}