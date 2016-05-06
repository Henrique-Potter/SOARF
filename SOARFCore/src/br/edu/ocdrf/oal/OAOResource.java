package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.message.OntologyContextQuery;
import br.edu.ocdrf.message.OntologyQuery;
import br.edu.ocdrf.oal.domain.OResourceEntity;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateRequest;
import java.util.ArrayList;
import java.util.List;

public class OAOResource extends OntologyAccessObject {

    public OAOResource(Model model) {
        super(model);
    }

    public List<OResourceEntity> getAllResources() throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;
        try {
            ParameterizedSparqlString queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?resource ?name ?model ?vendor ?uuid ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource a owl:Resource .");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?uuid } .");
            queryStr.append("}");

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resList = setOResourceList(rs, false);

            qe.close();

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;
    }

    public OResourceEntity getFullResourceDataByInvokeOp(String invokeOp) throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;
        try {
            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?resource ?name ?model ?vendor ?uuid ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource a  owl:Resource .");
            queryStr.append("  ?resource    owl:hasComponent ?componentId .");
            queryStr.append("  ?componentId owl:hasInvokeOperation ?invokeOp .");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?uuid } .");
            queryStr.append("}");

            if (invokeOp != null && !invokeOp.isEmpty()) {
                queryStr.createResourceAndSetParameter("invokeOp", invokeOp, super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resList = setOResourceList(rs, true);

            if (resList.size() > 1) {
                throw new OALException("More then one Resource is related to the same invoke operation.");
            }

            qe.close();

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList.get(0);
    }

    public OResourceEntity getSingleResource() throws OALException {

        QueryExecution qe = null;
        OResourceEntity resEnti = null;
        try {
            ParameterizedSparqlString queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?resource ?name ?model ?vendor ?uuid ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource a owl:Resource .");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?uuid } .");
            queryStr.append("}");

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            List<OResourceEntity> resList = setOResourceList(rs, true);

            resEnti = resList.get(0);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resEnti;

    }

    public OResourceEntity getResourceByUUID(String uuid) throws ServiceBaseException {

        OResourceEntity res = null;
        QueryExecution qe = null;

        try {
            ParameterizedSparqlString queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?resource ?name ?model ?vendor ?resUUID ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource a owl:Resource .");
            queryStr.append("  ?resource owl:hasUUID ?uuid .");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?resUUID } .");
            queryStr.append("}");

            queryStr.setLiteral("uuid", uuid);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            res = setOResource(rs, false);

            if (res != null) {
                res.setUUID(uuid);
            }
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return res;

    }

    public List<OResourceEntity> getResourcesInvokeStrings() throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;
        try {
            ParameterizedSparqlString queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?resource ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource a owl:Resource .");
            queryStr.append("}");

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resList = setOResourceInvokeStringsList(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;

    }

    public List<OResourceEntity> getResourceById(String resourceId) throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;
        try {
            ParameterizedSparqlStringEx queryXstrs = createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT ?name ?model ?vendor ?uuid ");
            queryXstrs.append("WHERE {");
            queryXstrs.append("  ?resourceId a owl:Resource .");
            queryXstrs.append("  OPTIONAL { ?resourceId owl:hasName ?name } .");
            queryXstrs.append("  OPTIONAL { ?resourceId owl:hasModel ?model } .");
            queryXstrs.append("  OPTIONAL { ?resourceId owl:hasVendor ?vendor } .");
            queryXstrs.append("  OPTIONAL { ?resourceId owl:hasUUID ?uuid } .");
            queryXstrs.append("}");

            if (resourceId != null && !resourceId.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("resourceId", resourceId, super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();
            rs.getResultVars();
            resList = setOResourceList(rs, false);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;
    }

    public Model setResourceUUID(String uuid) throws OALException {

        try {
            ParameterizedSparqlStringEx updateString = super.createParameterizedSparqlString();

            updateString.append("INSERT { ");
            updateString.append("   ?nodeID owl:hasUUID ?uuid .");
            updateString.append(" } ");
            updateString.append("WHERE { ");
            updateString.append("   ?nodeID a owl:Resource .");
            updateString.append("}");

            updateString.setLiteral("uuid", uuid, XSDDatatype.XSDstring);

            UpdateRequest request = updateString.asUpdate();

            UpdateAction.execute(request, ontologyModel);

            //ontoDLModel.prepare();
        } catch (Exception ex) {
            throw new OALException(ex);
        }

        return ontologyModel;
    }

    public Node getOnlyResourceNodeId() throws OALException {

        QueryExecution qe = null;
        QuerySolution sol = null;

        try {
            ParameterizedSparqlStringEx queryXstrs = createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT ?resource ");
            queryXstrs.append("WHERE {");
            queryXstrs.append("  ?resource a owl:Resource .");
            queryXstrs.append("}");

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            sol = rs.nextSolution();
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return sol.get("resource").asNode();
    }

    public void deleteResourceById(String resourceId) throws OALException {

        try {
            Resource res = ontologyModel.getResource(OWL_NSPREFIX + resourceId);

            NodeIterator resCompNodeIter = this.ontologyModel.listObjectsOfProperty(res, hasComponent);
            NodeIterator invNodeIter = this.ontologyModel.listObjectsOfProperty(res, invokedBy);

            new OAOResourceComponent(ontologyModel).deleteAllFromNode(resCompNodeIter);
            new OAOInvokeMethod(ontologyModel).deleteAllFromNode(invNodeIter);

            ontologyModel.removeAll(res, null, (RDFNode) null);
        } catch (Exception ex) {
            throw new OALException(ex);
        }
    }

    public List<OResourceEntity> queryResources(String resCompType, String resCompProperty, String resCompPropertyRange) throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;
        try {
            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT  ?resource ?name ?model ?vendor ?uuid ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource owl:hasComponent ?resComponent . ");
            queryStr.append("  ?resComponent a ?resCompType . ");
            queryStr.append("  ?resComponent ?resCompProperty ?resCompPropertyRange . ");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?uuid } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("}");

            if (resCompType != null && !resCompType.isEmpty()) {
                Node resCompTypeNode = NodeFactory.createAnon(AnonId.create(resCompType));
                queryStr.setParam("resCompType", resCompTypeNode);
            }
            if (resCompProperty != null && !resCompProperty.isEmpty()) {
                Node resCompPropertyNode = NodeFactory.createAnon(AnonId.create(resCompProperty));
                queryStr.setParam("resCompProperty", resCompPropertyNode);
            }
            if (resCompPropertyRange != null && !resCompPropertyRange.isEmpty()) {
                Node resCompPropertyRangeNode = NodeFactory.createAnon(AnonId.create(resCompPropertyRange));
                queryStr.setParam("resCompPropertyRange", resCompPropertyRangeNode);
            }

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resList = setOResourceList(rs, false);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;
    }

    public List<OResourceEntity> queryResources(OntologyQuery ontoQuery) throws OALException {

        OntologyContextQuery simpleContextQuery = ontoQuery.getSimpleContextQuery();

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;

        try {
            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT  ?resource ?name ?model ?vendor ?uuid ");
            queryStr.append("WHERE {");
            queryStr.append("  ?resource owl:hasComponent ?resComponent . ");
            queryStr.append("  ?resComponent owl:hasCapacity ?capacity . ");
            queryStr.append("  ?resComponent owl:hasLocation* ?location . ");
            queryStr.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasUUID ?uuid } .");
            queryStr.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryStr.append("}");

            if (simpleContextQuery.getCapability() != null && !simpleContextQuery.getCapability().isEmpty()) {
                queryStr.createResourceAndSetParameter("capacity", simpleContextQuery.getCapability(), super.OWL_NSPREFIX);
            }
            if (simpleContextQuery.getLocation() != null && !simpleContextQuery.getLocation().isEmpty()) {
                queryStr.createResourceAndSetParameter("location", simpleContextQuery.getLocation(), super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resList = setOResourceList(rs, false);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;
    }

    public List<OResourceEntity> queryResources(String resourceType, String objectProperty, String objectPropertyRangeInstances, String attName, String attType, String attUnit) throws OALException {

        QueryExecution qe = null;
        List<OResourceEntity> resList = null;

        try {
            ParameterizedSparqlStringEx queryXstrs = super.createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT  ?resource ?name ?model ?vendor  ");
            queryXstrs.append(" WHERE {");
            queryXstrs.append(" ?resource owl:hasComponent ?resourceComp .");
            queryXstrs.append(" ?resourceComp rdf:type ?resourceType .");
            queryXstrs.append(" ?resourceType rdfs:subClassOf owl:ResourceComponent.");
            queryXstrs.append(" ?resourceComp ?objectProperty ?objectPropertyRangeInstances .");
            queryXstrs.append(" ?objectProperty a owlDef:ObjectProperty .");
            queryXstrs.append(" ?objectProperty rdfs:domain owl:ResourceComponent .");
            queryXstrs.append("  OPTIONAL {?objectPropertyRangeInstances owl:hasAttribute ?attribute .}");
            queryXstrs.append("  OPTIONAL {?attribute owl:hasName ?attName .}");
            queryXstrs.append("  OPTIONAL {?attribute owl:hasAttributeType ?attType .}");
            queryXstrs.append("  OPTIONAL {?attribute owl:hasAttributeUnit ?attUnit .}");
            queryXstrs.append("  OPTIONAL { ?resource owl:hasName ?name } .");
            queryXstrs.append("  OPTIONAL { ?resource owl:hasModel ?model } .");
            queryXstrs.append("  OPTIONAL { ?resource owl:hasVendor ?vendor } .");
            queryXstrs.append("}");

            if (resourceType != null && !resourceType.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("resourceType", resourceType, super.OWL_NSPREFIX);
            }
            if (objectProperty != null && !objectProperty.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("objectProperty", objectProperty, super.OWL_NSPREFIX);
            }
            if (objectPropertyRangeInstances != null && !objectPropertyRangeInstances.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("objectPropertyRangeInstances", objectPropertyRangeInstances, super.OWL_NSPREFIX);
            }
            if (attName != null && !attName.isEmpty()) {
                queryXstrs.setLiteral("attName", attName);
            }
            if (attType != null && !attType.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("attType", attType, super.OWL_NSPREFIX);
            }
            if (attUnit != null && !attUnit.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("attUnit", attUnit, super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            resList = setOResourceList(rs, false);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resList;

    }

    private List<OResourceEntity> setOResourceList(ResultSet rs, boolean isSingleinOntology) throws OALException {

        List<OResourceEntity> resList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            resList.add(createResourceEntity(sol, isSingleinOntology));
        }

        return resList;

    }

    private OResourceEntity setOResource(ResultSet rs, boolean isSingleinOntology) throws OALException {

        OResourceEntity res = null;

        if (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            res = createResourceEntity(sol, isSingleinOntology);
        }

        return res;
    }

    protected OResourceEntity createResourceEntity(QuerySolution sol, boolean isSingleinOntology) throws OALException {

        OResourceEntity res = new OResourceEntity();
        if (checkIfNotNullAndNotBlank(sol.get("name"))) {

            res.setName(sol.get("name").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("model"))) {

            res.setModel(sol.get("model").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("vendor"))) {

            res.setVendor(sol.get("vendor").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("uuid"))) {

            res.setUUID(sol.get("uuid").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("resource"))) {
            res.setNodeId(sol.get("resource").asNode().getLocalName());
            if (isSingleinOntology) {
                res.setResourceComponents(new OAOResourceComponent(ontologyModel).getComponentsByResourceSingle(sol.get("resource").asNode()));
            } else {
                res.setResourceComponents(new OAOResourceComponent(ontologyModel).getComponentsByResource(sol.get("resource").asNode()));
            }
            res.setInvokeMethod(new OAOInvokeMethod(ontologyModel).getInvokeMethodByResource(sol.get("resource").asNode()));
        }

        return res;
    }

    private List<OResourceEntity> setOResourceInvokeStringsList(ResultSet rs) throws OALException {

        List<OResourceEntity> resList = new ArrayList<>();

        while (rs.hasNext()) {

            QuerySolution sol = rs.nextSolution();

            OResourceEntity res = new OResourceEntity();

            res.setNodeId(sol.get("resource").asNode().getLocalName());

            res.setInvokeMethod(new OAOInvokeMethod(ontologyModel).getInvokeMethodByResource(sol.get("resource").asNode()));

            resList.add(res);

        }
        return resList;

    }
}
