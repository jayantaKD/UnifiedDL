package org.infobeyondtech.unifieddl.gui;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObject;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObjectType;
import org.infobeyondtech.unifieddl.guiservice.SearchActionListener;
import org.infobeyondtech.unifieddl.guiservice.SearchResetActionListener;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;
import org.infobeyondtech.unifieddl.utilities.UnifiedDLUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ProjectTreeObjectView extends JPanel {

    String htmlTemplate = "<html> %s <html>";
    String textTemplate = "<font color = \"%s\"> %s </font>";

    ProjectTreeObject treeObject;

    public ProjectTreeObjectView(ProjectTreeObject treeObject){
        super(new BorderLayout());
        this.treeObject =treeObject;
        buildContent();
    }

    private void buildContent(){
        // build menu contents
        buildHeader();
        buildBody();
    }

    private void buildHeader(){
        ProjectTreeObjectType treeObjectType = treeObject.getType();
        String blankLabelText = "";
        switch(treeObjectType){
            case RELATIONAL_DATA_SOURCE:
            case JSON_DATA_SOURCE:
            case UNIFIED_DATA_MODEL:
                blankLabelText = String.format(htmlTemplate,
                        treeObjectType.toString() + ":" +
                        String.format(textTemplate,"blue","Name =") + String.format(textTemplate,"red",treeObject.getLabel()) + ","+
                        String.format(textTemplate,"blue","Connection String =") + String.format(textTemplate,"red",treeObject.get(UnifiedDLUtil.KEY_SOURCE_sourceConnString)));
                break;
            case PROJECT_TREE_MENU:
                blankLabelText = String.format(htmlTemplate,String.format(textTemplate,"blue",treeObjectType.toString()));
                break;
            default:
                blankLabelText = "<html> <font color = \"blue\"> UnifiedDL Prototype</font><html>";
                break;
        }
        JLabel blankLabel = new JLabel(blankLabelText);
        blankLabel.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        blankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blankLabel.setVerticalAlignment(SwingConstants.TOP);
        this.add(BorderLayout.NORTH,blankLabel);
    }
    private void buildBody(){
        ProjectTreeObjectType treeObjectType = treeObject.getType();
        switch(treeObjectType){
            case RELATIONAL_DATA_SOURCE:
            case JSON_DATA_SOURCE:
            case UNIFIED_DATA_MODEL:
                String rdf4jRepo = (String) treeObject.get(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo);
                if (rdf4jRepo == null){
                    JLabel blankLabel = new JLabel("<html><br><br><br><br> <font color = \"blue\"> <font color = \"red\"> RDF Mapping</font> "
                            + " data is not available! </font> </font><html>");
                    blankLabel.setFont(new Font("Calibri Light", Font.PLAIN, 25));
                    blankLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    blankLabel.setVerticalAlignment(SwingConstants.TOP);
                    this.add(BorderLayout.CENTER,blankLabel);
                } else {
                    Boolean isLoadRdfInGui = (Boolean) treeObject.get(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI);
                    if(isLoadRdfInGui!=null && isLoadRdfInGui){
                        //Creating the panel at bottom and adding components
                        JPanel panel = new JPanel(); // the panel is not visible in output
                        JLabel label = new JLabel("Search Query");
                        JTextArea  tf = new JTextArea (10,50); // accepts upto 10 characters
                        JButton send = new JButton("Search");
                        JButton reset = new JButton("Reset");
                        Border border = BorderFactory.createLineBorder(Color.BLACK);
                        tf.setBorder(BorderFactory.createCompoundBorder(border,
                                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

                        //Lay out the label and scroll pane from top to bottom.
                        JPanel checkPane = new JPanel();
                        checkPane.setLayout(new BoxLayout(checkPane, BoxLayout.PAGE_AXIS));
                        CheckboxGroup queryTypeGroup = new CheckboxGroup();
                        Checkbox keyword = new Checkbox("Keyword", queryTypeGroup,true);
                        Checkbox sparql = new Checkbox("SPARQL", queryTypeGroup,false);

                        // Create checkboxes with different names
                        checkPane.add(keyword);
                        checkPane.add(sparql);

                        // Text Area at the Center
                        panel.add(label); // Components Added using Flow Layout
                        panel.add(tf);
                        panel.add(checkPane);
                        panel.add(send);
                        panel.add(reset);

                        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
                        Graph combineGraphs = gsFactory.buildFullGraphStreamModel(new String[]{rdf4jRepo},false);
                        SwingViewer viewer = new SwingViewer(combineGraphs, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
                        viewer.enableAutoLayout();
                        View view = viewer.addDefaultView(false);

                        send.addActionListener(new SearchActionListener(combineGraphs,tf,queryTypeGroup));
                        reset.addActionListener(new SearchResetActionListener(combineGraphs,tf));

                        //Adding Components to the frame.
                        this.add(BorderLayout.SOUTH, panel);
                        this.add(BorderLayout.CENTER, (Component) view);
                    }
                    else {
                        JLabel blankLabel = new JLabel("<html><br><br><br><br> <font color = \"blue\"> <font color = \"red\"> RDF Mapping</font> "
                                + " data is available. But RDF graph is hidden. </font> </font><html>");
                        blankLabel.setFont(new Font("Calibri Light", Font.PLAIN, 25));
                        blankLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        blankLabel.setVerticalAlignment(SwingConstants.TOP);
                        this.add(BorderLayout.CENTER,blankLabel);
                    }
                }
                break;
            case PROJECT_TREE_MENU:
                break;
            default:
                break;
        }
    }
}
