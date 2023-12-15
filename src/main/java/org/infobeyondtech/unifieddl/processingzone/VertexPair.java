package org.infobeyondtech.unifieddl.processingzone;
import com.google.common.hash.Hashing;
import org.graphstream.graph.Node;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VertexPair implements Serializable {
    Node vertex1;
    Node vertex2;
    String id;
    List<Double> vertex1EmVector;
    List<Double> vertex2EmVector;
    Double l2NormDistance;

    Double l2NormDistancePercentage;

    Double cosineSimilarity;

    public Double getL2NormDistancePercentage() {
        return l2NormDistancePercentage;
    }

    public void setL2NormDistancePercentage(Double l2NormDistancePercentage) {
        this.l2NormDistancePercentage = l2NormDistancePercentage;
    }

    public Double getL2NormDistance() {
        return l2NormDistance;
    }

    public void setL2NormDistance(Double l2NormDistance) {
        this.l2NormDistance = l2NormDistance;
    }

    public VertexPair(Node vertex1, Node vertex2, List<Double> vertex1EmVector, List<Double> vertex2EmVector, double l2Norm){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex1EmVector = vertex1EmVector;
        this.vertex2EmVector = vertex2EmVector;
        this.l2NormDistance = l2Norm;
        //this.cosineSimilarity = cosineSimilarity(this.vertex1EmVector, this.vertex1EmVector);
//        this.id = Hashing.sha256()
//                .hashString(vertex1.getId().concat(vertex2.getId()), StandardCharsets.UTF_8)
//                .toString();
        this.id =vertex1.getId().concat(vertex2.getId());
//        Hashing.sha256()
//                .hashString(vertex1.getId().concat(vertex2.getId()), StandardCharsets.UTF_8)
//                .toString();
    }

    private double calculateL2Norm(){
        int vectorSize = vertex1EmVector.size();
        Double sum = Double.parseDouble("0");
        for(int i=0; i<vectorSize; i++){
            Double diff = vertex1EmVector.get(i) - vertex2EmVector.get(i);
            sum = sum + (diff * diff);
        }
        return Math.sqrt(sum);
    }

    public static double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public Node getVertex1() {
        return vertex1;
    }

    public Node getVertex2() {
        return vertex2;
    }

    public String getId() {
        return id;
    }

    public Double getCosineSimilarity() {
        return cosineSimilarity;
    }

    public void setCosineSimilarity(Double cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    @Override
    public String toString() {
//      return "(" + vertex1.getId().toString() + "<---->" + vertex2.getId().toString() + "<---->" + this.l2NormDistance + ")";
//      return "(" + vertex1.getAttribute("ui.label").toString() + "," + vertex2.getAttribute("ui.label").toString() + ")";
        return "(" + vertex1.getAttribute("rdf.triple").toString() + "<----->" + vertex2.getAttribute("rdf.triple").toString()+" = "+ this.l2NormDistance +", "+this.cosineSimilarity+ ")";
    }
}