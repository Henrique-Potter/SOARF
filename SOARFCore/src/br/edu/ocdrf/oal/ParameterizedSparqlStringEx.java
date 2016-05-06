package br.edu.ocdrf.oal;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 *
 * @author henrique
 */
public class ParameterizedSparqlStringEx extends ParameterizedSparqlString {

    public void createResourceAndSetParameter(String sparqlVar,String resourceId, String NS_Prefix) {
        Resource res = ResourceFactory.createResource(NS_Prefix + resourceId);
        this.setParam(sparqlVar, res);
    }
}
