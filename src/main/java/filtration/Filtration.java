package filtration;

import GUI.MainGUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filtration {

    public BufferedImage convolutionallyVFilterImage(BufferedImage image, int[][] table) {
        BufferedImage copyImage = MainGUI.copyImage(image);
        int sum = 0;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                sum += table[i][j];
            }
        }
        int[][] red = new int[image.getWidth()][image.getHeight()];
        int[][] green = new int[image.getWidth()][image.getHeight()];
        int[][] blue = new int[image.getWidth()][image.getHeight()];
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                red[w][h] = color.getRed();
                green[w][h] = color.getGreen();
                blue[w][h] = color.getBlue();
            }
        }
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for (int w = 1; w < image.getWidth() - 1; w++) {
            for (int h = 1; h < image.getHeight() - 1; h++) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (w + i > image.getWidth() || h + j > image.getHeight())
                            continue;
                        sumR += red[w + i][h + j] * table[i + 1][j + 1];
                        sumG += green[w + i][h + j] * table[i + 1][j + 1];
                        sumB += blue[w + i][h + j] * table[i + 1][j + 1];
                    }
                }
                if (sum != 0) {
                    sumR = sumR / sum;
                    sumG = sumG / sum;
                    sumB = sumB / sum;
                }
                if (sumR > 255)
                    sumR = 255;
                if (sumR < 0)
                    sumR = 0;
                if (sumG > 255)
                    sumG = 255;
                if (sumG < 0)
                    sumG = 0;
                if (sumB > 255)
                    sumB = 255;
                if (sumB < 0)
                    sumB = 0;


                copyImage.setRGB(w, h, new Color(sumR, sumG, sumB).getRGB());
                sumR = 0;
                sumG = 0;
                sumB = 0;
            }
        }
        return copyImage;
    }

    public BufferedImage medianFilter(BufferedImage image, int windowSize) {
        BufferedImage copyImage = MainGUI.copyImage(image);
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                List<Integer> red = new ArrayList<>();
                List<Integer> green = new ArrayList<>();
                List<Integer> blue = new ArrayList<>();
                Color color;

                for (int i = 0; i < (windowSize / 2) + 1; i++) {
                    int w1 = w + i;
                    int w2 = w - i;
                    for (int j = 0; j < (windowSize / 2) + 1; j++) {
                        int h1 = h + j;
                        int h2 = h - j;
                        if (i == 0 && j == 0)
                            continue;
                        if (w1 < image.getWidth() - 1) {
                            if (h1 < image.getHeight() - 1) {
                                color = new Color(image.getRGB(w1, h1));
                                red.add(color.getRed());
                                green.add(color.getGreen());
                                blue.add(color.getBlue());
                                if (w2 > 0) {
                                    color = new Color(image.getRGB(w2, h1));
                                    red.add(color.getRed());
                                    green.add(color.getGreen());
                                    blue.add(color.getBlue());
                                }
                            }
                        }
                        if (w2 > 0) {
                            if (h2 > 0) {
                                color = new Color(image.getRGB(w2, h2));
                                red.add(color.getRed());
                                green.add(color.getGreen());
                                blue.add(color.getBlue());
                            }
                            if (h1 < image.getHeight() - 1) {
                                color = new Color(image.getRGB(w2, h1));
                                red.add(color.getRed());
                                green.add(color.getGreen());
                                blue.add(color.getBlue());
                            }
                        }

                        if (w2 > 0 && h2 > 0) {
                            color = new Color(image.getRGB(w2, h2));
                            red.add(color.getRed());
                            green.add(color.getGreen());
                            blue.add(color.getBlue());
                        }
                        if (w1 < image.getWidth() - 1 && h1 < image.getHeight() - 1) {
                            color = new Color(image.getRGB(w1, h1));
                            red.add(color.getRed());
                            green.add(color.getGreen());
                            blue.add(color.getBlue());
                        }
                    }
                }
                Collections.sort(red);
                Collections.sort(green);
                Collections.sort(blue);
                if (red.size() != 0 || blue.size() != 0 || green.size() != 0)
                    copyImage.setRGB(w, h, new Color(red.get(red.size() / 2), green.get(green.size() / 2), blue.get(green.size() / 2)).getRGB());

            }
        }
        return copyImage;
    }
}