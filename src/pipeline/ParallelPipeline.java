package pipeline;

import pipeline.microcode.MicroCode;

import java.util.List;
import java.util.Set;

/**
 * Created by denis on 1/18/2017.
 */

public class ParallelPipeline extends Pipeline {
    List<MicroCode> currentInstructions;
    private final FDLESModel fdles;

    public ParallelPipeline() {
        fdles = new FDLESModel();
    }

    public void execute(MicroCode microCode){
        clock += fdles.push(microCode);
    }

    public void finish() { clock += fdles.finish(); }
}
