package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.domain.OResourceComponent;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Dataset;
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

public class OAOResourceComponent extends OntologyAccessObject {

    public OAOResourceComponent(Model model) {
        super(model);
    }

    public List<OResourceComponent> getComponentsByResource(Node id) throws OALException {

        QueryExecution qe = null;
        List<OResourceComponent> componentsList = null;

        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?dataBaseType ?componentId ?name ?resourceType ");
            queryStr.append("WHERE { ");
            queryStr.append(" ?nodeId owl:hasComponent ?componentId .");
            queryStr.append(" ?componentId rdf:type ?resourceType .");
            queryStr.append(" ?resourceType rdfs:subClassOf owl:ResourceComponent .");
            queryStr.append(" filter(?resourceType!=owl:ResourceComponent) . ");
            queryStr.append(" OPTIONAL { ?componentId owl:hasName ?name } .");
            queryStr.append(" OPTIONAL { ?componentId owl:hasDataBase ?dataBaseType } .");
            queryStr.append("}");

            queryStr.setParam("nodeId", id);

            Query qry = QueryFactory.create(queryStr.asQuery());

            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            componentsList = setComponentMembersList(rs);

            qe.close();
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public List<OResourceComponent> getComponentsByResourceSingle(Node id) throws OALException {

        QueryExecution qe = null;
        List<OResourceComponent> componentsList = null;

        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?componentId ?name ?resourceType ");
            queryStr.append("WHERE { ");
            queryStr.append(" ?nodeId owl:hasComponent ?componentId .");
            queryStr.append(" ?componentId rdf:type ?resourceType .");
            queryStr.append(" filter(?componentId != ?resourceType) . ");
            queryStr.append(" filter(?resourceType != owl:ResourceComponent ) . ");
            queryStr.append(" filter(?resourceType = owl:Sensor || ?resourceType = owl:Actuator ) . ");
            queryStr.append(" OPTIONAL { ?componentId owl:hasName ?name } .");
            queryStr.append("}");

            queryStr.setParam("nodeId", id);

            Query qry = QueryFactory.create(queryStr.asQuery());

            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            componentsList = setComponentMembersListSingle(rs);

            qe.close();
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public List<OResourceComponent> findResCompByCapability(String capability) throws OALException {

        QueryExecution qe = null;
        List<OResourceComponent> componentsList = null;

        try {

            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?componentId ?name ?resourceType");
            queryStr.append("WHERE { ");
            queryStr.append(" ?componentId owl:hasCapability ?capability . ");
            queryStr.append(" ?componentId rdf:type ?resourceType .");
            queryStr.append(" filter(?componentId != ?resourceType) . ");
            queryStr.append(" OPTIONAL { ?componentId owl:hasName ?name } .");
            queryStr.append("}");

            if (capability != null && !capability.isEmpty()) {
                queryStr.createResourceAndSetParameter("capability", capability, super.OWL_NSPREFIX);
            }
            
            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();
            
            componentsList = setComponentMembersListSingle(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }
    
    public List<OResourceComponent> findResCompByCapability(String capability,Dataset dataset) throws OALException {

        QueryExecution qe = null;
        List<OResourceComponent> componentsList = null;

        try {

            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?componentId ?name ?resourceType");
            queryStr.append("WHERE { ");
            queryStr.append(" ?componentId owl:hasCapability ?capability . ");
            queryStr.append(" ?componentId rdf:type ?resourceType .");
            queryStr.append(" filter(?componentId != ?resourceType) . ");
            queryStr.append(" OPTIONAL { ?componentId owl:hasName ?name } .");
            queryStr.append("}");

            if (capability != null && !capability.isEmpty()) {
                queryStr.createResourceAndSetParameter("capability", capability, super.OWL_NSPREFIX);
            }
            
            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, dataset);

            ResultSet rs = qe.execSelect();
            
            componentsList = setComponentMembersListSingle(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public OResourceComponent getResComponentObjProperty() throws OALException {

        QueryExecution qe = null;
        OResourceComponent resComponent = null;
        try {
            ParameterizedSparqlString queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT  ?objProperty ");
            queryStr.append("WHERE {");
            queryStr.append("  ?objProperty rdfs:domain owl:ResourceComponent . ");
            queryStr.append("}");

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            resComponent = setComponentObjPropertiesList(rs, "objProperty");
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resComponent;
    }

    public OResourceComponent getObjectPropertiesRangeInstances(String resourceType, String objectProperty) throws OALException {

        QueryExecution qe = null;
        OResourceComponent resComp = null;

        try {
            ParameterizedSparqlStringEx queryXstrs = super.createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT  ?objectPropertyRangeInstances ");
            queryXstrs.append(" WHERE {");

            queryXstrs.append(" ?resource rdf:type ?resourceType .");
            queryXstrs.append(" ?resourceType rdfs:subClassOf owl:ResourceComponent.");
            queryXstrs.append(" ?resource ?objectProperty ?objectPropertyRangeInstances .");
            queryXstrs.append(" ?objectProperty a owlDef:ObjectProperty .");
            queryXstrs.append(" ?objectProperty rdfs:domain owl:ResourceComponent .");
            queryXstrs.append("      MINUS { ?objectPropertyRangeInstances a owl:Resource }");
            queryXstrs.append("}");

            if (resourceType != null && !resourceType.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("resourceType", resourceType, super.OWL_NSPREFIX);
            }
            if (objectProperty != null && !objectProperty.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("objectProperty", objectProperty, super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            resComp = setObjPropRangeInstancesID(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return resComp;
    }

    private List<OResourceComponent> setComponentMembersList(ResultSet rs) throws OALException {

        List<OResourceComponent> componentsList = new ArrayList<>();

        while (rs.hasNext()) {

            QuerySolution sol = rs.nextSolution();
            OResourceComponent resComponent = new OResourceComponent();
            setResCompBaseEntity(resComponent, sol);

            if (sol.get("componentId") != null) {
                resComponent.setCapabilities(new OAOCapability(ontologyModel).getCapabilityByResourceComponent(sol.get("componentId").asNode()));
                resComponent.setInvokeOperations(new OAOInvokeOperation(ontologyModel).getInvokeOperationsByResComp(sol.get("componentId").asNode()));
            }

            componentsList.add(resComponent);
        }

        return componentsList;
    }

    private List<OResourceComponent> setComponentMembersListSingle(ResultSet rs) throws OALException {

        List<OResourceComponent> componentsList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            OResourceComponent resComponent = new OResourceComponent();

            setResCompBaseEntity(resComponent, sol);

            if (sol.get("componentId") != null) {
                resComponent.setCapabilities(new OAOCapability(ontologyModel).getCapabilityByResourceComponentSingle(sol.get("componentId").asNode()));
                resComponent.setInvokeOperations(new OAOInvokeOperation(ontologyModel).getInvokeOperationsByResComp(sol.get("componentId").asNode()));
            }

            componentsList.add(resComponent);
        }

        return componentsList;
    }

    private void setResCompBaseEntity(OResourceComponent resComponent, QuerySolution sol) {
        
        if (checkIfNotNullAndNotBlank(sol.get("componentId"))) {

            resComponent.setNodeId(sol.get("componentId").asNode().getLocalName());
        }
        if (checkIfNotNullAndNotBlank(sol.get("name"))) {

            resComponent.setName(sol.get("name").asLiteral().getString());
        }

        if (checkIfNotNullAndNotBlank(sol.get("resourceType"))) {

            resComponent.setType(sol.get("resourceType").asNode().getLocalName());
        }
    }

    private OResourceComponent setComponentObjPropertiesList(ResultSet rs, String objProperty) {

        OResourceComponent resComponent = new OResourceComponent();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            resComponent.getObjPropertiesList().add(sol.get(objProperty).asNode().getLocalName());
        }

        return resComponent;
    }

    private OResourceComponent setObjPropRangeInstancesID(ResultSet rs) {

        OResourceComponent res = new OResourceComponent();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            res.getObjectPropRangeInstancesList().add(sol.get("objectPropertyRangeInstances").asNode().getLocalName());
        }

        return res;
    }

    public void deleteAllFromNode(NodeIterator nodeIter) {
        while (nodeIter.hasNext()) {

            Resource componentRes = nodeIter.nextNode().asResource();

            NodeIterator invNodeIter = this.ontologyModel.listObjectsOfProperty(componentRes, hasInvokeOperation);

            new OAOInvokeOperation(ontologyModel).deleteAllFromNode(invNodeIter);

            ontologyModel.removeAll(componentRes, null, (RDFNode) null);
        }
    }

}
