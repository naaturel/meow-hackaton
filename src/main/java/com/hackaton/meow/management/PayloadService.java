package com.hackaton.meow.management;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackaton.meow.domain.MqttMessageWrapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayloadService {

    public PayloadService() {

    }

    public String getPayload(MqttMessageWrapper message){
        //            time
        //            tenant_name (A qui ça appartient)
        //            application_name (Où se situe l'appareil (sur quel site))
        //            device_name (C'est quoi comme appareil)
        //            dev_eui (identifiant unique de l'appareil)
        //            object (les données de l'objet)
        //            rx_info (les données de réception)
        //            tx_info (les données de transmission)

        Map<String, Object> deviceInfo = message.getObject(new TypeToken<Map<String, Object>>(){}.getType(), "deviceInfo");
        Map<String, Object> obj = message.getObject(new TypeToken<Map<String, Object>>(){}.getType(), "object");
        List<Map<String, Object>> rxInfo = (message.getObject(new TypeToken<List<Map<String, Object>>>(){}.getType(), "rxInfo"));
        Map<String, Object> res = new HashMap<>();

        res.put("device_name", deviceInfo.get("deviceName"));
        res.put("snr", rxInfo.getFirst().get("snr"));
        res.put("rssi", rxInfo.getFirst().get("rssi"));
        res.put("time", rxInfo.getFirst().get("time"));

        String eui = (String)deviceInfo.get("devEui");
        if(isGaz(eui)){
            res.put("value", obj.get("TOTAL_PULSE"));
        } else if(isWater(eui)){
            res.put("value", obj.get("PA4_TOTAL_PULSE"));
        } else if(isElectricity(eui)){
        }

        return new Gson().toJson(res);
    }

    private boolean isGaz(String id){
        return true;
    }

    private boolean isWater(String id){
        return true;
    }

    private boolean isElectricity(String id){
        return true;
    }
}
