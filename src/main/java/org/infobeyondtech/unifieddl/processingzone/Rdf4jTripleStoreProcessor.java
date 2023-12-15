package org.infobeyondtech.unifieddl.processingzone;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleIRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.sail.base.config.BaseSailConfig;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.config.NativeStoreConfig;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.infobeyondtech.unifieddl.utilities.RDF4JDatabaseHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;

public class Rdf4jTripleStoreProcessor {
    private String baseRDF4JDirectory="C:\\unifiedDLGraphDatabase\\";
    private String repositoryId = "";
    private String baseIRI = "http://infobeyondtech.com/unifieddl/";
//    private String matchLiteralIRI = "http://infobeyondtech.com/unifieddl/matchLiteral";
    private RDF4JDatabaseHelper rdf4JDatabaseHelper;

    public Rdf4jTripleStoreProcessor(String repositoryId, String baseIRI){
        this.repositoryId=repositoryId;
        this.baseIRI=baseIRI;
        this.rdf4JDatabaseHelper = new RDF4JDatabaseHelper(this.baseRDF4JDirectory);
    }

    public Rdf4jTripleStoreProcessor(String repositoryId){
        this.repositoryId=repositoryId;
        this.rdf4JDatabaseHelper = new RDF4JDatabaseHelper(this.baseRDF4JDirectory);
    }

    public Rdf4jTripleStoreProcessor(){
        this.rdf4JDatabaseHelper = new RDF4JDatabaseHelper(this.baseRDF4JDirectory);
    }

