import gui.MainView;
import javafx.application.Application;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import tornadofx.App;
import videocard.ImageGenerator;

/**
 *  NOTE: all start-up code is in the `MainController`.
 *  MVC architecture is used.
 *  */

public class Main extends App {

    public Main() {
        super(JvmClassMappingKt.getKotlinClass(MainView.class));
    }

    public static void main(String[] args) {
        Application.launch(Main.class);  // JavaFX start up procedure
    }
}
