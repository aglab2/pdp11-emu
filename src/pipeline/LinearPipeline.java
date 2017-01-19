package pipeline;

import pipeline.microcode.MicroCode;

/**
 * Created by denis on 1/18/2017.
 */
public class LinearPipeline extends Pipeline {
    public LinearPipeline() {

    }

    public void execute(MicroCode microCode){
        this.clock += microCode.getTotalCost();
    }

    public void finish(){ }
}
