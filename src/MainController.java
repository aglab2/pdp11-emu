import instruction.primitives.RegAddr;
import interpreter.Executor;
import javafx.scene.image.WritableImage;
import memory.MemoryModel;
import memory.MemoryStorage;
import memory.primitives.MemSize;
import memory.primitives.Word;
import tornadofx.Controller;
import videomanager.VideoManager;

import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Here is the start-up code and all the __data__.
 * All the property of `MainController` are accessible from the GUI.
 */

public class MainController extends Controller {
    public final MemoryModel memoryModel;

    public final WritableImage screen = new WritableImage(/*width*/ 256, /*height*/ 256);
    public final VideoManager videoManager;
    public final Executor executor;

    public MainController() throws IOException, URISyntaxException, ValidationException {
        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        MemoryStorage rom = new MemoryStorage(Files.readAllBytes(path));


        this.memoryModel = new MemoryModel(new MemSize(1024 * 8), new MemSize(1024 * 8), rom);
        this.videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);

        this.executor = new Executor(memoryModel);
//        executor.sleepMillisDelay = 100;
        memoryModel.registers.load(RegAddr.PC.offset, new Word(memoryModel.romOffset));
    }

    public void startButtonHandler() {
        executor.startExecution();
    }

    public void pauseButtonHandler() {
        executor.stopExecution();
    }

    public void resetButtonHandler() {
        memoryModel.flags.clean();
        memoryModel.registers.clean();
        memoryModel.ram.clean();
        memoryModel.vram.clean();
        memoryModel.registers.load(RegAddr.PC.offset, new Word(memoryModel.romOffset));
    }

    public void stepButtonHandler() {
        executor.executeStep();
    }
}
