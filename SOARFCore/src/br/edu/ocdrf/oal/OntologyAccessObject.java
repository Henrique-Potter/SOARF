package br.edu.ocdrf.oal;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

public abstract class OntologyAccessObject {

    protected String OWL_NSPREFIX = "http://localhost/ocdrf/conf/Resource.owl#";
    protected String RDF_NSPREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    protected String RDFS_NSPREFIX = "http://www.w3.org/2000/01/rdf-schema#";
    protected String OWL_DEF_NSPREFIX = "http://www.w3.org/2002/07/owl#";

    protected final Model ontologyModel;
    //protected final Dataset ontoDataset;

    protected Property hasName;
    protected Property invokedBy;
    protected Property invokeString;
    protected Property hasInvokeOperation;
    protected Property operationName;
    protected Property invokeReturnType;
    protected Property hasComponent;
    protected Property hasCapacity;
    protected Property hasAttribute;
    protected Property hasValue;
    protected Property hasAttributeType;
    protected Property hasAttributeUnit;

    public OntologyAccessObject(Model model) {
        //ontologyDLModel = ontoDLModel;

        ontologyModel = model;
        uploadProperties(model);

    }

    protected ParameterizedSparqlStringEx createParameterizedSparqlString() {
        ParameterizedSparqlStringEx queryStr = new ParameterizedSparqlStringEx();
        queryStr.setNsPrefix("owl", OWL_NSPREFIX);
        queryStr.setNsPrefix("rdf", RDF_NSPREFIX);
        queryStr.setNsPrefix("rdfs", RDFS_NSPREFIX);
        queryStr.setNsPrefix("owlDef", OWL_DEF_NSPREFIX);
        return queryStr;
    }

    public String getOWL_NSPREFIX() {
        return OWL_NSPREFIX;
    }

    public void setOWL_NSPREFIX(String OWL_NSPREFIX) {
        this.OWL_NSPREFIX = OWL_NSPREFIX;
    }

    public String getRDF_NSPREFIX() {
        return RDF_NSPREFIX;
    }

    public void setRDF_NSPREFIX(String RDF_NSPREFIX) {
        this.RDF_NSPREFIX = RDF_NSPREFIX;
    }

    public String getRDFS_NSPREFIX() {
        return RDFS_NSPREFIX;
    }

    public void setRDFS_NSPREFIX(String RDFS_NSPREFIX) {
        this.RDFS_NSPREFIX = RDFS_NSPREFIX;
    }

    public String getOWL_DEF_NSPREFIX() {
        return OWL_DEF_NSPREFIX;
    }

    public void setOWL_DEF_NSPREFIX(String OWL_DEF_NSPREFIX) {
        this.OWL_DEF_NSPREFIX = OWL_DEF_NSPREFIX;
    }

    private void uploadProperties(Model iModel) {

        hasName = iModel.getProperty(OWL_NSPREFIX + "hasName");
        hasValue = iModel.getProperty(OWL_NSPREFIX + "hasValue");
        invokedBy = iModel.getProperty(OWL_NSPREFIX + "invokedBy");
        invokeString = iModel.getProperty(OWL_NSPREFIX + "invokeString");
        hasInvokeOperation = iModel.getProperty(OWL_NSPREFIX + "hasInvokeOperation");
        operationName = iModel.getProperty(OWL_NSPREFIX + "operationName");
        invokeReturnType = iModel.getProperty(OWL_NSPREFIX + "invokeReturnType");
        hasComponent = iModel.getProperty(OWL_NSPREFIX + "hasComponent");
        hasCapacity = iModel.getProperty(OWL_NSPREFIX + "hasCapacity");
        hasAttribute = iModel.getProperty(OWL_NSPREFIX + "hasAttribute");

        hasAttributeType = iModel.getProperty(OWL_NSPREFIX + "hasAttributeType");
        hasAttributeUnit = iModel.getProperty(OWL_NSPREFIX + "hasAttributeUnit");
    }

    protected void closeQueryExecution(QueryExecution qe) {
        if (qe != null) {
            qe.close();
        }
    }

    protected boolean checkIfNotNullAndNotBlank(RDFNode rdfNode) {
        if (rdfNode != null) {
            if (!rdfNode.asNode().isBlank()) {
                return true;
            }
        }
        return false;
    }

}
