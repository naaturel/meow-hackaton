package com.hackaton.meow.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttProperties {

    @Value("${mqtt.broker-url.plain}")
    private String brokerUrl;

    @Value("${mqtt.broker-url.secure}")
    private String brokerSecureUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    public String getBrokerUrl() { return brokerUrl; }
    public String getBrokerSecureUrl() { return brokerSecureUrl; }
    public String getClientId() { return clientId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
