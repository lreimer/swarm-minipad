package dev.ops.tools.swarm;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * The model class for Docker Swarm Services.
 */
public class SwarmService {

    private final String name;
    private Integer replicas;

    public SwarmService(String name) {
        this.name = name;
    }

    public SwarmService(String name, Integer replicas) {
        this.name = name;
        this.replicas = replicas;
    }

    public String getName() {
        return name;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SwarmService.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("replicas='" + replicas + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmService that = (SwarmService) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
