package org.infobeyondtech.unifieddl.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.zip.GZIPInputStream;


public class AppView extends JFrame {

    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screensize = toolkit.getScreenSize();
    private int screenWidth = screensize.width;
    private int screenHeight = screensize.height;

    JSplitPane splitMainBody;

    // left part of horizontal split
    private JPanel mainBody;

    // right part of horizontal split
    ProjectTreePanel projectTreePanel;
    private JPanel pageStart = new JPanel(new BorderLayout());

    public AppView() {
        super("UnifiedDL Prototype");
        buildContent();
        showViewFrame();
    }

    private void showViewFrame(){
        //        appStatusPane.setBackground(AppColorUti.sptSilver);
        //        ImageIcon mainicon1 = AppUti.createImageIcon("/icons/" + AppUti.PRODUCT_NAME + " Icon.png");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        this.setIconImage(mainicon1.getImage());
        this.setPreferredSize(new Dimension((int) (screenWidth), (int) (screenWidth)));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    private void buildContent()  {
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        try {
            File savedFile = new File("c:\\Users\\jayan\\workspace\\test");
            FileInputStream input = new FileInputStream(savedFile);
            GZIPInputStream inputunzip = new GZIPInputStream(input);
            ObjectInputStream reader = new ObjectInputStream(inputunzip);

            JTree tree = (JTree) reader.readObject();
            projectTreePanel = new ProjectTreePanel(this, tree);
            System.out.println(tree);

        } catch(Exception Ex){
            System.out.println(Ex.getMessage());
            projectTreePanel = new ProjectTreePanel(this);
        }

        mainBody = new JPanel(new BorderLayout());
        prepareMenu();
        splitMainBody = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectTreePanel, mainBody);
        splitMainBody.setDividerSize(5);
        splitMainBody.setOneTouchExpandable(true);
        splitMainBody.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        container.add(splitMainBody, BorderLayout.CENTER);
        container.add(pageStart, BorderLayout.PAGE_START);
        this.pack();
        setLocationRelativeTo(null);
    }

    private void prepareMenu(){
        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);
        pageStart.add(mb);
    }

    public void loadMainBodyContent (JPanel view){
        mainBody.removeAll();
        mainBody.add(view);
        mainBody.revalidate();
        mainBody.repaint();
    }
}
