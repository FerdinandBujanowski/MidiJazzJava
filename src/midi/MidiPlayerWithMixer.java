package midi;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.util.Arrays;

public class MidiPlayerWithMixer {

    public static void main(String[] args) {
        try {
            // Get information about available mixers
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

            // Choose a mixer (replace with the desired mixer info)
            Mixer.Info chosenMixerInfo = mixerInfo[0]; // Replace with the desired mixer info

            // Get the chosen mixer
            Mixer mixer = AudioSystem.getMixer(chosenMixerInfo);

            // Set up a synthesizer
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            Instrument[] instruments = synthesizer.getAvailableInstruments();
            synthesizer.loadInstrument(instruments[0]);


            Receiver synthReceiver = synthesizer.getReceiver();

            // Set up a transmitter to receive MIDI events from the MIDI controller
            MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
            System.out.println(Arrays.toString(midiDeviceInfo));

            for(MidiDevice.Info infos : midiDeviceInfo) {
                MidiDevice midiDevice = MidiSystem.getMidiDevice(infos); // Replace with the desired MIDI device info
                System.out.println(midiDevice.getDeviceInfo());

                for(Transmitter transmitter : midiDevice.getTransmitters()) {
                    transmitter.setReceiver(new Receiver() {
                        @Override
                        public void send(MidiMessage message, long timeStamp) {
                            System.out.println("Message received.");
                            synthReceiver.send(message, timeStamp);
                        }

                        @Override
                        public void close() {
                            synthReceiver.close();
                        }
                    });
                }
                midiDevice.getTransmitter().setReceiver(synthReceiver);

                if(midiDevice.getDeviceInfo().toString().equals("MPK mini Plus")) {
                    midiDevice.open();
                }
            }


            // Open the mixer, synthesizer, and MIDI device
            mixer.open();
            synthesizer.open();

            // Sleep for a while to allow MIDI events to be received
            Thread.sleep(Long.MAX_VALUE); // Adjust the duration as needed

            // Close resources
            for(MidiDevice.Info infos : midiDeviceInfo) {
                MidiDevice midiDevice = MidiSystem.getMidiDevice(infos);
                for(Transmitter transmitter : midiDevice.getTransmitters()) {
                    transmitter.close();
                }
                midiDevice.close();
            }
            synthesizer.close();
            mixer.close();

        } catch (MidiUnavailableException ignored) {}
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