    public void importRdfFromList(List<Statement> triples, String repositoryId){
        boolean isRepoExists = new RDF4JDatabaseHelper().isRepositoryExists(repositoryId);

        if(isRepoExists) {
            new RDF4JDatabaseHelper().removeRepository(repositoryId);
        }

        File baseDir = new File("/unifiedDLGraphDatabase");
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        BaseSailConfig storeConfig = new NativeStoreConfig();
        RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(storeConfig);
        RepositoryConfig repConfig = new RepositoryConfig(repositoryId, repositoryTypeSpec);
        manager.addRepositoryConfig(repConfig);
        Repository repository = manager.getRepository(repositoryId);
        try (RepositoryConnection conn = repository.getConnection()) {
            conn.add(triples);
            try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                while (result.hasNext()) {
                    System.out.println("Imported db contains: " + result.next());
                }
            }
        } finally {
            repository.shutDown();
        }
    }


    public void showTriplies(){
        List<Statement> triples = new RDF4JDatabaseHelper().retrieveRDFRepoTriples("Fused-unifiedDLSource1V1L-unifiedDLSource2V1L");
//        triples.stream().forEach(x->System.out.println(x));

        String queryString = "PREFIX ex: <http://infobeyondtech.com/unifieddl/> ";
//        queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
        queryString += "SELECT ?p ?o ";
        queryString += "WHERE { ";
        queryString += "    <http://infobeyondtech.com/unifieddl/MunitionFireIncidents/id=1>  ?p ?o .";
//        queryString += "       foaf:firstName ?n .";
        queryString += "}";

        new RDF4JDatabaseHelper().executeSparqlQuery("Fused-unifiedDLSource1V1L-unifiedDLSource2V1L",queryString);

    }

    public boolean fuseRdfGraphs(String[] repositories, boolean includeBridgeTriples){
        List<Statement> fusedGraph = new ArrayList<Statement>();
        fusedGraph.stream().forEach(x-> System.out.println(x.getPredicate()));
        if(includeBridgeTriples)
        {
            try{
                String matchedPairsFileLocation = repositories[0] + "-" + repositories[1] + "-matchedPairs";
                FileInputStream fi = new FileInputStream(new File(matchedPairsFileLocation));
                ObjectInputStream oi = new ObjectInputStream(fi);
                // Read objects
                ArrayList<VertexPairLite> matchedPairs = (ArrayList<VertexPairLite>) oi.readObject();
                ValueFactory factory = SimpleValueFactory.getInstance();

                List<String> matchSources = matchedPairs.stream().map(x->x.vertex1Id).collect(Collectors.toList());
                List<String> matchDests = matchedPairs.stream().map(x->x.vertex2Id).collect(Collectors.toList());

                for (String repositoryId:repositories) {
                    List<Statement> triples = this.rdf4JDatabaseHelper.retrieveRDFRepoTriples(repositoryId);

                    for(Statement triple:triples){
                        String objectId = RDFObjectId(triple);
                       if(matchSources.contains(objectId) || matchDests.contains(objectId)){
                           Resource subject = triple.getSubject();
                           IRI predicate = triple.getPredicate();
                           Value object;

                           if(isLiteralObject(triple)){
                               object = factory.createIRI(objectId);
                           } else {
                               object = triple.getObject();
                           }

                           Statement nameStatement = factory.createStatement(subject, predicate, object);
                           fusedGraph.add(nameStatement);
                       } else {
                           fusedGraph.add(triple);
                       }
                    }
                    fusedGraph.addAll(triples);
                }

                for(VertexPairLite matchedPair:matchedPairs){
                    Resource subject;
                    Value object;
                    if(matchedPair.isVertex1Literal){
                        System.out.println(matchedPair.vertex1Id);
                        subject = factory.createIRI(matchedPair.vertex1Id);
                    } else {
                        subject = factory.createIRI(matchedPair.vertex1RdfUri);
                    }

                    IRI predicate = factory.createIRI(this.baseIRI + "unifiedDL#Bridge");

                    if(matchedPair.isVertex2Literal){
                        object = factory.createIRI(matchedPair.vertex2Id);
                    } else {
                        object = factory.createLiteral (matchedPair.vertex2RdfUri);
                    }

                    Statement nameStatement = factory.createStatement(subject, predicate, object);
                    fusedGraph.add(nameStatement);
                }
            } catch(Exception Ex){
                System.out.println(Ex.toString());
                return false;
            }
        } else {
            for (String repositoryId:repositories) {
                List<Statement> triples = this.rdf4JDatabaseHelper.retrieveRDFRepoTriples(repositoryId);
                fusedGraph.addAll(triples);
            }
        }
        String fusedRepo = "Fused-" + repositories[0] + "-" + repositories[1] ;
        importRdfFromList(fusedGraph, fusedRepo);
        return true;
    }
    public Graph buildFullGraphStreamModel(String[] repositories, boolean isFuse)
    {
        Graph graph = buildFullGraphStreamModel(repositories);

        if(isFuse)
        {
            try{
                String matchedPairsFileLocation = repositories[0] + "-" + repositories[1] + "-matchedPairs";
                FileInputStream fi = new FileInputStream(new File(matchedPairsFileLocation));
                ObjectInputStream oi = new ObjectInputStream(fi);
                // Read objects
                ArrayList<VertexPairLite> matchedPairs = (ArrayList<VertexPairLite>) oi.readObject();

                for(VertexPairLite matchedPair:matchedPairs){
                    // add predicate value
                    Edge predicateEdge;
                    String predicateId = matchedPair.vertex1Id + matchedPair.vertex2Id;
                    String predicateLabel = "bridge";
                    try {
                        predicateEdge = graph.addEdge(repositoryId+"-"+predicateId,matchedPair.vertex1Id,matchedPair.vertex2Id,false);
                        predicateEdge.setAttribute("ui.label", predicateLabel);
                        // predicateEdge.setAttribute("ui.label", predicate.getLocalName());
                        predicateEdge.setAttribute("rdf.triple", "None");
                        // predicateEdge.setAttribute("ui.style", fillColor[index]);
                        predicateEdge.setAttribute("rdf.repositoryId", repositoryId);
                        predicateEdge.setAttribute("gst.weight", 1);
//                        graph.getNode(matchedPair.vertex1Id).setAttribute("ui.style", "fill-color: rgb(73, 161, 215);stroke-color: rgb(255,0,0);");
//                        graph.getNode(matchedPair.vertex2Id).setAttribute("ui.style", "fill-color: rgb(202, 255, 199);stroke-color: rgb(255,0,0);");
                        predicateEdge.setAttribute("ui.style", "fill-color: rgb(255,0,0);");
                    }
                    catch(IdAlreadyInUseException alreadyInUseException) {
                        predicateEdge = graph.getEdge(repositoryId+"-"+predicateId);
                    }
                }
            } catch(Exception Ex){}
        }

        return graph;
    }
    public Graph buildFullGraphStreamModel(String[] repositories)
    {
        String graphRepo="";
        for(String repo:repositories){
            graphRepo = graphRepo+ (graphRepo.isEmpty()?"":"-")+repo;
        }

        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new MultiGraph(baseIRI);
        graph.setAttribute("ui.stylesheet", "node { size-mode: fit;  fill-color: white; stroke-mode: plain; padding: 12px, 8px; } edge { arrow-shape: arrow; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("graph.rdf4jrepo",graphRepo);
        String[] fillColor = new String[]{"fill-color: rgb(73, 161, 215);","fill-color: rgb(202, 255, 199);"};
        Integer gstNodeCounter = 0;
        for (String repositoryId:repositories){
            List<Statement> triples = this.rdf4JDatabaseHelper.retrieveRDFRepoTriples(repositoryId);
            repositoryId="";
            for (Statement triple:triples)
            {
                // graph stream model
                // add subject node in graph stream model
                gstNodeCounter = addSubjectNodeIntoGraphStream(graph,triple, gstNodeCounter);

                // add object node in graph stream model
                gstNodeCounter = addObjectNodeIntoGraphStream(graph,triple, gstNodeCounter);

                // add predicate value
                addPredicateIntoGraphStream(graph, triple);
            }
        }
        return graph;
    }

    public Graph buildFullGraphStreamModel(List<Statement> triples)
    {
        String graphRepo="";
//        for(String repo:repositories){
//            graphRepo = graphRepo+ (graphRepo.isEmpty()?"":"-")+repo;
//        }

        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new MultiGraph(baseIRI);
        graph.setAttribute("ui.stylesheet", "node { size-mode: fit;  fill-color: white; stroke-mode: plain; padding: 12px, 8px; } edge { arrow-shape: arrow; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("graph.rdf4jrepo",graphRepo);
        String[] fillColor = new String[]{"fill-color: rgb(73, 161, 215);","fill-color: rgb(202, 255, 199);"};
        Integer gstNodeCounter = 0;
//        for (String repositoryId:repositories){
//            List<Statement> triples = this.rdf4JDatabaseHelper.retrieveRDFRepoTriples(repositoryId);
//            repositoryId="";
            for (Statement triple:triples)
            {
                // graph stream model
                // add subject node in graph stream model
                gstNodeCounter = addSubjectNodeIntoGraphStream(graph,triple, gstNodeCounter);

                // add object node in graph stream model
                gstNodeCounter = addObjectNodeIntoGraphStream(graph,triple, gstNodeCounter);

                // add predicate value
                addPredicateIntoGraphStream(graph, triple);
            }
//        }
        return graph;
    }



    private Integer addSubjectNodeIntoGraphStream(Graph graph,Statement triple, Integer gstNodeCounter){
        Node subNode;
        String subId = RDFSubjectId(triple);//subject.stringValue();
        String subLabel = subId.replaceAll(this.baseIRI,"");
        String displayLabel = subLabel;
        try {
            subNode = graph.addNode(subId);
            subNode.setAttribute("ui.label", displayLabel);
            subNode.setAttribute("rdf.triple", subId);
            subNode.setAttribute("rdf.repositoryId", repositoryId);
            subNode.setAttribute("gst.counter",gstNodeCounter);
            subNode.setAttribute("gst.label", subLabel);
            subNode.setAttribute("rdf.isLiteral", false);
            gstNodeCounter = gstNodeCounter + 1;
        } catch(IdAlreadyInUseException alreadyInUseException) {}
        return gstNodeCounter;
    }
    private Integer addObjectNodeIntoGraphStream(Graph graph,Statement triple, Integer gstNodeCounter){
        Node objectNode;
        Value obj = triple.getObject();
        String objectId = RDFObjectId(triple);
        boolean isLiteral = isLiteralObject(triple);
        String objLabel = obj.stringValue().replaceAll(this.baseIRI,"");
        String objDisplayLabel = objLabel;
        try {
            objectNode = graph.addNode(objectId);
            objectNode.setAttribute("ui.label", objDisplayLabel);
            objectNode.setAttribute("rdf.triple", obj.stringValue());
            objectNode.setAttribute("rdf.repositoryId", repositoryId);
            objectNode.setAttribute("gst.counter",gstNodeCounter);
            objectNode.setAttribute("gst.label", objLabel);
            objectNode.setAttribute("rdf.isLiteral", isLiteral);
            gstNodeCounter = gstNodeCounter + 1;
        } catch(IdAlreadyInUseException alreadyInUseException) {
            objectNode = graph.getNode(objectId);
        }

        // process unifiedDL match resource vertices
        IRI predicate = triple.getPredicate();
        String subId = RDFSubjectId(triple);
        String subLabel = subId.replaceAll(this.baseIRI,"") + '/';
        String predicateLabel = predicate.stringValue().replaceAll(this.baseIRI,"")+'/';
        if(objDisplayLabel.contains(subLabel) && objDisplayLabel.contains(predicateLabel)){
            objDisplayLabel = objDisplayLabel.replaceAll(subLabel,"").replaceAll(predicateLabel,"");
            objectNode.setAttribute("ui.label", objDisplayLabel);
            objectNode.setAttribute("gst.label", objDisplayLabel);
        }

        return gstNodeCounter;
    }
    private Edge addPredicateIntoGraphStream(Graph graph,Statement triple){
        // add predicate value
        Edge predicateEdge;
        IRI predicate = triple.getPredicate();
        String predicateId = RDFPredicateId(triple); //subject.stringValue().concat(predicate.stringValue());
        String predicateLabel = predicate.stringValue().replaceAll(this.baseIRI,"");
        try {
            predicateEdge = graph.addEdge(predicateId,RDFSubjectId(triple),RDFObjectId(triple),true);
            predicateEdge.setAttribute("ui.label", predicateLabel.split("#")[1]);
            predicateEdge.setAttribute("rdf.triple", predicate.stringValue());
            predicateEdge.setAttribute("rdf.repositoryId", repositoryId);
            predicateEdge.setAttribute("gst.weight", 2);
            predicateEdge.setAttribute("rdf.isLiteral", false);
        }
        catch(IdAlreadyInUseException alreadyInUseException) {
            predicateEdge = graph.getEdge(predicateId);
        }
        return predicateEdge;
    }
    private boolean isUnifiedDLBridgeMatchTriple(Statement triple){
        IRI predicate = triple.getPredicate();
        String predicateLabel = predicate.stringValue().replaceAll(this.baseIRI,"");
        return predicateLabel.equals("unifiedDL-Bridge");
    }
    public  String RDFSubjectId(Statement tripple){
        Resource subject = tripple.getSubject();
        String subId = subject.stringValue();
        return subId;
    }
    public  String RDFPredicateId(Statement tripple){
        Resource subject = tripple.getSubject();
        IRI predicate = tripple.getPredicate();
        String predicateId = subject.stringValue().concat(predicate.stringValue());
        return predicateId;
    }
    public  String RDFObjectId(Statement tripple){
        String objectId;
        Resource subject = tripple.getSubject();
        IRI predicate = tripple.getPredicate();
        Value obj = tripple.getObject();

        if(obj.stringValue().startsWith(this.baseIRI)){
            objectId = obj.stringValue();
        }
        else {
            objectId = subject.stringValue().concat("/")
                    .concat(predicate.stringValue().replaceAll(this.baseIRI,""))
                    .concat("/")
                    .concat(obj.stringValue());
        }

        return objectId;
    }
    public  boolean isLiteralObject(Statement tripple){
        IRI predicate = tripple.getPredicate();
        Value obj = tripple.getObject();
        switch(predicate.stringValue()) {
            // RDF type
            case "http://www.w3.org/1999/02/22-rdf-syntax-ns#type":
                break;
            default:
                if(predicate.stringValue().contains("#ref-") ||
                        obj.stringValue().contains(this.baseIRI)) {
                }
                else {
                    return true;
                }
        }
        return false;
    }
}