package br.edu.ocdrf.agent;

import br.edu.ocdrf.agent.devicedriver.IDeviceDriver;


public class DeviceCommWorker extends Thread {

    private final IDeviceDriver deviceDriver;
    
    public DeviceCommWorker(IDeviceDriver dDriver) {

        deviceDriver = dDriver;
    }
    
    @Override
    public void run() {
        try {
            
            deviceDriver.initialize();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
