/*
 * Created by JFormDesigner on Wed Mar 13 16:40:46 CET 2019
 */

package GUI;

import binarization.BinarizationOperations;
import binarization.ColorChanger;
import filtration.Filtration;
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

    public static BufferedImage copyImage(BufferedImage source) {
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
        int otsuThreshold = binarizationOperations.thresholdOtsu(copyImage(firstImage));
        BufferedImage image = binarizationOperations.userValueBinarization(copyImage(firstImage), otsuThreshold);
        ImageShow imageShow = new ImageShow(image, otsuThreshold);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
    }

    private void button1ActionPerformed(ActionEvent e) {
        String bersenTresholdTextFieldText = bersenTresholdTextField.getText();
        String setThresholdBersenTextFieldText = setThresholdBersenTextField.getText();
        if (bersenTresholdTextFieldText.isEmpty() || setThresholdBersenTextFieldText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some field is empty!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int contrast = 0;
        int threshold = 0;
        try {
            contrast = Integer.parseInt(bersenTresholdTextFieldText);
            threshold = Integer.parseInt(setThresholdBersenTextFieldText);
        } catch (NumberFormatException ex) {
            log.severe("Text is not a number in  input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            JOptionPane.showMessageDialog(null, "Threshold field should be a number!!", ERROR, JOptionPane.ERROR_MESSAGE);
        }
        if (threshold < 0 || threshold > 255 || contrast < 0 || contrast > 255) {
            JOptionPane.showMessageDialog(null, "Threshold field should be a number between 0 and 255!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        BinarizationOperations binarizationOperations = new BinarizationOperations();
        BufferedImage image = binarizationOperations.bersenBinarization(copyImage(firstImage), contrast, threshold);
        ImageShow imageShow = new ImageShow(image);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
    }

    private void button2ActionPerformed(ActionEvent e) {
        String thresholdTextFieldText = niblackThresholdTextField.getText();
        String windowTextFieldText = niblackWindowTextField.getText();
        if (thresholdTextFieldText.isEmpty() || windowTextFieldText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some field is empty!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int window = 0;
        double threshold = 0;
        try {
            window = Integer.parseInt(windowTextFieldText);
            threshold = Double.parseDouble(thresholdTextFieldText);
        } catch (NumberFormatException ex) {
            log.severe("Text is not a number in  input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            JOptionPane.showMessageDialog(null, "Threshold field should be a number!!", ERROR, JOptionPane.ERROR_MESSAGE);
        }
        if (threshold < 0 || threshold > 255) {
            JOptionPane.showMessageDialog(null, "Threshold field should be a number between 0 and 255!!", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (window > 2 && window % 2 == 0) {
            JOptionPane.showMessageDialog(null, "Window size should be odd number", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        BinarizationOperations binarizationOperations = new BinarizationOperations();
        BufferedImage image = binarizationOperations.niblack(copyImage(firstImage), threshold, window);
        ImageShow imageShow = new ImageShow(image);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
    }

    private void convolutionallyMenuItemActionPerformed(ActionEvent e) {
        convWindow convWindow = new convWindow(copyImage(firstImage));
        convWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        convWindow.setVisible(true);
    }

    private void medianaMenuItemActionPerformed(ActionEvent e) {
        medianFilterStart(3);

    }

    private void medianaFiveMenuItemActionPerformed(ActionEvent e) {
        medianFilterStart(5);
    }

    private void medianFilterStart(int i) {
        Filtration filtration = new Filtration();
        BufferedImage newImage = filtration.medianFilter(copyImage(firstImage), i);
        ImageShow imageShow = new ImageShow(newImage);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
    }

    private void kuwaharMenuItemActionPerformed(ActionEvent e) {
        Filtration filtration = new Filtration();
        BufferedImage image = filtration.kuwaharaFilter(copyImage(firstImage));
        ImageShow imageShow = new ImageShow(image);
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
        menu5 = new JMenu();
        convolutionallyMenuItem = new JMenuItem();
        kuwaharMenuItem = new JMenuItem();
        medianaMenuItem = new JMenuItem();
        medianaFiveMenuItem = new JMenuItem();
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
        label5 = new JLabel();
        label6 = new JLabel();
        bersenTresholdTextField = new JTextField();
        label7 = new JLabel();
        setThresholdBersenTextField = new JTextField();
        button1 = new JButton();
        label8 = new JLabel();
        label9 = new JLabel();
        niblackThresholdTextField = new JTextField();
        label10 = new JLabel();
        niblackWindowTextField = new JTextField();
        button2 = new JButton();

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

            //======== menu5 ========
            {
                menu5.setText("filtration");

                //---- convolutionallyMenuItem ----
                convolutionallyMenuItem.setText("convolutionally");
                convolutionallyMenuItem.addActionListener(e -> convolutionallyMenuItemActionPerformed(e));
                menu5.add(convolutionallyMenuItem);

                //---- kuwaharMenuItem ----
                kuwaharMenuItem.setText("kuwahar");
                kuwaharMenuItem.addActionListener(e -> kuwaharMenuItemActionPerformed(e));
                menu5.add(kuwaharMenuItem);

                //---- medianaMenuItem ----
                medianaMenuItem.setText("mediana(3)");
                medianaMenuItem.addActionListener(e -> medianaMenuItemActionPerformed(e));
                menu5.add(medianaMenuItem);

                //---- medianaFiveMenuItem ----
                medianaFiveMenuItem.setText("mediana(5)");
                medianaFiveMenuItem.addActionListener(e -> medianaFiveMenuItemActionPerformed(e));
                menu5.add(medianaFiveMenuItem);
            }
            menuBar1.add(menu5);
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

        //---- label5 ----
        label5.setText("Bernsen binarization");

        //---- label6 ----
        label6.setText("constrast threshold");

        //---- label7 ----
        label7.setText("set_treshold parametr");

        //---- button1 ----
        button1.setText("make");
        button1.addActionListener(e -> button1ActionPerformed(e));

        //---- label8 ----
        label8.setText("Niblack binarization");

        //---- label9 ----
        label9.setText("threshold");

        //---- label10 ----
        label10.setText("window size");

        //---- button2 ----
        button2.setText("make");
        button2.addActionListener(e -> button2ActionPerformed(e));

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
                                            .addGroup(contentPaneLayout.createParallelGroup()
                                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                                            .addGroup(contentPaneLayout.createParallelGroup()
                                                                    .addComponent(label4)
                                                                    .addComponent(tresholdTextField))
                                                            .addGap(213, 213, 213))
                                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                                            .addGap(6, 6, 6)
                                                            .addComponent(thresholdCheckBox, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(81, 81, 81)
                                            .addComponent(makeBinarizationButton)
                                            .addGap(284, 284, 284)))
                            .addGroup(contentPaneLayout.createParallelGroup()
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                    .addComponent(label5, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label6)
                                    .addComponent(bersenTresholdTextField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label7)
                                    .addComponent(setThresholdBersenTextField, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
                                            .addGap(115, 115, 115)
                                            .addGroup(contentPaneLayout.createParallelGroup()
                                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                                            .addGap(6, 6, 6)
                                                            .addComponent(label10))
                                                    .addComponent(label9)
                                                    .addComponent(label8)
                                                    .addComponent(niblackThresholdTextField, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(niblackWindowTextField, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(37, 37, 37)
                            .addComponent(button1)
                                            .addGap(203, 203, 203)
                                            .addComponent(button2)))
                            .addContainerGap(134, Short.MAX_VALUE))
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
                        .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(label4)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(thresholdCheckBox, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(makeBinarizationButton)
                                        .addGap(87, 87, 87))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label5, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label8))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label6)
                                                .addComponent(label9))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(bersenTresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(niblackThresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label7)
                                                .addComponent(label10))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(setThresholdBersenTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(niblackWindowTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPaneLayout.createParallelGroup()
                                                .addComponent(button1)
                                                .addComponent(button2))
                                        .addContainerGap(31, Short.MAX_VALUE))))
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
    private JMenu menu5;
    private JMenuItem convolutionallyMenuItem;
    private JMenuItem kuwaharMenuItem;
    private JMenuItem medianaMenuItem;
    private JMenuItem medianaFiveMenuItem;
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
    private JLabel label5;
    private JLabel label6;
    private JTextField bersenTresholdTextField;
    private JLabel label7;
    private JTextField setThresholdBersenTextField;
    private JButton button1;
    private JLabel label8;
    private JLabel label9;
    private JTextField niblackThresholdTextField;
    private JLabel label10;
    private JTextField niblackWindowTextField;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
