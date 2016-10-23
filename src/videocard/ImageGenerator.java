package videocard;

import java.awt.image.BufferedImage;

/**
 * Created by aglab2 on 21/10/16.
 */

/*
  Image size is 256x256, 2 bits per pixel
  This will take 256*256/4/1024=16 kB of memory
*/

public class ImageGenerator {
    public BufferedImage image;

    public ImageGenerator(int size) {
        image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
    }

    public void setPixel(int x, int y, int color) {
        image.setRGB(x, y, color);
    }
}
