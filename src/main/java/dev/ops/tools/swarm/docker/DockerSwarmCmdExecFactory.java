package dev.ops.tools.swarm.docker;

import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

/**
 * Decorated {@link com.github.dockerjava.api.command.DockerCmdExecFactory} to add Swarm API support.
 */
public class DockerSwarmCmdExecFactory extends JerseyDockerCmdExecFactory {

    public ListServicesCmd.Exec createListServicesCmdExec() {
        return new ListServicesCmdExec(getBaseResource(), getDockerClientConfig());
    }
}
