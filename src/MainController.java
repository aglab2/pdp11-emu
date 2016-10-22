import gui.MainView;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import memory.MemoryModel;
import memory.primitives.MemSize;
import tornadofx.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Here is the start-up code and all the __data__.
 * All the property of `MainController` are accessible from the GUI.
 * */

public class MainController extends Controller {
    public final MemoryModel memoryModel;
    public final WritableImage screen = new WritableImage(/*width*/ 300, /*height*/ 300);

    public MainController() throws IOException, URISyntaxException {
        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        this.memoryModel = new MemoryModel(new MemSize(1024), new MemSize(1024), path);
    }

    public void startButtonHandler() {
        System.out.println("start");
    }
    public void pauseButtonHandler() {}
    public void resetButtonHandler() {}
    public void stepButtonHandler() {}
}
