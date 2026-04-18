package com.hackaton.meow.web.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndustrielResource {

    @GetMapping("/industriel/charge")
    public ResponseEntity<List<DataPointDTO>> getCharge(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("Ligne A", 82),
            new DataPointDTO("Ligne B", 88),
            new DataPointDTO("Ligne C", 65),
            new DataPointDTO("Ligne D", 88)
        ));
    }

    @GetMapping("/industriel/alertes")
    public ResponseEntity<List<DataPointDTO>> getAlertes(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",0),new DataPointDTO("04h",1),new DataPointDTO("08h",2),new DataPointDTO("12h",3),new DataPointDTO("16h",1),new DataPointDTO("20h",4));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",8),new DataPointDTO("Sem 2",12),new DataPointDTO("Sem 3",7),new DataPointDTO("Sem 4",10));
            case "mois"    -> List.of(new DataPointDTO("Jan",22),new DataPointDTO("Fév",18),new DataPointDTO("Mar",25),new DataPointDTO("Avr",15),new DataPointDTO("Mai",12),new DataPointDTO("Jun",8),new DataPointDTO("Jul",6),new DataPointDTO("Aoû",9),new DataPointDTO("Sep",14),new DataPointDTO("Oct",20),new DataPointDTO("Nov",24),new DataPointDTO("Déc",28));
            default        -> List.of(new DataPointDTO("Lun",3),new DataPointDTO("Mar",2),new DataPointDTO("Mer",4),new DataPointDTO("Jeu",1),new DataPointDTO("Ven",5),new DataPointDTO("Sam",1),new DataPointDTO("Dim",0));
        });
    }

    @GetMapping("/industriel/vibrations")
    public ResponseEntity<List<DataPointDTO>> getVibrations(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",1.2),new DataPointDTO("04h",0.9),new DataPointDTO("08h",1.5),new DataPointDTO("12h",2.8),new DataPointDTO("16h",2.4),new DataPointDTO("20h",3.2));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",1.9),new DataPointDTO("Sem 2",2.3),new DataPointDTO("Sem 3",2.0),new DataPointDTO("Sem 4",2.5));
            case "mois"    -> List.of(new DataPointDTO("Jan",2.4),new DataPointDTO("Fév",2.2),new DataPointDTO("Mar",2.0),new DataPointDTO("Avr",1.9),new DataPointDTO("Mai",1.8),new DataPointDTO("Jun",1.6),new DataPointDTO("Jul",1.5),new DataPointDTO("Aoû",1.6),new DataPointDTO("Sep",1.8),new DataPointDTO("Oct",2.0),new DataPointDTO("Nov",2.2),new DataPointDTO("Déc",2.5));
            default        -> List.of(new DataPointDTO("Lun",1.8),new DataPointDTO("Mar",1.6),new DataPointDTO("Mer",2.0),new DataPointDTO("Jeu",2.4),new DataPointDTO("Ven",2.2),new DataPointDTO("Sam",1.2),new DataPointDTO("Dim",1.0));
        });
    }

    @GetMapping("/industriel/fonctionnement")
    public ResponseEntity<List<DataPointDTO>> getFonctionnement(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("En marche",  75),
            new DataPointDTO("Maintenance", 6),
            new DataPointDTO("Arrêt",       4),
            new DataPointDTO("Veille",     15)
        ));
    }
}
