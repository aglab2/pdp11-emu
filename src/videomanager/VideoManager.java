package videomanager;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.PixelWriter;
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

/*
    Image data is written line-by-line, so indexing is
    x = index % width; y = index / width;
    One byte sets 4 pixels, so pixel_index = 4*byte_index+k, k=0..3
 */


public class VideoManager {
    static Color[] colors = {Color.BLACK, Color.GRAY, Color.RED, Color.WHITE};

    private int width;
    private int height;
    private PixelWriter pixelWriter;
    private ObservableList<Word> vramObservableList;

    void drawPixel(int pixelIndex, int colorCode) {
        int x = pixelIndex % width;
        int y = Math.floorDiv(pixelIndex, width);
        pixelWriter.setColor(x, y, colors[colorCode]);
    }

    void drawByte(int index, byte colorByte) { //TODO: Make this function not require 2BPP
        drawPixel(index * 4 + 0, (colorByte & 0b11000000) >> 6);
        drawPixel(index * 4 + 1, (colorByte & 0b00110000) >> 4);
        drawPixel(index * 4 + 2, (colorByte & 0b00001100) >> 2);
        drawPixel(index * 4 + 3, (colorByte & 0b00000011) >> 0);
    }

    void drawWord(int addr, Word colorWord) {
        drawByte(2 * addr + 0, colorWord.lowByte());
        drawByte(2 * addr + 1, colorWord.highByte());
    }

    private ListChangeListener<Word> listener = new ListChangeListener<Word>() {
        @Override
        public void onChanged(Change<? extends Word> c) {
            ObservableList<? extends Word> list = c.getList();
            while (c.next()) {
                if (c.wasAdded()) {
                    for (int addr = c.getFrom(); addr < c.getTo(); addr++) {
                        Word colorWord = list.get(addr);

//                        drawByte(2 * addr + 0, colorWord.lowByte());
//                        drawByte(2 * addr + 1, colorWord.highByte());

                        drawWord(addr, colorWord);
                    }
                }
            }
        }
    };

    public VideoManager(WritableImage screen, ObservableList<Word> vramObservableList) {
        this.width = 256;
        this.height = 256;
        //this.width = (int) screen.getWidth();
        //this.height = (int) screen.getHeight();
        this.pixelWriter = screen.getPixelWriter();
        this.vramObservableList = vramObservableList;
        this.vramObservableList.addListener(this.listener);

        for (int i = 0; i < vramObservableList.size(); i++) {
            drawWord(i, vramObservableList.get(i));
        }
    }
}
