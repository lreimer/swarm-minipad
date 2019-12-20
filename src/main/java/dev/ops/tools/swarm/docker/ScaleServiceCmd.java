package dev.ops.tools.swarm.docker;

import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.command.SyncDockerCmd;

/**
 * Command interface to scale a service.
 */
public interface ScaleServiceCmd extends SyncDockerCmd<Void> {

    String getServiceId();

    Integer getReplicas();

    ScaleServiceCmd withServiceId(String serviceId);

    ScaleServiceCmd withReplicas(Integer replicas);

    interface Exec extends DockerCmdSyncExec<ScaleServiceCmd, Void> {
    }
}
