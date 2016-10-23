package videomanager;

import javafx.collections.ObservableList;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

/**
 * Created by aglab2 on 21/10/16.
 */

/*
  Image size is 256x256, 2 bits per pixel
  This will take 256*256/4/1024=16 kB of memory
*/

public class VideoManager {
    WritableImage screen;
    Color[] colors  = {Color.BLACK, Color.DARKGRAY, Color.GRAY, Color.BLACK};

    public VideoManager(WritableImage screen, ObservableList<Integer> list){
        this.screen = screen;
    }

    public void setPixel(int x, int y, int colorCode){
        screen.getPixelWriter().setColor(x, y, colors[colorCode]);
    }
}
