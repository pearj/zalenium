package de.zalando.ep.zalenium.container;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.openqa.grid.internal.TestSession;

import de.zalando.ep.zalenium.proxy.DockerSeleniumRemoteProxy;

public interface ContainerClient {
    
    void setNodeId(String nodeId);

    ContainerClientRegistration registerNode(String zaleniumContainerName, URL remoteHost);

    InputStream copyFiles(String containerId, String folderName);

    void stopContainer(String containerId);

    void executeCommand(String containerId, String[] command, boolean waitForExecution);

    String getLatestDownloadedImage(String imageName);

    int getRunningContainers(String image);

    ContainerCreationStatus createContainer(String zaleniumContainerName, String image, Map<String, String> envVars, String nodePort);

    void initialiseContainerEnvironment();

    String getContainerIp(String containerName);
    
    boolean sessionCreated(TestSession session, DockerSeleniumRemoteProxy dockerProxy);
    
    SeleniumContainerMode getSeleniumContainerMode();
    
    boolean allocateRandomNodePort();
    
    public static enum SeleniumContainerMode {
        MULTINODE,
        GRID
    }
}
