package com.hackaton.meow.domain;


import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MqttMessageWrapper {

    private final String topic;
    private final MqttMessage message;
    private final Map<String, Object> deserialized;

    public MqttMessageWrapper(String topic, MqttMessage message){
        this.topic = topic;
        this.message = message;
        this.deserialized = new ObjectMapper().readValue(getContent(), new TypeReference<>() {});
    }

    public String getContent() {
        return new String(message.getPayload(), StandardCharsets.UTF_8);
    }

    public String getTopic() {
        return topic;
    }

    public <T> T getObject(Class<T> clazz, String key) {
        return clazz.cast(deserialized.get(key));
    }

    public <T> T getObject(Type type, String key) {
        Gson gson = new Gson();
        Object value = deserialized.get(key);
        return gson.fromJson(gson.toJson(value), type);
    }
}
