package dev.ops.tools.swarm;

import com.github.dockerjava.api.model.Info;
import dev.ops.tools.swarm.docker.SwarmClient;
import dev.ops.tools.swarm.docker.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The controller class to interact with the Docker Java Client API.
 */
public class SwarmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmController.class);

    private final SwarmConfig swarmConfig;
    private final long time;
    private final TimeUnit unit;
    private final Set<Consumer<List<SwarmService>>> consumers;

    private SwarmClient swarmClient;
    private ScheduledExecutorService executorService;
    private List<Service> services;
    private List<SwarmService> swarmServices;

    public SwarmController(File configFile, long time, TimeUnit unit) {
        this.swarmConfig = SwarmConfig.fromFile(configFile);
        this.time = time;
        this.unit = unit;
        this.consumers = new HashSet<>();
    }

    public void initialize() {
        LOGGER.info("Using Swarm configuration {}", swarmConfig);
        swarmClient = SwarmClient.build();

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::updateServices, time, time, unit);
    }

    public void info() {
        try {
            Info info = swarmClient.infoCmd().exec();
            LOGGER.info("Connected to Docker {}", info);
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }
    }

    public void register(Consumer<List<SwarmService>> consumer) {
        consumers.add(consumer);
    }

    private void updateServices() {
        LOGGER.info("Updating Swarm services.");

        try {
            services = swarmClient.listServiceCmd()
                    .withLabelFilter(swarmConfig.getLabels())
                    .exec();

            swarmServices = services.stream()
                    .map(s -> new SwarmService(s.getSpec().getName(), s.getSpec().getMode().getReplicated().getReplicas()))
                    .collect(Collectors.toList());

            LOGGER.info("Current list of Swarm services {}", swarmServices);
            consumers.forEach(receiver -> receiver.accept(swarmServices));
        } catch (RuntimeException e) {
            LOGGER.warn("Error getting Swarm service list.", e);
        }
    }

    public void close() {
        executorService.shutdown();
        try {
            this.swarmClient.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to close SwarmClient.", e);
        }
    }
}
