package dev.ops.tools.swarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The model class for the Swarm configuration.
 */
public class SwarmConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwarmConfig.class);

    private final Map<String, String> labels = new HashMap<>();
    private final List<SwarmService> services = new ArrayList<>();

    public Map<String, String> getLabels() {
        return labels;
    }

    public List<SwarmService> getServices() {
        return services;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SwarmConfig.class.getSimpleName() + "[", "]")
                .add("labels=" + labels)
                .add("services=" + services)
                .toString();
    }

    public static SwarmConfig fromFile(File configFile) {
        try (JsonReader reader = Json.createReader(new FileInputStream(configFile))) {
            SwarmConfig config = new SwarmConfig();
            JsonObject configObject = reader.readObject();

            labels(config, configObject);
            services(config, configObject);

            return config;
        } catch (IOException e) {
            LOGGER.error("Unable to get Swarm config from file.", e);
            throw new IllegalStateException(e);
        }
    }

    private static void services(SwarmConfig config, JsonObject configObject) {
        if (configObject.containsKey("services")) {
            List<SwarmService> services = configObject.getJsonArray("services").getValuesAs(JsonString::getString)
                    .stream().map(SwarmService::new).collect(Collectors.toList());
            config.services.addAll(services);
        }
    }

    private static void labels(SwarmConfig config, JsonObject configObject) {
        if (configObject.containsKey("labels")) {
            configObject.getJsonObject("labels").forEach((key, value) -> config.labels.put(key, ((JsonString) value).getString()));
        }
    }
}
