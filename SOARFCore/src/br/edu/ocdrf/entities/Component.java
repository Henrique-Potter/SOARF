package br.edu.ocdrf.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Leila
 *
 */
public class Component {

    private final Map<String, String> tecnologies = new HashMap<>();
    private final List<Capacity> capacities = new LinkedList<>();
    //Sensor ou Actuator
    String type = "";
    //private String description;
    private static final int FIVE_SECONDS = 5 * 1000;
    private long probePeriod = FIVE_SECONDS;

    /**
     * Período padrão em que as leituras serão efetuadas. Esse valor pode ser
     * redefinido pela classe especializada de ResourceAgent.
     *
     * @return the probePeriod
     */
    public final long getProbePeriod() {
        return probePeriod;
    }

    public final void setProbePeriod(long probePeriod) {
        this.probePeriod = probePeriod;
    }

    public void clearTecnologies() {
        tecnologies.clear();
    }

    /**
     * Tecnologias com as quais o agente se comunica
     *
     * @return the tecnologies
     */
    public final Map<String, String> getTecnologies() {
        return tecnologies;
    }

    /**
     * Adiciona um tipo de tecnologia ao recurso.
     *
     * @param type
     * @param url
     */
    public void addTecnology(String type, String url) {
        tecnologies.put(type, url);
    }

    public List<Capacity> getCapacities() {
        return capacities;
    }

    public void addCapacity(Capacity capacity) {
        this.capacities.add(capacity);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
