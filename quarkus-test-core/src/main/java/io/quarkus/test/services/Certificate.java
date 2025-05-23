package io.quarkus.test.services;

/**
 * Defines certificate requests for which this framework will generate a keystore and truststore.
 */
public @interface Certificate {

    /**
     * Default TLS Registry configuration name.
     * When set to {@link #tlsConfigName()}, default, and not named TLS configuration will be set for you.
     */
    String DEFAULT_CONFIG = "<<default-config>>";

    enum Format {
        ENCRYPTED_PEM,
        PEM,
        JKS,
        PKCS12
    }

    /**
     * Prefix keystore and truststore name with this attribute.
     */
    String prefix() default "quarkus-qe";

    /**
     * Secure file format.
     */
    Format format() default Format.PKCS12;

    /**
     * Keystore and truststore password.
     */
    String password() default "password";

    /**
     * Whether HTTP server should be configured for you.
     * Effect of this option depends on {@link #configureKeystore()}
     * and {@link #configureTruststore()} and {@link #useTlsRegistry()}.
     * That is, if {@link #useTlsRegistry()} is true, following property is configured to {@link #tlsConfigName()}:
     *
     * <pre>
     * {@code
     * quarkus.http.tls-configuration-name=<<tls-configuration-name>>
     * }
     * </pre>
     *
     * While if the {@link #useTlsRegistry()} is false, following properties are configured:
     *
     * <pre>
     * {@code
     * # if configure keystore is enabled
     * quarkus.http.ssl.certificate.key-store-file
     * quarkus.http.ssl.certificate.key-store-file-type
     * quarkus.http.ssl.certificate.key-store-password
     * # if configure truststore is enabled
     * quarkus.http.ssl.certificate.trust-store-file
     * quarkus.http.ssl.certificate.trust-store-file-type
     * quarkus.http.ssl.certificate.trust-store-password
     * }
     * </pre>
     *
     * You still can set and/or override these properties
     * with {@link io.quarkus.test.bootstrap.BaseService#withProperty(String, String)} service method.
     */
    boolean configureHttpServer() default false;

    /**
     * Whether management interface should be configured.
     * That is, when {@link #useTlsRegistry()} is set to {@code true}, we set:
     *
     * <pre>
     * {@code
     * quarkus.management.tls-configuration-name=<<tls-configuration-name>>
     * }
     * </pre>
     *
     * And when {@link #useTlsRegistry()} is set to {@code false} and {@link #configureKeystore()} is true, we set:
     *
     * <pre>
     * {@code
     * quarkus.management.ssl.certificate.key-store-file
     * quarkus.management.ssl.certificate.key-store-file-type
     * quarkus.management.ssl.certificate.key-store-password
     * }
     * </pre>
     *
     * You still can set and/or override these properties
     * with {@link io.quarkus.test.bootstrap.BaseService#withProperty(String, String)} service method.
     */
    boolean configureManagementInterface() default false;

    /**
     * Whether keystore should be generated and configured for you.
     */
    boolean configureKeystore() default false;

    /**
     * Whether truststore should be generated and configured for you.
     */
    boolean configureTruststore() default false;

    /**
     * Whether TLS registry should be configured with generated keystore and truststore.
     */
    boolean useTlsRegistry() default true;

    /**
     * Way to configure named TLS configuration.
     * For example, when set to {@code 0}, you can expect the TLS configuration to be named zero:
     *
     * - `quarkus.tls.key-store.pem.0.cert`
     */
    String tlsConfigName() default DEFAULT_CONFIG;

    /**
     * Facilitates support for OpenShift serving certificates.
     */
    ServingCertificates[] servingCertificates() default {};

    /**
     * Specify client certificates that should be generated.
     * Generation of more than one client certificate is only implemented for {@link Format#PKCS12}.
     */
    ClientCertificate[] clientCertificates() default {};

    /**
     * Client certificates.
     */
    @interface ClientCertificate {
        /**
         * Common Name (CN) attribute within Distinguished Name (DN) of X.509 certificate.
         */
        String cnAttribute() default "localhost";

        /**
         * Whether generated client certificate should be added to the server truststore.
         */
        boolean unknownToServer() default false;
    }

    /**
     * OpenShift serving certificates configuration. This only works when internal service is used as URL.
     * Support for management interface is not implemented.
     */
    @interface ServingCertificates {

        /**
         * Whether service CA bundle should be mounted to Quarkus pod.
         * This CA bundle can be used by a REST client to communicate with pod using service certificate.
         * To put it blunt, this is client side, while {@link #addServiceCertificate()} is server side.
         */
        boolean injectCABundle() default false;

        /**
         * Whether certificate generated by OpenShift should be mounted to Quarkus pod and the TLS registry
         * extension should be configured with the certificate. These certificates are only valid for the internal
         * service DNS and won't work outside the OpenShift cluster. That is, use internal service DNS name
         * to communicate with other pods.
         */
        boolean addServiceCertificate() default false;

        /**
         * By default, when the service certificate is added, we configure the TLS registry
         * with PEM certificate paths. However, when we want to test Quarkus TLS Registry KeyStore provider,
         * we shouldn't configure the PEM certificates paths, so that the provider is used.
         */
        boolean useKeyStoreProvider() default false;
    }

}
