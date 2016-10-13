import gui.MainView;
import javafx.application.Application;
import kotlin.jvm.JvmClassMappingKt;
import tornadofx.App;

/**
 * Created by aglab2 on 07/10/16.
 */
public class Main extends App {
    public Main() {
        // some magic to initialise TornadoFX
        super(JvmClassMappingKt.getKotlinClass(MainView.class));
    }

    public static void main(String[] args) {
        Application.launch(Main.class);  // JavaFX start up procedure
    }
}
