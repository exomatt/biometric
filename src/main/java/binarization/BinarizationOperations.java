package binarization;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public BufferedImage bersenBinarization(BufferedImage image, int contrast_threshold, int set_threshold) {
        int contrast[][] = new int[image.getWidth()][image.getHeight()];
        int midgray[][] = new int[image.getWidth()][image.getHeight()];
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                List<Integer> list = new ArrayList<>();
                int value;
                if (w != 0) {
                    value = new Color(image.getRGB(w - 1, h)).getRed();
                    list.add(value);
                    if (h != image.getHeight() - 1) {
                        value = new Color(image.getRGB(w - 1, h + 1)).getRed();
                        list.add(value);
                    }
                }
                if (h != 0) {
                    value = new Color(image.getRGB(w, h - 1)).getRed();
                    list.add(value);
                    if (w != image.getWidth() - 1) {
                        value = new Color(image.getRGB(w + 1, h - 1)).getRed();
                        list.add(value);
                    }
                }
                if (w != 0 && h != 0) {
                    value = new Color(image.getRGB(w - 1, h - 1)).getRed();
                    list.add(value);
                }
                if (w != image.getWidth() - 1 && h != image.getHeight() - 1) {
                    value = new Color(image.getRGB(w + 1, h + 1)).getRed();
                    list.add(value);
                }
                if (w != image.getWidth() - 1) {
                    value = new Color(image.getRGB(w + 1, h)).getRed();
                    list.add(value);
                }
                if (h != image.getHeight() - 1) {
                    value = new Color(image.getRGB(w, h + 1)).getRed();
                    list.add(value);
                }
                Integer max = Collections.max(list);
                Integer min = Collections.min(list);
                midgray[w][h] = (min + max) / 2;
                contrast[w][h] = (max - min) / 2;
            }
        }
        int black = Color.black.getRGB();
        int white = Color.white.getRGB();
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                System.out.println(contrast[w][h]);
                System.out.println(midgray[w][h]);
                if (contrast[w][h] < contrast_threshold) {
                    image.setRGB(w, h, midgray[w][h] >= set_threshold ? white : black);
                } else {
                    image.setRGB(w, h, new Color(image.getRGB(w, h)).getRed() >= midgray[w][h] ? white : black);
                }
            }
        }


        return image;
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
