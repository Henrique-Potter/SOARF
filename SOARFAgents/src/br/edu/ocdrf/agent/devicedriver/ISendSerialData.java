package br.edu.ocdrf.agent.devicedriver;

import br.edu.ocdrf.agent.entities.AgentOperationTarget;
import java.io.OutputStream;


public interface ISendSerialData {
    public void sendSerialData(OutputStream output,AgentOperationTarget agTarget);
}
