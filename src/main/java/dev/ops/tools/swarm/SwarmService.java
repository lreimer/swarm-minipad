package dev.ops.tools.swarm;

import java.util.StringJoiner;

/**
 * The model class for Docker Swarm Services.
 */
public class SwarmService {

    private final String name;

    public SwarmService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SwarmService.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
