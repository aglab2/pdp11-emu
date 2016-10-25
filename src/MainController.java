import instruction.instuctions.INC;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import javafx.scene.image.WritableImage;
import memory.MemoryModel;
import memory.primitives.MemSize;
import memory.primitives.Word;
import tornadofx.Controller;
import videomanager.VideoManager;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Here is the start-up code and all the __data__.
 * All the property of `MainController` are accessible from the GUI.
 */

public class MainController extends Controller {
    public final MemoryModel memoryModel;
    public final WritableImage screen = new WritableImage(/*width*/ 256, /*height*/ 256);
    public final VideoManager videoManager;

    public MainController() throws IOException, URISyntaxException {
        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        this.memoryModel = new MemoryModel(new MemSize(1024 * 8), new MemSize(1024 * 8), path);
        this.videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);
    }

    public void startButtonHandler() {
        //Show off loading from data bus
        for (int i = 0; i < 1024 * 8; i++)
            this.memoryModel.bus.load(memoryModel.vramOffset + i, new Word((int) (Math.random() * 65535)));

        INC op = new INC(RegMode.Index, RegAddr.R0, Word.ZERO);
        op.apply(memoryModel);
        System.out.println("yeah");
    }

    public void pauseButtonHandler() {
    }

    public void resetButtonHandler() {
    }

    public void stepButtonHandler() {
    }
}
