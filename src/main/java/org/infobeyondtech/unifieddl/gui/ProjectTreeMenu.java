package org.infobeyondtech.unifieddl.gui;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.infobeyondtech.unifieddl.core.UnifiedDL;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObject;
import org.infobeyondtech.unifieddl.guimodel.ProjectTreeObjectType;
import org.infobeyondtech.unifieddl.guiservice.ParallelProcess;
import org.infobeyondtech.unifieddl.processingzone.GHDI;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;
import org.infobeyondtech.unifieddl.utilities.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class ProjectTreeMenu extends JPopupMenu {

    DefaultMutableTreeNode originatorNode;
    JTree projectTree;
    JFrame parentFrame;

    public ProjectTreeMenu(JFrame parentFrame,JTree projectTree, DefaultMutableTreeNode originatorNode){
        this.parentFrame = parentFrame;
        this.projectTree = projectTree;
        this.originatorNode = originatorNode;
        init();
//        System.out.println("origin node:");
//        System.out.println(originatorNode);
    }

    private void init(){
        ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
        ProjectTreeObjectType treeObjectType = menuObject.getType();
        String treeObjectLabel = menuObject.getLabel();
        List<String> menuItemLabels = new ArrayList<String>();
        List<ActionListener> menuItemActions = new ArrayList<ActionListener>();

        // build menu contents
        switch (treeObjectType){
            // build menus for fixed tree title nodes
            case PROJECT_TREE_MENU:
                switch(treeObjectLabel){
                    case ProjectTreePanel.PROJECT_TREE_MENU_ROOT_TITLE:
                        menuItemLabels.add("Save Project");
                        menuItemActions.add(new SaveProjectAction());
                        break;
                    case ProjectTreePanel.RELATIONAL_DATA_SOURCE_MENU_TITLE:
                        menuItemLabels.add("Add Relational Data Source");
                        menuItemActions.add(new ShowRelationalDataSourceInputDialog());
                        break;
                    case ProjectTreePanel.JSON_DATA_SOURCE_MENU_TITLE:
                        menuItemLabels.add("Add JSON Data Source");
//                        menuItemLabels.add("Add Custom JSON Data Source");
                        menuItemActions.add(new ShowJsonDataSourceInputDialog());
//                        menuItemActions.add(new JsonCustomAddAction());
                        break;
                    case ProjectTreePanel.UNIFIED_VIEW_MENU_TITLE:
                        menuItemLabels.add("Fuse Heterogeneous Data Sources");
                        menuItemActions.add(new ShowFuseDataSourceInputDialog());
                        break;
                    default:
                        // default menu
                }
                // build menu
                break;

            case RELATIONAL_DATA_SOURCE:
            case JSON_DATA_SOURCE:
                // build menu
                menuItemLabels.add("Process RDF Mapping Procedure");
                menuItemLabels.add("Remove RDF Mapping");
                menuItemLabels.add("Show RDF Graph in Gui");
                menuItemLabels.add("Hide RDF Graph in Gui");
                menuItemLabels.add("Remove Relational Data Source");
                menuItemActions.add(new RdfGraphMappingProcedure(RdfMapper.Rdb2Rdf));
                menuItemActions.add(new RemoveRdfGraphMappingProcedure(RdfMapper.Rdb2Rdf));
                menuItemActions.add(new ShowRdfGraphInGui());
                menuItemActions.add(new HideRdfGraphInGui());
                menuItemActions.add(new RemoveTreeNodeAction());
                break;

            case UNIFIED_DATA_MODEL:
                // build menu
                menuItemLabels.add("Generate Word2Vec Training Corpus");
                menuItemLabels.add("Train Word2Vec Embedding Model");
                menuItemLabels.add("Run UnifiedDL Fusing Algorithm");

                menuItemLabels.add("Remove RDF Mapping");
                menuItemLabels.add("Show RDF Graph in Gui");
                menuItemLabels.add("Hide RDF Graph in Gui");

                menuItemLabels.add("Remove Unified Data Model");
                menuItemActions.add(new GenerateWord2VecTrainingCorpusAction());
                menuItemActions.add(new TrainWord2VecEmbeddingModelAction());
                menuItemActions.add(new RunFusingAlgorithmAction());

                menuItemActions.add(new RemoveRdfGraphMappingProcedure(RdfMapper.Rdb2Rdf));
                menuItemActions.add(new ShowRdfGraphInGui());
                menuItemActions.add(new HideRdfGraphInGui());

                menuItemActions.add(new RemoveTreeNodeAction());
                break;

            default:
                break;
        }

        // build menu items
        for (int i=0; i < menuItemLabels.size(); i++){
            JMenuItem menuItem = new JMenuItem(menuItemLabels.get(i));
            menuItem.addActionListener(menuItemActions.get(i));
            this.add(menuItem);
        }
    }

    private void selectTreeNode(DefaultMutableTreeNode node){
        // select parent tree node
        try {
            TreePath tPath = new TreePath(node.getPath());
            projectTree.setSelectionPath(tPath);
            Rectangle bounds = projectTree.getPathBounds(tPath);
            bounds.width = 0;
            projectTree.scrollRectToVisible(bounds);
        } catch (Exception ex) {
            System.out.println("Error selecting tree node");
            System.out.println(ex.getMessage());
        }
    }

    private class ShowRelationalDataSourceInputDialog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new RelationalDataSourceInsertDialog(parentFrame, projectTree,originatorNode);
        }
    }

    private class ShowJsonDataSourceInputDialog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new JsonDataSourceInsertDialog(parentFrame, projectTree,originatorNode);
        }
    }

