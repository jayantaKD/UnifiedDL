package org.infobeyondtech.unifieddl.utilities;

import javax.swing.*;
import java.awt.*;


public class GuiUtil {

    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Dimension screensize = toolkit.getScreenSize();
    private static int screenWidth = screensize.width;
    private static int screenHeight = screensize.height;

    public static ImageIcon createImageIcon(String path /* , String description, Class<?> X */) {
        java.net.URL imgURL = /* getClass() */GuiUtil.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL/* , description */);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void getInformationDialog(Component parentComponent, Object msg) {

        JFrame frame = new JFrame();
        ImageIcon icon = GuiUtil.createImageIcon("/icons/Security Policy Tool Icon.png");

        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setLocation((screenWidth / 2) - (int) (frame.getPreferredSize().getWidth() / 2),
                (screenHeight / 2) - (int) (frame.getPreferredSize().getHeight() / 2));
        frame.setIconImage(icon.getImage());

        ImageIcon infoImg = GuiUtil.createImageIcon("/icons/Information.png");
        JOptionPane.showMessageDialog(parentComponent == null ? frame : parentComponent, msg,
                "UnifiedDL Prototype", JOptionPane.INFORMATION_MESSAGE, infoImg);
        frame.dispose();
    }

    public static void getWarningDialog(Component parentComponent, Object msg) {
        JFrame frame = new JFrame();
        ImageIcon icon = GuiUtil.createImageIcon("/icons/Security Policy Tool Icon.png");

        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setLocation((screenWidth / 2) - (int) (frame.getPreferredSize().getWidth() / 2),
                (screenHeight / 2) - (int) (frame.getPreferredSize().getHeight() / 2));
        frame.setIconImage(icon.getImage());

        ImageIcon infoImg = GuiUtil.createImageIcon("/icons/Warning.png");
        JOptionPane.showMessageDialog(parentComponent == null ? frame : parentComponent, msg,
                "UnifiedDL Prototype", JOptionPane.WARNING_MESSAGE, infoImg);

        frame.dispose();
    }

    public static void getWarningDialog(Component parentComponent, Object msg, String title) {
        JFrame frame = new JFrame();
        ImageIcon icon = GuiUtil.createImageIcon("/icons/Security Policy Tool Icon.png");

        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setLocation((screenWidth / 2) - (int) (frame.getPreferredSize().getWidth() / 2),
                (screenHeight / 2) - (int) (frame.getPreferredSize().getHeight() / 2));
        frame.setIconImage(icon.getImage());

        ImageIcon infoImg = GuiUtil.createImageIcon("/icons/Warning.png");
        JOptionPane.showMessageDialog(parentComponent == null ? frame : parentComponent, msg, title,
                JOptionPane.WARNING_MESSAGE, infoImg);
        frame.dispose();
    }
}
