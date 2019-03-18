package histogramOperations;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

    public int[] lookUpTableHistogramEqualization(int[] histogram, double pixels) {
        int[] lookUpTable = new int[256];
        int sum = 0;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            int val = (int) (sum * 255 / pixels);
            if (val > 255) {
                lookUpTable[i] = 255;
            } else lookUpTable[i] = val;
        }
        return lookUpTable;
    }

    public BufferedImage histogramEqualization(int[] lookUpTable, BufferedImage image, int type) {
        return modifyImage(lookUpTable, image, type);
    }

    private BufferedImage modifyImage(int[] lookUpTable, BufferedImage image, int type) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<int[]> calculateHistograms = calculateHistograms(image);
        int[] lookRed = lookUpTableHistogramEqualization(calculateHistograms.get(0), image.getWidth() * image.getHeight());
        int[] lookGreen = lookUpTableHistogramEqualization(calculateHistograms.get(1), image.getWidth() * image.getHeight());
        int[] lookBlue = lookUpTableHistogramEqualization(calculateHistograms.get(2), image.getWidth() * image.getHeight());
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

    public BufferedImage stretchingHistogram(BufferedImage image) {
        List<int[]> calculateHistograms = calculateHistograms(image);
        int[] reds = calculateHistograms.get(0);
        int[] greens = calculateHistograms.get(1);
        int[] blues = calculateHistograms.get(2);
        int minRed;
        int minGreen;
        int minBlue;

        minRed = getMin(reds);
        minGreen = getMin(greens);
        minBlue = getMin(blues);

        int maxRed;
        int maxGreen;
        int maxBlue;
        maxGreen = getMax(reds);
        maxRed = getMax(greens);
        maxBlue = getMax(blues);

        int[] lutRed;
        int[] lutGreen;
        int[] lutBlue;

        lutRed = getLUT(reds, maxRed, minRed);
        lutGreen = getLUT(greens, maxGreen, minGreen);
        lutBlue = getLUT(blues, maxBlue, minBlue);

        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                Color newColor = new Color(lutRed[red], lutGreen[green], lutBlue[blue]);
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        return image;
    }

    private int[] getLUT(int[] histogram, int max, int min) {
        int[] lut = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            lut[i] = (int) ((i - min) * ((float) 255.0) / ((float) (max - min)));
        }
        return lut;
    }

    private int getMin(int[] histogram) {
        int min = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] == 0) {
                min = i + 1;
            } else {
                break;
            }
        }
        return min;
    }

    private int getMax(int[] histogram) {
        int max = 255;
        for (int i = 255; i > 0; i--) {
            if (histogram[i] == 0) {
                max = 255 - 1;
            } else {
                break;
            }
        }
        return max;
    }

    public BufferedImage ligthen(BufferedImage image) {

        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                int red = (int) (40 * Math.log(color.getRed() + 1));
                int green = (int) (40 * Math.log(color.getGreen() + 1));
                int blue = (int) (40 * Math.log(color.getBlue() + 1));
                if (red > 255) {
                    red = 255;
                }
                if (green > 255) {
                    green = 255;
                }
                if (blue > 255) {
                    blue = 255;
                }
                Color newColor = new Color(red, green, blue);
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        return image;
    }

    public BufferedImage darker(BufferedImage image) {

        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                int red = (int) (0.002 * Math.pow(color.getRed(), 2));
                int green = (int) (0.002 * Math.pow(color.getGreen(), 2));
                int blue = (int) (0.002 * Math.pow(color.getBlue(), 2));
                if (red > 255) {
                    red = 255;
                }
                if (green > 255) {
                    green = 255;
                }
                if (blue > 255) {
                    blue = 255;
                }
                Color newColor = new Color(red, green, blue);
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        return image;
    }
}

