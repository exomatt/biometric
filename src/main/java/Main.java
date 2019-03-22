import GUI.MainGUI;

import javax.swing.*;
import java.awt.*;

public class  Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
//        try {
//            UIManager.setLookAndFeel (new MaterialLookAndFeel());
//        } catch (UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        MainGUI mainGUI = new MainGUI();
        mainGUI.setMinimumSize(new Dimension(800,600));
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setVisible(true);
//        JFrame app = new JFrame("App");
//        app.setMinimumSize(new Dimension(800,600));
//        app.setContentPane(new ImageGUI().getPanel());
//        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        app.pack();
//        app.setVisible(true);

    }
}
