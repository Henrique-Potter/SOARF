package br.edu.ocdrf.agent.devicedriver;

import br.edu.ocdrf.agent.entities.DeviceConfig;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

;

public abstract class OCDRFDeviceDriver implements IDeviceDriver {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    protected final DeviceConfig dConfig;
    protected final Map deviceData;
    protected final Map deviceOperations;

    protected final List<IDeviceRawDataReader> dataReadersList = new ArrayList<>();

    protected abstract void setByteReaders(List<IDeviceRawDataReader> dataReadersList);

    public OCDRFDeviceDriver(DeviceConfig dConfig, Map deviceData, Map deviceOperations) {

        this.dConfig = dConfig;
        this.deviceData = deviceData;
        this.deviceOperations = deviceOperations;
        setDeviceActions();
    }

    private void setDeviceActions() {
        setByteReaders(dataReadersList);
    }

    protected void addDeviceRawDataReader(IDeviceRawDataReader dataReader) {
        dataReadersList.add(dataReader);
    }

    protected int readDataToInteger(byte[] serialData, int index, int lenght) {

        int initPos = index;
        int finalPos = index + lenght;

        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();

        for (int i = initPos; i < finalPos; i++) {
            bOutStream.write(serialData[i]);
        }

        if (bOutStream.size() < 2) {
            bOutStream.write(0);
        }

        ByteBuffer bytes = ByteBuffer.wrap(bOutStream.toByteArray());
        int moteData;

        if (dConfig.getDeviceConfigByteArrayMap().isLittleEndian()) {
            moteData = bytes.order(ByteOrder.LITTLE_ENDIAN).getChar();
        } else {
            //Data is in BigEndian 16 unsigned bit integer
            moteData = bytes.order(ByteOrder.BIG_ENDIAN).getChar();
        }

        return moteData;

    }

    protected String readDataToString(byte[] serialData, int index, int lenght) {

        int initPos = index;
        int finalPos = index + lenght;

        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();

        for (int i = initPos; i < finalPos; i++) {
            bOutStream.write(serialData[i]);
        }
        String dataString = null;
        try {
            dataString = new String(bOutStream.toByteArray(), dConfig.getDeviceConfigByteArrayMap().getCharSet());
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            System.out.println("Please check the charset choosen in the agent configuration file.");
        }
        return dataString;

    }

    private static String bytesToHex(byte[] bytes, boolean detailedInfo) {
        if (detailedInfo) {
            return bytesToHexWithInfo(bytes);
        } else {
            return bytesToHexRaw(bytes);
        }
    }

    protected void defaultByteSysOutPrinter(byte[] bOutStream) {

        System.out.println("Byte Array as Hex String: " + bytesToHex(bOutStream, false));
        System.out.println("Byte Array as Hex String: " + bytesToHex(bOutStream, true));

    }

    public static String bytesToHexRaw(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        StringBuilder str = new StringBuilder();

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;

            hexChars[j * 2] = hexArray[v >>> 4];
            str.append("[")
                    .append(hexChars[j * 2]);

            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            str.append(hexChars[j * 2 + 1])
                    .append("]");
        }

        return str.toString();
    }

    public static String bytesToHexWithInfo(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        StringBuilder str = new StringBuilder();

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            str.append(" Index: ").append(j);
            hexChars[j * 2] = hexArray[v >>> 4];
            str.append(" Byte:")
                    .append("[")
                    .append(hexChars[j * 2]);

            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            str.append(hexChars[j * 2 + 1])
                    .append("]")
                    .append("\n");
        }

        return str.toString();
    }

}
