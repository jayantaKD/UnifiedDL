package org.infobeyondtech.unifieddl.gui;



import org.infobeyondtech.unifieddl.utilities.GuiUtil;

import javax.swing.*;
import java.awt.*;

public class ProcessingDialog extends JDialog {


  Window parent;
  String loadingLabel = "Processing";

  public ProcessingDialog(Window parent) {
    super(parent);
    this.parent = parent;
    init();
  }

  public ProcessingDialog(Window parent, String loadingLabel) {
    super(parent);
    this.parent = parent;
    this.loadingLabel = loadingLabel;
    init();
  }

  private void init() {
    Container contentPane = this.getContentPane();
    ImageIcon loadImg = GuiUtil.createImageIcon("/icons/Loading.gif");
    JLabel loading = new JLabel(loadImg, SwingConstants.CENTER);
    JLabel loadLbl = new JLabel("<html> " + loadingLabel + " .. </html>", SwingConstants.CENTER);
    JPanel loadPnl = new JPanel(new BorderLayout());
    Font font = new Font("Calibri Light", Font.PLAIN, 18);

    loadLbl.setFont(font);
    loadLbl.setForeground(Color.WHITE);
    loadPnl.add(loading, BorderLayout.PAGE_START);
    loadPnl.add(loadLbl, BorderLayout.CENTER);
    contentPane.add(loadPnl);
    contentPane.setBackground(new Color(25, 111, 61));
    loadPnl.setBackground(new Color(25, 111, 61));
    loadPnl.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

    // this.setModal(true);
    this.setUndecorated(true);
    this.pack();

    this.setLocationRelativeTo(parent);
    this.setVisible(true);
  }
}
