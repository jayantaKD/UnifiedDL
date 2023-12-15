package org.infobeyondtech.unifieddl.gui.base;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class InfobeyondDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  protected JButton addButton;
  protected JButton cancelButton;
  protected JPanel bodyPanel;
  protected boolean isCancel = false;
  protected boolean isAddClicked = false;
  protected boolean isAutoKill = true;
  protected PropertyChangeSupport changes = new PropertyChangeSupport(this);


  public InfobeyondDialog(Frame baseFrame, String actionButtonLabel, String dialogTitle) {
    super(baseFrame, dialogTitle, true);
    commonBuild(actionButtonLabel);
  }

  public InfobeyondDialog(Dialog baseFrame, String actionButtonLabel, String dialogTitle) {
    super(baseFrame, dialogTitle, true);
    commonBuild(actionButtonLabel);
//    this.bodyPanel = new JPanel();
//    this.addButton = new JButton(buttonTxt);
//    this.cancelButton = new JButton("Cancel");
//    bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
  }

  public InfobeyondDialog(Window baseWindow, String actionButtonLabel, String dialogTitle) {
    super(baseWindow, dialogTitle);
    this.setModal(true);
    commonBuild(actionButtonLabel);
//    this.bodyPanel = new JPanel();
//    this.addButton = new JButton(buttonTxt);
//    this.cancelButton = new JButton("Cancel");
//    bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
  }

  private void commonBuild(String actionButtonLabel){
    this.bodyPanel = new JPanel();
    this.addButton = new JButton(actionButtonLabel);
    this.cancelButton = new JButton("Cancel");
    bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
  }

  public void buildComponent() {
    JPanel masterInPanel = new JPanel(new BorderLayout());
    JPanel buttonInputPanel = new JPanel();
    JPanel pageEndPanel = new JPanel();

    Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    Container contentPane = this.getContentPane();
    pageEndPanel.setLayout(new BoxLayout(pageEndPanel, BoxLayout.Y_AXIS));

    pageEndPanel.add(bodyPanel);
    pageEndPanel.add(Box.createRigidArea(new Dimension(1, 20)));
    pageEndPanel.add(buttonInputPanel);
    cancelButton.addActionListener(new CancelListener());
    addButton.addActionListener(new DefaultAddlListener());
    buttonInputPanel.add(addButton);
    buttonInputPanel.add(cancelButton);

    masterInPanel.add(pageEndPanel, BorderLayout.PAGE_START);
    masterInPanel.setBorder(emptyBorder);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(masterInPanel, BorderLayout.LINE_START);

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(null);
    scrollPane.setBounds(50, 30, 300, 50);

    contentPane.add(scrollPane);
    this.getRootPane().setDefaultButton(addButton);
    addButton.requestFocus();
    this.addWindowListener(new WindowListener());
    scrollPane.addMouseListener(new MouseEvent());

    this.pack();
    this.setLocationRelativeTo(this.getOwner());
    this.setVisible(true);
  }

  public JDialog getDialogPnl() {
    return this;
  }

  public Boolean getIsCancel() {
    return isCancel;
  }

  public void setAddButtonListener(ActionListener I) {
    addButton.addActionListener(I);
  }

  public void setCancelButtonListener(ActionListener I) {
    cancelButton.addActionListener(I);
  }

  public void addBodyComponent(Component comp) {
    bodyPanel.add(comp);
  }

  public void addBodyComponent(Component comp, int index) {
    bodyPanel.add(comp, index);
  }

  public void kill() {
    this.dispose();
  }

  public JButton getAddButton() {
    return addButton;
  }

  public JButton getCancelButton() {
    return cancelButton;
  }

  public void setAddButton(JButton addButton) {
    this.addButton = addButton;
  }

  public void addCustomPropertyChangeListener(PropertyChangeListener l) {
    changes.addPropertyChangeListener(l);
  }

  public void setAddButtonEnabled(boolean isEnabled) {
    boolean oldValue = !isEnabled;
    getAddButton().setEnabled(isEnabled);
    changes.firePropertyChange("isAddButtonEnabled", oldValue, isEnabled);
  }

  public void setEnableAutoKill(boolean isEnable) {
    isAutoKill = isEnable;
  }

  class CancelListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      isCancel = true;
      kill();
    }
  }

  class DefaultAddlListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      isAddClicked = true;
      if (isAutoKill) {
        kill();
      }
    }
  }

  class WindowListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      isCancel = true;
    }
  }

  public class MouseEvent implements MouseListener {

    @Override
    public void mouseClicked(java.awt.event.MouseEvent arg0) {
      // System.out.println("--mouseClicked--");
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent arg0) {
      // System.out.println("--mouseEntered--");
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent arg0) {
      // System.out.println("--mouseExited--");
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent arg0) {
      // System.out.println("--mousePressed--");
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent arg0) {
      // System.out.println("--mouseReleased--");
      changes.firePropertyChange("isWindowAreaClicked", false, true);
    }
  }
}
