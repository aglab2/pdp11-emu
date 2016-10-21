import memory.MemoryModel;
import memory.primitives.MemSize;
import tornadofx.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Here is the start-up code and all the data.
 * All the property of `MainController` are accessible from the GUI.
 * */

public class MainController extends Controller {


    public final MemoryModel memoryModel;

    public MainController() throws IOException, URISyntaxException {
        System.out.println("Hello, world!");

        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        this.memoryModel = new MemoryModel(new MemSize(1024), new MemSize(1024), path);
    }
}
