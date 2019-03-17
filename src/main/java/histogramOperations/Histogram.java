package histogramOperations;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Histogram {
    public static List<int[]> calculateHistograms(BufferedImage image) {
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
                histogramAverage[(color.getBlue()+color.getGreen()+color.getRed())/3]++;
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
        paintHistogram(xTable, data.get(0),"RED", Color.RED);
        paintHistogram(xTable, data.get(1),"GREEN",Color.GREEN);
        paintHistogram(xTable, data.get(2),"BLUE",Color.BLUE);
        paintHistogram(xTable, data.get(3),"AVERAGE",Color.GRAY);
    }

    private void paintHistogram(int[] xTable, int[] data, String name, Color color) {
        CategoryChart chart = new CategoryChartBuilder().width(1200).height(1000).title(name ).xAxisTitle("Value").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setXAxisLabelAlignmentVertical(Styler.TextAlignment.Centre);

        chart.getStyler().setSeriesColors(new Color[]{color});
        chart.addSeries(name, xTable, data);
        Thread t = new Thread(() -> {
            new SwingWrapper(chart).displayChart().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
        t.start();
    }
}

