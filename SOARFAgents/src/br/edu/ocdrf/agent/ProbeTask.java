package br.edu.ocdrf.agent;

import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.interfaces.IAgentDataNotification;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProbeTask extends TimerTask {

    private final IAgentDataNotification agent;

    public ProbeTask(IAgentDataNotification agent) {
        this.agent = agent;
    }

    @Override
    public void run() {
        if (agent.checkDataModification()) {
            try {
                agent.notifyContextService();
            } catch (ContextException ex) {
                Logger.getLogger(ProbeTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
