import gui.MainView;
import javafx.application.Application;
import kotlin.jvm.JvmClassMappingKt;
import tornadofx.App;

/**
 * NOTE: all start-up code is in the `MainController`.
 * MVC architecture is used.
 */

public class Main extends App {

    public Main() {
        super(JvmClassMappingKt.getKotlinClass(MainView.class));
    }

    public static void main(String[] args) {
        Application.launch(Main.class);  // JavaFX start up procedure
    }
}
