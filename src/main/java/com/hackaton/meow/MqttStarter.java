package com.hackaton.meow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackaton.meow.domain.exceptions.ServiceException;
import com.hackaton.meow.domain.utils.Logger;
import com.hackaton.meow.management.MqttService;
import com.hackaton.meow.management.PayloadService;
import com.hackaton.meow.management.SSEService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

@Component
public class MqttStarter {

    private final MqttService mqttService;
    private final SSEService sseService;
    private final PayloadService payloadService;

    public MqttStarter(MqttService mqttService, SSEService sseService, PayloadService payloadService) {
        this.mqttService = mqttService;
        this.sseService = sseService;
        this.payloadService = payloadService;
    }

    @PostConstruct
    public void start(){
        try {
            Logger.displayInfo("Broker initialized");
            setCallback();
            mqttService.subscribe("application/+/device/+/event/up");
        } catch (Exception e){
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @PreDestroy
    public void stop() {
        try {
            mqttService.disconnect();
        } catch (ServiceException se){
            Logger.displayError("Failed to disconnect : " + se.getMessage());
        }
    }

    private void setCallback(){

        mqttService.onConnectionLost((cause) -> {
            Logger.displayError("Connection lost: " + cause.getMessage());
        });

        mqttService.onMessageReceived((msg) -> {
            Logger.displayInfo("Received message on topic " + msg.getTopic() + ": " + msg.getContent());

            String payload = payloadService.getPayload(msg);
            sseService.broadcast(payload);
        });

        mqttService.registerCallback();
    }
}
