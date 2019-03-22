package binarization;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BinarizationOperations {
    public BufferedImage userValueBinarization(BufferedImage img, int threshold) {
        for (int w = 0; w < img.getWidth(); w++) {
            for (int h = 0; h < img.getHeight(); h++) {
                Color c = new Color(img.getRGB(w, h));
                if (c.getRed() >= threshold) img.setRGB(w, h, Color.BLACK.getRGB());
                else img.setRGB(w, h, Color.WHITE.getRGB());
            }
        }
        return img;
    }
}
