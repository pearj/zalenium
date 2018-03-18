package de.zalando.ep.zalenium.registry;

import java.util.Optional;
import java.util.logging.Logger;

import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.web.Hub;

import de.zalando.ep.zalenium.container.ContainerFactory;
import de.zalando.ep.zalenium.container.kubernetes.KubernetesContainerClient;
import de.zalando.ep.zalenium.proxy.DockerSeleniumRemoteProxy;

public class KubernetesZaleniumRegistry extends ZaleniumRegistry {
    private KubernetesContainerClient k8sClient;
    
    private static final Logger logger = Logger.getLogger(KubernetesZaleniumRegistry.class.getName());

    public KubernetesZaleniumRegistry() {
        this(null);
    }
    
    public KubernetesZaleniumRegistry(Hub hub) {
        super(hub);
        if (ContainerFactory.getIsKubernetes().get()) {
            k8sClient = (KubernetesContainerClient) ContainerFactory.getContainerClient();
            if (k8sClient == null) {
                throw new IllegalStateException("Kubernetes Container Client not initialised, cannot start");
            }
            else {
                k8sClient.startPodWatchers(this);
                logger.info("Kubernetes Zalenium Registry is enabled.");
            }
        }
        else {
            throw new IllegalStateException("Kubernetes not detected, cannot use the Kubernetes Registry");
        }
    }
}
