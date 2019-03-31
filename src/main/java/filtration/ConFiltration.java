package filtration;

import GUI.MainGUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ConFiltration {

    public BufferedImage filterImage(BufferedImage image, int[][] table) {
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
}
