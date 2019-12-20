package dev.ops.tools.swarm.docker;

import com.github.dockerjava.core.command.AbstrDockerCmd;
import com.github.dockerjava.core.util.FiltersBuilder;
import dev.ops.tools.swarm.docker.model.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List services.
 */
public class ListServicesCmdImpl extends AbstrDockerCmd<ListServicesCmd, List<Service>> implements ListServicesCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListServicesCmdImpl(ListServicesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListServicesCmd withLabelFilter(String... labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }

    @Override
    public ListServicesCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }
}
