import instruction.Instruction;
import instruction.primitives.RegAddr;
import interpreter.Executor;
import interpreter.Parser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.WritableImage;
import memory.MemoryModel;
import memory.MemoryStorage;
import memory.primitives.MemSize;
import memory.primitives.Word;
import org.fxmisc.easybind.EasyBind;
import tornadofx.Controller;
import util.IndexedValue;
import util.Util;
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
    public final MemoryModel memoryModel
            = new MemoryModel(new MemSize(1024 * 8), new MemSize(1024 * 8),
                    new MemoryStorage(loadResource("rom_default.hex")));

    public final WritableImage screen = new WritableImage(/*width*/ 256, /*height*/ 256);
    public final VideoManager videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);
    public final Parser parser = new Parser();
    public final Executor executor = new Executor(memoryModel, parser);

    public final ObservableList<IndexedValue<Instruction>> instructions;

    public final BooleanProperty executorPlays = new SimpleBooleanProperty(false);
    public final BooleanBinding executorIsHalted = Bindings.equal(
            Bindings.valueAt(memoryModel.registers.dataObservableList, RegAddr.PC.offset.value), Word.NaN);


    public MainController() throws IOException, URISyntaxException, ValidationException {
        ObservableList<Word> data = memoryModel.rom.dataObservableList;

        FilteredList<IndexedValue<Word>> valuables = Util.withIndices(data)
                .filtered((iv) -> {
                    Word prev = Util.getOrNull(data, iv.index - 1);
                    return iv.value.value != 0 || (prev != null && prev.value != 0);
                });

        instructions = EasyBind.map(valuables, iv ->
                new IndexedValue<>(iv.index,
                        parser.parseInstruction(
                                iv.value, Util.getOrNull(data, iv.index + 1), Util.getOrNull(data, iv.index + 2))));

//        executor.sleepMillisDelay = 1000;
        memoryModel.registers.load(RegAddr.PC.offset, new Word(memoryModel.romOffset));
    }

    public void startButtonHandler() {
        executorPlays.set(true);
        executor.execute();
        executorPlays.set(false);
    }

    public void pauseButtonHandler() {
        executor.stopExecution();
        executorPlays.set(false);
    }

    public void resetButtonHandler() {
        pauseButtonHandler();
        memoryModel.flags.clean();
        memoryModel.registers.clean();
        memoryModel.ram.clean();
        memoryModel.vram.clean();
        memoryModel.registers.load(RegAddr.PC.offset, new Word(memoryModel.romOffset));
    }

    public void stepButtonHandler() {
        pauseButtonHandler();
        executor.executeStep();
    }

    static byte[] loadResource(String name) throws URISyntaxException, IOException {
        URL defaultRom = MainController.class.getClassLoader().getResource(name);
        Path path = new File(defaultRom.toURI()).toPath();
        return Files.readAllBytes(path);
    }
}
