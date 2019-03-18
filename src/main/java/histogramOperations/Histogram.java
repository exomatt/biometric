package histogramOperations;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Histogram {
    public List<int[]> calculateHistograms(BufferedImage image) {
        int[] histogramRed = new int[256];
        int[] histogramGreen = new int[256];
        int[] histogramBlue = new int[256];
        int[] histogramAverage = new int[256];

        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                histogramRed[color.getRed()]++;
                histogramGreen[color.getGreen()]++;
                histogramBlue[color.getBlue()]++;
                histogramAverage[(color.getBlue() + color.getGreen() + color.getRed()) / 3]++;
            }
        }

        List<int[]> histograms = new ArrayList<>();
        histograms.add(histogramRed);
        histograms.add(histogramGreen);
        histograms.add(histogramBlue);
        histograms.add(histogramAverage);

        return histograms;
    }

    public void display(BufferedImage firstImage) {
        int[] xTable = new int[256];
        for (int i = 0; i < 256; i++) {
            xTable[i] = i;
        }

        List<int[]> data = calculateHistograms(firstImage);
        paintHistogram(xTable, data.get(0), "RED", Color.RED, firstImage);
        paintHistogram(xTable, data.get(1), "GREEN", Color.GREEN, firstImage);
        paintHistogram(xTable, data.get(2), "BLUE", Color.BLUE, firstImage);
        paintHistogram(xTable, data.get(3), "AVERAGE", Color.GRAY, firstImage);
    }

    public void displayOneType(BufferedImage firstImage, int type) {
        int[] xTable = new int[256];
        for (int i = 0; i < 256; i++) {
            xTable[i] = i;
        }

        List<int[]> data = calculateHistograms(firstImage);
        if (type == 0) {
            paintHistogram(xTable, data.get(0), "RED", Color.RED, firstImage);
        }
        if (type == 1) {
            paintHistogram(xTable, data.get(1), "GREEN", Color.GREEN, firstImage);
        }
        if (type == 2) {
            paintHistogram(xTable, data.get(2), "BLUE", Color.BLUE, firstImage);
        }
        if (type == 3) {
            paintHistogram(xTable, data.get(3), "Gray", Color.GRAY, firstImage);
        }
    }

    private void paintHistogram(int[] xTable, int[] data, String name, Color color, BufferedImage image) {
        CategoryChart chart = new CategoryChartBuilder().width(1200).height(1000).title(name).xAxisTitle("Value").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setXAxisLabelAlignmentVertical(Styler.TextAlignment.Centre);

        chart.getStyler().setSeriesColors(new Color[]{color});
        chart.addSeries(name, xTable, data);
        Thread t = new Thread(() -> {
            JFrame jFrame = new SwingWrapper(chart).displayChart();
            ScrollPane scrollPane = new ScrollPane();
            JLabel jLabel = new JLabel(new ImageIcon(image));
            jLabel.setSize(new Dimension(600, 400));
            scrollPane.add(jLabel);
            scrollPane.setSize(new Dimension(600, 400));
            jFrame.add(scrollPane, BorderLayout.WEST);
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        });
        t.start();
    }

    public double[] distribution(int[] histogram, double pixels) {
        double[] tableDistribution = new double[256];
        int counter = 1;
        for (int i = 0; i < tableDistribution.length; i++) {
            for (int j = 0; j < counter; j++) {
                tableDistribution[i] += histogram[j] / pixels;
            }
//            tableDistribution[i]=tableDistribution[i]/pixels;
            counter++;
        }
        return tableDistribution;

    }

    public int[] lookUpTableHistogramEqualization(double[] distribution) {
        int[] lookUpTable = new int[256];
        for (int i = 0; i < lookUpTable.length; i++) {
            lookUpTable[i] = (int) (((distribution[i] - distribution[0]) / (1 - distribution[0])) * (255 - 1));
        }
        return lookUpTable;
    }

    public BufferedImage histogramEqualization(int[] lookUpTable, BufferedImage image, int type) {
        return modifyImage(lookUpTable, image, type);
    }

    private BufferedImage modifyImage(int[] lookUpTable, BufferedImage image, int type) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] lookRed = lookUpTableHistogramStretching(image, 0);
        int[] lookGreen = lookUpTableHistogramStretching(image, 1);
        int[] lookBlue = lookUpTableHistogramStretching(image, 2);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));
                if (type == 0) {
                    int red = color.getRed();
                    int newValue = lookUpTable[red];
                    image.setRGB(i, j, new Color(newValue, color.getGreen(), color.getBlue()).getRGB());
                }
                if (type == 1) {
                    int green = color.getGreen();
                    int newValue = lookUpTable[green];
                    image.setRGB(i, j, new Color(color.getRed(), newValue, color.getBlue()).getRGB());
                } else if (type == 2) {
                    int blue = color.getBlue();
                    int newValue = lookUpTable[blue];
                    image.setRGB(i, j, new Color(color.getRed(), color.getGreen(), newValue).getRGB());
                } else {
                    int blue = color.getBlue();
                    int green = color.getGreen();
                    int red = color.getRed();

                    int newValueRed = lookRed[red];
                    int newValueBlue = lookBlue[blue];
                    int newValueGreen = lookGreen[green];
                    image.setRGB(i, j, new Color(newValueRed, newValueGreen, newValueBlue).getRGB());
                }
            }
        }
        return image;
    }

    ///todo naprawic kurłą
    public BufferedImage histogramStretching(BufferedImage image, int type) {
        int[] lookUpTableHistogramStretching = lookUpTableHistogramStretching(image, type);
        BufferedImage modifyImage = modifyImage(lookUpTableHistogramStretching, image, type);
        return modifyImage;
    }

    public int[] lookUpTableHistogramStretching(BufferedImage image, int type) {
        List<int[]> calculateHistograms = calculateHistograms(image);
        int[] table;
        if (type == 0) {
            table = calculateHistograms.get(0);
        } else if (type == 1) {
            table = calculateHistograms.get(1);
        } else if (type == 2) {
            table = calculateHistograms.get(2);
        } else {
            table = calculateHistograms.get(3);
        }
        List<Integer> listHistogram = Arrays.stream(table).boxed().collect(Collectors.toList());
        Integer min = 0;
        Integer max = 255;
        int temp = max - min;
        for (int i = 0; i < table.length; i++) {
            int newValue = (int) ((table[i] - min) / (double) temp) * 255;
            if (newValue > 255) {
                newValue = 255;
            }
            table[i] = newValue;
        }
        return table;
    }

    public BufferedImage stretchingHistogram(BufferedImage image) {
        List<int[]> calculateHistograms = calculateHistograms(image);
        int[] reds = calculateHistograms.get(0);
        int[] greens = calculateHistograms.get(1);
        int[] blues = calculateHistograms.get(2);
        List<Integer> listHistogramRed = Arrays.stream(reds).boxed().collect(Collectors.toList());
        List<Integer> listHistogramGreen = Arrays.stream(greens).boxed().collect(Collectors.toList());
        List<Integer> listHistogramBlue = Arrays.stream(blues).boxed().collect(Collectors.toList());
        int minRed = 0;
        int minGreen = 0;
        int minBlue = 0;
        for (int i = 0; i < listHistogramGreen.size(); i++) {
            if (!listHistogramGreen.get(i).equals(0)) {
                minGreen = i;
                break;
            }
        }
        for (int i = 0; i < listHistogramRed.size(); i++) {
            if (!listHistogramRed.get(i).equals(0)) {
                minRed = i;
                break;
            }
        }
        for (int i = 0; i < listHistogramBlue.size(); i++) {
            if (!listHistogramBlue.get(i).equals(0)) {
                minBlue = i;
                break;
            }
        }
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        Collections.reverse(listHistogramBlue);
        Collections.reverse(listHistogramGreen);
        Collections.reverse(listHistogramRed);
        maxGreen = getMax(listHistogramGreen);
        maxRed = getMax(listHistogramRed);
        maxBlue = getMax(listHistogramBlue);
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int newRed = (red - minRed) / (maxRed - minRed) * 255;
                int newBlue = (green - minGreen) / (maxBlue - minBlue) * 255;
                int newGreen = (blue - minBlue) / (maxGreen - minGreen) * 255;
                Color newColor = new Color(newRed, newGreen, newBlue);
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        return image;
    }

    private int getMax(List<Integer> listHistogram) {
        int maxGreen = 0;
        for (int i = 0; i < listHistogram.size(); i++) {
            if (!listHistogram.get(i).equals(0)) {
                maxGreen = 255 - i;
                break;
            }
        }
        return maxGreen;
    }
}

