package org.infobeyondtech.unifieddl.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RdfDirectMapper {
    String pythonExecutable = "C:\\Users\\jayan\\workspace\\pyrdb2rdf\\venv\\Scripts\\python.exe";
    String rdb2RdfmapperExecutable = "C:\\Users\\jayan\\workspace\\pyrdb2rdf\\rdb2RdfMapper.py";
    String json2RdfmapperExecutable = "C:\\Users\\jayan\\workspace\\pyrdb2rdf\\json2RdfMapper.py";
    String mapperExecutableDirectory = "C:\\Users\\jayan\\workspace\\pyrdb2rdf";
    String outputFile = "C:\\Users\\jayan\\workspace\\pyrdb2rdf\\rdfMappingOutput.n3";
    String rdfBase = "<http://infobeyondtech.com/unifieddl/>";

    public RdfDirectMapper(){}


    public boolean processRdfMapper(RdfMapper mapper, String dbConnString){
        String mapperExecutable = "";

        if(mapper == RdfMapper.Rdb2Rdf){
            mapperExecutable = rdb2RdfmapperExecutable;
        }
        else if(mapper == RdfMapper.Rdb2Rdf){
            mapperExecutable = json2RdfmapperExecutable;
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command( "cmd.exe", "/c", pythonExecutable, mapperExecutable,
                        "-c", dbConnString
//                "-o", outputFile,
//                "-r", rdfBase,
//                "-m", mapper.toString()
        );


        processBuilder.directory(new File(mapperExecutableDirectory));

//        System.out.println(processBuilder.command());

        try {
            Process process = processBuilder.start();
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String line;
//
//            List<Double> results = new ArrayList<Double>() ;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
////                results.add(Double.valueOf(line.trim()));
//            }
            boolean isFinished = process.waitFor(60, TimeUnit.SECONDS);

//            System.out.println("\nExited with code : " + exitCode);
            return isFinished;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getOutputFile() {
        return outputFile;
    }
}