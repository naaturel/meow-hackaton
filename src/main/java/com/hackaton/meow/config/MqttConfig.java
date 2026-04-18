package com.hackaton.meow.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private final MqttProperties properties;

    @Autowired
    public MqttConfig(MqttProperties properties){
        this.properties = properties;
    }

    @Bean
    public MqttClient mqttBroker() throws MqttException {
        return new MqttClient(properties.getBrokerUrl(), properties.getClientId());
    }

    @Bean
    public MqttClient mqttBrokerSecure() throws MqttException {
        return new MqttClient(properties.getBrokerSecureUrl(), properties.getClientId());
    }

    @Bean
    public MqttConnectOptions connectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        return options;
    }
}
