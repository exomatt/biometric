/*
 * Created by JFormDesigner on Fri Mar 22 23:09:42 CET 2019
 */

package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author name
 */
public class ImageShow extends JFrame {
    public ImageShow() {
        initComponents();
    }

    public ImageShow(BufferedImage binarizationImage) {
        initComponents();
        labelImage.setIcon(new ImageIcon(binarizationImage));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - name
        scrollPane1 = new JScrollPane();
        labelImage = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(labelImage);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(171, 171, 171)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 503, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(249, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(31, Short.MAX_VALUE)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 353, GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - name
    private JScrollPane scrollPane1;
    private JLabel labelImage;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
