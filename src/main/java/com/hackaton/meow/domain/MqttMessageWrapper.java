package com.hackaton.meow.domain;


import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class MqttMessageWrapper {

    private final String topic;
    private final MqttMessage message;

    public MqttMessageWrapper(String topic, MqttMessage message){
        this.topic = topic;
        this.message = message;
    }

    public String getContent() {
        return new String(message.getPayload(), StandardCharsets.UTF_8);
    }

    public String getTopic() {
        return topic;
    }
}
