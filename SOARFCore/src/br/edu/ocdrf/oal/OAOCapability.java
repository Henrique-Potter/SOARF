package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.domain.OCapability;
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
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import java.util.ArrayList;
import java.util.List;

public class OAOCapability extends OntologyAccessObject {

    public OAOCapability(Model model) {
        super(model);
    }

    public List<OCapability> getCapabilityByResourceComponent(Node resId) throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?capability  ?capabilityType ");
            queryStr.append("WHERE { ");
            queryStr.append(" ?nodeId owl:hasCapability ?capability . ");
            queryStr.append(" ?capability rdf:type ?capabilityType . ");
            queryStr.append(" ?capabilityType rdfs:subClassOf owl:Capacity . ");
            queryStr.append(" filter(?capabilityType != owl:Capability) . ");
            queryStr.append("}");

            queryStr.setParam("nodeId", resId);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapabilityList(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public OCapability getCapabilityData(Node capabilityID) throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?capabilityType ?accessPolicy ?name");
            queryStr.append("WHERE { ");
            queryStr.append(" ?nodeId a owl:Capability . ");
            queryStr.append(" ?nodeId rdf:type ?capabilityType . ");
            queryStr.append(" ?capabilityType rdfs:subClassOf owl:Capability . ");
            queryStr.append(" filter(?capabilityType != owl:Capability) . ");
            queryStr.append(" filter(?capabilityType = owl:InputCapability || ?capabilityType = owl:ExecutionCapability) . ");
            queryStr.append(" OPTIONAL { ?nodeId owl:hasAccessPolicy ?accessPolicy } .");
            queryStr.append(" OPTIONAL { ?nodeId owl:hasName ?name } .");
            queryStr.append("}");

            queryStr.setParam("nodeId", capabilityID);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapabilityList(rs);

            if (componentsList.size() > 1) {
                throw new OALException("More then 1 capability found as return for an InvokeOperation");
            }

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        if (componentsList.isEmpty()) {
            return null;
        } else {
            return componentsList.get(0);
        }
    }

    public List<OCapability> getCapabilityByResourceComponentSingle(Node resId) throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?capability ?name ?capabilityType ?accessPolicy");
            queryStr.append("WHERE { ");
            queryStr.append(" ?nodeId owl:hasCapability ?capability . ");
            queryStr.append(" ?capability rdf:type ?capabilityType . ");
            queryStr.append(" filter(?capability != ?capabilityType) . ");
            queryStr.append(" filter(?capabilityType != owl:Capability) . ");
            queryStr.append(" filter(?capabilityType = owl:InputCapability || ?capabilityType = owl:ExecutionCapability) . ");
            queryStr.append(" OPTIONAL { ?capability owl:hasAccessPolicy ?accessPolicy } .");
            queryStr.append(" OPTIONAL { ?capability owl:hasName ?name } .");
            queryStr.append("}");

            queryStr.setParam("nodeId", resId);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapabilityList(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public List<OCapability> getCapacitiesByComponentType(String resourceType) throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {
            ParameterizedSparqlString queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT ?resource ?capabilityInstances ");
            queryStr.append(" WHERE {");
            queryStr.append(" ?resource  a ?resourceCompType.");
            queryStr.append(" ?resource owl:hasCapability ?capabilityInstances.");
            queryStr.append("}");

            Resource resCompType = ResourceFactory.createResource(OWL_NSPREFIX + resourceType);
            queryStr.setParam("resourceCompType", resCompType);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapacitiesListOnlyInstancesID(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public List<OCapability> getAllAvailableCapabilitiesByLocation(String location) throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {

            ParameterizedSparqlStringEx queryXstrs = createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT ?capabilityInstances");
            queryXstrs.append(" WHERE {");
            queryXstrs.append(" ?resComp  owl:hasLocation ?location.");
            queryXstrs.append(" ?resComp owl:hasCapability ?capabilityInstances.");
            queryXstrs.append("}");

            if (location != null && !location.isEmpty()) {
                queryXstrs.createResourceAndSetParameter("location", location, super.OWL_NSPREFIX);
            }

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapacitiesListOnlyInstancesID(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    public List<OCapability> getAllAvailableCapabilities() throws OALException {

        QueryExecution qe = null;
        List<OCapability> componentsList = null;
        try {

            ParameterizedSparqlStringEx queryXstrs = createParameterizedSparqlString();

            queryXstrs.append("SELECT DISTINCT ?capabilityInstances");
            queryXstrs.append(" WHERE {");
            queryXstrs.append(" ?resComp owl:hasCapability ?capabilityInstances.");
            queryXstrs.append("}");

            Query qry = QueryFactory.create(queryXstrs.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            componentsList = setCapacitiesListOnlyInstancesID(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return componentsList;
    }

    private List<OCapability> setCapabilityList(ResultSet rs) throws OALException {

        List<OCapability> capabilityList = new ArrayList<>();

        while (rs.hasNext()) {

            QuerySolution sol = rs.nextSolution();

            OCapability capability = new OCapability();
            if (checkIfNotNullAndNotBlank(sol.get("capabilityType"))) {
                capability.setCapacityType(sol.get("capabilityType").asNode().getLocalName());
            }
            if (checkIfNotNullAndNotBlank(sol.get("name"))) {
                capability.name = (sol.get("name").asLiteral().getString());
            }
            if (checkIfNotNullAndNotBlank(sol.get("accessPolicy"))) {
                capability.accessPolicy = sol.get("accessPolicy").asLiteral().getInt();
            }
            if (checkIfNotNullAndNotBlank(sol.get("capability"))) {
                capability.setNodeId(sol.get("capability").asNode().getLocalName());
                capability.setAttributes(new OAOAttribute(ontologyModel).getAttributesByCapability(sol.get("capability").asNode()));
            }
            capabilityList.add(capability);

        }

        return capabilityList;
    }

    private List<OCapability> setCapacitiesListOnlyInstancesID(ResultSet rs) {

        List<OCapability> capacityList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            OCapability capacity = new OCapability();

            capacity.setNodeId(sol.get("capabilityInstances").asNode().getLocalName());

            capacityList.add(capacity);
        }

        return capacityList;
    }

    public void deleteAllFromNode(NodeIterator nodeIter) throws OALException {
        while (nodeIter.hasNext()) {

            Resource capacity = nodeIter.nextNode().asResource();
            NodeIterator attributes = ontologyModel.listObjectsOfProperty(capacity, hasAttribute);
            new OAOAttribute(ontologyModel).deleteAllFromNode(attributes);
            ontologyModel.removeAll(capacity, null, (RDFNode) null);

        }
    }
}
