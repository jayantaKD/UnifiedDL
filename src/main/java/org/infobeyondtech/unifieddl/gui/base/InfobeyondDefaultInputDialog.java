package org.infobeyondtech.unifieddl.gui.base;

import javax.swing.*;
import java.awt.*;

public abstract class InfobeyondDefaultInputDialog extends InfobeyondDialog {

    public InfobeyondDefaultInputDialog(Frame baseFrame, String actionButtonLabel, String dialogTitle) {
        super(baseFrame, actionButtonLabel, dialogTitle);
        initDefaultInputDialog();
    }

    protected abstract String[] initLabels();
    protected abstract JComponent[] initInputComponents();
    protected abstract String initTitleBorder();

    private void initDefaultInputDialog(){
        String[] inputLabels = initLabels();
        JComponent[] inputs = initInputComponents();
        String titleBorder = initTitleBorder();
        JPanel inputFieldsPanel = buildInputFieldsPanel(inputLabels, inputs,titleBorder);
        this.addBodyComponent(inputFieldsPanel);
    }

    private JPanel buildInputFieldsPanel(String[] inputLabels, JComponent[] inputs, String titleBorder) {
        JPanel inputFieldsPanel = new JPanel(new SpringLayout());
        inputFieldsPanel.setBorder(new InfobeyondTitleBorder(titleBorder));
        int rowCount = 0;

        for(int i = 0; i<inputLabels.length; i++){
            inputFieldsPanel.add(new JLabel(inputLabels[i]));
            inputFieldsPanel.add(inputs[i]);
            inputFieldsPanel.add(Box.createRigidArea(new Dimension(4, 1)));
            inputFieldsPanel.add(Box.createRigidArea(new Dimension(4, 1)));
            rowCount = rowCount + 2;
        }
        SpringUtilities.makeCompactGrid(inputFieldsPanel, rowCount, 2, 6, 6, 6, 6);
        return inputFieldsPanel;
    }
}
