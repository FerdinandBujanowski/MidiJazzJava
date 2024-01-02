package midi;

import javax.sound.midi.*;
import java.util.Arrays;

public class MidiTest {

    public static void main(String[] args) {
        try {
            // Get the default MIDI device

            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            Instrument[] instruments = synthesizer.getAvailableInstruments();
            synthesizer.loadInstrument(instruments[0]);
            System.out.println(Arrays.toString(instruments));
            synthesizer.open();

            // Get the default MIDI input device
            MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
            MidiDevice midiInputDevice = null;

            for (MidiDevice.Info info : midiDeviceInfo) {
                midiInputDevice = MidiSystem.getMidiDevice(info);
                if (midiInputDevice instanceof Transmitter) {
                    break;
                }
            }

            if (midiInputDevice == null) {
                System.out.println("No MIDI input device found.");
                return;
            }

            System.out.println(midiInputDevice.getDeviceInfo());
            // Open the MIDI input device and connect it to the synthesizer
            Transmitter transmitter = midiInputDevice.getTransmitter();
            Receiver receiver = synthesizer.getReceiver();

            //transmitter.setReceiver(synthesizer.getReceiver());
            transmitter.setReceiver(new Receiver() {
                @Override
                public void send(MidiMessage message, long timeStamp) {
                    System.out.println("Received MIDI message: " + message);
                    receiver.send(message, -1);
                }

                @Override
                public void close() {
                    receiver.close();
                }
            });
            midiInputDevice.open();

            System.out.println("Listening to MIDI input. Press Ctrl+C to exit.");

            // Keep the program running
            Thread.sleep(Long.MAX_VALUE);

            // Close the MIDI devices
            midiInputDevice.close();
            synthesizer.close();
        } catch (MidiUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
