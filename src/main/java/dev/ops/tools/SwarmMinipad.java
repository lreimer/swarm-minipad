package dev.ops.tools;

import dev.ops.tools.midi.MidiSystemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;

/**
 * Main application for the K8s Minipad.
 */
@Command(version = "Docker Swarm Minipad 1.0", mixinStandardHelpOptions = true)
class SwarmMinipad implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmMinipad.class);

    @Option(names = {"-f", "--file"}, paramLabel = "JSON_CONFIG", description = "the configuration file", required = true)
    private File configFile;

    public static void main(String[] args) {
        CommandLine.run(new SwarmMinipad(), args);
    }

    @Override
    public void run() {
        LOGGER.info("Running Docker Swarm Minipad ...");

        MidiSystemHandler midiSystem = new MidiSystemHandler();
        midiSystem.infos();

        SwarmMinipadController minipadController = new SwarmMinipadController(midiSystem);
        minipadController.initialize();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutdown Docker Swarm Minipad.");
            minipadController.close();
            midiSystem.destroy();
        }));
    }
}
