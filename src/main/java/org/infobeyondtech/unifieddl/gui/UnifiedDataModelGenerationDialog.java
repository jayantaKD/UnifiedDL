package org.infobeyondtech.unifieddl.gui;

import org.infobeyondtech.unifieddl.gui.base.InfobeyondDefaultInputDialog;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObject;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObjectType;
import org.infobeyondtech.unifieddl.utilities.UnifiedDLUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class UnifiedDataModelGenerationDialog extends InfobeyondDefaultInputDialog {

    private JTextField relationalSourceNameField;
    private JTextField jsonSourceNameField;
    private JTextField fusedDataModelNameField;
    private JTextField descriptionField;
    private JTree projectTree;
    private DefaultMutableTreeNode originatorNode;

    public UnifiedDataModelGenerationDialog(Frame baseFrame, JTree projectTree, DefaultMutableTreeNode originatorNode) {
        super(baseFrame, "OK","Unified Data Model Input");
        this.projectTree = projectTree;
        this.originatorNode = originatorNode;
        init();
    }

    private void init(){
        addButton.addActionListener(new UnifiedDataModelGenerationAction());
        buildComponent();
    }

    @Override
    protected String[] initLabels() {
        return new String[]{"Relational Data Source Name", "JSON Data Source Name", "Fused Data Model Name", "Description"};
    }

    @Override
    protected JComponent[] initInputComponents() {
        relationalSourceNameField = new JTextField(30);
        jsonSourceNameField = new JTextField(30);
        fusedDataModelNameField = new JTextField(30);
        descriptionField = new JTextField(30);
        return new JComponent[]{relationalSourceNameField, jsonSourceNameField, fusedDataModelNameField, descriptionField};
    }

    @Override
    protected String initTitleBorder() {
        return "Unified Data Model Generation";
    }

    private class UnifiedDataModelGenerationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Generating unified data model ...");
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) projectTree.getModel().getRoot();
            Enumeration<TreeNode> child = root.children();
            ProjectTreeObject relObject = null;
            ProjectTreeObject jsonObject = null;

            while(child.hasMoreElements()){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) child.nextElement();
                ProjectTreeObject nodeObject = (ProjectTreeObject) node.getUserObject();
                Boolean isRelationalTitle = nodeObject.getLabel().equals(ProjectTreePanel.RELATIONAL_DATA_SOURCE_MENU_TITLE) &&
                        nodeObject.getType().equals(ProjectTreeObjectType.PROJECT_TREE_MENU);
                Boolean isJsonTitle = nodeObject.getLabel().equals(ProjectTreePanel.JSON_DATA_SOURCE_MENU_TITLE) &&
                        nodeObject.getType().equals(ProjectTreeObjectType.PROJECT_TREE_MENU);

                if(isRelationalTitle){
                    Enumeration<TreeNode> relationalTitleChild = node.children();
                    while(relationalTitleChild.hasMoreElements()){
                        DefaultMutableTreeNode relChild = (DefaultMutableTreeNode) relationalTitleChild.nextElement();
                        ProjectTreeObject relChildObject = (ProjectTreeObject) relChild.getUserObject();
                        if(relChildObject.getLabel().toLowerCase().equals(relationalSourceNameField.getText().trim().toLowerCase())){
                            relObject = relChildObject;
                            break;
                        }
                    }
                }

                if(isJsonTitle){
                    Enumeration<TreeNode> jsonTitleChild = node.children();
                    while(jsonTitleChild.hasMoreElements()){
                        DefaultMutableTreeNode relChild = (DefaultMutableTreeNode) jsonTitleChild.nextElement();
                        ProjectTreeObject relChildObject = (ProjectTreeObject) relChild.getUserObject();
                        if(relChildObject.getLabel().toLowerCase().equals(jsonSourceNameField.getText().trim().toLowerCase())){
                            jsonObject = relChildObject;
                            break;
                        }
                    }
                }

                if(relObject!=null && jsonObject!=null){
                    break;
                }
            }

            if(relObject!=null && jsonObject!=null){
                boolean isRelMappedToRdf = (boolean) relObject.get(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf);
                boolean isJsonMappedToRdf = (boolean) jsonObject.get(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf);

                if(isRelMappedToRdf && isJsonMappedToRdf){
                    String relRdf4jRepo = (String) relObject.get(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo);
                    String jsonRdf4jRepo = (String) jsonObject.get(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo);

                    ProjectTreeObject rDataSource = new ProjectTreeObject(ProjectTreeObjectType.UNIFIED_DATA_MODEL, fusedDataModelNameField.getText());
//                  relationalSourceNameField, jsonSourceNameField, fusedDataModelNameField, descriptionField
                    rDataSource.put(UnifiedDLUtil.KEY_UNIFIEDMODEL_relationalSourceName, relationalSourceNameField.getText());
                    rDataSource.put(UnifiedDLUtil.KEY_UNIFIEDMODEL_jsonSourceName, jsonSourceNameField.getText());
                    rDataSource.put(UnifiedDLUtil.KEY_UNIFIEDMODEL_relationalSourceEdf4jRepo, relRdf4jRepo);
                    rDataSource.put(UnifiedDLUtil.KEY_UNIFIEDMODEL_jsonSourceRdf4jRepo, jsonRdf4jRepo);
                    rDataSource.put(UnifiedDLUtil.KEY_description, descriptionField.getText());
                    rDataSource.put(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI,true);

                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rDataSource);
                    ((DefaultTreeModel) projectTree.getModel()).insertNodeInto(newNode, originatorNode, originatorNode.getChildCount());

                    try {
                        TreePath tPath = new TreePath(newNode.getPath());
                        projectTree.setSelectionPath(tPath);
                        Rectangle bounds = projectTree.getPathBounds(tPath);
                        bounds.width = 0;
                        projectTree.scrollRectToVisible(bounds);
                    } catch (Exception ex) {
                        System.out.println("Error Generating Unified Data Model");
                        System.out.println(ex.getMessage());
                    }
                }
            }

//            rDataSource.put(ProjectTreeObject.KEY_SOURCE_sourceConnString, jsonSourceNameField.getText());
//            rDataSource.put(ProjectTreeObject.KEY_SOURCE_isMappedToRdf,false);
//            rDataSource.put(ProjectTreeObject.KEY_description,"This is a test");
//            this is testing - testing

        }
    }
}