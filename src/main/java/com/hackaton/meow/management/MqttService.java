package com.hackaton.meow.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.meow.domain.MqttMessageWrapper;
import com.hackaton.meow.domain.exceptions.ServiceException;
import com.hackaton.meow.domain.utils.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Service
public class MqttService {

    private final MqttClient brokerClient;
    private final MqttConnectOptions options;
    private Consumer<Throwable> onConnectionLost;
    private Consumer<MqttMessageWrapper> onMessageReceived;

    public MqttService(MqttClient mqttBroker, MqttConnectOptions options) {
        this.brokerClient = mqttBroker;
        this.options = options;
        this.onConnectionLost = null;
        this.onMessageReceived = null;
    }

    public void onConnectionLost(Consumer<Throwable> consumer){
        this.onConnectionLost = consumer;
    }

    public void onMessageReceived(Consumer<MqttMessageWrapper> consumer){
        this.onMessageReceived = consumer;
    }

    public void publish(String topic, Object data) throws ServiceException {
        try {
            String payload = new ObjectMapper().writeValueAsString(data);
            publish(topic, payload);
        } catch (Exception e){
            throw new ServiceException("Unable to serialize data", e);
        }
    }

    public void publish(String topic, String payload) throws ServiceException {
        try {
            connect();
            MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            message.setQos(1);
            brokerClient.publish(topic, message);
        } catch (MqttException e) {
            throw new ServiceException("Failed to publish on broker", e);
        }
    }

    public void subscribe(String topic) {
        try {
            connect();

            brokerClient.subscribe(topic, 1);
        } catch (Exception e) {
            Logger.displayError("An error occurred while subscribing : " + e.getMessage());
        }
    }

    public void registerCallback(){
        this.brokerClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                onConnectionLost.accept(cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                MqttMessageWrapper msg = new MqttMessageWrapper(topic, message);
                onMessageReceived.accept(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not needed for subscriber
            }
        });
    }

    public void disconnect() throws ServiceException {
        if(this.brokerClient.isConnected()){
            try {
                this.brokerClient.disconnect();
            } catch (MqttException e) {
                throw new ServiceException("Failed to disconnect the broker", e);
            }
        }
    }

    public void connect() throws MqttException {
        try {
            if (this.brokerClient.isConnected()) return;
            this.brokerClient.connect(this.options);
            Logger.displayInfo("Connected to " + this.brokerClient.getCurrentServerURI());
        } catch (Exception e){
            Logger.displayError("Unable to connect to broker : " + e.getMessage());
        }
    }
}
