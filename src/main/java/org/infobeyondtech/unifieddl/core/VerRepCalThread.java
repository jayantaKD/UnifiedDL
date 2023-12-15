package org.infobeyondtech.unifieddl.core;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.infobeyondtech.unifieddl.processingzone.GHDI;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class VerRepCalThread implements Callable<List<Node>> {

    Semaphore sem;
    String threadName;
    Dictionary<String, List<Double>> vertexVectorDictionary;
    Dictionary<String, List<Double>> edgeVectorDictionary;
    Graph inputGraph;


    public VerRepCalThread(Dictionary<String, List<Double>> vertexVectorDictionary,
                           Dictionary<String, List<Double>> edgeVectorDictionary,
                           Graph inputGraph)
    {
//        super("");
//        this.sem = sem;
        this.vertexVectorDictionary = vertexVectorDictionary;
        this.edgeVectorDictionary = edgeVectorDictionary;
        this.inputGraph = inputGraph;
    }


    @Override
    public List<Node> call()  {

        List<Node> result = new ArrayList<Node>();
        for (Node v : inputGraph){
            List<Double> vertexRep = calculateVertexRepresentation(v, vertexVectorDictionary, edgeVectorDictionary);

            if (vertexRep != null) {
                v.setAttribute("vertex.embedding", vertexRep);
                result.add(v);
            }
        }
        return result;
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