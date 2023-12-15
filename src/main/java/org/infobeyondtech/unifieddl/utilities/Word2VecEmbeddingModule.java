package org.infobeyondtech.unifieddl.utilities;

import com.medallia.word2vec.util.Common;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.sail.base.config.BaseSailConfig;
import org.eclipse.rdf4j.sail.nativerdf.config.NativeStoreConfig;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public  class Word2VecEmbeddingModule {

    String pythonExecutable = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\venv\\Scripts\\python.exe";
    String embeddingModelExecutable = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\scripts\\word2vec.py";
    String embeddingModelTrainingExecutable = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\scripts\\word2vecTrain.py";
    String directoryWord2VecModel = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\scripts";

    public Word2VecEmbeddingModule(){
    }
    public Word2VecEmbeddingModule(String pythonExecutable,
                                   String embeddingExecutable,
                                   String directoryWord2VecModel){
        this.pythonExecutable = pythonExecutable;
        this.embeddingModelExecutable = embeddingExecutable;
        this.directoryWord2VecModel = directoryWord2VecModel;
    }

    public List<Double> calculateWord2VecEmbeddingVector(String word, String vectorFileName){
        ProcessBuilder processBuilder = new ProcessBuilder();
        String wordPlusVectorFile = word.replaceAll("\\(","").replaceAll("\\)","")+"##UNIFIEDDL-SEPARATOR##"+vectorFileName;

//        System.out.println(wordPlusVectorFile);

        processBuilder.command("cmd.exe", "/c", pythonExecutable, embeddingModelExecutable,"-w", wordPlusVectorFile);
        processBuilder.directory(new File(directoryWord2VecModel));

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            List<Double> results = new ArrayList<Double>() ;
            while ((line = reader.readLine()) != null) {
                results.add(Double.valueOf(line.trim()));
            }
            process.waitFor(10, TimeUnit.SECONDS);
//            System.out.println("\nExited with error code : " + exitCode);
//            double sum = results.stream().mapToDouble(Double::doubleValue).sum();
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Double>();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<Double>();
        }
    }
    public boolean retrieveAndStoreVectors(String[] repositoryids) {
        Rdf4jTripleStoreProcessor gsFactory = new Rdf4jTripleStoreProcessor();
        Word2VecEmbeddingModule vectorCalculator = new Word2VecEmbeddingModule();
        Dictionary<String, List<Double>> vertexVectorDictionary = new Hashtable<>();
        Dictionary<String, List<Double>> edgeVectorDictionary = new Hashtable<>();
        String vertexFileName = "vertex-";
        String edgeFileName = "edge-";
        String sourceVectorFileName = "TrainCorpus";
        for(String repo:repositoryids){
            vertexFileName = vertexFileName + repo;
            edgeFileName = edgeFileName + repo;
            sourceVectorFileName = sourceVectorFileName + '-' +repo;
        }

        try {
            for (String repo : repositoryids) {
                Graph repoGraph = gsFactory.buildFullGraphStreamModel(new String[]{repo});

                for (Node n : repoGraph) {
                    String nodeText = n.getAttribute("rdf.triple").toString();
                    System.out.println(nodeText);
                    if (vertexVectorDictionary.get(nodeText) == null) {
                        List<Double> vector = vectorCalculator.calculateWord2VecEmbeddingVector(nodeText, sourceVectorFileName + ".kv");
                        if (!vector.isEmpty()) {
                            System.out.println(vector);
                            vertexVectorDictionary.put(nodeText, vector);
                        }
                    }
                }

                for(Edge e:repoGraph.edges().collect(Collectors.toList())){
                    String edgeText = e.getAttribute("rdf.triple").toString();
//                    System.out.println(edgeText);
                    if (edgeVectorDictionary.get(edgeText) == null) {
                        List<Double> vector = vectorCalculator.calculateWord2VecEmbeddingVector(edgeText, sourceVectorFileName + ".kv");

                        if (!vector.isEmpty()) {
                            edgeVectorDictionary.put(edgeText, vector);
                            System.out.print(vector);
                        }
                    }
                    System.out.println("-------");
                }
            }



//            File vertexStoreFile = Common. getResourceAsFile(
//                    this.getClass(),
//                    "/word2vecVectors/");
//
//            String test= vertexStoreFile.getAbsolutePath();

                 //   +vertexFileName
            FileOutputStream vertex = new FileOutputStream(new File(vertexFileName));
            ObjectOutputStream vertexo = new ObjectOutputStream(vertex);
            vertexo.writeObject(vertexVectorDictionary);
            vertexo.close();

//            File edgeStoreFile = Common.getResourceAsFile(
//                    this.getClass(),
//                    "/word2vecVectors/"+edgeFileName);
            FileOutputStream edge = new FileOutputStream(new File(edgeFileName));
            ObjectOutputStream edgeo = new ObjectOutputStream(edge);
            edgeo.writeObject(edgeVectorDictionary);
            edgeo.close();
        } catch (Exception Ex) {
            System.out.println(Ex.getMessage());
            return false;
        }
        return true;
    }
    public boolean extractWord2VecTrainingCorpus(String repositoryId, String queryString, String outputFile) {
//        System.out.println("Extracting");
        File baseDir = new File("/unifiedDLGraphDatabase");
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        // create a configuration for the SAIL stack
        BaseSailConfig storeConfig = new NativeStoreConfig();
        // create a configuration for the repository implementation
        RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(storeConfig);
        //String repositoryId = "combat-apparatus-db";
        Repository repository = manager.getRepository(repositoryId);
        List<String> predicateFilterList = new ArrayList<String>();

        try {
            File predicateFilterFile = Common.getResourceAsFile(
                    this.getClass(),
                    "/word2vec/unifieddl_predicate_filter.txt");
            // Creating an object of BufferedReader class
            BufferedReader br = new BufferedReader(new FileReader(predicateFilterFile));
            // Declaring a string variable
            String st;
            while ((st = br.readLine()) != null) {
                predicateFilterList.add(st);
            }
        } catch (Exception Ex) { return false;}

        try {
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(outputFile, true));
            for (int i = 0; i <= 10; i++) {
                writer1.newLine();
                writer1.append("NAN-INTENTIONAL-SPACING NAN-INTENTIONAL-SPACING NAN-INTENTIONAL-SPACING");
            }
        }
        catch (Exception Ex){ return false;}

        try (RepositoryConnection conn = repository.getConnection()) {
            if (queryString == null) {
                // let's check that our data is actually in the database
                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    Value prevSubject = null;
                    while (result.hasNext()) {
                        Statement s = result.next();
                        Value subject = s.getSubject();
//                        System.out.println(subject);
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
                            if (prevSubject != null && !prevSubject.stringValue().equals(subject.stringValue())) {
                                for (int i = 0; i <= 10; i++) {
                                    writer.newLine();
                                    writer.append("NAN-INTENTIONAL-SPACING NAN-INTENTIONAL-SPACING NAN-INTENTIONAL-SPACING");
                                }
                            }

                            if (predicateFilterList.contains(s.getPredicate().stringValue())) {
                                writer.newLine();
                                writer.append(s.getSubject().stringValue() + " " +
                                        s.getPredicate().stringValue() + " " + s.getObject().stringValue());
                            } else {
                                writer.newLine();
                                writer.append(s.getSubject().stringValue() + " " +
                                        s.getPredicate().stringValue());
                            }

                            prevSubject = subject;
//                            writer.append(s.getPredicate().stringValue());
                            writer.close();
                        } catch (Exception Ex) {
                            System.out.println("error appending ...");
                            System.out.println(s.getSubject().stringValue() + " " +
                                    s.getPredicate().stringValue() + " " + s.getObject().stringValue());
                        }
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
//                        System.out.println("?s = " + solution.getValue("s"));
//                        System.out.println("?n = " + solution.getValue("n"));
                    }
                }
            }
        } finally {
            // before our program exits, make sure the database is properly shut down.
            repository.shutDown();
        }
        return true;
    }

    public boolean trainWord2VecEmbeddingModel(String[] repositoryids ){
        if(repositoryids.length<2)
            return false;
        String corpusFileName = "TrainCorpus-" + repositoryids[0] + "-" + repositoryids[1];
        System.out.println(corpusFileName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", pythonExecutable, embeddingModelTrainingExecutable,"-t", corpusFileName);
        processBuilder.directory(new File(directoryWord2VecModel));
        try {
            Process process = processBuilder.start();
            boolean exitCode = process.waitFor(10,TimeUnit.SECONDS);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null) {
//                results.add(Double.valueOf(line.trim()));
              System.out.println(line.trim());
            }
            System.out.println("testing");

            return exitCode;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}