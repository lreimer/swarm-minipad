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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The controller class to interact with the Docker Java Client API.
 */
public class SwarmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmController.class);

    private final List<SwarmConfig> swarmConfigs;
    private final long time;
    private final TimeUnit unit;
    private final Set<Consumer<List<SwarmService>>> consumers;
    private final AtomicReference<SwarmClient> swarmClient;

    private SwarmConfig swarmConfig;
    private ScheduledExecutorService executorService;
    private List<Service> services;
    private List<SwarmService> swarmServices;

    public SwarmController(File configFile, long time, TimeUnit unit) {
        this.swarmConfigs = SwarmConfig.fromFile(configFile);
        this.time = time;
        this.unit = unit;
        this.consumers = new HashSet<>();
        this.swarmClient = new AtomicReference<>();
    }

    public void initialize() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        setSwarmConfig(swarmConfigs.get(0));
        executorService.scheduleWithFixedDelay(this::updateServices, 0, time, unit);
    }

    public void setSwarmConfig(SwarmConfig swarmConfig) {
        if (Objects.equals(this.swarmConfig, swarmConfig)) {
            return;
        }

        this.swarmConfig = swarmConfig;
        LOGGER.info("Using Swarm configuration {}", swarmConfig);

        SwarmClient old = swarmClient.getAndSet(SwarmClient.build(swarmConfig.getDockerHost()));
        closeSwarmClientQuietly(old);
    }

    public void info() {
        try {
            Info info = swarmClient.get().infoCmd().exec();
            LOGGER.info("Connected to Docker {}", info);
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }
    }

    public void register(Consumer<List<SwarmService>> consumer) {
        consumers.add(consumer);
    }

    public List<SwarmConfig> getSwarmConfigs() {
        return swarmConfigs;
    }

    public List<SwarmService> getSwarmServices() {
        return swarmServices;
    }

    private void updateServices() {
        LOGGER.info("Updating Swarm services.");

        try {
            services = swarmClient.get().listServiceCmd()
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
        closeSwarmClientQuietly(this.swarmClient.get());
    }

    private static void closeSwarmClientQuietly(SwarmClient swarmClient) {
        try {
            if (swarmClient != null) {
                swarmClient.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Unable to close SwarmClient.", e);
        }
    }

    public void scale(SwarmService swarmService, int replicas) {
        LOGGER.warn("NOT IMPLEMENTED: Scaling of Swarm service {} to {} replicas.", swarmServices, replicas);

        // int index = swarmServices.indexOf(swarmService);
        // TODO implement service scale logic
    }
}
