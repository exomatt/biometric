/*
 * Created by JFormDesigner on Wed Mar 13 16:40:46 CET 2019
 */

package GUI;

import ImageRS.ImageReaderSaver;
import histogramOperations.Histogram;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

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
        if (firstImage==null){
            return;
        }
        Histogram histogram = new Histogram();
        histogram.display(firstImage);
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
        scrollPane1 = new JScrollPane();
        imageLabel = new JLabel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        textFieldRed = new JTextField();
        textFieldGreen = new JTextField();
        acceptButton = new JButton();
        textFieldBlue = new JTextField();

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
                showHistogramButton.setText("show histogram");
                showHistogramButton.addActionListener(e -> showHistogramButtonActionPerformed(e));
                menu2.add(showHistogramButton);
            }
            menuBar1.add(menu2);
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

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 423, Short.MAX_VALUE))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(69, 69, 69)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label2)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(label3)
                            .addGap(83, 83, 83)
                            .addComponent(acceptButton))
                        .addComponent(label1)
                        .addComponent(textFieldRed, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(textFieldBlue, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(textFieldGreen, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                    .addContainerGap(758, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                    .addGap(30, 30, 30)
                    .addComponent(label1)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(textFieldRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label3)
                        .addComponent(acceptButton))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(textFieldGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(label2)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(textFieldBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 23, Short.MAX_VALUE))
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
    private JScrollPane scrollPane1;
    private JLabel imageLabel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textFieldRed;
    private JTextField textFieldGreen;
    private JButton acceptButton;
    private JTextField textFieldBlue;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
