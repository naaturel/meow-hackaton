package com.hackaton.meow.web.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TemperatureResource {

    @GetMapping("/temperature/moyenne")
    public ResponseEntity<List<DataPointDTO>> getMoyenne(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",12),new DataPointDTO("04h",10),new DataPointDTO("08h",11),new DataPointDTO("12h",17),new DataPointDTO("16h",21),new DataPointDTO("20h",18));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",13),new DataPointDTO("Sem 2",15),new DataPointDTO("Sem 3",17),new DataPointDTO("Sem 4",16));
            case "mois"    -> List.of(new DataPointDTO("Jan",3),new DataPointDTO("Fév",4),new DataPointDTO("Mar",8),new DataPointDTO("Avr",12),new DataPointDTO("Mai",17),new DataPointDTO("Jun",21),new DataPointDTO("Jul",24),new DataPointDTO("Aoû",23),new DataPointDTO("Sep",19),new DataPointDTO("Oct",14),new DataPointDTO("Nov",8),new DataPointDTO("Déc",4));
            default        -> List.of(new DataPointDTO("Lun",14),new DataPointDTO("Mar",16),new DataPointDTO("Mer",13),new DataPointDTO("Jeu",17),new DataPointDTO("Ven",15),new DataPointDTO("Sam",19),new DataPointDTO("Dim",18));
        });
    }

    @GetMapping("/temperature/zones")
    public ResponseEntity<List<DataPointDTO>> getZones(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("Zone A", 16),
            new DataPointDTO("Zone B", 20),
            new DataPointDTO("Zone C", 13),
            new DataPointDTO("Zone D", 18)
        ));
    }

    @GetMapping("/temperature/ecarts")
    public ResponseEntity<List<DataPointDTO>> getEcarts(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",1.0),new DataPointDTO("04h",-0.5),new DataPointDTO("08h",2.0),new DataPointDTO("12h",0.5),new DataPointDTO("16h",3.0),new DataPointDTO("20h",1.5));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",-0.5),new DataPointDTO("Sem 2",1.2),new DataPointDTO("Sem 3",2.0),new DataPointDTO("Sem 4",0.8));
            case "mois"    -> List.of(new DataPointDTO("Jan",-1.0),new DataPointDTO("Fév",0.5),new DataPointDTO("Mar",-0.5),new DataPointDTO("Avr",1.0),new DataPointDTO("Mai",2.0),new DataPointDTO("Jun",1.5),new DataPointDTO("Jul",3.0),new DataPointDTO("Aoû",2.5),new DataPointDTO("Sep",1.0),new DataPointDTO("Oct",-0.5),new DataPointDTO("Nov",-1.0),new DataPointDTO("Déc",0.5));
            default        -> List.of(new DataPointDTO("Lun",1.2),new DataPointDTO("Mar",-0.8),new DataPointDTO("Mer",0.5),new DataPointDTO("Jeu",2.1),new DataPointDTO("Ven",-0.3),new DataPointDTO("Sam",1.8),new DataPointDTO("Dim",0.9));
        });
    }

    @GetMapping("/temperature/historique")
    public ResponseEntity<List<DataPointDTO>> getHistorique(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",11),new DataPointDTO("04h",9),new DataPointDTO("08h",10),new DataPointDTO("12h",16),new DataPointDTO("16h",20),new DataPointDTO("20h",17));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",12),new DataPointDTO("Sem 2",14),new DataPointDTO("Sem 3",16),new DataPointDTO("Sem 4",15));
            case "mois"    -> List.of(new DataPointDTO("Jan",2),new DataPointDTO("Fév",3),new DataPointDTO("Mar",7),new DataPointDTO("Avr",11),new DataPointDTO("Mai",16),new DataPointDTO("Jun",20),new DataPointDTO("Jul",23),new DataPointDTO("Aoû",22),new DataPointDTO("Sep",18),new DataPointDTO("Oct",13),new DataPointDTO("Nov",7),new DataPointDTO("Déc",3));
            default        -> List.of(new DataPointDTO("Lun",13),new DataPointDTO("Mar",15),new DataPointDTO("Mer",12),new DataPointDTO("Jeu",16),new DataPointDTO("Ven",14),new DataPointDTO("Sam",18),new DataPointDTO("Dim",17));
        });
    }
}
