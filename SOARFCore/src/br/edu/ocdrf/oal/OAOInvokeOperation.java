package br.edu.ocdrf.oal;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.domain.OInvokeOpParameter;
import br.edu.ocdrf.oal.domain.OInvokeOperation;

import com.hp.hpl.jena.graph.Node;
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

public class OAOInvokeOperation extends OntologyAccessObject {

    public OAOInvokeOperation(Model iModel) {
        
        super(iModel);
    }

    public List<OInvokeOperation> getInvokeOperationsByResComp(Node resourceCompNodeId) throws OALException {

        QueryExecution qe = null;
        List<OInvokeOperation> invOpList = null;
        try {
            ParameterizedSparqlStringEx queryStr = super.createParameterizedSparqlString();

            queryStr.append("SELECT ?invokeOperationId ?opName ?invReturnType ?capability ");
            queryStr.append("WHERE { ");
            queryStr.append("  ?resourceCompNodeId owl:hasInvokeOperation ?invokeOperationId .");
            queryStr.append("  ?invokeOperationId rdf:type ?invokeOperationType .");
            queryStr.append("  filter(?invokeOperationType = owl:InvokeOperation) . ");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:operationName ?opName } .");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:invokeReturnType ?invReturnType } .");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:returnsCapability ?capability } .");
            queryStr.append("}");

            queryStr.setParam("resourceCompNodeId", resourceCompNodeId);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);
            ResultSet rs = qe.execSelect();

            invOpList = setOInvOpList(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return invOpList;
    }

    public OInvokeOperation getInvokeOperationCapability(String invOp) throws OALException {

        QueryExecution qe = null;
        OInvokeOperation invOpEntity = null;
        try {
            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?capability ?opName ?invReturnType ");
            queryStr.append("WHERE {");
            queryStr.append("  ?invokeOperationId owl:returnsCapability ?capability . ");
            queryStr.append("  ?invokeOperationId rdf:type ?invokeOperationType .");
            queryStr.append("  filter(?invokeOperationType = owl:InvokeOperation) . ");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:operationName ?opName } .");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:invokeReturnType ?invReturnType } .");
            queryStr.append("  OPTIONAL { ?invokeOperationId owl:returnsCapability ?capability } .");
            queryStr.append("}");

            queryStr.createResourceAndSetParameter("invokeOperationId", invOp, super.OWL_NSPREFIX);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            invOpEntity = setOInvOpInstance(rs);
        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return invOpEntity;
    }

    public List<OInvokeOpParameter> getInvokeOperationParameters(Node invOpNode) throws OALException {

        List<OInvokeOpParameter> invoAtt = null;
        QueryExecution qe = null;
        try {
            ParameterizedSparqlStringEx queryStr = createParameterizedSparqlString();

            queryStr.append("SELECT DISTINCT ?invOpParam ?parameterName ?pFormat ?pType");
            queryStr.append("WHERE {");
            queryStr.append("  ?invokeOperationId owl:hasInvokeParameter ?invOpParam . ");
            queryStr.append("  ?invOpParam rdf:type ?invokeParamType .");
            queryStr.append("  filter(?invokeParamType = owl:InvokeParameter) . ");
            queryStr.append("  OPTIONAL { ?invOpParam owl:hasName ?parameterName } .");
            queryStr.append("  OPTIONAL { ?invOpParam owl:hasParameterFormat ?pFormat } .");
            queryStr.append("  OPTIONAL { ?invOpParam owl:hasParameterType ?pType } .");
            queryStr.append("}");

            queryStr.setParam("invokeOperationId", invOpNode);

            Query qry = QueryFactory.create(queryStr.asQuery());
            qe = QueryExecutionFactory.create(qry, ontologyModel);

            ResultSet rs = qe.execSelect();

            invoAtt = setOInvOpParamsList(rs);

        } catch (Exception ex) {
            throw new OALException(ex);
        } finally {
            closeQueryExecution(qe);
        }

        return invoAtt;
    }

    private OInvokeOperation setOInvOpInstance(ResultSet rs) throws OALException {

        OInvokeOperation oinv = null;

        if (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            oinv = createInvokeOperationEntity(sol);
        }

        return oinv;
    }

    private List<OInvokeOperation> setOInvOpList(ResultSet rs) throws OALException {

        List<OInvokeOperation> oinvList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            oinvList.add(createInvokeOperationEntity(sol));
        }

        return oinvList;
    }

    private List<OInvokeOpParameter> setOInvOpParamsList(ResultSet rs) {

        List<OInvokeOpParameter> oinvOpParamList = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();

            OInvokeOpParameter oinvOpParam = new OInvokeOpParameter();

            if (checkIfNotNullAndNotBlank(sol.get("invOpParam"))) {
                oinvOpParam.nodeId = (sol.get("invOpParam").asNode().getLocalName());
            }
            if (checkIfNotNullAndNotBlank(sol.get("parameterName"))) {
                oinvOpParam.name = (sol.get("parameterName").asLiteral().getString());
            }
            if (checkIfNotNullAndNotBlank(sol.get("pFormat"))) {
                oinvOpParam.format = (sol.get("pFormat").asLiteral().getString());
            }
            if (checkIfNotNullAndNotBlank(sol.get("pType"))) {
                oinvOpParam.type = (sol.get("pType").asLiteral().getString());
            }
        }
        return oinvOpParamList;
    }

    protected OInvokeOperation createInvokeOperationEntity(QuerySolution sol) throws OALException {

        OInvokeOperation invOp = new OInvokeOperation();
        
        if (checkIfNotNullAndNotBlank(sol.get("opName"))) {
            invOp.setOperationName(sol.get("opName").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("invReturnType"))) {

            invOp.setInvokeReturnType(sol.get("invReturnType").asLiteral().getString());
        }
        if (checkIfNotNullAndNotBlank(sol.get("capability"))) {
            invOp.setReturnsCapacity(new OAOCapability(ontologyModel).getCapabilityData(sol.get("capability").asNode()));
        }
        if (checkIfNotNullAndNotBlank(sol.get("invokeOperationId"))) {
            invOp.setNodeId(sol.get("invokeOperationId").asNode().getLocalName());
            invOp.setParameters(getInvokeOperationParameters(sol.get("invokeOperationId").asNode()));

        }

        return invOp;

    }

    protected String getInvokeAtt(QuerySolution sol) {

        if (sol.get("invokeAttribute") != null) {
            return sol.get("invokeAttribute").asNode().getLocalName();
        } else {
            return "";
        }
    }

    public void deleteAllFromNode(NodeIterator nodeIter) {
        while (nodeIter.hasNext()) {
            Resource invRes = nodeIter.nextNode().asResource();
            ontologyModel.removeAll(invRes, null, (RDFNode) null);
        }
    }

}
