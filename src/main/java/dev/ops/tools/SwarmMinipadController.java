package dev.ops.tools;

import dev.ops.tools.midi.LaunchpadDevice;
import dev.ops.tools.midi.MidiSystemHandler;
import dev.ops.tools.swarm.SwarmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launchpad controller implementation handles logic for button events and colors.
 */
public class SwarmMinipadController extends LaunchpadDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmMinipadController.class);

    private final MidiSystemHandler midiSystem;
    private final SwarmController swarmController;

    public SwarmMinipadController(MidiSystemHandler midiSystem, SwarmController swarmController) {
        this.midiSystem = midiSystem;
        this.swarmController = swarmController;
    }

    public void initialize() {
        swarmController.initialize();
        midiSystem.initialize(this);
        reset();
    }

    @Override
    public void close() {
        swarmController.close();
        super.close();
    }

    @Override
    protected void handle(int command, int data1, int data2) {
        if (command == 176 && data2 == 127) {
            // a 1-8 button has been pressed
            LOGGER.info("Received MIDI event for 1-8 button [command={},data1={},data2={}]", command, data1, data2);

        } else if (command == 144 && data2 == 127) {
            LOGGER.info("Received MIDI event for A-H button [command={},data1={},data2={}]", command, data1, data2);

        }
    }
}
