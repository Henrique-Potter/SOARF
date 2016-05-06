package br.edu.ocdrf.agent.devicedriver;

import br.edu.ocdrf.agent.entities.DeviceConfig;
import br.edu.ocdrf.agent.excpetions.InvalidByteArrayFormat;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.TooManyListenersException;
import org.apache.log4j.Logger;

public abstract class USBDeviceDriver extends OCDRFDeviceDriver implements SerialPortEventListener {

    private static final Logger log = Logger.getLogger(USBDeviceDriver.class.getName());

    private SerialPort serialPort;
    private InputStream input;
    private OutputStream output;

    @Override
    public void initialize() {
        setupSerialPort();
    }

    public USBDeviceDriver(DeviceConfig dConfig, Map dData, Map deviceOp) {
        super(dConfig, dData, deviceOp);
    }

    public void setupSerialPort() {

        CommPortIdentifier portId = findCommPort();

        if (portId == null) {
            log.error("Device is not connected on port: " + dConfig.getDeviceConfigConnection().getCommPort());
            return;
        }

        try {
            serialPort = setSerialPort(portId);
            // open the streams
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        } catch (PortInUseException | InterruptedException | UnsupportedCommOperationException | IOException | TooManyListenersException ex) {
            System.err.println(ex.toString());
        }
    }

    private SerialPort setSerialPort(CommPortIdentifier portId) throws PortInUseException, InterruptedException, UnsupportedCommOperationException {

        SerialPort sPort = (SerialPort) portId.open(this.getClass().getName(), dConfig.getDeviceConfigConnection().getTimeout());
        Thread.sleep(2000);

        sPort.setSerialPortParams(
                dConfig.getDeviceConfigConnection().getDataRate(),
                dConfig.getDeviceConfigConnection().getDatabits(),
                dConfig.getDeviceConfigConnection().getStopBits(),
                dConfig.getDeviceConfigConnection().getParity());
        return sPort;
        
    }

    private CommPortIdentifier findCommPort() {

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            if (currPortId.getName().equals(dConfig.getDeviceConfigConnection().getCommPort())) {
                portId = currPortId;
                break;
            }
        }
        return portId;

    }

    public synchronized void close() {

        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }

    }

    @Override
    public void serialEvent(SerialPortEvent spe) {

        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                Thread.sleep(1000);
                ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();

                while (input.available() > 0) {
                    bOutStream.write((byte) input.read());
                }

                byte[] dataByteArray = bOutStream.toByteArray();

                if (validateBytes(dataByteArray)) {
                    if (dataReadersList.isEmpty()) {
                        defaultByteSysOutPrinter(bOutStream.toByteArray());
                    } else {
                        for (IDeviceRawDataReader dataSerialReader : dataReadersList) {
                            dataSerialReader.readDeviceRawData(bOutStream.toByteArray(), deviceData, dConfig.getDeviceConfigByteArrayMap());
                        }
                    }
                }

            } catch (NumberFormatException e) {
                System.err.println("Corrupted Data :" + e.toString());
            } catch (Exception e) {
                System.err.println("Corrupted Data :" + e.toString());
            }
        }
    }

    private boolean validateBytes(byte[] dataBytes) throws InvalidByteArrayFormat {

        int validSerialPacket = dConfig.getDeviceConfigByteArrayMap().getValidSerialPacketSize();
        String startOfText = dConfig.getDeviceConfigByteArrayMap().getStartOfText();
        String endOfText = dConfig.getDeviceConfigByteArrayMap().getEndtOfText();

        if (validSerialPacket != 0 && dataBytes.length < validSerialPacket) {
            throw new InvalidByteArrayFormat("Invalid data byte array size");
        }

        if (startOfText != null && dataBytes[0] != Integer.parseInt(startOfText, 16)) {
            throw new InvalidByteArrayFormat("Invalid start of text");
        }

        if (endOfText != null && dataBytes[dataBytes.length - 1] != Integer.parseInt(endOfText, 16)) {
            throw new InvalidByteArrayFormat("Invalid end of text");
        }

        return true;
    }

}