//    private class JsonCustomAddAction implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//           // new JsonDataSourceInsertDialog(parentFrame, projectTree,originatorNode);
//
//
//            for(int i=39; i<=70; i++ ){
//                String sourceName = "unifiedDLSource2V" + i;
//                String connStr = "vehchain:Inf0Bey0nd!@localhost:3307/unifiedDLSource2V" + i;
//                System.out.println("Adding JSON data source ...");
//                ProjectTreeObject rDataSource = new ProjectTreeObject(ProjectTreeObjectType.JSON_DATA_SOURCE, sourceName);
//                rDataSource.put(UnifiedDLUtil.KEY_SOURCE_sourceConnString, connStr);
//                rDataSource.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf,false);
//                rDataSource.put(UnifiedDLUtil.KEY_description,"This is a test");
//                rDataSource.put(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI,false);
//                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rDataSource);
//                ((DefaultTreeModel) projectTree.getModel()).insertNodeInto(newNode, originatorNode, originatorNode.getChildCount());
//
////                try {
////                    TreePath tPath = new TreePath(newNode.getPath());
////                    projectTree.setSelectionPath(tPath);
////                    Rectangle bounds = projectTree.getPathBounds(tPath);
////                    bounds.width = 0;
////                    projectTree.scrollRectToVisible(bounds);
////                } catch (Exception ex) {
////                    System.out.println("Error adding JSON data");
////                    System.out.println(ex.getMessage());
////                }
//
//                ParallelProcess rdfMappingProcess = new ParallelProcess("Processing RDF Mapping") {
//                    @Override
//                    public void mainBackgroundProcess() {
//                        try {
//                            System.out.println("Data ingestion procedure  ...");
//                            ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
//                            String connString = (String) menuObject.get(UnifiedDLUtil.KEY_SOURCE_sourceConnString);
//                            if (connString != null) {
//                                RdfDirectMapper mapper = new RdfDirectMapper();
//                               mapper.processRdfMapper(mapping, connString);
//                                File f = new File(mapper.getOutputFile());
//                                if (isFinished && f.exists()) {
//                                    RDF4JDatabaseHelper helper = new RDF4JDatabaseHelper();
//                                    GHDI ghdiModule = new GHDI();
//                                    String rdfRepo = menuObject.getLabel() + "-RdfRepo";
//                                    if (helper.isRepositoryExists(rdfRepo)) {
//                                        helper.removeRepository(rdfRepo);
//                                    }
//                                    ghdiModule.importRdfFromFile(mapper.getOutputFile(), RDFFormat.TURTLE, rdfRepo);
//                                    menuObject.put(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo, rdfRepo);
//                                    menuObject.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf, true);
//                                    selectTreeNode((DefaultMutableTreeNode) originatorNode.getParent());
//                                    selectTreeNode(originatorNode);
//                                }
//                            }
//                        } catch (Exception Ex){
//                            isFinished = false;
//                        }
//                    }
//
//                    @Override
//                    public void doneProcess() {
//                        if (isFinished) {
//                            GuiUtil.getInformationDialog(parentFrame,
//                                    "<html>UnifiedDL prototype completes RDF mapping procedure.</html>");
//                        } else {
//                            GuiUtil.getWarningDialog(parentFrame,
//                                    "<html>UnifiedDL prototype did not process RDF mapping algorithm successfully.<br> Please try again later.</html>");
//                        }
//                        System.out.println("finished ...");
//                    };
//                };
//                rdfMappingProcess.execute();
//
//            }
//
//
//
//        }
//    }

    private class ShowFuseDataSourceInputDialog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Adding json data source ...");
            new UnifiedDataModelGenerationDialog(parentFrame, projectTree,originatorNode);
        }
    }

    private class RemoveTreeNodeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectTreeNode((DefaultMutableTreeNode)originatorNode.getParent());
            ((DefaultTreeModel) projectTree.getModel()).removeNodeFromParent(originatorNode);
        }
    }

    private class RdfGraphMappingProcedure implements ActionListener {
        RdfMapper mapping;
        Boolean isFinished = false;
        RdfGraphMappingProcedure(RdfMapper mapping){
            this.mapping = mapping;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess rdfMappingProcess = new ParallelProcess("Processing RDF Mapping") {
                @Override
                public void mainBackgroundProcess() {
                    try {
                        System.out.println("Data ingestion procedure  ...");
                        ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                        String connString = (String) menuObject.get(UnifiedDLUtil.KEY_SOURCE_sourceConnString);
                        if (connString != null) {
                            RdfDirectMapper mapper = new RdfDirectMapper();
                            isFinished = mapper.processRdfMapper(mapping, connString);
                            File f = new File(mapper.getOutputFile());
                            if (isFinished && f.exists()) {
                                RDF4JDatabaseHelper helper = new RDF4JDatabaseHelper();
                                GHDI ghdiModule = new GHDI();
                                String rdfRepo = menuObject.getLabel() + "-RdfRepo";
                                if (helper.isRepositoryExists(rdfRepo)) {
                                    helper.removeRepository(rdfRepo);
                                }
                                ghdiModule.importRdfFromFile(mapper.getOutputFile(), RDFFormat.TURTLE, rdfRepo);
                                menuObject.put(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo, rdfRepo);
                                menuObject.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf, true);
                                selectTreeNode((DefaultMutableTreeNode) originatorNode.getParent());
                                selectTreeNode(originatorNode);
                            }
                        }
                    } catch (Exception Ex){
                        isFinished = false;
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html>UnifiedDL prototype completes RDF mapping procedure.</html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html>UnifiedDL prototype did not process RDF mapping algorithm successfully.<br> Please try again later.</html>");
                    }
                    System.out.println("finished ...");
                };
            };
            rdfMappingProcess.execute();

        }
    }

    private class RemoveRdfGraphMappingProcedure implements ActionListener {
        RdfMapper mapping;
        Boolean isFinished = true;
        RemoveRdfGraphMappingProcedure(RdfMapper mapping){
            this.mapping = mapping;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess rdfMappingProcess = new ParallelProcess("Processing RDF Mapping") {
                @Override
                public void mainBackgroundProcess() {
                    try {
                        System.out.println("Data ingestion procedure  ...");
                        ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                        String rdfRepo = (String) menuObject.get(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo);
                        RDF4JDatabaseHelper helper = new RDF4JDatabaseHelper();
                        if (rdfRepo!=null && helper.isRepositoryExists(rdfRepo)) {
                            helper.removeRepository(rdfRepo);
                            menuObject.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf,false);
                            menuObject.remove(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo);
                            selectTreeNode((DefaultMutableTreeNode) originatorNode.getParent());
                            selectTreeNode(originatorNode);
                        }
                    } catch (Exception Ex){
                        isFinished = false;
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html>UnifiedDL prototype completes RDF mapping procedure.</html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html>UnifiedDL prototype did not process RDF mapping algorithm successfully.<br> Please try again later.</html>");
                    }
                    System.out.println("finished ...");
                };
            };
            rdfMappingProcess.execute();


        }
    }

    private class ShowRdfGraphInGui implements ActionListener {
        Boolean isFinished = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess rdfMappingProcess = new ParallelProcess("Loading RDF Graph in GUI") {
                @Override
                public void mainBackgroundProcess() {
                    try {
                        ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                        menuObject.put(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI,true);
                        selectTreeNode((DefaultMutableTreeNode) originatorNode.getParent());
                        selectTreeNode(originatorNode);
                    } catch (Exception Ex){
                        isFinished = false;
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html>UnifiedDL prototype loads Rdf graph successfully in GUI.</html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html>UnifiedDL prototype could not loads Rdf graph successfully in GUI.<br> Please try again later.</html>");
                    }
                    System.out.println("finished ...");
                };
            };
            rdfMappingProcess.execute();
        }
    }

    private class HideRdfGraphInGui implements ActionListener {
        Boolean isFinished = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess rdfMappingProcess = new ParallelProcess("Hiding RDF Graph in GUI") {
                @Override
                public void mainBackgroundProcess() {
                    try {
                        ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                        menuObject.put(UnifiedDLUtil.KEY_IS_LOAD_RDF_IN_GUI,false);
                        selectTreeNode((DefaultMutableTreeNode) originatorNode.getParent());
                        selectTreeNode(originatorNode);
                    } catch (Exception Ex){
                        isFinished = false;
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html>UnifiedDL prototype loads Rdf graph successfully in GUI.</html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html>UnifiedDL prototype could not loads Rdf graph successfully in GUI.<br> Please try again later.</html>");
                    }
                    System.out.println("finished ...");
                };
            };
            rdfMappingProcess.execute();
        }
    }

    private class SaveProjectAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Saving Project");
            File savedFile = new File("c:\\Users\\jayan\\workspace\\test");
            try {
                ObjectOutputStream out =
                        new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(savedFile)));
                out.writeObject(projectTree);
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class GenerateWord2VecTrainingCorpusAction implements ActionListener {
        Boolean isFinished = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess trainingCorpusGenerationProcess = new ParallelProcess("Processing RDF Mapping") {
                @Override
                public void mainBackgroundProcess() {
                    System.out.println("Data ingestion procedure  ...");
                    ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                    String rdf4jRepo1 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_relationalSourceEdf4jRepo);
                    String rdf4jRepo2 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_jsonSourceRdf4jRepo);
                    String[] repositories = new String[]{rdf4jRepo1, rdf4jRepo2};
                    Word2VecEmbeddingModule word2VectorModule = new Word2VecEmbeddingModule();
                    String corpusOutputFile = "unifieddlWord2VecTrainCorpusFinalLatest";
                    String outputFileLoc = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\"+corpusOutputFile+".txt";

                    boolean isCorpusSuccess = true;
                    for(String repository:repositories){
                        boolean isSuccess = word2VectorModule.extractWord2VecTrainingCorpus(repository, null, outputFileLoc);
                        if(!isSuccess){
                            isCorpusSuccess = false;
                            break;
                        }
                    }

                    boolean isTrainSuccess = false;
                    if(isCorpusSuccess){
                        isTrainSuccess = word2VectorModule.trainWord2VecEmbeddingModel(repositories);
                    }

                    if(isCorpusSuccess && isTrainSuccess){
                        isFinished = true;
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html> UnifiedDL prototype completes generating training corpus </html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html> UnifiedDL prototype did not generate Word2Vec training corpus successfully.<br> Please try again later.</html>");
                    }
                };
            };
            trainingCorpusGenerationProcess.execute();
        }
    }

    private class TrainWord2VecEmbeddingModelAction implements ActionListener {
        Boolean isFinished = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess trainingCorpusGenerationProcess = new ParallelProcess("Processing RDF Mapping") {
                @Override
                public void mainBackgroundProcess() {
                    System.out.println("Data ingestion procedure  ...");
                    ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                    String rdf4jRepo1 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_relationalSourceEdf4jRepo);
                    String rdf4jRepo2 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_jsonSourceRdf4jRepo);
                    isFinished = new Word2VecEmbeddingModule().retrieveAndStoreVectors(new String[]{rdf4jRepo1, rdf4jRepo2});
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html> UnifiedDL prototype completes generating training corpus </html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html> UnifiedDL prototype did not generate Word2Vec training corpus successfully. <br> Please try again later.</html>");
                    }
                };
            };
            trainingCorpusGenerationProcess.execute();
        }
    }

    private class RunFusingAlgorithmAction implements ActionListener {
        Boolean isFinished = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            ParallelProcess runFusingAlgorithmProcess = new ParallelProcess("Running Fusing Algorithm") {
                @Override
                public void mainBackgroundProcess() {
                    System.out.println("Graph Fusing procedure  ...");
                    ProjectTreeObject menuObject = (ProjectTreeObject) originatorNode.getUserObject();
                    String rdf4jRepo1 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_relationalSourceEdf4jRepo);
                    String rdf4jRepo2 = (String)menuObject.get(UnifiedDLUtil.KEY_UNIFIEDMODEL_jsonSourceRdf4jRepo);

                    boolean isFusingAlgorithm = new GHDI().fusingAlgorithm(rdf4jRepo1,rdf4jRepo2, false);

                    if(isFusingAlgorithm){
                        boolean isFused = new Rdf4jTripleStoreProcessor().fuseRdfGraphs(new String[]{rdf4jRepo1, rdf4jRepo2},true);
                        if(isFused){
                            String fusedRdf4jRepo = "Fused-" + rdf4jRepo1 + "-" + rdf4jRepo2;
                            menuObject.put(UnifiedDLUtil.KEY_SOURCE_rdf4jRepo,fusedRdf4jRepo);
                            menuObject.put(UnifiedDLUtil.KEY_SOURCE_isMappedToRdf,true);
                            selectTreeNode((DefaultMutableTreeNode)originatorNode.getParent());
                            selectTreeNode(originatorNode);
                        }
                    }
                }

                @Override
                public void doneProcess() {
                    if (isFinished) {
                        GuiUtil.getInformationDialog(parentFrame,
                                "<html> UnifiedDL prototype completes running fusing algorithm </html>");
                    } else {
                        GuiUtil.getWarningDialog(parentFrame,
                                "<html> UnifiedDL prototype did not finished fusing algorithm successfully.<br> Please try again later.</html>");
                    }
                };
            };
            runFusingAlgorithmProcess.execute();
        }
    }



//    menuItemLabels.add("Generate Word2Vec Training Corpus");
//                menuItemLabels.add("Train Word2Vec Embedding Model");
//                menuItemLabels.add("Run UnifiedDL Fusing Algorithm");
//                menuItemLabels.add("Remove Unified Data Model");
}