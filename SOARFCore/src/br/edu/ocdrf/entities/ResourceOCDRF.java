package br.edu.ocdrf.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResourceOCDRF {

    private String description;
    private String ontologyFileName;
    private String ontologyRulesFileName;
    private final Map<String, String> tecnologies = new HashMap<>();
    
    private String uri;//TODO: Matar URI
    private final List<Component> components = new LinkedList<>();

    public void setInvokeTecnology(String mapKey, String invokeString) {
        tecnologies.put(mapKey, invokeString);
    }

    public String getInvokeTecnology(String mapKey) {
        return tecnologies.get(mapKey);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return uri;
    }

    public void addAttribute(Attribute attribute) {
        Component component;
        if (components.isEmpty()) {
            component = new Component();
            components.add(component);
        } else {
            component = components.get(0);
        }

        Capacity capacity = new Capacity();
        component.addCapacity(capacity);
        capacity.addAttribute(attribute);
    }

    public void addInvokeOperation(Attribute attribute) {
        Component component;
        if (components.isEmpty()) {
            component = new Component();
            components.add(component);
        } else {
            component = components.get(0);
        }

        Capacity capacity = new Capacity();
        component.addCapacity(capacity);
        capacity.addAttribute(attribute);
    }

    /**
     * @return Todos os atributos de todos os componentes do Resource
     */
    public List<Attribute> getAllAttributes() {
        List<Attribute> l = new LinkedList<>();

        for (Component component : components) {
            for (Capacity capacity : component.getCapacities()) {
                for (Attribute attribute : capacity.getAttributes()) {
                    l.add(attribute);
                }
            }
        }

        return Collections.unmodifiableList(l);
    }

    public int numberOfAttributes() {
        int result = 0;

        for (Component component : components) {
            for (Capacity capacity : component.getCapacities()) {
                result += capacity.numberOfAttributes();
            }
        }

        return result;
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {

        if (this != obj) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return true;
    }

    public void setOntologyFileName(String ontologyFileName) {
        this.ontologyFileName = ontologyFileName;
    }

    public String getOntologyFileName() {
        return ontologyFileName;
    }

    public String serializeOntology(String mainServicesConfigPath) {
        StringBuilder ontologyString = new StringBuilder();
        try {

            String SOURCE = mainServicesConfigPath + getOntologyFileName();

            //Validar ontologia
            //baseOntoModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
            //baseOntoModel.read( SOURCE, "RDF/XML" );
            // create the reasoning model using the base
            //infOntoModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, baseOntoModel );
            //InputStream inputStream = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new FileReader(SOURCE));

            while (in.ready()) {
                ontologyString.append(in.readLine());
            }

            in.close();

        } catch (IOException e) {
            System.out.println("serializeOntology: Ontology file not found!");
            e.printStackTrace();
        }
        return ontologyString.toString();
    }

    public String getOntologyRulesFileName() {
        return ontologyRulesFileName;
    }

    public void setOntologyRulesFileName(String ontologyRulesFileName) {
        this.ontologyRulesFileName = ontologyRulesFileName;
    }
}
