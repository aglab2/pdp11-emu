package keyboard;

import interpreter.Executor;
import javafx.scene.input.KeyEvent;
import memory.primitives.Word;

/**
 * Created by aglab2 on 1/11/2017.
 */
public class VirtualKeyboard{
    private Executor exec;

    public VirtualKeyboard(Executor exec){
        this.exec = exec;
    }

    public void processKey(KeyEvent e) {
        Word keypress = new Word((byte) e.getCode().ordinal(), e.isShiftDown() ? (byte) 1 : (byte) 0);
        if(!e.getCode().isModifierKey()) {
            exec.interrupt(0, keypress);
        }
    }
}