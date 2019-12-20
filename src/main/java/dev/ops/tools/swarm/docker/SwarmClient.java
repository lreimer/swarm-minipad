package dev.ops.tools.swarm.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decorating DockerClient implementation that adds some Docker Swarm support.
 */
public class SwarmClient implements DockerClient {

    private final DockerClient dockerClient;
    private final DockerSwarmCmdExecFactory dockerCmdExecFactory;

    private SwarmClient(DockerClient dockerClient, DockerSwarmCmdExecFactory dockerCmdExecFactory) {
        this.dockerClient = dockerClient;
        this.dockerCmdExecFactory = dockerCmdExecFactory;
    }

    public static SwarmClient build(String dockerHost) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withDockerTlsVerify(false)
                .build();

        DockerSwarmCmdExecFactory dockerCmdExecFactory = new DockerSwarmCmdExecFactory()
                .withReadTimeout(5000)
                .withConnectTimeout(5000);

        DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();

        return new SwarmClient(dockerClient, dockerCmdExecFactory);
    }

    public ListServicesCmd listServiceCmd() {
        return new ListServicesCmdImpl(dockerCmdExecFactory.createListServicesCmdExec());
    }

    public ScaleServiceCmd scaleServiceCmd(String serviceId, Integer replicas) {
        return new ScaleServiceCmdImpl(dockerCmdExecFactory.createScaleServicesCmdExec(), serviceId, replicas);
    }

    @Override
    public AuthConfig authConfig() throws DockerException {
        return dockerClient.authConfig();
    }

    @Override
    public AuthCmd authCmd() {
        return dockerClient.authCmd();
    }

    @Override
    public InfoCmd infoCmd() {
        return dockerClient.infoCmd();
    }

    @Override
    public PingCmd pingCmd() {
        return dockerClient.pingCmd();
    }

    @Override
    public VersionCmd versionCmd() {
        return dockerClient.versionCmd();
    }

    @Override
    public PullImageCmd pullImageCmd(String repository) {
        return dockerClient.pullImageCmd(repository);
    }

    @Override
    public PushImageCmd pushImageCmd(String name) {
        return dockerClient.pushImageCmd(name);
    }

    @Override
    public PushImageCmd pushImageCmd(Identifier identifier) {
        return dockerClient.pushImageCmd(identifier);
    }

    @Override
    public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
        return dockerClient.createImageCmd(repository, imageStream);
    }

    @Override
    public LoadImageCmd loadImageCmd(InputStream imageStream) {
        return dockerClient.loadImageCmd(imageStream);
    }

    @Override
    public SearchImagesCmd searchImagesCmd(String term) {
        return dockerClient.searchImagesCmd(term);
    }

    @Override
    public RemoveImageCmd removeImageCmd(String imageId) {
        return dockerClient.removeImageCmd(imageId);
    }

    @Override
    public ListImagesCmd listImagesCmd() {
        return dockerClient.listImagesCmd();
    }

    @Override
    public InspectImageCmd inspectImageCmd(String imageId) {
        return dockerClient.inspectImageCmd(imageId);
    }

    @Override
    public SaveImageCmd saveImageCmd(String name) {
        return dockerClient.saveImageCmd(name);
    }

    @Override
    public ListContainersCmd listContainersCmd() {
        return dockerClient.listContainersCmd();
    }

    @Override
    public CreateContainerCmd createContainerCmd(String image) {
        return dockerClient.createContainerCmd(image);
    }

    @Override
    public StartContainerCmd startContainerCmd(String containerId) {
        return dockerClient.startContainerCmd(containerId);
    }

    @Override
    public ExecCreateCmd execCreateCmd(String containerId) {
        return dockerClient.execCreateCmd(containerId);
    }

    @Override
    public InspectContainerCmd inspectContainerCmd(String containerId) {
        return dockerClient.inspectContainerCmd(containerId);
    }

    @Override
    public RemoveContainerCmd removeContainerCmd(String containerId) {
        return dockerClient.removeContainerCmd(containerId);
    }

    @Override
    public WaitContainerCmd waitContainerCmd(String containerId) {
        return dockerClient.waitContainerCmd(containerId);
    }

    @Override
    public AttachContainerCmd attachContainerCmd(String containerId) {
        return dockerClient.attachContainerCmd(containerId);
    }

    @Override
    public ExecStartCmd execStartCmd(String execId) {
        return dockerClient.execStartCmd(execId);
    }

    @Override
    public InspectExecCmd inspectExecCmd(String execId) {
        return dockerClient.inspectExecCmd(execId);
    }

    @Override
    public LogContainerCmd logContainerCmd(String containerId) {
        return dockerClient.logContainerCmd(containerId);
    }

    @Override
    public CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(String containerId, String resource) {
        return dockerClient.copyArchiveFromContainerCmd(containerId, resource);
    }

    @Override
    @Deprecated
    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource) {
        return dockerClient.copyFileFromContainerCmd(containerId, resource);
    }

    @Override
    public CopyArchiveToContainerCmd copyArchiveToContainerCmd(String containerId) {
        return dockerClient.copyArchiveToContainerCmd(containerId);
    }

    @Override
    public ContainerDiffCmd containerDiffCmd(String containerId) {
        return dockerClient.containerDiffCmd(containerId);
    }

    @Override
    public StopContainerCmd stopContainerCmd(String containerId) {
        return dockerClient.stopContainerCmd(containerId);
    }

    @Override
    public KillContainerCmd killContainerCmd(String containerId) {
        return dockerClient.killContainerCmd(containerId);
    }

    @Override
    public UpdateContainerCmd updateContainerCmd(String containerId) {
        return dockerClient.updateContainerCmd(containerId);
    }

    @Override
    public RenameContainerCmd renameContainerCmd(String containerId) {
        return dockerClient.renameContainerCmd(containerId);
    }

    @Override
    public RestartContainerCmd restartContainerCmd(String containerId) {
        return dockerClient.restartContainerCmd(containerId);
    }

    @Override
    public CommitCmd commitCmd(String containerId) {
        return dockerClient.commitCmd(containerId);
    }

    @Override
    public BuildImageCmd buildImageCmd() {
        return dockerClient.buildImageCmd();
    }

    @Override
    public BuildImageCmd buildImageCmd(File dockerFileOrFolder) {
        return dockerClient.buildImageCmd(dockerFileOrFolder);
    }

    @Override
    public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
        return dockerClient.buildImageCmd(tarInputStream);
    }

    @Override
    public TopContainerCmd topContainerCmd(String containerId) {
        return dockerClient.topContainerCmd(containerId);
    }

    @Override
    public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
        return dockerClient.tagImageCmd(imageId, repository, tag);
    }

    @Override
    public PauseContainerCmd pauseContainerCmd(String containerId) {
        return dockerClient.pauseContainerCmd(containerId);
    }

    @Override
    public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
        return dockerClient.unpauseContainerCmd(containerId);
    }

    @Override
    public EventsCmd eventsCmd() {
        return dockerClient.eventsCmd();
    }

    @Override
    public StatsCmd statsCmd(String containerId) {
        return dockerClient.statsCmd(containerId);
    }

    @Override
    public CreateVolumeCmd createVolumeCmd() {
        return dockerClient.createVolumeCmd();
    }

    @Override
    public InspectVolumeCmd inspectVolumeCmd(String name) {
        return dockerClient.inspectVolumeCmd(name);
    }

    @Override
    public RemoveVolumeCmd removeVolumeCmd(String name) {
        return dockerClient.removeVolumeCmd(name);
    }

    @Override
    public ListVolumesCmd listVolumesCmd() {
        return dockerClient.listVolumesCmd();
    }

    @Override
    public ListNetworksCmd listNetworksCmd() {
        return dockerClient.listNetworksCmd();
    }

    @Override
    public InspectNetworkCmd inspectNetworkCmd() {
        return dockerClient.inspectNetworkCmd();
    }

    @Override
    public CreateNetworkCmd createNetworkCmd() {
        return dockerClient.createNetworkCmd();
    }

    @Override
    public RemoveNetworkCmd removeNetworkCmd(String networkId) {
        return dockerClient.removeNetworkCmd(networkId);
    }

    @Override
    public ConnectToNetworkCmd connectToNetworkCmd() {
        return dockerClient.connectToNetworkCmd();
    }

    @Override
    public DisconnectFromNetworkCmd disconnectFromNetworkCmd() {
        return dockerClient.disconnectFromNetworkCmd();
    }

    @Override
    public void close() throws IOException {
        dockerClient.close();
    }
}
