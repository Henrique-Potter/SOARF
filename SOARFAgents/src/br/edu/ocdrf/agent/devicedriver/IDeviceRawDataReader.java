package br.edu.ocdrf.agent.devicedriver;

import br.edu.ocdrf.agent.entities.DeviceConfigByteArrayMap;
import java.util.Map;

public interface IDeviceRawDataReader {
    public abstract void readDeviceRawData(byte[] deviceRawData,Map deviceData,DeviceConfigByteArrayMap dDataArrayConf);
}
