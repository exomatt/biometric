/*
 * Created by JFormDesigner on Sun Mar 31 21:38:58 CEST 2019
 */

package GUI;

import filtration.ConFiltration;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author name
 */
@Log
public class convWindow extends JFrame {
    private final BufferedImage image;

    public convWindow() {
        initComponents();
        image = null;
    }

    public convWindow(BufferedImage image) {
        initComponents();
        this.image = image;
    }

    private void buttonMakeActionPerformed(ActionEvent e) {
        String zeroText = textFieldZero.getText();
        String oneText = textFieldOne.getText();
        String twoText = textFieldTwo.getText();
        String threeText = textFieldThree.getText();
        String fourText = textFieldFour.getText();
        String fiveText = textFieldFive.getText();
        String sixText = textFieldSix.getText();
        String sevenText = textFieldSeven.getText();
        String eightText = textFieldEight.getText();
        if (zeroText.isEmpty() || oneText.isEmpty() || twoText.isEmpty() || threeText.isEmpty() || fourText.isEmpty() || fiveText.isEmpty() || sixText.isEmpty() || sevenText.isEmpty() || eightText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some field is empty!!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int[][] table = new int[3][3];
        try {
            table[0][0] = Integer.parseInt(zeroText);
            table[0][1] = Integer.parseInt(oneText);
            table[0][2] = Integer.parseInt(twoText);
            table[1][0] = Integer.parseInt(threeText);
            table[1][1] = Integer.parseInt(fourText);
            table[1][2] = Integer.parseInt(fiveText);
            table[2][0] = Integer.parseInt(sixText);
            table[2][1] = Integer.parseInt(sevenText);
            table[2][2] = Integer.parseInt(eightText);
        } catch (NumberFormatException ex) {
            log.severe("Text is not a number in  input error:  " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            JOptionPane.showMessageDialog(null, "ALL fields should be a number!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        ConFiltration conFiltration = new ConFiltration();
        BufferedImage newImage = conFiltration.filterImage(this.image, table);
        ImageShow imageShow = new ImageShow(newImage);
        imageShow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageShow.setVisible(true);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - name
        textFieldZero = new JTextField();
        textFieldOne = new JTextField();
        textFieldTwo = new JTextField();
        textFieldThree = new JTextField();
        textFieldFour = new JTextField();
        textFieldFive = new JTextField();
        textFieldSix = new JTextField();
        textFieldSeven = new JTextField();
        textFieldEight = new JTextField();
        buttonMake = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //---- buttonMake ----
        buttonMake.setText("make");
        buttonMake.addActionListener(e -> buttonMakeActionPerformed(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(231, 231, 231)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textFieldZero, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                                        .addComponent(textFieldThree, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                                        .addComponent(textFieldSix, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                                                .addGap(39, 39, 39)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textFieldOne, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                                        .addComponent(textFieldSeven, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                                        .addComponent(textFieldFour, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                                                .addGap(49, 49, 49)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textFieldTwo, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                                        .addComponent(textFieldFive, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                                        .addComponent(textFieldEight, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(354, 354, 354)
                                                .addComponent(buttonMake)))
                                .addContainerGap(424, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textFieldOne, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldZero, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldTwo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textFieldThree, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldFour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldFive, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textFieldSix, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldSeven, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldEight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addComponent(buttonMake)
                                .addContainerGap(371, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - name
    private JTextField textFieldZero;
    private JTextField textFieldOne;
    private JTextField textFieldTwo;
    private JTextField textFieldThree;
    private JTextField textFieldFour;
    private JTextField textFieldFive;
    private JTextField textFieldSix;
    private JTextField textFieldSeven;
    private JTextField textFieldEight;
    private JButton buttonMake;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
