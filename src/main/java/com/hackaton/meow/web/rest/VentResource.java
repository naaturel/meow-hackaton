package com.hackaton.meow.web.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VentResource {

    @GetMapping("/vent/vitesse")
    public ResponseEntity<List<DataPointDTO>> getVitesse(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",18),new DataPointDTO("04h",12),new DataPointDTO("08h",20),new DataPointDTO("12h",35),new DataPointDTO("16h",30),new DataPointDTO("20h",42));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",24),new DataPointDTO("Sem 2",31),new DataPointDTO("Sem 3",27),new DataPointDTO("Sem 4",35));
            case "mois"    -> List.of(new DataPointDTO("Jan",38),new DataPointDTO("Fév",35),new DataPointDTO("Mar",30),new DataPointDTO("Avr",25),new DataPointDTO("Mai",20),new DataPointDTO("Jun",18),new DataPointDTO("Jul",16),new DataPointDTO("Aoû",17),new DataPointDTO("Sep",22),new DataPointDTO("Oct",28),new DataPointDTO("Nov",33),new DataPointDTO("Déc",40));
            default        -> List.of(new DataPointDTO("Lun",22),new DataPointDTO("Mar",18),new DataPointDTO("Mer",30),new DataPointDTO("Jeu",25),new DataPointDTO("Ven",35),new DataPointDTO("Sam",40),new DataPointDTO("Dim",28));
        });
    }

    @GetMapping("/vent/direction")
    public ResponseEntity<List<DataPointDTO>> getDirection(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("N",12), new DataPointDTO("NE",6), new DataPointDTO("E",8),  new DataPointDTO("SE",14),
            new DataPointDTO("S",22), new DataPointDTO("SO",20), new DataPointDTO("O",10), new DataPointDTO("NO",8)
        ));
    }

    @GetMapping("/vent/rafales")
    public ResponseEntity<List<DataPointDTO>> getRafales(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",32),new DataPointDTO("04h",22),new DataPointDTO("08h",38),new DataPointDTO("12h",68),new DataPointDTO("16h",55),new DataPointDTO("20h",80));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",55),new DataPointDTO("Sem 2",70),new DataPointDTO("Sem 3",60),new DataPointDTO("Sem 4",80));
            case "mois"    -> List.of(new DataPointDTO("Jan",75),new DataPointDTO("Fév",70),new DataPointDTO("Mar",60),new DataPointDTO("Avr",50),new DataPointDTO("Mai",40),new DataPointDTO("Jun",35),new DataPointDTO("Jul",30),new DataPointDTO("Aoû",32),new DataPointDTO("Sep",45),new DataPointDTO("Oct",58),new DataPointDTO("Nov",65),new DataPointDTO("Déc",80));
            default        -> List.of(new DataPointDTO("Lun",45),new DataPointDTO("Mar",38),new DataPointDTO("Mer",62),new DataPointDTO("Jeu",52),new DataPointDTO("Ven",70),new DataPointDTO("Sam",85),new DataPointDTO("Dim",58));
        });
    }

    @GetMapping("/vent/evolution")
    public ResponseEntity<List<DataPointDTO>> getEvolution(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",16),new DataPointDTO("04h",10),new DataPointDTO("08h",18),new DataPointDTO("12h",33),new DataPointDTO("16h",28),new DataPointDTO("20h",40));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",22),new DataPointDTO("Sem 2",29),new DataPointDTO("Sem 3",25),new DataPointDTO("Sem 4",33));
            case "mois"    -> List.of(new DataPointDTO("Jan",36),new DataPointDTO("Fév",33),new DataPointDTO("Mar",28),new DataPointDTO("Avr",23),new DataPointDTO("Mai",18),new DataPointDTO("Jun",16),new DataPointDTO("Jul",14),new DataPointDTO("Aoû",15),new DataPointDTO("Sep",20),new DataPointDTO("Oct",26),new DataPointDTO("Nov",31),new DataPointDTO("Déc",38));
            default        -> List.of(new DataPointDTO("Lun",20),new DataPointDTO("Mar",16),new DataPointDTO("Mer",28),new DataPointDTO("Jeu",23),new DataPointDTO("Ven",33),new DataPointDTO("Sam",38),new DataPointDTO("Dim",26));
        });
    }
}
