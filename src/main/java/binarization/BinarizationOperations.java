package binarization;

import histogramoperations.Histogram;

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

    public int otsuThreshold(BufferedImage image) {
        int[] histogram = new Histogram().calculateHistograms(image).get(0);
        double[] inClassVariance = new double[256];
        for (int i = 0; i < 255; i++) {
            inClassVariance[i] = 0.0;
        }
        int pixelNumber = image.getWidth() * image.getHeight();
        double temp_ = 2 / (double) 255;
        int hist_threshold = (int) Math.ceil(temp_);
        for (int i = 0; i < 255; i++) {
            double varB = 0.0;
            double varF = 0.0;
            int sumF = 0;
            int sumB = 0;
            double wF = 0.0;
            double meanF = 0.0;
            double wB = 0.0;
            double meanB = 0.0;
            for (int j = 0; j < i; j++) { //background
                int currentValue = histogram[j];
                //calculate weight
                wB += currentValue;
                //calculate mean
                meanB += (j * currentValue);
                //get the total of pixel in range
                sumB += currentValue;
            }
            wB = wB / pixelNumber;
            meanB = meanB / sumB;
            //calculate background variance
            for (int j = 0; j < i; j++) { //background
                int currentValue = histogram[j];
                varB += (Math.pow((j - meanB), 2) * currentValue);
            }
            varB = varB / sumB;

            for (int j = i; j < 255; j++) { //background
                int currentValue = histogram[j];
                //calculate weight
                wF += currentValue;
                //calculate mean
                meanF += (j * currentValue);
                //get the total of pixel in range
                sumF += currentValue;
            }
            wF = wF / pixelNumber;
            meanF = meanF / sumF;
            //calculate foreground variance
            for (int j = i; j < 255; j++) { //foreground
                int currentValue = histogram[j];
                varF += (Math.pow((j - meanF), 2) * currentValue) / wF;
            }
            varF = varF / sumF;
            double inClassVar = (wB * Math.pow(varB, 2)) + (wF * Math.pow(varF, 2));
            inClassVariance[i] = inClassVar;
        }
        int result = 0;
        double temp_min = Double.MAX_VALUE;
        for (int i = 0; i < histogram.length; i++) {
            if (inClassVariance[i] < temp_min) {
                temp_min = inClassVariance[i];
                result = i;
            }
        }
        int threshold = hist_threshold * (result + 1);
        return threshold;
    }

    public int ostsuThreshold(BufferedImage img) {
        int[] histogram = new Histogram().calculateHistograms(img).get(0);
        int pixelNumber = img.getWidth() * img.getHeight();
        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histogram[t];
        float sumB = 0;
        int wB = 0;
        int wF = 0;
        float varMax = 0;
        int threshold = 0;
//        float[] vB = new float[256];
//        float[] vF = new float[256];


        for (int t = 0; t < 256; t++) {
            // Weight Background
            wB += histogram[t];
            if (wB == 0) continue;
            // Weight Foreground
            wF = pixelNumber - wB;
            if (wF == 0) break;

            sumB += (float) (t * histogram[t]);
            // Mean Background
            float mB = sumB / wB;
            // Mean Foreground
            float mF = (sum - sumB) / wF;
            //Variance Background
            float vB = (float) (wB * Math.pow((t - mB), 2) / wB);
            //Variance Foreground
            float vF = (float) (wF * Math.pow((t - mF), 2) / wF);
            // Calculate Between Class Variance
            float varBetween = (float) (wB * Math.pow(vB, 2) + wF * Math.pow(vF, 2));
//            float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);
            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }
}
