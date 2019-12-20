package dev.ops.tools.swarm;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * The model class for the Swarm configuration.
 */
public class SwarmConfig {

    private final String name;
    private final String dockerHost;

    public SwarmConfig(String name, String dockerHost) {
        this.name = name;
        this.dockerHost = dockerHost;
    }

    private final Map<String, String> labels = new HashMap<>();

    public Map<String, String> getLabels() {
        return labels;
    }

    public String getName() {
        return name;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SwarmConfig.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("dockerHost='" + dockerHost + "'")
                .add("labels=" + labels)
                .toString();
    }

    public static List<SwarmConfig> fromFile(File configFile) {
        try (JsonReader reader = Json.createReader(new FileInputStream(configFile))) {
            JsonArray values = reader.readArray();

            return values.getValuesAs(SwarmConfig::transform);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to get Swarm config from file.", e);
        }
    }

    private static SwarmConfig transform(JsonObject configObject) {
        String name = configObject.getString("name", "Default");
        String dockerHost = configObject.getString("dockerHost", "tcp://localhost:2375");

        SwarmConfig config = new SwarmConfig(name, dockerHost);
        labels(config, configObject);

        return config;
    }

    private static void labels(SwarmConfig config, JsonObject configObject) {
        if (configObject.containsKey("labels")) {
            configObject.getJsonObject("labels").forEach((key, value) -> config.labels.put(key, ((JsonString) value).getString()));
        }
    }
}
