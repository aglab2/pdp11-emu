import instruction.instuctions.INC;
import instruction.primitives.RegAddr;
import instruction.primitives.RegMode;
import javafx.scene.image.WritableImage;
import memory.MemoryModel;
import memory.primitives.MemSize;
import memory.primitives.Word;
import tornadofx.Controller;
import videomanager.VideoManager;

import java.io.*;
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

    public MainController() throws IOException, URISyntaxException {
        URL defaultRom = MainController.class.getClassLoader().getResource("rom_default.hex");
        Path path = new File(defaultRom.toURI()).toPath();
        this.memoryModel = new MemoryModel(new MemSize(1024 * 8), new MemSize(1024 * 8), path);
        this.videoManager = new VideoManager(screen, memoryModel.vram.dataObservableList);
    }

    public void startButtonHandler() {
        try {
            URL vram = MainController.class.getClassLoader().getResource("dragon.vram");
            Path path = new File(vram.toURI()).toPath();
            DataInputStream is = new DataInputStream(new FileInputStream(path.toString()));

            //Show off loading from data bus
            for (int i = 0; i < 1024 * 8; i++)
                this.memoryModel.bus.load(memoryModel.vramOffset + i, new Word(is.readShort()));
        }catch (IOException e) {
            System.out.print(e);
        }
        catch (URISyntaxException e) {
            System.out.print(e);
        }
    }

    public void pauseButtonHandler() {
        try {
            URL vram = MainController.class.getClassLoader().getResource("dragon.zram");
            Path path = new File(vram.toURI()).toPath();
            DataInputStream is = new DataInputStream(new FileInputStream(path.toString()));

            int offset = 0;
            while(is.available() > 0) {
                int curCnt = new Word(is.readByte(), is.readByte()).value;
                Word curWord = new Word(is.readByte(), is.readByte()); //Big endian :)
                for (int i = 0; i < curCnt; i++, offset++) {
                    this.memoryModel.bus.load(memoryModel.vramOffset + offset, curWord);
                }
            }
            System.out.print(offset);
        }catch (IOException e) {
            System.out.print(e);
        }
        catch (URISyntaxException e) {
            System.out.print(e);
        }
    }

    public void resetButtonHandler() {
    }

    public void stepButtonHandler() {
    }
}
