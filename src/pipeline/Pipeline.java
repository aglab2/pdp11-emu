package pipeline;

import pipeline.microcode.MicroCode;

/**
 * Created by denis on 1/18/2017.
 */
public abstract class Pipeline {
    public int clock;

    public abstract void execute(MicroCode microCode);
    public abstract void finish(MicroCode microCode);
}
