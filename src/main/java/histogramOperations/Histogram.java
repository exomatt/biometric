package histogramOperations;

import org.apache.xmlgraphics.image.loader.impl.ImageBuffered;
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
        paintHistogram(xTable, data.get(0),"RED", Color.RED,firstImage);
        paintHistogram(xTable, data.get(1),"GREEN",Color.GREEN,firstImage);
        paintHistogram(xTable, data.get(2),"BLUE",Color.BLUE,firstImage);
        paintHistogram(xTable, data.get(3),"AVERAGE",Color.GRAY,firstImage);
    }
    public void displayOneType(BufferedImage firstImage, int type) {
        int[] xTable = new int[256];
        for (int i = 0; i < 256; i++) {
            xTable[i] = i;
        }

        List<int[]> data = calculateHistograms(firstImage);
        if (type==0) {
            paintHistogram(xTable, data.get(0),"RED", Color.RED,firstImage);
        }
        if (type==1) {
            paintHistogram(xTable, data.get(1),"GREEN",Color.GREEN,firstImage);
        }
        if (type==2) {
            paintHistogram(xTable, data.get(2),"BLUE",Color.BLUE,firstImage);
        }
    }

    private void paintHistogram(int[] xTable, int[] data, String name, Color color, BufferedImage image) {
        CategoryChart chart = new CategoryChartBuilder().width(1200).height(1000).title(name ).xAxisTitle("Value").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setXAxisLabelAlignmentVertical(Styler.TextAlignment.Centre);

        chart.getStyler().setSeriesColors(new Color[]{color});
        chart.addSeries(name, xTable, data);
        Thread t = new Thread(() -> {
            JFrame jFrame = new SwingWrapper(chart).displayChart();
            ScrollPane scrollPane = new ScrollPane();
            JLabel jLabel = new JLabel(new ImageIcon(image));
            jLabel.setSize(new Dimension(600,400));
            scrollPane.add(jLabel);
            scrollPane.setSize(new Dimension(600,400));
            jFrame.add(scrollPane,BorderLayout.WEST);
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        });
        t.start();
    }

    public double[] distribution(int [] histogram, double pixels){
        double[] tableDistribution = new double[256];
        int counter = 1;
        for (int i = 0; i < tableDistribution.length; i++) {
            for (int j = 0; j < counter; j++) {
                tableDistribution[i]+=histogram[j]/pixels;
            }
//            tableDistribution[i]=tableDistribution[i]/pixels;
            counter++;
        }
        return tableDistribution;

    }

    public int[] lookUpTable(double[] distribution){
        int [] lookUpTable = new int[256];
        for (int i = 0; i < lookUpTable.length; i++) {
            lookUpTable[i]= (int) (((distribution[i]-distribution[0])/(1-distribution[0]))*(255-1));
        }
        return lookUpTable;
    }
    
    public BufferedImage histogramEqualization(int[] lookUpTable, BufferedImage image, int type){
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));
                if (type==0) {
                    int red = color.getRed();
                    int newValue = lookUpTable[red];
                    image.setRGB(i,j,new Color(newValue,color.getGreen(),color.getBlue()).getRGB());
                }
                if (type==1) {
                    int green = color.getGreen();
                    int newValue = lookUpTable[green];
                    image.setRGB(i,j,new Color(color.getRed(),newValue,color.getBlue()).getRGB());
                }else {
                    int blue = color.getBlue();
                    int newValue = lookUpTable[blue];
                    image.setRGB(i,j,new Color(color.getRed(),color.getGreen(),newValue).getRGB());
                }
            }
        }
        return image;
    }
}

