package org.infobeyondtech.unifieddl.processingzone;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.base.config.BaseSailConfig;
import org.eclipse.rdf4j.sail.nativerdf.config.NativeStoreConfig;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.infobeyondtech.unifieddl.core.SingleVerRepCalThread;
import org.infobeyondtech.unifieddl.core.VerRepCalThread;
import org.infobeyondtech.unifieddl.processingzone.groupsteinertree.*;
import org.infobeyondtech.unifieddl.utilities.Word2VecEmbeddingModule;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class GHDI {

    public volatile Semaphore sema = new Semaphore(1);

    public volatile ArrayList<VertexPair> vertexPairs = new ArrayList<VertexPair>();

    public GHDI() {
    }

    public void runDatabase(String repositoryId, String queryString) {
        File baseDir = new File("/unifiedDLGraphDatabase");
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        // create a configuration for the SAIL stack
        BaseSailConfig storeConfig = new NativeStoreConfig();
        // create a configuration for the repository implementation
        RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(storeConfig);
        //String repositoryId = "combat-apparatus-db";
        Repository repository = manager.getRepository(repositoryId);

        try (RepositoryConnection conn = repository.getConnection()) {
            if (queryString == null) {
                // let's check that our data is actually in the database
                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    while (result.hasNext()) {
                        System.out.println("db contains: " + result.next());
                    }
                }
            } else {
                TupleQuery query = conn.prepareTupleQuery(queryString);

                // A QueryResult is also an AutoCloseable resource, so make sure it gets closed when done.
                try (TupleQueryResult result = query.evaluate()) {
                    // we just iterate over all solutions in the result...
                    while (result.hasNext()) {
                        BindingSet solution = result.next();
                        // ... and print out the value of the variable binding for ?s and ?n
                        System.out.println("?s = " + solution.getValue("s"));
                        System.out.println("?n = " + solution.getValue("n"));
                    }
                }
            }
        } finally {
            // before our program exits, make sure the database is properly shut down.
            repository.shutDown();
        }
    }


    public void importRdfFromFile(String filename, RDFFormat format, String repositoryId) throws Exception {
            InputStream input = new FileInputStream(filename);
            Model model = Rio.parse(input, "", format);
            File baseDir = new File("/unifiedDLGraphDatabase");
            RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
            manager.init();
            BaseSailConfig storeConfig = new NativeStoreConfig();
            RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(storeConfig);
            RepositoryConfig repConfig = new RepositoryConfig(repositoryId, repositoryTypeSpec);
            manager.addRepositoryConfig(repConfig);
            Repository repository = manager.getRepository(repositoryId);
            System.out.println("unifiedDLSource1 in RDF N-Triples Format: "+filename);
            System.out.println("RDF4J Storage Repository Id: "+repositoryId);
            System.out.println("RDF triples stored in RDF4J graph database:");
            try (RepositoryConnection conn = repository.getConnection()) {
                conn.add(model);
                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    while (result.hasNext()) {
                        System.out.println("db contains: " + result.next());
                    }
                }
            } finally {
                repository.shutDown();
            }
    }

    public void test() {
        File baseDir = new File("/unifiedDLGraphDatabase");
        System.out.println(baseDir.getAbsolutePath());
    }

    public void namedGraphTest() {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/");

        // In named graph 1, we add info about Picasso
        builder.namedGraph("ex:namedGraph1")
                .subject("ex:Picasso")
                .add(RDF.TYPE, "ex:Artist")
                .add(FOAF.FIRST_NAME, "Pablo");

        // In named graph 2, we add info about Van Gogh.
        builder.namedGraph("ex:namedGraph2")
                .subject("ex:VanGogh")
                .add(RDF.TYPE, "ex:Artist")
                .add(FOAF.FIRST_NAME, "Vincent");

        // We're done building, create our Model
        Model model = builder.build();

        // Each named graph is stored as a separate context in our Model
        for (Resource context : model.contexts()) {
            System.out.println("Named graph " + context + " contains: ");
            Rio.write(model.filter(null, null, null, context), System.out, RDFFormat.NTRIPLES);
            System.out.println();
        }
    }

    public void visualize() throws IOException, GraphParseException {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.setAttribute("ui.stylesheet", "node { size-mode: fit; fill-color: white; stroke-mode: plain; padding: 3px, 2px; } edge { arrow-shape: arrow; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        Node a = graph.addNode("A");
        a.setAttribute("ui.label", "Azxzxz zxzxz");
        System.out.println(a.getId());

        Node b = graph.addNode("B");
        b.setAttribute("ui.label", "B");
        Node c = graph.addNode("C");
        c.setAttribute("ui.label", "C");
        Edge AB = graph.addEdge("AB", "A", "B", true);
        Edge AC = graph.addEdge("AC", "A", "C", true);

        AB.setAttribute("ui.label", "AB");
        AC.setAttribute("ui.label", "AC");

//        graph.addEdge("CA", "C", "A",true);
        graph.display();
    }


    public void graphCheck(String repository1Id, String repository2Id){
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
        Word2VecEmbeddingModule vectorCalculator = new Word2VecEmbeddingModule();
        Dictionary<String, List<Double>> vertexVectorDictionary = new Hashtable<>();
        Dictionary<String, List<Double>> edgeVectorDictionary = new Hashtable<>();

        try {
            FileInputStream fi = new FileInputStream(new File("vertex-vector-Dict"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            vertexVectorDictionary = (Dictionary<String, List<Double>>) oi.readObject();
            FileInputStream fi1 = new FileInputStream(new File("edge-vector-Dict"));
            ObjectInputStream oi1 = new ObjectInputStream(fi1);
            // Read objects
            edgeVectorDictionary = (Dictionary<String, List<Double>>) oi1.readObject();
            Graph inputGraph1 = gsFactory.buildFullGraphStreamModel(new String[]{repository1Id});
            Graph inputGraph2 = gsFactory.buildFullGraphStreamModel(new String[]{repository2Id});


            for (Node v1 : inputGraph1) {
                String nodeText = v1.getAttribute("rdf.triple").toString();
                List<Double> attributeVector = vertexVectorDictionary.get(nodeText);

                System.out.println(nodeText);
                System.out.println(attributeVector);
                System.out.println("---------------");

//                for (Node v2 : inputGraph2) {
//
//                }
            }
        } catch (Exception Ex){
        }
    }

    public void showGraph(String repository1Id, String repository2Id) {
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();

        try {
            Graph inputGraph1 = gsFactory.buildFullGraphStreamModel(new String[]{repository1Id});
            Graph inputGraph2 = gsFactory.buildFullGraphStreamModel(new String[]{repository2Id});
            Graph combinedGraph = gsFactory.buildFullGraphStreamModel(new String[]{repository1Id,repository2Id});
            ArrayList<VertexPair> vertexPairs = new ArrayList<VertexPair>();
            inputGraph1.display();
            inputGraph2.display();
            combinedGraph.display();
        } catch (Exception Ex) {
            System.out.println(Ex.getMessage());
        }
    }

    public void showGraph(String repositoryId){
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
        try {
            Graph inputGraph = gsFactory.buildFullGraphStreamModel(new String[]{repositoryId});
            inputGraph.display();
        } catch (Exception Ex) {
            System.out.println(Ex.getMessage());
        }
    }

    private List<List<Integer>> produceGstGroups(Graph combineGraphs,String[] keywords){
        Dictionary<String, List<Integer>> steinerTreeGroups= new Hashtable<>();
        Arrays.stream(keywords).forEach(x -> steinerTreeGroups.put(x.toLowerCase(),new ArrayList<Integer>()));
        // keyword matching on graph
        for (Node node:combineGraphs){
            String gstLabel = node.getAttribute("gst.label").toString();
            int gstCounter = Integer.parseInt(node.getAttribute("gst.counter").toString());
            for(String keyword:keywords){

                // exact keyword match
//                List<String> matches = Arrays.stream(gstLabel.toLowerCase().split(" "))
//                        .filter(x->x.contains(keyword.toLowerCase())).collect(Collectors.toList());

                // contains keyword match
                List<String> matches = Arrays.stream(gstLabel.toLowerCase().split(" "))
                        .filter(x->x.contains(keyword.toLowerCase())).collect(Collectors.toList());

                if(matches.size() > 0){
                    steinerTreeGroups.get(keyword.toLowerCase()).add(gstCounter);
                    System.out.println(gstLabel + "-->" + gstCounter +"---key-->"+keyword);
                }
            }
        }

        Arrays.stream(keywords).forEach(x-> System.out.println(steinerTreeGroups.get(x.toLowerCase())));

        List<List<Integer>> groups = new ArrayList<List<Integer>> ();

        Enumeration<String> matchedKeywords = steinerTreeGroups.keys();
        while(matchedKeywords.hasMoreElements()){
            List<Integer> matchedNodes = steinerTreeGroups.get(matchedKeywords.nextElement());
            if(!matchedNodes.isEmpty()){
                groups.add(matchedNodes);
            }
        }
        return groups;
    }

    private List<List<Integer>> produceGstEdges(Graph combineGraphs){
        List<List<Integer>> edges = new ArrayList<List<Integer>>();
        // building GST graph
        for (Edge edge:combineGraphs.edges().collect(Collectors.toList())){
            Integer source = Integer.parseInt(edge.getSourceNode().getAttribute("gst.counter").toString());
            Integer target = Integer.parseInt(edge.getTargetNode().getAttribute("gst.counter").toString());
            edges.add(new ArrayList<Integer>(Arrays.asList(source, target, (Integer)edge.getAttribute("gst.weight"))));
        }
        return edges;
    }

    public List<Integer> keywordSearchAlgorithm(Graph combineGraphs, String[] keywords){
//        String[] keywords = new String[]{"DDG","sunburn","damage","no"};
        List<Integer> gstNodes = new ArrayList<>();

        try {
//            GraphStreamFromRDF4J gsFactory = new GraphStreamFromRDF4J();
//            Graph combineGraphs = gsFactory.buildFullGraphStreamModel(new String[]{repository1Id,repository2Id},true);
//            combineGraphs.display();

            int n = combineGraphs.getNodeCount();
            List<List<Integer>> edges = produceGstEdges(combineGraphs);//new ArrayList<List<Integer>>();
            List<List<Integer>> groups = produceGstGroups(combineGraphs,keywords);
            List<Integer> root = new ArrayList<>();
//            groups.add(0,root);

            int cost = Integer.MAX_VALUE;

//            for(int i=0; i<n;i++){
//                root.clear();
//                root.add(i);
                GroupSteinerTree gstTree = new GroupSteinerTree(n,edges,groups);
                Tree result=gstTree.executeGroupSteinerTreeAlgorithm();

//                if(result.getCost()<cost){
                    cost = result.getCost();
                    gstNodes = result.gstTreeVertices();
//                }
                System.out.println("-----------------------------------------");
//                System.out.println(i);
                System.out.println(result.gstTreeVertices() + ": " + result.getCost());
                System.out.println("-----------------------------------------");
//            }

//            root.clear();
//            root.add(72);
//            GroupSteinerTree gstTree = new GroupSteinerTree(n,edges,groups);
//            Tree result=gstTree.executeGroupSteinerTreeAlgorithm();
//            List<Integer> gstNodes = result.gstTreeVertices();

//            for(Node node:combineGraphs){
//                Integer nodeInt = (Integer) node.getAttribute("gst.counter");
//                for(Integer gstnode:gstNodes){
//                    if(nodeInt.intValue() == gstnode.intValue()){
//                        node.setAttribute("ui.style", "fill-color: rgb(202, 255, 199);stroke-color: rgb(0,0,0);");
//                        break;
//                    }
//                }
//            }

        } catch (Exception ex){
            System.out.println(ex);
        }
        return gstNodes;
    }

//    public static boolean areAllTerminated(List<VerRepCalThread> threads)
//    {
//        for(Thread b : threads) if(!(b.getState()==Thread.State.TERMINATED)) return false;
//        return true;
//    }


    public boolean fusingAlgorithm(String repository1Id, String repository2Id, boolean isUseCommonVectorDict) {
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
        Word2VecEmbeddingModule vectorCalculator = new Word2VecEmbeddingModule();
        Dictionary<String, List<Double>> vertexVectorDictionary = new Hashtable<>();
        Dictionary<String, List<Double>> edgeVectorDictionary = new Hashtable<>();
        String vertexFileName;
        String edgeFileName;

        if(isUseCommonVectorDict){
            vertexFileName = "vertex-unifiedDLSource1V20-RdfRepounifiedDLSource2V20-RdfRepo";
            edgeFileName = "edge-unifiedDLSource1V20-RdfRepounifiedDLSource2V20-RdfRepo";
        } else {
            vertexFileName = "vertex-"+repository1Id+repository2Id;
            edgeFileName = "edge-"+repository1Id+repository2Id;
        }

        try {
            FileInputStream fi = new FileInputStream(new File(vertexFileName));
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            vertexVectorDictionary = (Dictionary<String, List<Double>>) oi.readObject();
            FileInputStream fi1 = new FileInputStream(new File(edgeFileName));
            ObjectInputStream oi1 = new ObjectInputStream(fi1);
            // Read objects
            edgeVectorDictionary = (Dictionary<String, List<Double>>) oi1.readObject();
            Graph inputGraph1 = gsFactory.buildFullGraphStreamModel(new String[]{repository1Id});
            Graph inputGraph2 = gsFactory.buildFullGraphStreamModel(new String[]{repository2Id});

            int pairIndex = 0;
            Double maxValue = Double.MIN_VALUE;
            Double minValue = Double.MAX_VALUE;



            List<Node> input1 = new ArrayList<Node>();
            List<Node> input2 = new ArrayList<Node>();


            Semaphore semaVerDict = new Semaphore(1);
            Semaphore semaEdgeDict = new Semaphore(1);
            Semaphore semaGraphNode = new Semaphore(1);
//            ExecutorService pool = Executors.newFixedThreadPool(1000);

            ExecutorService pool = Executors.newCachedThreadPool();

            final long vertexRepCalTimeStart = System.currentTimeMillis();
// ------------------- parallel with each graph vertex rep calculation ---------
            /*VerRepCalThread t1 = new VerRepCalThread(semaVerDict,semaEdgeDict,semaGraphNode,vertexVectorDictionary, edgeVectorDictionary, inputGraph1);
            VerRepCalThread t2 = new VerRepCalThread(semaVerDict,semaEdgeDict,semaGraphNode,vertexVectorDictionary, edgeVectorDictionary, inputGraph2);
            pool.submit(t1);
            pool.submit(t2);
            input1 = t1.call();
            input2 = t2.call();*/


// ------------------- parallel with each node vertex rep calculation ---------
            List<SingleVerRepCalThread> tList1 = new ArrayList<SingleVerRepCalThread>();
            List<SingleVerRepCalThread> tList2 = new ArrayList<SingleVerRepCalThread>();
            for (Node v : inputGraph1) {
                SingleVerRepCalThread t = new SingleVerRepCalThread(semaVerDict,semaEdgeDict,semaGraphNode,vertexVectorDictionary, edgeVectorDictionary, v);
//                Future<Node> tf = pool.submit(t);
                tList1.add(t);
            }
            for (Node v : inputGraph2) {
                SingleVerRepCalThread t = new SingleVerRepCalThread(semaVerDict,semaEdgeDict,semaGraphNode,vertexVectorDictionary, edgeVectorDictionary, v);
//                pool.submit(t);
                tList2.add(t);
            }

            List<Future<Node>> nodes1Futures = pool.invokeAll(tList1);
            List<Future<Node>> nodes2Futures = pool.invokeAll(tList2);
            for(Future<Node> nodes1Future:nodes1Futures){
                Node n = nodes1Future.get();
                if(n!=null){
                    input1.add(n);
                }
            }
            for(Future<Node> nodes2Future:nodes2Futures){
                Node n = nodes2Future.get();
                if(n!=null){
                    input2.add(n);
                }
            }

            pool.shutdown();

            final long vertexRepCalTimeEnd = System.currentTimeMillis();




//            List<VerPairCalThread> pthreads = new ArrayList<VerPairCalThread>();
//
//            for (Node v1 : input1) {
////                List<Double> v1VertexRep = (List<Double>) v1.getAttribute("vertex.embedding");
//                    for (Node v2 : input2) {
////                       List<Double> v2VertexRep = (List<Double>) v2.getAttribute("vertex.embedding");
//                            pairIndex = pairIndex + 1;
//                            VerPairCalThread t = new VerPairCalThread(sema,v1,v2);
//                            pthreads.add(t);
////                            pool.submit(t);
////                            VertexPair pair = new VertexPair(v1, v2, v1VertexRep, v2VertexRep,0);
////                            vertexPairs.add(pair);
//                    }
//            }
//            pthreads.stream().forEach(x-> pool.submit(x));

//            pool.shutdown();
//            for (Node v : inputGraph1){
//                List<Double> vertexRep = calculateVertexRepresentation(v, vertexVectorDictionary, edgeVectorDictionary);
//                if (vertexRep != null) {
//                    v.setAttribute("vertex.embedding", vertexRep);
//                    input1.add(v);
//                }
//            }
//
//            for (Node v : inputGraph2){
//                List<Double> vertexRep = calculateVertexRepresentation(v, vertexVectorDictionary, edgeVectorDictionary);
//                if (vertexRep != null) {
//                    v.setAttribute("vertex.embedding", vertexRep);
//                    input2.add(v);
//                }
//            }


//            for (Node v1 : input1) {
//                List<Double> v1VertexRep = (List<Double>) v1.getAttribute("vertex.embedding");
//                    for (Node v2 : input2) {
//                       List<Double> v2VertexRep = (List<Double>) v2.getAttribute("vertex.embedding");
//                            pairIndex = pairIndex + 1;
//
//                            try {
//                                VertexPair pair = new VertexPair(v1, v2, v1VertexRep, v2VertexRep, 0);
//                                vertexPairs.add(pair);
//                            } catch(Exception Ex){
//                                System.out.println(v1);
//                                System.out.println(v1VertexRep);
//                                System.out.println(v2);
//                                System.out.println(v2VertexRep);
//                                System.out.println(Ex);
//                            }
//                    }
//            }

            //            int index = 0;
            for (VertexPair pair: vertexPairs) {
//                VertexPair pair = pthread.call();
                Double distance = pair.getL2NormDistance();
                if (distance > maxValue)
                    maxValue = distance;
                if (distance < minValue)
                    minValue = distance;

               // vertexPairs.add(pair);
            }



            for (VertexPair pair : vertexPairs) {
                Double distance = pair.getL2NormDistance();
                Double distancePercentage = (distance / maxValue) * 100;
                pair.setL2NormDistancePercentage(distancePercentage);
            }


            System.out.println(inputGraph1.nodes().count()*inputGraph2.nodes().count());
            System.out.println(input1.size()+"--"+input2.size()+"-->"+input1.size()*input2.size());
//            System.out.println(input11.size()+"--"+input22.size()+"-->"+input11.size()*input22.size());
            System.out.println(vertexPairs.size());
            System.out.println("vertex representation delay:"+ (vertexRepCalTimeEnd-vertexRepCalTimeStart));




//            final long timeEnd = System.currentTimeMillis();

            final long matchPairCalTimeStart = System.currentTimeMillis();
            List<VertexPair> matchedPairs = applyMatchingEntityAlgorithmOptimized(vertexPairs);//applyMatchingEntityAlgorithm(vertexPairs);
            final long matchPairCalTimeEnd = System.currentTimeMillis();
            System.out.println("matching by loss optimization delay:"+ (matchPairCalTimeEnd-matchPairCalTimeStart));
           // Thread.sleep(500);

            System.out.println(repository1Id+":"+inputGraph1.nodes().count()+", "+inputGraph1.edges().count());
            System.out.println(repository2Id+":"+inputGraph2.nodes().count()+", "+inputGraph2.edges().count());

            FileWriter fw = new FileWriter("fusingPerformance.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(repository1Id+" "+inputGraph1.nodes().count()+" "+inputGraph1.edges().count()+" "+
                    repository2Id+" "+inputGraph2.nodes().count()+" "+inputGraph2.edges().count()+" " + (vertexRepCalTimeEnd-vertexRepCalTimeStart)
            +" "+(matchPairCalTimeEnd-matchPairCalTimeStart));
            bw.newLine();
            bw.close();

//            List<VertexPair> filterByCosineSimilarityThreshold = matchedPairs.stream().filter(e->e.getL2NormDistance() <= Double.valueOf("0.018") ).collect(Collectors.toList());
////            System.out.println(filterByCosineSimilarityThreshold.size());
//            List<VertexPairLite> matchingPairsForStoring = new ArrayList<VertexPairLite>();
//            for(VertexPair pair:filterByCosineSimilarityThreshold){
//                String matchedPairV1Id = pair.getVertex1().getId();
//                String matchedPairV2Id = pair.getVertex2().getId();
//                Node vertex1 = combinedGraph.getNode(matchedPairV1Id);
//                Node vertex2 = combinedGraph.getNode(matchedPairV2Id);
//                Edge bridge = combinedGraph.addEdge((matchedPairV1Id+matchedPairV2Id),matchedPairV1Id,matchedPairV2Id,false);
//                vertex1.setAttribute("ui.style", "fill-color: rgb(73, 161, 215);stroke-color: rgb(255,0,0);");
//                vertex2.setAttribute("ui.style", "fill-color: rgb(202, 255, 199);stroke-color: rgb(255,0,0);");
//                bridge.setAttribute("ui.style", "fill-color: rgb(255,0,0);");
//                matchingPairsForStoring.add(new VertexPairLite(matchedPairV1Id,matchedPairV2Id,
//                        (String)vertex1.getAttribute("rdf.triple"),
//                        (String)vertex2.getAttribute("rdf.triple"),
//                        (String)vertex1.getAttribute("rdf.repositoryId"),
//                        (String)  vertex2.getAttribute("rdf.repositoryId"),
//                        (Boolean) vertex1.getAttribute("rdf.isLiteral"),
//                        (Boolean) vertex2.getAttribute("rdf.isLiteral")));
//            }

            // save matched pairs into file system
//            String matchedPairsFileLocation = repository1Id+"-"+repository2Id+"-matchedPairs";
//            FileOutputStream matchedPairsFile = new FileOutputStream(new File(matchedPairsFileLocation));
//            ObjectOutputStream matchedPairsFileStream = new ObjectOutputStream(matchedPairsFile);
//            matchedPairsFileStream.writeObject(matchingPairsForStoring);
//            matchedPairsFileStream.close();

//            combinedGraph.nodes();
//            inputGraph1.display();
//            inputGraph2.display();
//            combinedGraph.display();
            return true;
        } catch (Exception Ex) {
            System.out.println(Ex);
            return false;
        }
    }

    public List<VertexPair> applyMatchingEntityAlgorithmOptimized(List<VertexPair> vertexPairs){
        List<VertexPair> finalMatchedPairs = new ArrayList<>();
        Double previousLoss = Double.MAX_VALUE;
        List<VertexPair> preMatchedPairs = new ArrayList<VertexPair>();
        List<VertexPair> notMatchedPairs = new ArrayList<VertexPair>();
        Double preMatchedLoss = Double.parseDouble("0.0");

        for(int distanceFilterValue = 9; distanceFilterValue < 10; distanceFilterValue++){

//            final long matchPairCalTimeStart = System.currentTimeMillis();
            Double lambda = Double.parseDouble("1.5");
            Double eta = Double.parseDouble("0.1");
            Double notMatchedLoss = Double.parseDouble("0.0");

            for (VertexPair pair : vertexPairs) {
                if (pair.getL2NormDistancePercentage() < distanceFilterValue) {
                    preMatchedPairs.add(pair);
                    preMatchedLoss = preMatchedLoss + pair.getL2NormDistance();
                } else {
                    if(lambda - pair.getL2NormDistance() > 0){
                        notMatchedLoss = notMatchedLoss + (eta * (lambda - pair.getL2NormDistance()));
                    }
                }
            }




            if(preMatchedPairs.isEmpty()){
                continue;
            }

            //vertexPairs.removeAll(preMatchedPairs);

//            final long matchPairCalTimeEnd = System.currentTimeMillis();
//            System.out.println("matching by loss optimization delay:"+ (matchPairCalTimeEnd-matchPairCalTimeStart));

//            List<VertexPair> notMatchedPairs = vertexPairs.stream().filter(c->!(preMatchedPairs.stream().map(p->p.id).collect(Collectors.toList())).contains(c.id)).collect(Collectors.toList());
//            for(VertexPair pair:notMatchedPairs){
//                if(lambda - pair.getL2NormDistance() > 0){
//                    loss = loss + (eta * (lambda - pair.getL2NormDistance()));
//                }
//            }

            if((preMatchedLoss + notMatchedLoss) < previousLoss){
                previousLoss = preMatchedLoss + notMatchedLoss;
                finalMatchedPairs.clear();
                finalMatchedPairs.addAll(preMatchedPairs);
            }

//            System.out.println("Total pairs: " + vertexPairs.size());
//            System.out.println("Pre-matched pairs: " + preMatchedPairs.size());
//            System.out.println("Not-matched pairs: " + vertexPairs.size());
//            System.out.println("Loss: " + (preMatchedLoss + notMatchedLoss));
//            System.out.println("---------------------------------------------");
        }

        return finalMatchedPairs;
    }


    public List<VertexPair> applyMatchingEntityAlgorithm(ArrayList<VertexPair> vertexPairs){
        List<VertexPair> finalMatchedPairs = new ArrayList<>();
        Double previousLoss=Double.MAX_VALUE;


        for(int distanceFilterValue=1; distanceFilterValue<10; distanceFilterValue++){
            List<VertexPair> preMatchedPairs = new ArrayList<VertexPair>();
            Double lambda = Double.parseDouble("1.5");
            Double eta = Double.parseDouble("0.1");
            Double loss = Double.parseDouble("0.0");

            for (VertexPair pair : vertexPairs) {
                if (pair.getL2NormDistancePercentage() < distanceFilterValue) {
                    preMatchedPairs.add(pair);
                    loss = loss + pair.getL2NormDistance();
                }
            }

            if(preMatchedPairs.isEmpty()){
                continue;
            }

            List<VertexPair> notMatchedPairs = vertexPairs.stream().filter(c->!(preMatchedPairs.stream().map(p->p.id).collect(Collectors.toList())).contains(c.id)).collect(Collectors.toList());

            for(VertexPair pair:notMatchedPairs){
                if(lambda - pair.getL2NormDistance() > 0){
                    loss = loss + (eta * (lambda - pair.getL2NormDistance()));
                }
            }

            if(loss < previousLoss){
                previousLoss = loss;
                finalMatchedPairs.clear();
                finalMatchedPairs.addAll(preMatchedPairs);
            }

//            System.out.println("Total pairs: " + vertexPairs.size());
//            System.out.println("Pre-matched pairs: " + preMatchedPairs.size());
//            System.out.println("Not-matched pairs: " + notMatchedPairs.size());
//            System.out.println("Loss: " + loss);
//            System.out.println("---------------------------------------------");
        }

        return finalMatchedPairs;
    }
    public void grabRelationPathUptoParent(Node parentNode, Node node, List<Edge> relationPath, List<List<Edge>> finalPaths) {
        List<Edge> parentEdgeList = node.edges().filter(c -> c.getTargetNode().getId().equals(node.getId())).collect(Collectors.toList());
        for (Edge parentEdge : parentEdgeList) {
            Node sourceNode = parentEdge.getSourceNode();
            if (sourceNode.getId().equals(parentNode.getId())) {
                relationPath.add(parentEdge);
                finalPaths.add(relationPath);
            } else {
                List<Edge> currentPathTemp = new ArrayList<Edge>();
                currentPathTemp.addAll(relationPath);
                currentPathTemp.add(parentEdge);
                grabRelationPathUptoParent(parentNode, parentEdge.getSourceNode(), currentPathTemp, finalPaths);
            }
        }
    }
    public List<Double> calculateVertexRepresentation(Node node, Dictionary<String, List<Double>> vertexVectorDictionary,
                                              Dictionary<String, List<Double>> edgeVectorDictionary) {

        List<Double> asu = calculateAttributeEmbedding(node, vertexVectorDictionary);
        if(asu !=null){
            List<Double> hsu = calculateStructuralEmbedding(node, edgeVectorDictionary);
            List<Double> multi1 = vectorHadamardProduct(sigmoidVector(asu),hsu);
            List<Double> multi2 = vectorHadamardProduct(hsu,hsu);
            List<Double> part2 = vectorSubtract(hsu, multi2);
            return vectorSum(multi1, part2);
        } else {
            return null;
        }
    }
    public List<Double> calculateAttributeEmbedding(Node targetNode,
                                                    Dictionary<String,
                                                            List<Double>> vertexVectorDictionary){
        String nodeText = targetNode.getAttribute("rdf.triple").toString();
        List<Double> attributeVector = vertexVectorDictionary.get(nodeText);
        return attributeVector;
    }
    public List<Double> calculateStructuralEmbedding(Node targetNode, Dictionary<String, List<Double>> edgeVectorDictionary) {
//        System.out.println("Descendants of --> " + targetNode.getAttribute("rdf.triple"));
        // considering all the descendants without any hop limits
        Iterator<Node> descendants = targetNode.getBreadthFirstIterator();
        List<Double> allDescendantPathVectorSum = zeroVector(50);

        //skip root
        descendants.next();

        while (descendants.hasNext()) {
            Node child = descendants.next();
            List<Edge> descendantRelationPath = new ArrayList<>();
            List<List<Edge>> descendantRelationPaths = new ArrayList<>();
//            System.out.println("----------------------- Relation paths of " + child.getAttribute("rdf.triple"));
            grabRelationPathUptoParent(targetNode, child, descendantRelationPath, descendantRelationPaths);
            int pathNumber = 1;
            List<Double> allPathReluExpVectorSum = zeroVector(50);

            for (List<Edge> relationPaths : descendantRelationPaths) {
//                System.out.println("----------- Path " + pathNumber + " Starts -------------");
                for (Edge relationPath : relationPaths) {
                    // calculate hs_i_j
                    String edgeText = relationPath.getAttribute("rdf.triple").toString();
                    List<Double> pathVector = edgeVectorDictionary.get(edgeText);
                    List<Double> reluVector = reluTransform( pathVector);
                    List<Double> reluVectorExp = expVector(reluVector);
                    allPathReluExpVectorSum = vectorSum(allPathReluExpVectorSum, reluVectorExp);
//                    System.out.println(relationPath.getSourceNode().getAttribute("rdf.triple") + " ---" + relationPath.getAttribute("rdf.triple") + "---> " + relationPath.getTargetNode().getAttribute("rdf.triple"));
                }
//                System.out.println("----------- Path " + pathNumber + " Ends ---------------");
                pathNumber = pathNumber + 1;
            }
            Double allPathReluVectorSumNorm = l2NormVector(allPathReluExpVectorSum);

            List<Double> allPathVectorSum = zeroVector(50);
            for (List<Edge> relationPaths : descendantRelationPaths) {
                for (Edge relationPath : relationPaths) {
                    // calculate hs_i_j
                    String edgeText = relationPath.getAttribute("rdf.triple").toString();
                    List<Double> pathVector = edgeVectorDictionary.get(edgeText);
                    List<Double> reluVector = reluTransform( pathVector);
                    Double reluVectorNorm = l2NormVector(reluVector);
                    Double weightCoefficient = reluVectorNorm/allPathReluVectorSumNorm;
                    vectorWeightMultiply(pathVector, weightCoefficient);
                    vectorSum(allPathVectorSum,vectorWeightMultiply(pathVector, weightCoefficient));
                }
                pathNumber = pathNumber + 1;
            }

             vectorSum(allDescendantPathVectorSum, sigmoidVector(allPathVectorSum)) ;
        }

        return sigmoidVector(allDescendantPathVectorSum);
    }























    public List<Double> reluTransform(List<Double> vector){
        List<Double> reluVector = new ArrayList<>();
        for(Double value:vector){
            if(value<0){
                reluVector.add(value*-1);
            } else {
                reluVector.add(value);
            }
        }
        return reluVector;
    }

    public List<Double> expVector(List<Double> vector){
        List<Double> expVector = new ArrayList<>();
        for(Double value:vector){
            expVector.add(Math.exp(value));
        }
        return expVector;
    }

    public List<Double> vectorSum(List<Double> vector1, List<Double> vector2){
        List<Double> sumVector = new ArrayList<>();
        for(int i=0; i<vector1.size(); i++){
            sumVector.add(vector1.get(i) + vector2.get(i));
        }
        return sumVector;
    }

    public List<Double> vectorSubtract(List<Double> vector1, List<Double> vector2){
        List<Double> subtractVector = new ArrayList<>();
        for(int i=0; i<vector1.size(); i++){
            subtractVector.add(vector1.get(i) - vector2.get(i));
        }
        return subtractVector;
    }

    public List<Double> vectorHadamardProduct(List<Double> vector1, List<Double> vector2){
        List<Double> multiplyVector = new ArrayList<>();
        for(int i=0; i<vector1.size(); i++){
            multiplyVector.add(vector1.get(i) * vector2.get(i));
        }
        return multiplyVector;
    }

    public List<Double> zeroVector(int size){
        List<Double> zeroVector = new ArrayList<>();
        for(int i=0; i < size; i++){
            zeroVector.add(Double.parseDouble("0"));
        }
        return zeroVector;
    }

    public Double l2NormVector(List<Double> vector){
        Double norm = Double.parseDouble("0");
        for(int i=0; i < vector.size(); i++){
            Double value = vector.get(i);
            norm = norm + (value*value);
        }
        return norm;
    }

    public List<Double> sigmoidVector(List<Double> vector){
        List<Double> sigmoidVector = new ArrayList<>();
        for(Double value:vector){
            Double exp = Math.exp(value);
            sigmoidVector.add(exp/(1 + exp));
        }
        return sigmoidVector;
    }

    public List<Double> vectorWeightMultiply(List<Double> vector, Double weight){
        List<Double> resVector = new ArrayList<>();
        for(int i=0; i<vector.size(); i++){
            resVector.add(vector.get(i) * weight);
        }
        return resVector;
    }
}