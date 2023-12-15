package org.infobeyondtech.unifieddl.processingzone;

import org.graphstream.graph.Node;

import java.io.Serializable;
import java.util.List;

public class VertexPairLite implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    String vertex1Id;
    String vertex2Id;
    String vertex1Repo;
    String vertex2Repo;

    String vertex1RdfUri;
    String vertex2RdfUri;

    boolean isVertex1Literal;
    boolean isVertex2Literal;

    public VertexPairLite(String vertex1Id, String vertex2Id,String vertex1RdfUri,String vertex2RdfUri, String vertex1Repo, String vertex2Repo, boolean isVertex1Literal,boolean isVertex2Literal){
        this.vertex1Id = vertex1Id;
        this.vertex2Id = vertex2Id;
        this.vertex1Repo = vertex1Repo;
        this.vertex2Repo = vertex2Repo;
        this.vertex1RdfUri = vertex1RdfUri;
        this.vertex2RdfUri = vertex2RdfUri;

        this.isVertex1Literal = isVertex1Literal;
        this.isVertex2Literal = isVertex2Literal;
    }

    @Override
    public String toString() {
//      return "(" + vertex1.getId().toString() + "<---->" + vertex2.getId().toString() + "<---->" + this.l2NormDistance + ")";
//      return "(" + vertex1.getAttribute("ui.label").toString() + "," + vertex2.getAttribute("ui.label").toString() + ")";
        return "(" + vertex1Id + "<----->" + vertex2Id + ")";
    }
}
