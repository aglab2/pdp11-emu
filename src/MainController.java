import instruction.Instruction;
import interpreter.Executor;
import interpreter.Parser;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Here is the start-up code and all the __data__.
 * All the property of `MainController` are accessible from the GUI.
 */

public class MainController extends Controller {
    public final ObservableList<Path> romFiles;
    {
        List<Path> resources = findResources(Pattern.compile(".*\\.rom"));
        if(resources.isEmpty())
            resources.add(Paths.get(MainController.class.getResource("default.rom").toURI()));

        for(Path p : resources)
            if(p.endsWith("/default.rom")) {
                resources.remove(p);
                resources.add(0, p);
            }

        romFiles =  FXCollections.observableList(resources);
    }

    public final ObjectProperty<Path> romFile = new SimpleObjectProperty<>(romFiles.get(0));

    public final MemoryModel memoryModel
            = new MemoryModel(new MemSize(1024 * 8), new MemSize(1024 * 8),
                    new MemoryStorage(loadResource(romFile.get())));

    public final WritableImage screen = new WritableImage(/*width*/ 256, /*height*/ 256);
    public final VideoManager videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);
    public final Parser parser = new Parser();
    public final Executor executor = new Executor(memoryModel, parser);

    public final ObservableList<IndexedValue<Instruction>> instructions;


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
    }



    static byte[] loadResource(Path resource) throws IOException {
        return Files.readAllBytes(resource);
    }

    static List<Path> findResources(Pattern regex) {
        Predicate<String> predicate = regex.asPredicate();
        ArrayList<Path> paths = new ArrayList<>();
        try {
            URI uri = MainController.class.getResource("/").toURI();
            Stream<Path> walk = Files.walk(Paths.get(uri), 1);

            for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                Path path = it.next();
                if(predicate.test(path.toString()))
                    paths.add(path);
            }
        } catch (URISyntaxException | IOException e) {
            return paths;
        }

        return paths;
    }
}
