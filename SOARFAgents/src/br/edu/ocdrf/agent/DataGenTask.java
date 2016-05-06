package br.edu.ocdrf.agent;

import br.edu.ocdrf.message.CapabilityData;
import br.edu.ocdrf.util.MapListener;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

public class DataGenTask extends TimerTask {

    protected final Map<String, CapabilityData> resourceColectedData;

    private final Random randomGenerator = new Random();

    int dataToCreate = 100;
    int fakedataCreated = 0;

    public DataGenTask(MapListener<String, CapabilityData> resColectedData) {

        resourceColectedData = resColectedData;

    }

    @Override
    public void run() {

        if (fakedataCreated < dataToCreate) {
            
            CapabilityData capData = resourceColectedData.get("Iris_01LuminositySensor");

            int avgResult = randomGenerator.nextInt(800);

            System.out.println("New Fake Data: " + avgResult);
            capData.addAttributeValue("rawLuminosity", String.valueOf(avgResult), System.currentTimeMillis());
            fakedataCreated++;
            
        }
    }

}
