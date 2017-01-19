package pipeline.microcode.instruction;

import bus.BusAddr;
import pipeline.microcode.MicroDataInstruction;

import java.util.List;

/**
 * Created by aglab2 on 1/18/2017.
 */
public class MicroDecode extends MicroDataInstruction {
    public MicroDecode(List<BusAddr> indexes){
        super(1, indexes);
    }
}