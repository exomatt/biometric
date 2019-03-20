package imageoperation;

import jankovicsandras.imagetracer.ImageTracer;
import lombok.extern.java.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log
public class ImageReaderSaver {
    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            log.severe("Error while reading file " + ex.getMessage() + "\n " + Arrays.toString(ex.getStackTrace()));
        }
        return image;
    }

    public static void saveImage(BufferedImage img, String path) {
        String format = path.substring(path.lastIndexOf('.') + 1);
        if (!format.equals("svg") && !format.equals("tiff") && !format.equals("png") && !format.equals("jpg") && !format.equals("bmp") && !format.equals("jpeg")) {
            JOptionPane.showMessageDialog(null, "File format is not image file (.jpg, .png, .tiff, .bmp, .svg)!!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (format.equals("svg")) {
            try {

                ImageTracer.saveString(
                        path,
                        ImageTracer.imageToSVG(img, null, null)
                );
            } catch (Exception ex) {
                log.severe("Error while saving file " + ex.getMessage() + "\n " + Arrays.toString(ex.getStackTrace()));
            }
        }
        try {
            ImageIO.write(img, format, new File(path));
        } catch (IOException ex) {
            log.severe("Error while saving file " + ex.getMessage() + "\n " + Arrays.toString(ex.getStackTrace()));
        }
    }

    public static BufferedImage convertIconToImage(ImageIcon icon) {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.createGraphics();
        icon.paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        return image;
    }
}
