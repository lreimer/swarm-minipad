package dev.ops.tools.swarm.docker;

import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.command.SyncDockerCmd;
import dev.ops.tools.swarm.docker.model.Service;

import java.util.List;
import java.util.Map;

/**
 * Command interfaces to list Swarm services.
 */
public interface ListServicesCmd extends SyncDockerCmd<List<Service>> {

    Map<String, List<String>> getFilters();

    /**
     * @param labels - Show only containers with the passed labels.
     */
    ListServicesCmd withLabelFilter(String... labels);

    /**
     * @param labels - Show only containers with the passed labels. Labels is a {@link Map} that contains label keys and values
     */
    ListServicesCmd withLabelFilter(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<ListServicesCmd, List<Service>> {
    }
}
