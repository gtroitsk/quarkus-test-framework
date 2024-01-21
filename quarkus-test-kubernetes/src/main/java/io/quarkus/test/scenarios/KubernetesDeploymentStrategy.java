package io.quarkus.test.scenarios;

/**
 * Kubernetes Deployment strategies.
 */
public enum KubernetesDeploymentStrategy {
    /**
     * Will build the Quarkus app image and push it into a Container Registry to be accessed by Kubernetes to deploy the app.
     */
    UsingContainerRegistry,
    /**
     * Will use the OpenShift Quarkus extension to build and deploy into Kubernetes.
     */
    UsingKubernetesExtension
}
