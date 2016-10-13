import memory.MemoryModel;
import memory.primitives.MemSize;
import tornadofx.Controller;

/**
 * Here is the start-up code and all the data.
 * All the property of `MainController` are accessible from the GUI.
 * */

public class MainController extends Controller {
    public final MemoryModel memoryModel = new MemoryModel(new MemSize(1024), new MemSize(1024));

    public MainController() {
        System.out.println("Hello, world!");
    }
}
