package org.infobeyondtech.unifieddl.core;

import com.google.common.util.concurrent.Futures;
import org.infobeyondtech.unifieddl.processingzone.GHDI;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;
import org.infobeyondtech.unifieddl.utilities.Word2VecEmbeddingModule;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UnifiedDLExperiment {

    String[] relationalSources = new String[]{
            "unifiedDLSource1V1",
            "unifiedDLSource1V2",
            "unifiedDLSource1V3",
            "unifiedDLSource1V4",
            "unifiedDLSource1V5",
            "unifiedDLSource1V6",
            "unifiedDLSource1V7",
            "unifiedDLSource1V8",
            "unifiedDLSource1V9",
            "unifiedDLSource1V10",
            "unifiedDLSource1V11",
            "unifiedDLSource1V12",
            "unifiedDLSource1V13",
            "unifiedDLSource1V14",
            "unifiedDLSource1V15",
            "unifiedDLSource1V16",
            "unifiedDLSource1V17",
            "unifiedDLSource1V18",
            "unifiedDLSource1V19",
            "unifiedDLSource1V20"};

//    String[] jsonSources = new String[]{
//            "unifiedDLSource2V1",
//            "unifiedDLSource2V2",
//            "unifiedDLSource2V3",
//            "unifiedDLSource2V4",
//            "unifiedDLSource2V5",
//            "unifiedDLSource2V6",
//            "unifiedDLSource2V7",
//            "unifiedDLSource2V8",
//            "unifiedDLSource2V9",
//            "unifiedDLSource2V10",
//            "unifiedDLSource2V11",
//            "unifiedDLSource2V12",
//            "unifiedDLSource2V13",
//            "unifiedDLSource2V14",
//            "unifiedDLSource2V15",
//            "unifiedDLSource2V16",
//            "unifiedDLSource2V17",
//            "unifiedDLSource2V18",
//            "unifiedDLSource2V19",
//            "unifiedDLSource2V20"};


    public void executeFusingExperiments() {
        for(int i=20; i<=20; i++){
            for(int j=70; j<=70; j++){
                String repo1 = "unifiedDLSource1V" + i;
                String repo2 = "unifiedDLSource2V" + j;

//               boolean issuccess = extractWord2VecTrainingCorpus(new String[]{repo1+"-RdfRepo", repo2+"-RdfRepo"});
//               System.out.println("Training corpus ("+repo1+"-RdfRepo"+","+repo2+"-RdfRepo"+") Generation Successful: " + issuccess);
//                boolean isTrainSuccess = new Word2VecEmbeddingModule().trainWord2VecEmbeddingModel(new String[]{repo1+"-RdfRepo", repo2+"-RdfRepo"});
//                System.out.println("Training vector model ("+repo1+"-RdfRepo"+","+repo2+"-RdfRepo"+") Generation Successful: "+isTrainSuccess);

                  boolean isRetrieveVectorSuccess = new Word2VecEmbeddingModule().retrieveAndStoreVectors(new String[]{repo1+"-RdfRepo", repo2+"-RdfRepo"});

                  System.out.println("---------------------------------------------------------------------------------------------------------------------");
                  System.out.println("---------------------------------------------------------------------------------------------------------------------");
                  System.out.println("---------------------------------------------------------------------------------------------------------------------");
//                  System.out.println("Retrieve and store vector ("+repo1+"-RdfRepo"+","+repo2+"-RdfRepo"+") Generation Successful: "+isRetrieveVectorSuccess);


//                  boolean isFuseSuccess = new GHDI().fusingAlgorithm(repo1+"-RdfRepo",repo2+"-RdfRepo", true);
//                boolean isFuseStoreSuccess = new Rdf4jTripleStoreProcessor().fuseRdfGraphs(new String[]{repo1+"-RdfRepo",repo2+"-RdfRepo"},true);
//                System.out.println("Embedding model vectors ("+repo1+"-RdfRepo"+","+repo2+"-RdfRepo"+") Generation Successful: "+issuccess);

//                break;
            }
//            break;
        }

//        ExecutorService pool = Executors.newFixedThreadPool(2);
//        test t = new test();
//        test t2 = new test();
//
//        Future<String> value = pool.submit(t);
//        Future<String> value1 = pool.submit(t2);
//
//        pool.shutdown();
//
//        try {
//            value.get();
//            value1.get();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.print("done");


    }

    private boolean extractWord2VecTrainingCorpus(String[] repositories){
        Word2VecEmbeddingModule word2VectorModule = new Word2VecEmbeddingModule();
        String corpusOutputFile = "unifieddlWord2VecTrainCorpusFinalLatest";
        String outputFileLoc = "C:\\Users\\jayan\\workspace\\unified-dl\\nlp-in-practice\\word2vec\\expCorpus\\TrainCorpus-" + repositories[0] + "-" + repositories[1] + ".txt";

        for(String repository:repositories){
            boolean isSuccess = word2VectorModule.extractWord2VecTrainingCorpus(repository, null, outputFileLoc);
            if(!isSuccess){
                return false;
            }
        }
        return true;
    }

}
