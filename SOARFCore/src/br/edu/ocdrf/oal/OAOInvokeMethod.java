package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.domain.OInvokeMethod;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enuma
 */
public class OAOInvokeMethod extends OntologyAccessObject {

    public OAOInvokeMethod(Model iModel) {
        super(iModel);
    }

    public List<OInvokeMethod> getInvokeMethodByResource(Node resourceId) throws OALException {

        QueryExecution qe = null;
        List<OInvokeMethod> invokeMethodList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT ?invokeMethodId ?invokeTech ?invokeString ");
            queryStr.append("WHERE { ?x  owl:invokedBy ?invokeMethodId .");
            queryStr.append("  OPTIONAL { ?invokeMethodId owl:hasTechnology ?invokeTech } .");
            queryStr.append("  OPTIONAL { ?invokeMethodId owl:invokeString ?invokeString } .");
            queryStr.append("}");

            queryStr.setParam("x", resourceId);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            invokeMethodList = setInvokeMethodList(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return invokeMethodList;
    }

    public List<OInvokeMethod> getAllInvokeMethods() throws OALException {

        QueryExecution qe = null;
        List<OInvokeMethod> invokeMethodList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT ?invokeMethodId ?invokeTech ?invokeString ");
            queryStr.append("WHERE { ?invokeMethodId  a owl:InvokeMethod .");
            queryStr.append("  OPTIONAL { ?invokeMethodId owl:hasTechnology ?invokeTech } .");
            queryStr.append("  OPTIONAL { ?invokeMethodId owl:invokeString ?invokeString } .");
            queryStr.append("}");

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            invokeMethodList = setInvokeMethodList(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return invokeMethodList;
    }

    public void deleteAllFromNode(NodeIterator nodeIter) throws OALException {
        try {
            while (nodeIter.hasNext()) {
                Resource invokMethods = nodeIter.nextNode().asResource();
                ontologyModel.removeAll(invokMethods, null, (RDFNode) null);
            }
        } catch (Exception ex) {
            throw new OALException(ex);
        }
    }

    private List<OInvokeMethod> setInvokeMethodList(ResultSet rs) {

        List<OInvokeMethod> invMethodList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();

            OInvokeMethod invMethod = new OInvokeMethod();

            invMethod.setNodeId(sol.get("invokeMethodId").asNode().getLocalName());
            invMethod.setInvokeTechnology(sol.get("invokeTech").asNode().getLocalName());
            invMethod.setInvokeString(sol.get("invokeString").asLiteral().getString());

            invMethodList.add(invMethod);
        }
        return invMethodList;
    }
}
