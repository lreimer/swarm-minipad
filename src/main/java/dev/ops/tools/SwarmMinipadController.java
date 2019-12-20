package dev.ops.tools;

import dev.ops.tools.midi.LaunchpadColor;
import dev.ops.tools.midi.LaunchpadDevice;
import dev.ops.tools.midi.MidiSystemHandler;
import dev.ops.tools.swarm.SwarmConfig;
import dev.ops.tools.swarm.SwarmController;
import dev.ops.tools.swarm.SwarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Launchpad controller implementation handles logic for button events and colors.
 */
public class SwarmMinipadController extends LaunchpadDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmMinipadController.class);

    private final MidiSystemHandler midiSystem;
    private final SwarmController swarmController;

    private SwarmConfig swarmConfig;

    public SwarmMinipadController(MidiSystemHandler midiSystem, SwarmController swarmController) {
        this.midiSystem = midiSystem;
        this.swarmController = swarmController;
    }

    public void initialize() {
        midiSystem.initialize(this);
        reset();

        updateSwarmSelectors();

        swarmController.initialize();
        swarmController.register(this::display);

        swarmController.setSwarmConfig(swarmConfig);
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

            int index = data1 - 104;
            if (index < swarmController.getSwarmConfigs().size()) {
                this.swarmConfig = swarmController.getSwarmConfigs().get(index);
                LOGGER.info("Selected {}", this.swarmConfig.getName());

                clear();
                swarmController.setSwarmConfig(swarmConfig);
                swarmController.info();
                updateSwarmSelectors();
            }
        } else if (command == 144 && data2 == 127) {
            boolean isAH = A_H_BUTTONS.contains(data1);
            if (isAH) {
                // a A-H button has been pressed
                LOGGER.info("Received MIDI event for A-H kill button [command={},data1={},data2={}]", command, data1, data2);

                int row = A_H_BUTTONS.indexOf(data1);
                if (row < swarmController.getSwarmServices().size()) {
                    SwarmService swarmService = swarmController.getSwarmServices().get(row);
                    swarmController.scale(swarmService, 0);
                }
            } else {
                // a square button has been pressed
                LOGGER.info("Received MIDI event for Square scale button [command={},data1={},data2={}]", command, data1, data2);

                int row = data1 / 16;
                int col = data1 % 16;

                SwarmService swarmService = swarmController.getSwarmServices().get(row);
                int replicas = swarmService.getReplicas();
                if (col + 1 != replicas) {
                    swarmController.scale(swarmService, col + 1);
                }
            }
        }
    }

    private void updateSwarmSelectors() {
        List<SwarmConfig> swarms = swarmController.getSwarmConfigs();
        if (swarmConfig == null) {
            this.swarmConfig = swarms.get(0);
        }

        for (int i = 0; i < swarms.size(); i++) {
            SwarmConfig sc = swarms.get(i);
            if (Objects.equals(sc, swarmConfig)) {
                top(i, LaunchpadColor.BRIGHT_AMBER);
            } else {
                top(i, LaunchpadColor.DARK_AMBER);
            }
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
