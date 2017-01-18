package pipeline.microcode;

/**
 * Created by denis on 1/18/2017.
 */
public abstract class MicroConstInstruction extends MicroInstruction{
    public MicroConstInstruction(int cost){
        this.cost = cost;
    }
}
