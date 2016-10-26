package interpreter;

import com.sun.istack.internal.Nullable;
import instruction.Instruction;
import instruction.instuctions.*;
import instruction.primitives.InstructionRange;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import memory.primitives.Word;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by aglab2 on 07/10/16.
 */


public class Parser {
    public final Class<?>[] declaredInstructions = new Class<?>[]{
            ADD.class, ASH.class, ASL.class, ASR.class,
            BEQ.class, BGE.class, BGT.class, BLE.class,
            BLT.class, BMI.class, BNE.class, BPL.class,
            BR.class, CLR.class, CMP.class, DEC.class,
            DIV.class, INC.class, MOV.class, MUL.class,
            SUB.class, SWAP.class, TST.class};

    public final Map<InstructionRange, Instruction> instructionRanges = new HashMap<>(declaredInstructions.length);
    {
        for(Class<?> cl: declaredInstructions) {
            Instruction i = tryGetDefaultInstance(cl);
            InstructionRange range = i.range;
            instructionRanges.put(range, i);
        }
    }

    public Instruction parce(Word word) {
        for(InstructionRange range : instructionRanges.keySet()) {
            if(range.contains(word)) {
                Instruction i = instructionRanges.get(range);
                return null; /*TODO*/
            }
        }
        throw new UnsupportedOperationException("Word " + word.fmtBinary() + " belongs to no known instruction");
    }

    /**
     * @param clazz a subclass of `Instruction`
     * @return      a dummy instance of `clazz`. The only permitted operation is getting Instruction#range.
     */
    public @Nullable
    Instruction tryGetDefaultInstance(Class<?> clazz) {
        Constructor<?> constructor = clazz.getConstructors()[0];
        Class<?>[] types = constructor.getParameterTypes();

        ArrayList<Object> parameters = new ArrayList<>();
        for(Class<?> t : types) {
            if(t == int.class)
                parameters.add(0);
            else if(t == Word.class)
                parameters.add(Word.ZERO);
            else if(t == RegMode.class)
                parameters.add(RegMode.Register);
            else if(t == RegAddr.class)
                parameters.add(RegAddr.R0);
            else
                parameters.add(null);
        }

        try {
            return ((Instruction) constructor.newInstance(parameters.toArray()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            return null;
        }
    }
}
