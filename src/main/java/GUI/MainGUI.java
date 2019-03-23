/*
 * Created by JFormDesigner on Wed Mar 13 16:40:46 CET 2019
 */

package GUI;

import binarization.BinarizationOperations;
import binarization.ColorChanger;
import histogramoperations.Histogram;
import imageoperation.ImageReaderSaver;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author unknown
 */
@Log
public class MainGUI extends JFrame {
    public static final String ERROR = "Error";
    private int x;
    private int y;
    private BufferedImage firstImage;

    public MainGUI() {
        initComponents();

        this.loadImageButton.addActionListener((ActionEvent e) -> {
            JFileChooser imageOpener = new JFileChooser();
            imageOpener.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String fileName = f.getName().toLowerCase();
                    return fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".bmp") || fileName.endsWith(".svg");
                }

                @Override
                public String getDescription() {
                    return "Image files (.jpg, .png, .tiff, .bmp, .svg)";
                }
            });

            int returnValue = imageOpener.showDialog(null, "Select image");
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                firstImage = ImageReaderSaver.loadImage(imageOpener.getSelectedFile().getPath());
                this.imageLabel.setIcon(new ImageIcon(firstImage));
            }
        });

        this.saveImageButton.addActionListener((ActionEvent e) -> {
            JFileChooser imageOpener = new JFileChooser();
            imageOpener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            imageOpener.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String fileName = f.getName().toLowerCase();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".bmp") || fileName.endsWith(".svg")) {
                        return true;
                    } else return false;
                }

                @Override
                public String getDescription() {
                    return "Image files (.jpg, .png, .tiff, .bmp, .svg)";
                }
            });
            int returnValue = imageOpener.showDialog(null, "Save image");
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String path = imageOpener.getSelectedFile().getPath();
                BufferedImage img = ImageReaderSaver.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
                ImageReaderSaver.saveImage(img, path);
            }
        });
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                firstImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
                x = e.getX();
                y = e.getY();
                Color color = new Color(firstImage.getRGB(x, y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();
                textFieldRed.setText(String.valueOf(red));
                textFieldGreen.setText(String.valueOf(green));
                textFieldBlue.setText(String.valueOf(blue));
                log.info("Clicked dimensions: X " + x + " Y " + y);
            }
        });

        acceptButton.addActionListener(e -> {
            String blueText = textFieldBlue.getText().trim();
            String redText = textFieldRed.getText().trim();
            String greenText = textFieldGreen.getText().trim();
            if (greenText.isEmpty() || redText.isEmpty() || blueText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "One field is empty", ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }
            int blue = 0, red = 0, green = 0;
            try {
                blue = Integer.parseInt(blueText);
                red = Integer.parseInt(redText);
                green = Integer.parseInt(greenText);
            } catch (NumberFormatException ex) {
                log.severe("Text is not a number in RGB input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
                JOptionPane.showMessageDialog(null, "One field is a text not a number!!", ERROR, JOptionPane.ERROR_MESSAGE);
            }
            if (blue > 255 || red > 255 || green > 255 || blue < 0 || red < 0 || green < 0) {
                JOptionPane.showMessageDialog(null, "Number should be beetween  0 and 255 ", ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }
            firstImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
            Color color = new Color(red, green, blue);
            firstImage.setRGB(x, y, color.getRGB());
            imageLabel.setIcon(new ImageIcon(firstImage));
        });
        imageLabel.addMouseWheelListener(e -> {
            double zoom;
            if (e.getWheelRotation() >= 0) {
                zoom = 0.8;

            } else
                zoom = 1.2;
            BufferedImage bufferedImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
            int newImageWidth = (int) (bufferedImage.getWidth() * zoom);
            int newImageHeight = (int) (bufferedImage.getHeight() * zoom);
            Image scaledInstance = firstImage.getScaledInstance(newImageWidth, newImageHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledInstance));
        });
    }

    private void showHistogramButtonActionPerformed(ActionEvent e) {
        if (firstImage == null) {
            return;
        }
        Histogram histogram = new Histogram();
        histogram.display(firstImage);

    }

    private void histogramEqualizationMenuItemActionPerformed(ActionEvent e) {
        //red 0 green 1 blue 2
        Histogram histogram = new Histogram();
        List<int[]> calculateHistograms = histogram.calculateHistograms(firstImage);
        int pixels = firstImage.getWidth() * firstImage.getHeight();
        //red
        int[] lookUpTable = histogram.lookUpTableHistogramEqualization(calculateHistograms.get(0), pixels);
        BufferedImage imageRedEq = histogram.histogramEqualization(lookUpTable, copyImage(firstImage), 0);
        histogram.displayOneType(imageRedEq, 0);
        //green
        lookUpTable = histogram.lookUpTableHistogramEqualization(calculateHistograms.get(1), pixels);
        imageRedEq = histogram.histogramEqualization(lookUpTable, copyImage(firstImage), 1);
        histogram.displayOneType(imageRedEq, 1);
        //blue
        lookUpTable = histogram.lookUpTableHistogramEqualization(calculateHistograms.get(2), pixels);
        imageRedEq = histogram.histogramEqualization(lookUpTable, copyImage(firstImage), 2);
        histogram.displayOneType(imageRedEq, 2);
        //all
        imageRedEq = histogram.histogramEqualization(lookUpTable, copyImage(firstImage), 3);
        histogram.displayOneType(imageRedEq, 3);

    }

    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    private void histogramStretchingMenuItemActionPerformed(ActionEvent e) {
        //red 0 green 1 blue 2
        Histogram histogram = new Histogram();
        BufferedImage copyImage = copyImage(firstImage);
        BufferedImage stretchingHistogram = histogram.stretchingHistogram(copyImage);
        histogram.display(stretchingHistogram);
    }

    private void lightenButtonActionPerformed(ActionEvent e) {
        firstImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
        Histogram histogram = new Histogram();
        BufferedImage ligthen = histogram.ligthen(firstImage);
        firstImage = ligthen;
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void darkerButtonActionPerformed(ActionEvent e) {
        firstImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
        Histogram histogram = new Histogram();
        BufferedImage darker = histogram.darker(firstImage);
        firstImage = darker;
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void redChangeMenuItemActionPerformed(ActionEvent e) {
        ColorChanger changer = new ColorChanger();
        firstImage = changer.changeToGrey(firstImage, 0);
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void greenChangeMenuItemActionPerformed(ActionEvent e) {
        ColorChanger changer = new ColorChanger();
        firstImage = changer.changeToGrey(firstImage, 1);
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void blueChangeMenuItemActionPerformed(ActionEvent e) {
        ColorChanger changer = new ColorChanger();
        firstImage = changer.changeToGrey(firstImage, 2);
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void averangeChangeMenuItemActionPerformed(ActionEvent e) {
        ColorChanger changer = new ColorChanger();
        firstImage = changer.changeToGrey(firstImage, 3);
        imageLabel.setIcon(new ImageIcon(firstImage));
    }

    private void menuItem4ActionPerformed(ActionEvent e) {
        ColorChanger changer = new ColorChanger();
        BufferedImage imageRed = changer.changeToGrey(copyImage(firstImage), 0);
        BufferedImage imageGreen = changer.changeToGrey(copyImage(firstImage), 1);
        BufferedImage imageBlue = changer.changeToGrey(copyImage(firstImage), 2);
        BufferedImage imageAverage = changer.changeToGrey(copyImage(firstImage), 3);
        List<BufferedImage> bufferedImages = new ArrayList<>();
        bufferedImages.add(firstImage);
        bufferedImages.add(imageRed);
        bufferedImages.add(imageGreen);
        bufferedImages.add(imageBlue);
        bufferedImages.add(imageAverage);
        ImageGreyChangerResult changerResultDisplay = new ImageGreyChangerResult(bufferedImages);
        changerResultDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changerResultDisplay.setVisible(true);
    }

    private void makeBinarizationButtonActionPerformed(ActionEvent e) {
        BinarizationOperations binarizationOperations = new BinarizationOperations();
        String textThreshold = tresholdTextField.getText();
        if (textThreshold.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Threshold field is empty!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int threshold = 0;
        try {
            threshold = Integer.parseInt(textThreshold);
        } catch (NumberFormatException ex) {
            log.severe("Text is not a number in threshold input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            JOptionPane.showMessageDialog(null, "Threshold field should be a number!!", ERROR, JOptionPane.ERROR_MESSAGE);
        }
        if (threshold < 0 || threshold > 255) {
            JOptionPane.showMessageDialog(null, "Threshold field should be a number between 0 and 255!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        BufferedImage binarizationImage = binarizationOperations.userValueBinarization(copyImage(firstImage), threshold);
        if (thresholdCheckBox.isSelected()) {
            imageLabel.setIcon(new ImageIcon(binarizationImage));
            firstImage = binarizationImage;
        } else {
            ImageShow imageShow = new ImageShow(binarizationImage);
            imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageShow.setVisible(true);
        }
    }

    private void otsuMenuItemActionPerformed(ActionEvent e) {
        BinarizationOperations binarizationOperations = new BinarizationOperations();
        int otsuThreshold = binarizationOperations.otsuThreshold(copyImage(firstImage));
        BufferedImage image = binarizationOperations.userValueBinarization(copyImage(firstImage), otsuThreshold);
        ImageShow imageShow = new ImageShow(image, otsuThreshold);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - name
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        loadImageButton = new JMenuItem();
        saveImageButton = new JMenuItem();
        menu2 = new JMenu();
        showHistogramButton = new JMenuItem();
        histogramEqualizationMenuItem = new JMenuItem();
        histogramStretchingMenuItem = new JMenuItem();
        menu3 = new JMenu();
        redChangeMenuItem = new JMenuItem();
        greenChangeMenuItem = new JMenuItem();
        blueChangeMenuItem = new JMenuItem();
        averangeChangeMenuItem = new JMenuItem();
        menuItem4 = new JMenuItem();
        menu4 = new JMenu();
        otsuMenuItem = new JMenuItem();
        scrollPane1 = new JScrollPane();
        imageLabel = new JLabel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        textFieldRed = new JTextField();
        textFieldGreen = new JTextField();
        acceptButton = new JButton();
        textFieldBlue = new JTextField();
        lightenButton = new JButton();
        darkerButton = new JButton();
        label4 = new JLabel();
        tresholdTextField = new JTextField();
        makeBinarizationButton = new JButton();
        thresholdCheckBox = new JCheckBox();

        //======== this ========
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("file");

                //---- loadImageButton ----
                loadImageButton.setText("load");
                menu1.add(loadImageButton);

                //---- saveImageButton ----
                saveImageButton.setText("save");
                menu1.add(saveImageButton);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("histogram");

                //---- showHistogramButton ----
                showHistogramButton.setText("show histograms");
                showHistogramButton.addActionListener(e -> showHistogramButtonActionPerformed(e));
                menu2.add(showHistogramButton);

                //---- histogramEqualizationMenuItem ----
                histogramEqualizationMenuItem.setText("histogram equalization");
                histogramEqualizationMenuItem.addActionListener(e -> histogramEqualizationMenuItemActionPerformed(e));
                menu2.add(histogramEqualizationMenuItem);

                //---- histogramStretchingMenuItem ----
                histogramStretchingMenuItem.setText("histogram stretching ");
                histogramStretchingMenuItem.addActionListener(e -> histogramStretchingMenuItemActionPerformed(e));
                menu2.add(histogramStretchingMenuItem);
            }
            menuBar1.add(menu2);

            //======== menu3 ========
            {
                menu3.setText("change to gray ");

                //---- redChangeMenuItem ----
                redChangeMenuItem.setText("red change");
                redChangeMenuItem.addActionListener(e -> redChangeMenuItemActionPerformed(e));
                menu3.add(redChangeMenuItem);

                //---- greenChangeMenuItem ----
                greenChangeMenuItem.setText("green change");
                greenChangeMenuItem.addActionListener(e -> greenChangeMenuItemActionPerformed(e));
                menu3.add(greenChangeMenuItem);

                //---- blueChangeMenuItem ----
                blueChangeMenuItem.setText("blue change");
                blueChangeMenuItem.addActionListener(e -> blueChangeMenuItemActionPerformed(e));
                menu3.add(blueChangeMenuItem);

                //---- averangeChangeMenuItem ----
                averangeChangeMenuItem.setText("averange change");
                averangeChangeMenuItem.addActionListener(e -> averangeChangeMenuItemActionPerformed(e));
                menu3.add(averangeChangeMenuItem);

                //---- menuItem4 ----
                menuItem4.setText("get all");
                menuItem4.addActionListener(e -> menuItem4ActionPerformed(e));
                menu3.add(menuItem4);
            }
            menuBar1.add(menu3);

            //======== menu4 ========
            {
                menu4.setText("binarization");

                //---- otsuMenuItem ----
                otsuMenuItem.setText("otsu");
                otsuMenuItem.addActionListener(e -> otsuMenuItemActionPerformed(e));
                menu4.add(otsuMenuItem);
            }
            menuBar1.add(menu4);
        }
        setJMenuBar(menuBar1);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(imageLabel);
        }

        //---- label1 ----
        label1.setText("red");

        //---- label2 ----
        label2.setText("blue");

        //---- label3 ----
        label3.setText("green");

        //---- acceptButton ----
        acceptButton.setText("change");

        //---- lightenButton ----
        lightenButton.setText("lighten");
        lightenButton.addActionListener(e -> lightenButtonActionPerformed(e));

        //---- darkerButton ----
        darkerButton.setText("darker");
        darkerButton.addActionListener(e -> darkerButtonActionPerformed(e));

        //---- label4 ----
        label4.setText("Treshold parametr to binarization");

        //---- makeBinarizationButton ----
        makeBinarizationButton.setText("Make");
        makeBinarizationButton.addActionListener(e -> makeBinarizationButtonActionPerformed(e));

        //---- thresholdCheckBox ----
        thresholdCheckBox.setText("setOnMainWindow");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(lightenButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                                .addComponent(darkerButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                    .addGroup(contentPaneLayout.createParallelGroup()
                            .addComponent(textFieldRed, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1)
                            .addComponent(label3)
                            .addComponent(textFieldGreen, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label2)
                            .addComponent(textFieldBlue, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                            .addComponent(acceptButton))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(29, 29, 29)
                                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(label4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(tresholdTextField)))
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(51, 51, 51)
                                            .addComponent(thresholdCheckBox, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(93, 93, 93)
                                            .addComponent(makeBinarizationButton)))
                            .addContainerGap(793, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(lightenButton)
                                                .addComponent(label1))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup()
                                                .addComponent(darkerButton)
                                                .addComponent(textFieldRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label3)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textFieldGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textFieldBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(acceptButton)))
                        .addGap(27, 27, 27)
                        .addComponent(label4)
                        .addGap(18, 18, 18)
                        .addComponent(tresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(thresholdCheckBox, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(makeBinarizationButton)
                        .addGap(61, 61, 61))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - name
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem loadImageButton;
    private JMenuItem saveImageButton;
    private JMenu menu2;
    private JMenuItem showHistogramButton;
    private JMenuItem histogramEqualizationMenuItem;
    private JMenuItem histogramStretchingMenuItem;
    private JMenu menu3;
    private JMenuItem redChangeMenuItem;
    private JMenuItem greenChangeMenuItem;
    private JMenuItem blueChangeMenuItem;
    private JMenuItem averangeChangeMenuItem;
    private JMenuItem menuItem4;
    private JMenu menu4;
    private JMenuItem otsuMenuItem;
    private JScrollPane scrollPane1;
    private JLabel imageLabel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textFieldRed;
    private JTextField textFieldGreen;
    private JButton acceptButton;
    private JTextField textFieldBlue;
    private JButton lightenButton;
    private JButton darkerButton;
    private JLabel label4;
    private JTextField tresholdTextField;
    private JButton makeBinarizationButton;
    private JCheckBox thresholdCheckBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
