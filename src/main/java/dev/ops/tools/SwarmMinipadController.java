package dev.ops.tools;

import dev.ops.tools.midi.LaunchpadColor;
import dev.ops.tools.midi.LaunchpadDevice;
import dev.ops.tools.midi.MidiSystemHandler;
import dev.ops.tools.swarm.SwarmController;
import dev.ops.tools.swarm.SwarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        midiSystem.initialize(this);
        reset();

        // currently only bank 1 support
        top(0, LaunchpadColor.BRIGHT_AMBER);

        swarmController.initialize();
        swarmController.register(this::display);
        swarmController.info();
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

    private void display(List<SwarmService> services) {
        for (int i = 0; i < services.size() && i < 8; i++) {
            SwarmService service = services.get(i);
            LOGGER.info("Displaying Swarm service {} at row {}", service.getName(), i);

            List<LaunchpadColor> colors = IntStream.range(0, service.getReplicas())
                    .mapToObj(r -> LaunchpadColor.BRIGHT_GREEN)
                    .collect(Collectors.toList());

            clearRow(i);
            colorRow(i, colors);

            right(i, LaunchpadColor.BRIGHT_RED);
        }

        // clear any unused rows
        for (int i = services.size(); i < 8; i++) {
            clearRow(i);
        }
    }
}
