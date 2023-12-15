package org.infobeyondtech.unifieddl.gui;

import org.infobeyondtech.unifieddl.gui.base.InfobeyondDefaultInputDialog;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObject;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObjectType;
import org.infobeyondtech.unifieddl.utilities.UnifiedDLUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JsonDataSourceInsertDialog extends InfobeyondDefaultInputDialog {

    private JTextField sourceNameField;
    private JTextField connStringField ;
    private JTextField description ;
    private JTree projectTree;
    private DefaultMutableTreeNode originatorNode;

    public JsonDataSourceInsertDialog(Frame baseFrame, JTree projectTree, DefaultMutableTreeNode originatorNode) {
        super(baseFrame, "OK","JSON Data Source Input");
        this.projectTree = projectTree;
        this.originatorNode = originatorNode;
        init();
    }

    private void init(){
        addButton.addActionListener(new AddJsonDataSourceAction());
        buildComponent();
    }

    @Override
    protected String[] initLabels() {
        return new String[]{"Source Name", "Connection String", "Description"};
    }

    @Override
    protected JComponent[] initInputComponents() {
        sourceNameField = new JTextField(30);
        connStringField = new JTextField(30);
        description = new JTextField(30);
        return new JComponent[]{sourceNameField,connStringField,description};
    }

    @Override
    protected String initTitleBorder() {
        return "JSON Data Source";
    }

    private class AddJsonDataSourceAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Adding JSON data source ...");
            ProjectTreeObject rDataSource = new ProjectTreeObject(ProjectTreeObjectType.JSON_DATA_SOURCE,sourceNameField.getText());
            rDataSource.put(UnifiedDLUtil.KEY_SOURCE_sourceConnString,connStringField.getText());
            rDataSource.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf,false);
            rDataSource.put(UnifiedDLUtil.KEY_description,"This is a test");
            rDataSource.put(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI,false);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rDataSource);
            ((DefaultTreeModel) projectTree.getModel()).insertNodeInto(newNode, originatorNode, originatorNode.getChildCount());

            try {
                TreePath tPath = new TreePath(newNode.getPath());
                projectTree.setSelectionPath(tPath);
                Rectangle bounds = projectTree.getPathBounds(tPath);
                bounds.width = 0;
                projectTree.scrollRectToVisible(bounds);
            } catch (Exception ex) {
                System.out.println("Error adding JSON data");
                System.out.println(ex.getMessage());
            }
        }
    }
}