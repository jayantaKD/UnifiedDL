package org.infobeyondtech.unifieddl.guiservice;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class SearchResetActionListener implements ActionListener {
    Graph combineGraphs;
    JTextArea keywordsField;

    public SearchResetActionListener(Graph combineGraphs, JTextArea keywordsField){
        this.combineGraphs = combineGraphs;
        this.keywordsField = keywordsField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.keywordsField.setText("");
        for(Node node:combineGraphs){
            node.removeAttribute( "ui.hide");
            node.setAttribute("ui.style", "fill-color: rgb(255, 255, 255);stroke-color: rgb(0,0,0);");
        }

        for (Edge edge:combineGraphs.edges().collect(Collectors.toList())){
            edge.removeAttribute( "ui.hide");
        }
    }
}
