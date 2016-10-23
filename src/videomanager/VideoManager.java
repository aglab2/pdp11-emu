package videomanager;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import memory.primitives.Word;

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
    ObservableList<Word> vramObservableList;

    public VideoManager(WritableImage screen, ObservableList<Word> vramObservableList){
        this.screen = screen;
        this.vramObservableList = vramObservableList;
        this.vramObservableList.addListener(new ListChangeListener<Word>() {
            @Override
            public void onChanged(Change<? extends Word> c) {
                System.out.println("A");
                while (c.next()) {
                    System.out.println("B");
                    if (c.wasPermutated()) {
                        System.out.println("C");
                    }
                    if (c.wasUpdated()) {
                        System.out.println("D");
                    }

                    /* if (c.wasPermutated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            //permutate
                        }
                    } else if (c.wasUpdated()) {
                        //update item
                    } else {
                        for (Word remitem : c.getRemoved()) {
                            remitem.remove(Outer.this);
                        }
                        for (Item additem : c.getAddedSubList()) {
                            additem.add(Outer.this);
                        }
                    }*/
                }
            }
        });

    }

    public void setPixel(int x, int y, int colorCode){
        screen.getPixelWriter().setColor(x, y, colors[colorCode]);
    }
}
