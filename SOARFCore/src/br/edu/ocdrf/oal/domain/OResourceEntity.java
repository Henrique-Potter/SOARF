package br.edu.ocdrf.oal.domain;

import br.edu.ocdrf.exceptions.ServiceBaseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OResourceEntity {

    private String vendor;

    private String model;

    private String name;

    private List<OResourceComponent> resourceComponents = new ArrayList<>();

    private List<OInvokeMethod> invokeMethods = new ArrayList<>();

    private String nodeId;

    private String uuid;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return nodeId;
    }

    public List<OInvokeMethod> getInvokeMethod() {
        return invokeMethods;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setInvokeMethod(List<OInvokeMethod> InvokeMethod) {
        this.invokeMethods = InvokeMethod;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OResourceComponent> getResourceComponents() {
        return resourceComponents;
    }

    public void setResourceComponents(List<OResourceComponent> resourceComponents) {
        this.resourceComponents = resourceComponents;
    }

    public void addInvokeMethod(OInvokeMethod oinvMethod) {
        getInvokeMethod().add(oinvMethod);
    }

    public void addComponentCapability(String capability) {
        OResourceComponent oresComp = new OResourceComponent();
        OCapability cap = new OCapability();
        cap.setCapacityType(capability);

        oresComp.getCapabilities().add(cap);
        resourceComponents.add(oresComp);

    }

    public String serializeOntology(String mainServicesConfigPath, String ontologyFileName) throws ServiceBaseException {

        StringBuilder ontologyString = new StringBuilder();

        if (ontologyFileName == null || ontologyFileName.trim().isEmpty()) {
            throw new ServiceBaseException("Ontology file name not set");
        }

        try {

            String SOURCE = mainServicesConfigPath + ontologyFileName;
            System.out.println("Resource.readOntology - " + SOURCE);

            FileInputStream in = new FileInputStream(new File(SOURCE));
            BufferedReader input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String aux = "";

            String onlyOnce = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
            boolean once = true;

            while ((aux = input.readLine()) != null) {
                if (once) {
                    ontologyString.append(onlyOnce);
                    once = false;
                } else {
                    ontologyString.append(aux);
                }
            }

        } catch (IOException e) {
            throw new ServiceBaseException(e);
        }
        return ontologyString.toString();
    }

    public String serializeOntology(InputStream ontologyFileStream) throws ServiceBaseException {

        StringBuilder ontologyString = new StringBuilder();

        try {

            //TODO Validar ontologia criando um infoModel temporario.
            try (BufferedReader in = new BufferedReader(new InputStreamReader(ontologyFileStream, "UTF-8"));) {
                while (in.ready()) {
                    ontologyString.append(in.readLine());
                }
            }

        } catch (IOException e) {
            throw new ServiceBaseException(e);
        }
        return ontologyString.toString();
    }

}
