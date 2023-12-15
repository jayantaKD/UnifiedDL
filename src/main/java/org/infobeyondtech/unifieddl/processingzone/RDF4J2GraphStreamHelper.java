package org.infobeyondtech.unifieddl.processingzone;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.graphstream.graph.Node;

public class RDF4J2GraphStreamHelper {


    public static String RDFSubjectId(Statement tripple){
        Resource subject = tripple.getSubject();
        String subId = subject.stringValue();
        return subId;
    }

    public static String RDFPredicateId(Statement tripple){
        Resource subject = tripple.getSubject();
        IRI predicate = tripple.getPredicate();
        String predicateId = subject.stringValue().concat(predicate.stringValue());
        return predicateId;
    }

    public static String RDFObjectId(Statement tripple){
        String objectId;
        Resource subject = tripple.getSubject();
        IRI predicate = tripple.getPredicate();
        Value obj = tripple.getObject();
        switch(predicate.stringValue()) {
            // RDF type
            case "http://www.w3.org/1999/02/22-rdf-syntax-ns#type":
                objectId = predicate.stringValue().concat(obj.stringValue());
//                isObjHide = false;
                break;
            default:
                if(predicate.stringValue().contains("#ref-")) {
                    objectId = obj.stringValue();
//                    isObjHide = false;
                }
                else {
                    objectId = subject.stringValue().concat(predicate.stringValue()).concat(obj.stringValue());
                }
        }
        return objectId;
    }
}
