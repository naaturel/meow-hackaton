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
        String eui = (String)deviceInfo.get("devEui");
        Map<String, Object> res = new HashMap<>();

        res.put("device_eui", eui);
        res.put("device_name", deviceInfo.get("deviceName"));
        res.put("snr", rxInfo.getFirst().get("snr"));
        res.put("rssi", rxInfo.getFirst().get("rssi"));
        res.put("time", rxInfo.getFirst().get("time"));

        if(isGaz(eui)){
            res.put("value", obj.get("TOTAL_PULSE"));
        } else if(isWater(eui)){
            res.put("value", obj.get("PA4_TOTAL_PULSE"));
        } else if(isElectricity(eui)){
        }

        return new Gson().toJson(res);
    }

    private boolean isGaz(String id){
        return id.equalsIgnoreCase("a84041bc185f4796");
    }

    private boolean isWater(String id){
        return id.equalsIgnoreCase("a840410e818855bf")
            || id.equalsIgnoreCase("a84041e4018855c2")
            || id.equalsIgnoreCase("a8404102e18855bc")
            || id.equalsIgnoreCase("a840417df18860ac")
            || id.equalsIgnoreCase("a8404152d35d680a")
            || id.equalsIgnoreCase("a84041f09188609e");
    }

    private boolean isElectricity(String id){
        return id.equalsIgnoreCase("102cefffe0111a7")
            || id.equalsIgnoreCase("102ceffffe0111e4")
            || id.equalsIgnoreCase("102cefffe011102b")
            || id.equalsIgnoreCase("102cefffe011026")
            || id.equalsIgnoreCase("102cefffe0111c0")
            || id.equalsIgnoreCase("102cefffe011134");
    }
}
