package org.infobeyondtech.unifieddl.core;

import org.graphstream.graph.Node;
import org.infobeyondtech.unifieddl.processingzone.GHDI;
import org.infobeyondtech.unifieddl.processingzone.VertexPair;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class VerPairCalThread implements Callable<VertexPair> {

    volatile Semaphore sem;
    Node node1;
    Node node2;

    public VerPairCalThread(Semaphore sem, Node node1, Node node2)
    {
        this.sem = sem;
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public VertexPair call()  {
        VertexPair resultingPair = null;
        List<Double> v1VertexRep;
        List<Double> v2VertexRep;
        try {
            sem.acquire();
            v1VertexRep = (List<Double>) node1.getAttribute("vertex.embedding");
            v2VertexRep = (List<Double>) node2.getAttribute("vertex.embedding");
            sem.release();


            int vectorSize = v1VertexRep.size();
            Double sum = Double.parseDouble("0");
            for(int i=0; i<vectorSize; i++){
                Double diff = v1VertexRep.get(i) - v2VertexRep.get(i);
                sum = sum + (diff * diff);
            }
            double vecSum = Math.sqrt(sum);

            sem.acquire();
            resultingPair = new VertexPair(node1, node2, v1VertexRep, v2VertexRep, vecSum);
            sem.release();

        } catch (InterruptedException e) {
            System.out.print("catch!!!");
            throw new RuntimeException(e);
        }

        return resultingPair;
    }
}