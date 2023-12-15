package org.infobeyondtech.unifieddl.utilities;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RDF4JDatabaseHelper {

    private String baseRDF4JDirectory="C:\\unifiedDLGraphDatabase\\";

    public RDF4JDatabaseHelper(String baseRDF4JDirectory){
        this.baseRDF4JDirectory = baseRDF4JDirectory;
    }

    public RDF4JDatabaseHelper(){}

    public boolean isRepositoryExists(String repositoryId){
        File baseDir = new File(this.baseRDF4JDirectory);
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        Repository repository = manager.getRepository(repositoryId);
        return repository != null;
    }

    public boolean removeRepository(String repositoryId){
        File baseDir = new File(this.baseRDF4JDirectory);
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        boolean isRemoved = manager.removeRepository(repositoryId);
        return isRemoved;
    }

    public List<org.eclipse.rdf4j.model.Statement> retrieveRDFRepoTriples(String repositoryId) {
        File baseDir = new File(this.baseRDF4JDirectory);
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        // create a configuration for the SAIL stack
        BaseSailConfig storeConfig = new NativeStoreConfig();
        // create a configuration for the repository implementation
        Repository repository = manager.getRepository(repositoryId);

        List<org.eclipse.rdf4j.model.Statement> tripples = new ArrayList<org.eclipse.rdf4j.model.Statement>();
        try (RepositoryConnection conn = repository.getConnection()) {
            // let's check that our data is actually in the database
            try (RepositoryResult<org.eclipse.rdf4j.model.Statement> result = conn.getStatements(null, null, null)) {
                while (result.hasNext()) {
                    tripples.add(result.next());
                }
            }
        }
        finally {manager.shutDown();}
        return tripples;
    }

    public void executeSparqlQuery(String repositoryId, String queryString) {
        File baseDir = new File(this.baseRDF4JDirectory);
        RepositoryManager manager = RepositoryProvider.getRepositoryManager(baseDir);
        manager.init();
        // create a configuration for the SAIL stack
        BaseSailConfig storeConfig = new NativeStoreConfig();
        // create a configuration for the repository implementation
        Repository repository = manager.getRepository(repositoryId);

        List<org.eclipse.rdf4j.model.Statement> tripples = new ArrayList<org.eclipse.rdf4j.model.Statement>();
        try (RepositoryConnection conn = repository.getConnection()) {
            // let's check that our data is actually in the database
//            try (RepositoryResult<org.eclipse.rdf4j.model.Statement> result = conn.getStatements(null, null, null)) {
//                while (result.hasNext()) {
//                    tripples.add(result.next());
//                }
//            }

            TupleQuery query = conn.prepareTupleQuery(queryString);

              // A QueryResult is also an AutoCloseable resource, so make sure it gets closed when done.
              try (TupleQueryResult result = query.evaluate()) {
                    // we just iterate over all solutions in the result...

                  while(result.hasNext()){
                      BindingSet solution = result.next();
                      System.out.println("?p = " + solution.getValue("p"));
                      System.out.println("?o = " + solution.getValue("o"));
                  }

//                    for (BindingSet solution : result) {
//                          // ... and print out the value of the variable binding for ?s and ?n
//                          System.out.println("?s = " + solution.getValue("s"));
//                          System.out.println("?n = " + solution.getValue("n"));
//                      }
                  }

        } finally {
            manager.shutDown();
        }

    }
}