package org.infobeyondtech.unifieddl.guiservice;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.infobeyondtech.unifieddl.processingzone.GHDI;
import org.infobeyondtech.unifieddl.processingzone.Rdf4jTripleStoreProcessor;
import org.infobeyondtech.unifieddl.utilities.RDF4JDatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActionListener implements ActionListener {
    Graph combineGraphs;
    JTextArea queryField;
    CheckboxGroup queryTypeGroup;

    public SearchActionListener(Graph combineGraphs, JTextArea keywordsField, CheckboxGroup queryTypeGroup){
        this.combineGraphs = combineGraphs;
        this.queryField = keywordsField;
        this.queryTypeGroup = queryTypeGroup;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String checkboxSelection = queryTypeGroup.getSelectedCheckbox().getLabel();

        switch(checkboxSelection){
            case "Keyword":
                processKeywordSearchOnRdfGraph();
                break;
            case "SPARQL":
                processSPARQL();
                break;
            default:
                System.out.println("No Selection!");
                break;
        }
    }

    private void processSPARQL(){
        System.out.println("SPARQL query execution .....");
        String repo = this.combineGraphs.getAttribute("graph.rdf4jrepo").toString();
        System.out.println(repo);
        String querySPARQL = queryField.getText().trim();
//      new Rdf4jTripleStoreProcessor().showTriplies();
        System.out.println(querySPARQL);
        new RDF4JDatabaseHelper().executeSparqlQuery(repo,querySPARQL);
    }

    private void processKeywordSearchOnRdfGraph(){
        String queryText = queryField.getText().trim();
        if(queryText.isEmpty())
            return;
        String[] keywords = queryText.split(" ");
        List<Integer> gstNodes = new GHDI().keywordSearchAlgorithm(combineGraphs,keywords);
        List<Node> answerNodes = new ArrayList<Node>();
        for(Node node:combineGraphs){
            Integer nodeInt = (Integer) node.getAttribute("gst.counter");
            boolean isAnswerTreeNode = false;
            for(Integer gstnode:gstNodes){
                if(nodeInt.intValue() == gstnode.intValue()){
                    isAnswerTreeNode = true;
                    break;
                }
            }

            if(isAnswerTreeNode) {
                node.setAttribute("ui.style", "fill-color: rgb(202, 255, 199);stroke-color: rgb(0,0,0);");
                answerNodes.add(node);
            }
            else {
                node.setAttribute("ui.style", "fill-color: rgb(255, 255, 255);stroke-color: rgb(0,0,0);");
                node.setAttribute("ui.hide");
            }
        }

        for (Edge edge:combineGraphs.edges().collect(Collectors.toList())){
            if(answerNodes.contains(edge.getSourceNode()) && answerNodes.contains(edge.getTargetNode())){

            } else {
                edge.setAttribute("ui.hide");
            }
        }
    }
}