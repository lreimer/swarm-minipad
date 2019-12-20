package dev.ops.tools.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * The controller class to interact with the Docker Java Client API.
 */
public class SwarmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmController.class);

    private final DockerClient dockerClient;
    private final SwarmConfig swarmConfig;

    public SwarmController(File configFile) {
        this.dockerClient = DockerClientBuilder.getInstance().build();
        this.swarmConfig = SwarmConfig.fromFile(configFile);
    }

    public void initialize() {
        LOGGER.info("Using Swarm configuration {}", swarmConfig);
    }

    public void close() {
        try {
            this.dockerClient.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to close DockerClient.", e);
        }
    }
}
