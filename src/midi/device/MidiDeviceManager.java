package midi.device;

import javax.sound.midi.*;
import de.tobiaserichsen.tevm.TeVirtualMIDI;
import midi.MidiMessageListener;

import java.util.*;

public class MidiDeviceManager {

    private static MidiDeviceManager INSTANCE;
    private Map<String, MidiDevice.Info> deviceInfoMap;
    private Map<String, MidiDevice> deviceMap;

    private String selectedPortName;
    private MidiDevice selectedInputDevice;
    private TeVirtualMIDI virtualPort;
    private String virtualPortName;

    private Receiver mainReceiver;

    private List<MidiDeviceListener> deviceListeners;
    private List<MidiMessageListener> messageListeners;


    private MidiDeviceManager() {
        this.deviceInfoMap = new HashMap<>();
        this.deviceMap = new HashMap<>();
        this.deviceListeners = new ArrayList<>();
        this.messageListeners = new ArrayList<>();

        // receiver that forward to virtual port

        this.mainReceiver = new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                // handle message
                if(message instanceof ShortMessage sm) {
                    int channel = sm.getChannel();
                    int key = sm.getData1();
                    int velocity = sm.getData2();

                    // update listeners
                    for(MidiMessageListener listener : messageListeners) {
                        switch(message.getStatus()) {
                            case ShortMessage.NOTE_ON -> listener.noteOn(channel, key, velocity);
                            case ShortMessage.NOTE_OFF -> listener.noteOff(channel, key, velocity);
                            default -> {}
                        }
                    }

                    // forward to virtual port
                    if(virtualPort != null) virtualPort.sendCommand(message.getMessage());
                }
            }

            @Override
            public void close() {
                if(virtualPort != null) virtualPort.shutdown();
            }
        };
    }

    public static MidiDeviceManager getInstance() {
        if(INSTANCE == null) INSTANCE = new MidiDeviceManager();
        return INSTANCE;
    }

    public void refreshDevices() {
        deviceInfoMap.clear();
        deviceMap.clear();

        for(MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                String name = info.getName();
                MidiDevice device = MidiSystem.getMidiDevice(info);

                //check if transmitter is available === input port
                if(device.getTransmitter() != null && !name.equals(this.virtualPortName)) {
                    deviceInfoMap.put(name, info);
                    deviceMap.put(name, device);
                }
            } catch (MidiUnavailableException ignored) {}
        }
    }

    public void selectPort(String selectedPortName) {
        // close old device
        if(this.selectedInputDevice != null) this.selectedInputDevice.close();

        // set device
        this.selectedPortName = selectedPortName;
        this.selectedInputDevice = this.deviceMap.get(selectedPortName);
        try {
            this.selectedInputDevice.getTransmitter().setReceiver(this.mainReceiver);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }

        //reset virtual port
        if(this.virtualPort != null) this.virtualPort.shutdown();
        this.virtualPortName = selectedPortName + " - Virtual Input Port";
        this.virtualPort = new TeVirtualMIDI(virtualPortName);
    }

    public Collection<String> getDeviceNames() {
        return this.deviceMap.keySet();
    }
}
