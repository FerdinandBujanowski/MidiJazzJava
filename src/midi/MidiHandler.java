package midi;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
public class MidiHandler {

    public MidiHandler() throws MidiUnavailableException, LineUnavailableException {
        //TODO delete
        MidiDevice device;
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        Instrument[] instruments = synthesizer.getAvailableInstruments();
        synthesizer.loadInstrument(instruments[3]);

        // Get information about available mixers
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        // Choose a mixer (replace with the desired mixer info)
        Mixer.Info chosenMixerInfo = mixerInfo[7]; // Replace with the desired mixer info
        for(Mixer.Info info : mixerInfo) {
            AudioSystem.getMixer(info).close();
        }

        // Get the chosen mixer
        Mixer mixer = AudioSystem.getMixer(chosenMixerInfo);

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for(int i = 0; i < infos.length; i++) {
            try {

                device = MidiSystem.getMidiDevice(infos[i]);
                //does the device have any transmitters?
                //if it does, add it to the device list
                System.out.println(infos[i]);

                //get all transmitters
                List<Transmitter> transmitters = device.getTransmitters();
                //and for each transmitter

                for (Transmitter transmitter : transmitters) {
                    //create a new receiver
                    transmitter.setReceiver(
                            //using my own MidiInputReceiver
                            new MidiInputReceiver(device.getDeviceInfo().toString(), synthesizer.getReceiver())
                    );
                }

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString(), synthesizer.getReceiver()));

                //open each device
                if(device.getDeviceInfo().toString().equals("MPK mini Plus")) {
                    device.open();
                    //if code gets this far without throwing an exception
                    //print a success message
                    System.out.println(device.getDeviceInfo()+" Was Opened");
                }
            } catch (MidiUnavailableException e) {}
        }

        synthesizer.open();
        mixer.open();
    }
    //tried to write my own class. I thought the send method handles an MidiEvents sent to it
    public class MidiInputReceiver implements Receiver {
        public String name;
        public Receiver ogReceiver;
        public MidiInputReceiver(String name, Receiver ogReceiver) {
            this.name = name;
            this.ogReceiver = ogReceiver;
        }
        public void send(MidiMessage msg, long timeStamp) {
            System.out.println("midi received : " + this.name);
            this.ogReceiver.send(msg, timeStamp);
        }
        public void close() {
            this.ogReceiver.close();
        }
    }
    public static void main(String[] args) throws MidiUnavailableException, InterruptedException, LineUnavailableException {
        MidiHandler midiHandler = new MidiHandler();

        midiHandler.logMixers();
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void logMixers() {
        //TODO delete
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for(Mixer.Info info : mixerInfo) {
            Mixer mixer = AudioSystem.getMixer(info);
            System.out.println(info.getName() + " -> " + (mixer.isOpen() ? "open" : "closed"));
            for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
                System.out.println("  Line Info: " + lineInfo);
            }
        }
    }

    public static void logMidiDevices() throws MidiUnavailableException {

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for(int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            System.out.print(infos[i].getName());
            if(device instanceof Synthesizer) {
                System.out.println(" (Synthesizer)");
            } else if(device instanceof Sequencer) {
                System.out.println(" (Sequencer)");
            } else if(device instanceof Transmitter) {
                System.out.println(" (Transmitter)");
            } else if(device instanceof Receiver) {
                System.out.println(" (Receiver)");
            } else {
                System.out.println(" " + infos[i].getDescription());
            }
        }
        Synthesizer defaultSynthesizer = MidiSystem.getSynthesizer();
        System.out.println("Default Synthesizer : " + defaultSynthesizer.getDeviceInfo().getName());
        System.out.println("Default Sequencer : " + MidiSystem.getSequencer().getDeviceInfo().getName());

        Transmitter defaultTransmitter = MidiSystem.getTransmitter();
        MidiDeviceTransmitter defaultDeviceTransmitter = (MidiDeviceTransmitter) defaultTransmitter;
        System.out.println("Default Transmitter : " + defaultDeviceTransmitter.getMidiDevice().getDeviceInfo().getName());

        MidiDevice windowsSynth = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[3]);
        Receiver ogReceiver = windowsSynth.getReceiver();
        windowsSynth.open();
        defaultDeviceTransmitter.setReceiver(new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                System.out.println("MSG received");
                if(ogReceiver != null) {
                    ogReceiver.send(message, timeStamp);
                }
            }
            @Override
            public void close() {
                if(ogReceiver != null) {
                    ogReceiver.close();
                }
            }
        });

    }
}