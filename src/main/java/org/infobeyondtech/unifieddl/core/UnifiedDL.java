package org.infobeyondtech.unifieddl.core;
import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.util.Strings;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.infobeyondtech.unifieddl.gui.AppView;
import org.infobeyondtech.unifieddl.processingzone.GHDI;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;
import org.infobeyondtech.unifieddl.utilities.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Hello world!
 *
 */

public class UnifiedDL  {
    public static void main(String... args) {
        GHDI ghdiModel = new GHDI();
// RDF store repos: unifiedDLSource1, unifiedDLSource2, unifiedDLSource1V2, unifiedDLSource2V2, unifiedDLSource1V3, unifiedDLSource2V3
        // import into RDf4J repository
//      String fileLoc = "C:\\Users\\jayan\\workspace\\pyrdb2rdf\\FinalTriples\\unifiedDLSource1V3L.n3";
//      new UnifiedDL().importRDFDatabase("unifiedDLSource1V3L", fileLoc);

        // extract Word2Vec training corpus from RDF4J repositories
//      boolean isSuccessTrainCorpus =  new UnifiedDL().extractWord2VecTrainingCorpus(new String[]{"unifiedDLSource1V3L","unifiedDLSource2V3L"});
//      System.out.println(isSuccessTrainCorpus);

//        // train embedding model
//        boolean isSuccessTrain = new Word2VecEmbeddingModule().trainWord2VecEmbeddingModel();
//        System.out.println(isSuccessTrain);
//
////      // retrieve and store embedding vector
//        boolean isSuccessStore = new Word2VecEmbeddingModule().retrieveAndStoreVectors(new String[]{"unifiedDLSource1V1","unifiedDLSource2V1"});
//        System.out.println(isSuccessStore);
//
//        // applying fusing algorithm
//        ghdiModel.fusingAlgorithm("unifiedDLSource1V3L","unifiedDLSource2V3L");
//
//        //
//        new Rdf4jTripleStoreProcessor().fuseRdfGraphs(new String[]{"unifiedDLSource1V3L", "unifiedDLSource2V3L"},true);


// applying keyword search algorithm
//      ghdiModel.keywordSearchAlgorithm("unifiedDLSource1V1","unifiedDLSource2V1");

//      ghdiModel.showGraph("unifiedDLSource2V1L","unifiedDLSourceTurtle2V8");
//      new UnifiedDL().test();
//      new UnifiedDL().test();

//      new DataSourceUseCase().showMap();
//      new UnifiedDL().groupSteinerTreeTest();
//      ghdiModel.keywordSearchAlgorithm("unifiedDLSource1V1L","unifiedDLSource2V1L");
//        new UnifiedDL().swingViewGuiUnifiedDL();
//      ghdiModel.fusingAlgorithm("unifiedDLSource1V3L","unifiedDLSource2V3L");
//     new Rdf4jTripleStoreProcessor().fuseRdfGraphs(new String[]{"unifiedDLSource1V3L", "unifiedDLSource2V3L"},true);

//      System.out.println(new RDF4JDatabaseHelper().isRepositoryExists("testatestatesta"));
//      ghdiModel.showGraph("Fused-unifiedDLSource1V3L-unifiedDLSource2V3L");
//      ghdiModel.showGraph("unifiedDLSource1V1L");

//      new RDF4JDatabaseHelper().retrieveRDFRepoTriples("Fused-unifiedDLSource1V3L-unifiedDLSource2V3L");
//      new Rdf4jTripleStoreProcessor().showTriplies();

//        RdfDirectMapper mapper = new RdfDirectMapper();
//        int mapping = mapper.processRdfMapper(RdfMapper.Rdb2Rdf,"vehchain:Inf0Bey0nd!@192.168.1.136/unifiedDLSource1V3");
//
//        if(mapping==0)
//            System.out.println(mapper.getOutputFile());

//        ImageIcon loadImg = GuiUtil.createImageIcon("/icons/Loading.gif");
//
//        System.out.println(loadImg);

      new UnifiedDLExperiment().executeFusingExperiments();

//        new UnifiedDL().multiThreadTest();


//        System.out.println(Runtime.getRuntime().availableProcessors());

    }

