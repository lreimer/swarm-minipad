package dev.ops.tools.swarm.docker;

import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

/**
 * Decorated {@link com.github.dockerjava.api.command.DockerCmdExecFactory} to add Swarm API support.
 */
public class DockerSwarmCmdExecFactory extends JerseyDockerCmdExecFactory {

    public ListServicesCmd.Exec createListServicesCmdExec() {
        return new ListServicesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public DockerSwarmCmdExecFactory withReadTimeout(Integer readTimeout) {
        return (DockerSwarmCmdExecFactory) super.withReadTimeout(readTimeout);
    }

    @Override
    public DockerSwarmCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        return (DockerSwarmCmdExecFactory) super.withConnectTimeout(connectTimeout);
    }
}
