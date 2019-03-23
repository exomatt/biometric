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

    public BufferedImage bersenBinarization(BufferedImage img, int threshold, int windowSize) {
        return null;
    }

    public int thresholdOtsu(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        int temp = 0;
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                pixels[temp] = new Color(image.getRGB(w, h)).getRed();
                temp++;
            }
        }

        int threshold = 0;
        double maxVariance = 0;
        for (int i = 0; i < 256; i++) {
            double targetGrey = 0;
            double bgGrey = 0;
            double target = 0;
            double bg = 0;
            for (int j = 0; j < pixels.length; j++) {
                if (pixels[j] < i) {
                    targetGrey = targetGrey + pixels[j];
                    target++;
                } else {
                    bgGrey = bgGrey + pixels[j];
                    bg++;
                }
            }
            double grey = (targetGrey + bgGrey) / pixels.length;
            targetGrey = targetGrey / target;
            bgGrey = bgGrey / bg;
            target = target / pixels.length;
            bg = bg / pixels.length;
            double variance = target * (Math.pow(targetGrey - grey, 2) + bg * (Math.pow(bgGrey - grey, 2)));
            if (variance > maxVariance) {
                maxVariance = variance;
                threshold = i;
            }
        }
        return threshold;
    }

}
