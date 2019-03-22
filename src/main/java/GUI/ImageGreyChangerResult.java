/*
 * Created by JFormDesigner on Fri Mar 22 22:50:19 CET 2019
 */

package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author name
 */
public class ImageGreyChangerResult extends JFrame {
    public ImageGreyChangerResult() {
        initComponents();


    }

    public ImageGreyChangerResult(List<BufferedImage> list) {
        initComponents();
        labelOriginal.setIcon(new ImageIcon(list.get(0)));
        labelRed.setIcon(new ImageIcon(list.get(1)));
        labelGreen.setIcon(new ImageIcon(list.get(2)));
        labelBlue.setIcon(new ImageIcon(list.get(3)));
        labelAverange.setIcon(new ImageIcon(list.get(4)));

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - name
        scrollPane1 = new JScrollPane();
        labelOriginal = new JLabel();
        scrollPane3 = new JScrollPane();
        scrollPane4 = new JScrollPane();
        labelBlue = new JLabel();
        scrollPane5 = new JScrollPane();
        labelRed = new JLabel();
        scrollPane6 = new JScrollPane();
        labelAverange = new JLabel();
        scrollPane7 = new JScrollPane();
        labelGreen = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(labelOriginal);
        }

        //======== scrollPane3 ========
        {

            //======== scrollPane4 ========
            {
                scrollPane4.setViewportView(labelBlue);
            }
            scrollPane3.setViewportView(scrollPane4);
        }

        //======== scrollPane5 ========
        {
            scrollPane5.setViewportView(labelRed);
        }

        //======== scrollPane6 ========
        {
            scrollPane6.setViewportView(labelAverange);
        }

        //======== scrollPane7 ========
        {
            scrollPane7.setViewportView(labelGreen);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
                                                .addGap(56, 56, 56)
                                                .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                                .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(112, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - name
    private JScrollPane scrollPane1;
    private JLabel labelOriginal;
    private JScrollPane scrollPane3;
    private JScrollPane scrollPane4;
    private JLabel labelBlue;
    private JScrollPane scrollPane5;
    private JLabel labelRed;
    private JScrollPane scrollPane6;
    private JLabel labelAverange;
    private JScrollPane scrollPane7;
    private JLabel labelGreen;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
