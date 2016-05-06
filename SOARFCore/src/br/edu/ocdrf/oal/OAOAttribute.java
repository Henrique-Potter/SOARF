package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.domain.OAttribute;
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

public class OAOAttribute extends OntologyAccessObject {

    public OAOAttribute(Model iModel) {
        super(iModel);
    }

    public List<OAttribute> getAttributesByCapability(Node capacityId) throws OALException {

        QueryExecution qe = null;
        List<OAttribute> componentsList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT ?attributeId ?name ?unit ?type ");
            queryStr.append("WHERE { ?x owl:hasAttribute ?attributeId .");
            queryStr.append(" ?attributeId rdf:type ?attributeType .");
            queryStr.append(" filter(?attributeType = owl:Attribute) . ");
            queryStr.append(" OPTIONAL { ?attributeId owl:hasName ?name } .");
            queryStr.append(" OPTIONAL { ?attributeId owl:hasAttributeUnit ?unit } .");
            queryStr.append(" OPTIONAL { ?attributeId owl:hasAttributeType ?type } .");
            queryStr.append("}");

            queryStr.setParam("x", capacityId);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            componentsList = setAttributesList(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public void deleteAllFromNode(NodeIterator nodeIter) throws OALException {
        try {
            while (nodeIter.hasNext()) {
                Resource att = nodeIter.nextNode().asResource();
                ontologyModel.remove(att, null, (RDFNode) null);
            }
        } catch (Exception ex) {
            throw new OALException(ex);
        }
    }

    private List<OAttribute> setAttributesList(ResultSet rs) {

        List<OAttribute> attributes = new ArrayList<>();

        while (rs.hasNext()) {

            QuerySolution sol = rs.nextSolution();

            OAttribute attribute = new OAttribute();
            if (checkIfNotNullAndNotBlank(sol.get("attributeId"))) {
                attribute.setNodeId(sol.get("attributeId").asNode().getLocalName());
            }
            if (checkIfNotNullAndNotBlank(sol.get("name"))) {
                attribute.setName(sol.get("name").asLiteral().getString());
            }
            if (checkIfNotNullAndNotBlank(sol.get("type"))) {
                attribute.setType(sol.get("type").asNode().getLocalName());
            }
            if (checkIfNotNullAndNotBlank(sol.get("unit"))) {
                attribute.setUnit(sol.get("unit").asNode().getLocalName());
            }
            attributes.add(attribute);
        }

        return attributes;
    }

}
