package interpreter;

import bus.BusAddr;
import com.sun.javafx.application.PlatformImpl;
import instruction.Instruction;
import instruction.instuctions.WAIT;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import memory.MemoryModel;
import memory.primitives.Word;
import pipeline.LinearPipeline;
import pipeline.ParallelPipeline;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by voddan on 01/11/16.
 */
public class Executor {
    public final MemoryModel memory;
    private final Parser parser;

    public final Set<Integer> breakpointsAddresses = new HashSet<>();
    private boolean isFirstStep = true;
    private boolean isStepWaiting = false;
    private boolean isExecWaiting = false;

    public LinearPipeline linearPipeline = new LinearPipeline();
    public ParallelPipeline parallelPipeline = new ParallelPipeline();

    public ObjectProperty<SynchronisationMode> synchronisationMode = new SimpleObjectProperty<>(SynchronisationMode.OFF);
    public double milisecondsInOneTact = 0.1;

    public Executor(MemoryModel memory, Parser parser) {
        this.memory = memory;
        this.parser = parser;
    }

    public void clearCache() {
        parser.clearCache();
    }

    public boolean executeStep() {
        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        if(pc == Word.NaN) {
            System.out.format("Finished with: %d parallel, %d linear\n", parallelPipeline.clock, linearPipeline.clock);
            parallelPipeline.finish();
            linearPipeline.finish();
            System.out.format("Finished with: %d parallel, %d linear\n", parallelPipeline.clock, linearPipeline.clock);
            return false;
        }

        if (breakpointsAddresses.contains(pc.value) && !isFirstStep){
            memory.registers.load(RegAddr.PC.offset, Word.NaN);
            memory.registers.load(RegAddr.PC.offset, pc);
            isFirstStep = true;
            return false;
        }

        Word word0 = memory.bus.fetch(pc.value);
        if(word0 == null) return false;

        Word word1 = memory.bus.fetch(pc.value + 2);
        Word word2 = memory.bus.fetch(pc.value + 4);

        Instruction instruction = parser.parseInstructionCached(pc.value, word0, word1, word2);

        if (instruction.getClass() == WAIT.class && !isFirstStep) {
            isStepWaiting = stepService.isRunning();
            isExecWaiting = executeService.isRunning();
            isFirstStep = true;
            return false;
        }

        memory.registers.add(RegAddr.PC.offset, 2 * (1 + instruction.indexСapacity()));
        linearPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        parallelPipeline.execute(instruction.getMicrocode(new BusAddr(pc.value), memory));
        //if (parallelPipeline.clock != 0) System.out.format("%.4f\n", (double) linearPipeline.clock / (double) parallelPipeline.clock);

        instruction.execute(memory);
        isFirstStep = false;
        return true;
    }

    public final Service<Boolean> stepService = new Service<Boolean>() {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    final boolean[] result = new boolean[1];
                    PlatformImpl.runAndWait(() -> result[0] = executeStep());
                    return result[0];
                }
            };
        }
    };


    public final Service<Void> executeService = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            boolean graceEnd = false;

            return new Task<Void>() {
                @Override
                protected Void call() {
                    int stepsInBunch = 1;
                    switch(synchronisationMode.get()) {
                        case OFF: stepsInBunch = 200; break;
                        case Linear: stepsInBunch = 1; break;
                        case Parallel: stepsInBunch = 1; break;
                    }

                    while (true) {
                        final boolean[] keepGoing = new boolean[1];

                        int finalStepsInBunch = stepsInBunch;

                        PlatformImpl.runAndWait(() -> {
                            long clock = System.currentTimeMillis();
                            int linearClock = linearPipeline.clock;
                            int parallelClock = parallelPipeline.clock;

                            for (int i = 0; i < finalStepsInBunch; i++) {
                                boolean res = executeStep();
                                if(!res) {
                                    keepGoing[0] = false;
                                    return;
                                }
                            }
                            keepGoing[0] = true;

                            long difference;
                            switch(synchronisationMode.get()) {
                                case Linear: difference = linearPipeline.clock - linearClock; break;
                                case Parallel: difference = parallelPipeline.clock - parallelClock; break;
                                default: return;
                            }

                            long now = System.currentTimeMillis();
                            long compensateMilliseconds = Math.round(difference * milisecondsInOneTact) - (now - clock);

                            if(compensateMilliseconds > 0) {
                                try {
                                    System.out.println("compensate: " + compensateMilliseconds);
                                    Thread.sleep(compensateMilliseconds);
                                } catch (InterruptedException e) { }
                            } else {
                                System.out.println("emulation is slower than beats on " + (-compensateMilliseconds) + " ms");
                            }
                        });

                        if(!keepGoing[0] || isCancelled())
                            return null;
                    }
                }
            };
        }
    };


    public final void cancelAll() {
        stepService.cancel();
        executeService.cancel();
        linearPipeline = new LinearPipeline();
        parallelPipeline = new ParallelPipeline();

        memory.bus.load(memory.interruptOffset, Word.ZERO);

        isFirstStep = true;
    }

    public final void interrupt(int interruptCode, Word errorCode) {
        boolean stepRunning = stepService.isRunning();
        boolean execRunning = executeService.isRunning();

        if (!stepRunning && !execRunning && !isExecWaiting && !isStepWaiting) {
            return;
        }

        Word interruptHandlerPtr = memory.bus.fetch(memory.interruptOffset + interruptCode * 2);
        if (interruptHandlerPtr.value == 0) {
            if (isExecWaiting) executeService.restart();
            if (isStepWaiting) stepService.restart();
            isExecWaiting = false;
            isStepWaiting = false;
            return;
        }

        try {
            if (stepRunning) stepService.wait();
            if (execRunning) executeService.wait();
        }catch(Exception e){ }
        cancelAll();

        System.out.println("Interrupt " + interruptCode + ": " + errorCode);

        Word pc = memory.registers.fetch(RegAddr.PC.offset);
        Word ps = memory.bus.fetch(0xFFFE);

        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, ps);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, pc);
        RegMode.AutoDec.apply(memory, RegAddr.SP, null).load(memory, errorCode);

        memory.registers.load(RegAddr.PC.offset, interruptHandlerPtr);

        if (execRunning) executeService.restart();
        if (stepRunning) stepService.restart();
        if (isExecWaiting) executeService.restart();
        if (isStepWaiting) stepService.restart();
    }
}
