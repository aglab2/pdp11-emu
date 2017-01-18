package pipeline;

import pipeline.microcode.MicroCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by denis on 1/18/2017.
 */

//FDLES = Fetch Decode Load Execute Store
//This model does not differentiate each of FDLES, but it works with them as thing that has clock count
//By default each of FDLES can have only 1 or 0 process at time
//System can be relaxed by choosing the lowest clock and substracting that number from each basket.
//If any basket has 0 clock, move to the next one.

//We can add new elements into model only if Bernstein rule allows and F basket is free. As relaxation process is finite,
//by relaxing model, new element can be added by finite number of relaxations

public class FDLESModel {
    private MicroCode F = null;
    private MicroCode D = null;
    private MicroCode L = null;
    private MicroCode E = null;
    private MicroCode S = null;

    public FDLESModel() {
    }

    private int findMinimalClock() {
        List<Integer> clocks = new ArrayList<>();
        if (F != null && F.fetch.cost   != 0) clocks.add(F.fetch.cost);
        if (D != null && D.decode.cost  != 0) clocks.add(D.decode.cost);
        if (L != null && L.load.cost    != 0) clocks.add(L.load.cost);
        if (E != null && E.execute.cost != 0) clocks.add(E.execute.cost);
        if (S != null && S.store.cost   != 0) clocks.add(S.store.cost);
        if (clocks.isEmpty()) return -1;
        return Collections.min(clocks);
    }

    private void relax(int clock) { //TODO: Yes, this looks awful
        if (F != null && F.fetch.cost   != 0) F.fetch.cost    -= clock;
        if (D != null && D.decode.cost  != 0) D.decode.cost   -= clock;
        if (L != null && L.load.cost    != 0) L.load.cost     -= clock;
        if (E != null && E.execute.cost != 0) E.execute.cost  -= clock;
        if (S != null && S.store.cost   != 0) S.store.cost    -= clock;

        if (S != null && S.store.cost   == 0             ) {        S = null; }
        if (E != null && E.execute.cost == 0 && S == null) { S = E; E = null; }
        if (L != null && L.load.cost    == 0 && E == null) { E = L; L = null; }
        if (D != null && D.decode.cost  == 0 && L == null) { L = D; D = null; }
        if (F != null && F.fetch.cost   == 0 && D == null) { D = F; F = null; }
    }

    private int relax(){
        int clock = findMinimalClock();
        if (clock == -1) {
            relax(0);
            return 0;
        }else {
            relax(clock);
            return clock;
        }
    }

    int push(MicroCode microcode){
        int clock = 0;
        while (F != null) clock += relax();
        F = microcode;
        return clock;
    }

    void fini(){
        while(relax() != -1);
    }
}
