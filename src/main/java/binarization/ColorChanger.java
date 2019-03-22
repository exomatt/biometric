package binarization;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorChanger {
    public BufferedImage changeToGrey(BufferedImage image, int type) {
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                if (type == 0) {
                    int red = color.getRed();
                    image.setRGB(w, h, new Color(red, red, red).getRGB());
                }
                if (type == 1) {
                    int green = color.getGreen();
                    image.setRGB(w, h, new Color(green, green, green).getRGB());
                }
                if (type == 2) {
                    int blue = color.getBlue();
                    image.setRGB(w, h, new Color(blue, blue, blue).getRGB());
                } else {
                    int newValue = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
                    image.setRGB(w, h, new Color(newValue, newValue, newValue).getRGB());
                }
            }
        }
        return image;
    }
}