    private void multiThreadTest()  {

        Thread thread = new Thread(){
            public void run(){

                for(int i=0;i<20;i++){
                    System.out.println("Thread Running "+i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        thread.start();

//        Thread.State state = thread.getState();

        while (!(thread.getState() == Thread.State.TERMINATED)){
//            System.out.println(thread.getState());
        }

        System.out.println("Finished threads ...");

    }


    private void test(){
        Dictionary<String, List<Double>> edgeVectorDictionary = new Hashtable<>();

        try {
            FileInputStream fi1 = new FileInputStream(new File("vertex-vector-Dict"));
            ObjectInputStream oi1 = new ObjectInputStream(fi1);
            // Read objects
            edgeVectorDictionary = (Dictionary<String, List<Double>>) oi1.readObject();
            Enumeration keys = edgeVectorDictionary.keys();
            List<String> removeKeys = new ArrayList<>();

            int counter=1;
            while (keys.hasMoreElements()){
                counter = counter + 1;
//              keys.nextElement();
                String key = keys.nextElement().toString();

                try {
                    Double value = Double.parseDouble(key);
                    System.out.println(value);
//                    if(value<100){
//                        System.out.println(value);
//                        removeKeys.add(key);
//                    }
                } catch (Exception Ex){}

//                System.out.println(keys.nextElement().toString());
//                System.out.println(counter);
            }

            if(removeKeys.size() > 0){
                for(String key:removeKeys){
                    edgeVectorDictionary.remove(key);
                }

                FileOutputStream edge = new FileOutputStream(new File("vertex-vector-Dict"));
                ObjectOutputStream edgeo = new ObjectOutputStream(edge);
                edgeo.writeObject(edgeVectorDictionary);
                edgeo.close();
            }

        } catch (Exception ex){}
    }

    private boolean extractWord2VecTrainingCorpus(String[] repositories){
        Word2VecEmbeddingModule word2VectorModule = new Word2VecEmbeddingModule();
        String corpusOutputFile = "unifieddlWord2VecTrainCorpusFinalLatest";
        String outputFileLoc = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\" + corpusOutputFile + ".txt";

        for(String repository:repositories){
            boolean isSuccess = word2VectorModule.extractWord2VecTrainingCorpus(repository, null, outputFileLoc);
            if(!isSuccess){
                return false;
            }
        }

        return true;
    }

    private void importRDFDatabase(String repositoryId, String turtleFileLoc) throws Exception{
        GHDI ghdiModel = new GHDI();
        ghdiModel.importRdfFromFile(turtleFileLoc, RDFFormat.TURTLE, repositoryId);
    }

    private void visualize(String[] repositoryIds){
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor(repositoryIds[0]);
        try {
            Graph graph = gsFactory.buildFullGraphStreamModel(repositoryIds);

            for(Node n:graph) {
                String data = n.getAttribute("rdf.triple").toString();

//                data.split()

                System.out.println(n.getAttribute("rdf.triple"));


            }


//            Graph graph = gsFactory.buildCompactGraphStreamModel();

//            graph.display();

        } catch(Exception Ex){
            System.out.println(Ex.getMessage());
        }
    }

    private static void interact(Searcher searcher) throws IOException, Searcher.UnknownWordException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("Enter word or sentence (EXIT to break): ");
                String word = br.readLine();
                if (word.equals("EXIT")) {
                    break;
                }
                List<Searcher.Match> matches = searcher.getMatches(word, 20);
                System.out.println(Strings.joinObjects("\n", matches));
            }
        }
    }

    private void geoToolTest(){}

    private void swingViewGuiUnifiedDL(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppView view = new AppView();


//                //Creating the Frame
//                JFrame frame = new JFrame("UnifiedDL Prototype");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setSize(1800, 1000);
//
//                //Creating the MenuBar and adding components
//                JMenuBar mb = new JMenuBar();
//                JMenu m1 = new JMenu("FILE");
//                JMenu m2 = new JMenu("Help");
//                mb.add(m1);
//                mb.add(m2);
//                JMenuItem m11 = new JMenuItem("Open");
//                JMenuItem m22 = new JMenuItem("Save as");
//                m1.add(m11);
//                m1.add(m22);
//
//                //Creating the panel at bottom and adding components
//                JPanel panel = new JPanel(); // the panel is not visible in output
//                JLabel label = new JLabel("Search Query");
//                JTextArea  tf = new JTextArea (10,50); // accepts upto 10 characters
//                JButton send = new JButton("Search");
//                JButton reset = new JButton("Reset");
//                Border border = BorderFactory.createLineBorder(Color.BLACK);
//                tf.setBorder(BorderFactory.createCompoundBorder(border,
//                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));
//
//                //Lay out the label and scroll pane from top to bottom.
//                JPanel checkPane = new JPanel();
//                checkPane.setLayout(new BoxLayout(checkPane, BoxLayout.PAGE_AXIS));
//                CheckboxGroup queryTypeGroup = new CheckboxGroup();
//                Checkbox keyword = new Checkbox("Keyword", queryTypeGroup,true);
//                Checkbox sparql = new Checkbox("SPARQL", queryTypeGroup,false);
//
//                // Create checkboxes with different names
//                checkPane.add(keyword);
//                checkPane.add(sparql);
//
//
//                // Text Area at the Center
//                panel.add(label); // Components Added using Flow Layout
//                panel.add(tf);
//                panel.add(checkPane);
//                panel.add(send);
//                panel.add(reset);
//
//                Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
//                Graph relationalGraphs = gsFactory.buildFullGraphStreamModel(new String[]{"unifiedDLSource1V3L"},false);
//                Graph jsonGraphs = gsFactory.buildFullGraphStreamModel(new String[]{"unifiedDLSource2V3L"},false);
////      Graph combineGraphs = gsFactory.buildFullGraphStreamModel(new String[]{"unifiedDLSource1V1L","unifiedDLSource2V1L"},true);
//                Graph combineGraphs = gsFactory.buildFullGraphStreamModel(new String[]{"Fused-unifiedDLSource1V3L-unifiedDLSource2V3L"},false);
//
//                relationalGraphs.display();
//                jsonGraphs.display();
//                send.addActionListener(new SearchActionListener(combineGraphs,tf,queryTypeGroup));
//                reset.addActionListener(new SearchResetActionListener(combineGraphs,tf));
//
//                SwingViewer viewer = new SwingViewer(combineGraphs, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//                viewer.enableAutoLayout();
//                View view = viewer.addDefaultView(false);
//
//                SwingViewer relationalViewer = new SwingViewer(relationalGraphs, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//                relationalViewer.enableAutoLayout();
//                View relationalView = relationalViewer.addDefaultView(false);
//
//                SwingViewer jsonViewer = new SwingViewer(jsonGraphs, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//                jsonViewer.enableAutoLayout();
//                View jsonView = jsonViewer.addDefaultView(false);
//
//                //Adding Components to the frame.
//                frame.getContentPane().add(BorderLayout.SOUTH, panel);
//                frame.getContentPane().add(BorderLayout.NORTH, mb);
////      frame.getContentPane().add(BorderLayout.WEST, (Component) relationalView);
//                frame.getContentPane().add(BorderLayout.CENTER, (Component) view);
////      frame.getContentPane().add(BorderLayout.EAST, (Component) jsonView);
//
//                frame.setVisible(true);




            }
        });
    }

