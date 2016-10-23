import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import memory.MemoryModel;
import memory.primitives.Addr;
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
    public final WritableImage screen = new WritableImage(/*width*/ 300, /*height*/ 300);
    public final VideoManager videoManager;

    public MainController() throws IOException, URISyntaxException {
        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        this.memoryModel = new MemoryModel(new MemSize(1024), new MemSize(1024), path);
        this.videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);
    }

    public void startButtonHandler() {
        this.memoryModel.vram.load(new Addr(0), new Word(123));
        System.out.println("start");
    }

    public void pauseButtonHandler() {
    }

    public void resetButtonHandler() {
    }

    public void stepButtonHandler() {
    }
}
