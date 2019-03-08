package GUI;

import ImageRS.ImageReaderSaver;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

@Log
public class ImageGUI {
    public static final String ERROR = "Error";
    private JButton loadImageButton;
    private JButton saveImageButton;
    private JLabel imageLabel;
    private JPanel panel;
    private JTextField textFieldRed;
    private JTextField textFieldGreen;
    private JTextField textFieldBlue;
    private JButton acceptButton;
    private int x;
    private int y;

    public ImageGUI() {
        this.loadImageButton.addActionListener((ActionEvent e) -> {
            JFileChooser imageOpener = new JFileChooser();
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

            int returnValue = imageOpener.showDialog(null, "Select image");
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                BufferedImage img = ImageReaderSaver.loadImage(imageOpener.getSelectedFile().getPath());
                this.imageLabel.setIcon(new ImageIcon(img));
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
                BufferedImage bufferedImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
                x = e.getX();
                y = e.getY();
                Color color = new Color(bufferedImage.getRGB(x, y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();
                textFieldRed.setText(String.valueOf(red));
                textFieldGreen.setText(String.valueOf(green));
                textFieldBlue.setText(String.valueOf(blue));
                log.info("Clicked dimensions: X " + x + " Y " + y);
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String blueText = textFieldBlue.getText().trim();
                String redText = textFieldRed.getText().trim();
                String greenText = textFieldGreen.getText().trim();
                if (greenText.isEmpty() || redText.isEmpty() || blueText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "One field is empty", ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int blue = 0, red = 0, green = 0;
                try {
                    blue = Integer.parseInt(blueText);
                    red = Integer.parseInt(redText);
                    green = Integer.parseInt(greenText);
                } catch (NumberFormatException ex) {
                    log.severe("Text is not a number in RGB input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
                    JOptionPane.showMessageDialog(panel, "One field is a text not a number!!", ERROR, JOptionPane.ERROR_MESSAGE);
                }
                if (blue > 255 || red > 255 || green > 255) {
                    JOptionPane.showMessageDialog(panel, "Number should be less than 256 !!", ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                BufferedImage bufferedImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
                Color color = new Color(red, green, blue);
                bufferedImage.setRGB(x,y,color.getRGB());
                imageLabel.setIcon(new ImageIcon(bufferedImage));
            }
        });
        imageLabel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoom;
                if (e.getWheelRotation()>=0) {
                     zoom= 0.8;

                }else
                    zoom = 1.2;
                BufferedImage bufferedImage = ImageReaderSaver.convertIconToImage((ImageIcon) imageLabel.getIcon());
                int newImageWidth = (int) (bufferedImage.getWidth() * zoom);
                int newImageHeight = (int) (bufferedImage.getHeight() * zoom);
                BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, bufferedImage.getType());
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(bufferedImage, 0, 0, newImageWidth , newImageHeight , null);
                g.dispose();
                imageLabel.setIcon(new ImageIcon(resizedImage));
            }
        });
    }

    public JButton getLoadImageButton() {
        return loadImageButton;
    }

    public void setLoadImageButton(JButton loadImageButton) {
        this.loadImageButton = loadImageButton;
    }

    public JButton getSaveImageButton() {
        return saveImageButton;
    }

    public void setSaveImageButton(JButton saveImageButton) {
        this.saveImageButton = saveImageButton;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
}
