package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

/*
 <DiscoveryQuery>
 <CapacityConstraint Component="Sensor" Capacity="ActiveDevice">
 <Attribute Name="temperatura" op=">" Value="30" Unit="C" />
 <Attribute Name="xxx" op="<" Value="ID de alguem" Unit="CoreElement" />
 <Attribute Name="temperatura">
 </CapacityConstraint>
 <CapacityConstraint Component="Actuator">
 <Attribute Name="mimetype" value="text/plain" />
 </CapacityConstraint>
 <UserConstraint Element="Resource" Property="locatedIn" op="1meterFrom" value="PortaDaFrente"/>
 </DiscoveryQuery>
 */

/* 
 * Totalmente alterada ou nova
 * 
 * Parei de ler Type - soh vou aceitar UserElement - ie, elementos cadastrados na ontologia do usuario
 * 		<UserConstraint Element="Resource" Property="locatedIn" op="1meterFrom" value="ID Resource da Onto" type="CoreElement" />
 <UserConstraint Element="Resource" Property="locatedIn" op="1meterFrom" value="PortaDaFrente" type="UserElement" />
 */
public class DiscoveryQuery {

    private List<CapacityConstraint> capacityConstraints = new LinkedList<>();
    private List<ClientConstraint> clientConstraints = new LinkedList<>();

    public List<CapacityConstraint> getCapacityConstraints() {
        return capacityConstraints;
    }

    public void addCapacityConstraint(CapacityConstraint capacityConstraint) {
        this.capacityConstraints.add(capacityConstraint);
    }

    public List<ClientConstraint> getClientConstraints() {
        return clientConstraints;
    }

    public void addClientConstraint(ClientConstraint clientConstraint) {
        this.clientConstraints.add(clientConstraint);
    }

    public void setCapacityConstraints(List<CapacityConstraint> capacityConstraints) {
        this.capacityConstraints = capacityConstraints;
    }

    public void setClientConstraints(List<ClientConstraint> clientConstraints) {
        this.clientConstraints = clientConstraints;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((capacityConstraints == null) ? 0 : capacityConstraints.hashCode());
        result = prime * result + ((clientConstraints == null) ? 0 : clientConstraints.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiscoveryQuery other = (DiscoveryQuery) obj;
        if (capacityConstraints == null) {
            if (other.capacityConstraints != null) {
                return false;
            }
        }
        else if (!capacityConstraints.equals(other.capacityConstraints)) {
            return false;
        }
        if (clientConstraints == null) {
            if (other.clientConstraints != null) {
                return false;
            }
        }
        else if (!clientConstraints.equals(other.clientConstraints)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[capacityConstraints: " + capacityConstraints + ", clientConstraints: " + clientConstraints + "]";
    }

}
