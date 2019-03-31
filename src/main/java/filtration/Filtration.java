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

    public BufferedImage kuwaharaFilter(BufferedImage image) {
        BufferedImage copyImage = MainGUI.copyImage(image);

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

        for (int w = 2; w < image.getWidth() - 2; w++) {
            for (int h = 2; h < image.getHeight() - 2; h++) {
                int minVarRedIndex = 0;
                int minVarGreenIndex = 0;
                int minVarBlueIndex = 0;

                double[] redAverage = new double[4];
                double[] greenAverage = new double[4];
                double[] blueAverage = new double[4];

                double[] redVar = new double[4];
                double[] greenVar = new double[4];
                double[] blueVar = new double[4];

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        redAverage[0] += red[w - 2 + i][h - 2 + j] / 9.0;
                        redAverage[1] += red[w + i][h - 2 + j] / 9.0;
                        redAverage[2] += red[w - 2 + i][h + j] / 9.0;
                        redAverage[3] += red[w + i][h + j] / 9.0;

                        greenAverage[0] += green[w - 2 + i][h - 2 + j] / 9.0;
                        greenAverage[1] += green[w + i][h - 2 + j] / 9.0;
                        greenAverage[2] += green[w - 2 + i][h + j] / 9.0;
                        greenAverage[3] += green[w + i][h + j] / 9.0;

                        blueAverage[0] += blue[w - 2 + i][h - 2 + j] / 9.0;
                        blueAverage[1] += blue[w + i][h - 2 + j] / 9.0;
                        blueAverage[2] += blue[w - 2 + i][h + j] / 9.0;
                        blueAverage[3] += blue[w + i][h + j] / 9.0;
                    }
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        redVar[0] += Math.pow(red[w - 2 + i][h - 2 + j] - redAverage[0], 2);
                        redVar[1] += Math.pow(red[w + i][h - 2 + j] - redAverage[1], 2);
                        redVar[2] += Math.pow(red[w - 2 + i][h + j] - redAverage[2], 2);
                        redVar[3] += Math.pow(red[w + i][h + j] - redAverage[3], 2);

                        greenVar[0] += Math.pow(green[w - 2 + i][h - 2 + j] - greenAverage[0], 2);
                        greenVar[1] += Math.pow(green[w + i][h - 2 + j] - greenAverage[1], 2);
                        greenVar[2] += Math.pow(green[w - 2 + i][h + j] - greenAverage[2], 2);
                        greenVar[3] += Math.pow(green[w + i][h + j] - greenAverage[3], 2);

                        blueVar[0] += Math.pow(blue[w - 2 + i][h - 2 + j] - blueAverage[0], 2);
                        blueVar[1] += Math.pow(blue[w + i][h - 2 + j] - blueAverage[1], 2);
                        blueVar[2] += Math.pow(blue[w - 2 + i][h + j] - blueAverage[2], 2);
                        blueVar[3] += Math.pow(blue[w + i][h + j] - blueAverage[3], 2);
                    }
                }

                for (int i = 1; i < 4; i++) {
                    if (redVar[minVarRedIndex] > redVar[i]) {
                        minVarRedIndex = i;
                    }
                    if (greenVar[minVarGreenIndex] > greenVar[i])
                        minVarGreenIndex = i;
                    if (blueVar[minVarBlueIndex] > blueVar[i])
                        minVarBlueIndex = i;
                }
                copyImage.setRGB(w, h, new Color((int) redAverage[minVarRedIndex], (int) greenAverage[minVarGreenIndex], (int) blueAverage[minVarBlueIndex]).getRGB());
            }
        }

        return copyImage;
    }
}