//    int groupSteinerTreeTest()
//    {
//        int n=8, m=6, k=2, w=1;
////      readFirstLine(&n, &m, &k, &w);
//        GSTGraph myGSTGraph = new GSTGraph(n);
//
//        myGSTGraph.insertEdge(0, 1, 2);
//        myGSTGraph.insertEdge(0, 4, 1);
//        myGSTGraph.insertEdge(0, 6, 1);
//        myGSTGraph.insertEdge(4, 7, 3);
//        myGSTGraph.insertEdge(0, 5, 1);
//        myGSTGraph.insertEdge(5, 7, 1);
//
////      readEdges(m, myGraph);
//        GroupContainer myGroups = new GroupContainer(k);
//
//        myGroups.insertGroup(0,new Group(2));
//        myGroups.getGroup(0).insertVertex(4);
//        myGroups.getGroup(0).insertVertex(6);
//
//        myGroups.insertGroup(1,new Group(2));
//        myGroups.getGroup(1).insertVertex(1);
//        myGroups.getGroup(1).insertVertex(5);
//
////      myGroups->insertGroup(2,new Group(1));
////      myGroups->getGroup(2)->insertVertex(5);
//
////      readGroups(myGroups);
////      GroupSteinerTree test = new GroupSteinerTree();
////      Dijkstra[] results = new Dijkstra [n];
////      test.initializeResults(results,n);
////      if( w == 1){
////          test.algorithmOne(results, myGSTGraph, myGroups, n, k);
////      }
////      else if(w == 2){
////          test.algorithmTwo(results, myGSTGraph, myGroups, n, k);
////      }
////      cleanMemory(results,  myGraph, myGroups, n);
//        return 0;
//    }
}