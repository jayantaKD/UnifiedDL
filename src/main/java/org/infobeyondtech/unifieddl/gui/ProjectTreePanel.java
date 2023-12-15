package org.infobeyondtech.unifieddl.gui;

import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObject;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObjectType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProjectTreePanel extends JPanel {
    public static final String PROJECT_TREE_MENU_ROOT_TITLE = "UnifiedDL Prototype";
    public static final String RELATIONAL_DATA_SOURCE_MENU_TITLE = "Relational Database Source";
    public static final String JSON_DATA_SOURCE_MENU_TITLE = "JSON Database Source";
    public static final String UNIFIED_VIEW_MENU_TITLE = "Unified Data Model";

    private DefaultTreeModel model;
    JTree projectTree;
    AppView parentFrame;

    public ProjectTreePanel(AppView parentFrame) {
        this.parentFrame = parentFrame;
        this.model = initDefaultModel();
        this.projectTree = new JTree(model);
        buildContent();
    }

    public ProjectTreePanel(AppView parentFrame,DefaultTreeModel model) {
        this.parentFrame = parentFrame;
        this.model = model;
        this.projectTree = new JTree(model);
        buildContent();
    }

    public ProjectTreePanel(AppView parentFrame,JTree projectTree) {
        this.parentFrame = parentFrame;
        this.model = (DefaultTreeModel) projectTree.getModel();
        this.projectTree = projectTree;
        buildContent();
    }

    private void buildContent(){
//        JScrollPane treePanel = new JScrollPane(projectTree);
        Border empty = BorderFactory.createEmptyBorder(5, 12, 100, 12);
//        treePanel.setBorder(null);
        projectTree.setDragEnabled(true);
        projectTree.setDropMode(DropMode.ON_OR_INSERT);
//        projectTree.setTransferHandler(new TreeTransferHandler(controller));
//        projectTree.setCellRenderer(new CustomTreeCellRenderer(searchText));
        projectTree.setToggleClickCount(0);
        projectTree.setBorder(empty/* BorderFactory.createCompoundBorder(border, empty) */);
        // tree object selection listener for updating view
        projectTree.addTreeSelectionListener(new ProjectTreeSelectionListenner());
        // tree object click listener for action menu options
        projectTree.addMouseListener(new ProjectTreeMouseClickListenner());
        JScrollPane treePanel = new JScrollPane(projectTree);
        treePanel.setBorder(null);
        this.add(treePanel,BorderLayout.CENTER);
    }

    private DefaultTreeModel initDefaultModel(){
        DefaultMutableTreeNode projectRoot = new DefaultMutableTreeNode(
                new ProjectTreeObject(ProjectTreeObjectType.PROJECT_TREE_MENU,PROJECT_TREE_MENU_ROOT_TITLE));
        DefaultMutableTreeNode inputRelationDatabase = new DefaultMutableTreeNode(
                new ProjectTreeObject(ProjectTreeObjectType.PROJECT_TREE_MENU,RELATIONAL_DATA_SOURCE_MENU_TITLE));
        DefaultMutableTreeNode inputJsonDatabase = new DefaultMutableTreeNode(
                new ProjectTreeObject(ProjectTreeObjectType.PROJECT_TREE_MENU,JSON_DATA_SOURCE_MENU_TITLE));
        DefaultMutableTreeNode unifiedView = new DefaultMutableTreeNode(
                new ProjectTreeObject(ProjectTreeObjectType.PROJECT_TREE_MENU,UNIFIED_VIEW_MENU_TITLE));
        projectRoot.insert(inputRelationDatabase,0);
        projectRoot.insert(inputJsonDatabase,1);
        projectRoot.insert(unifiedView,2);
        return new DefaultTreeModel(projectRoot);
    }


 /* Listener classes */
    private class ProjectTreeSelectionListenner implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (e.getNewLeadSelectionPath() != null) {
//                System.out.println(e.getNewLeadSelectionPath());
                //TODO: Update view according to selected tree object
                DefaultMutableTreeNode originTreeNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
                ProjectTreeObject originTreeObject = (ProjectTreeObject) originTreeNode.getUserObject();
                System.out.println("got it here!");
                System.out.println(originTreeObject);

//                ProjectTreeObjectType treeObjectType = originTreeObject.getType();
//                String treeObjectLabel = originTreeObject.getLabel();

                parentFrame.loadMainBodyContent(new ProjectTreeObjectView(originTreeObject));
            }
        }
    }

    private class ProjectTreeMouseClickListenner implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            if (e.getComponent() instanceof JTree) {
                JTree tree = (JTree) e.getComponent();
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());

                if (treePath != null) {
                    // catch right click on tree object
                    if (SwingUtilities.isRightMouseButton(e) /*||
                            // hack for mac mouse right click
                            (AppUti.isMac() && e.isControlDown())*/) {

                        System.out.println("right click!!");

                        // select tree object upon right click
                        try {
                            tree.setSelectionPath(treePath);
                            Rectangle bounds = tree.getPathBounds(treePath);
                            bounds.width = 0;
                            tree.scrollRectToVisible(bounds);
                        } catch (Exception ex) {}

                        // TODO: show popup menu for action
                        Object[] pathNodes = treePath.getPath();
                        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) pathNodes[pathNodes.length-1];
                        new ProjectTreeMenu(parentFrame,tree, selNode).show(e.getComponent(),e.getX(),e.getY());



/*                        treeService.selectTreeNode(treePath);
                        GuiPopupMenuTree menu = new GuiPopupMenuTree(
                                new SptTreeActionListener(treeService, controller), treeService, controller);

                        menu.show(e.getComponent(), e.getX(), e.getY());*/
                    } /*else if (e.getClickCount() == 2) {
                        GuiPopupMenuTree menu = new GuiPopupMenuTree(
                                new SptTreeActionListener(treeService, controller), treeService, controller);

                        if (menu.getDoubleClickMenuItem() != null) {
                            menu.getDoubleClickMenuItem().doClick();
                        }
                    }*/
                }
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}