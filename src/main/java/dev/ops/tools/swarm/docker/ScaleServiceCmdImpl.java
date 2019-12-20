package dev.ops.tools.swarm.docker;

import com.github.dockerjava.core.command.AbstrDockerCmd;

/**
 * Scales services.
 */
public class ScaleServiceCmdImpl extends AbstrDockerCmd<ScaleServiceCmd, Void> implements ScaleServiceCmd {

    private String serviceId;
    private Integer replicas;

    public ScaleServiceCmdImpl(Exec exec, String serviceId, Integer replicas) {
        super(exec);
        withServiceId(serviceId);
        withReplicas(replicas);
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public Integer getReplicas() {
        return this.replicas;
    }

    @Override
    public ScaleServiceCmd withServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public ScaleServiceCmd withReplicas(Integer replicas) {
        this.replicas = replicas;
        return this;
    }
}
