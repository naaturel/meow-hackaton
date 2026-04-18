package com.hackaton.meow.web.rest.endpoints;

import java.util.List;

import com.hackaton.meow.management.RecordService;
import com.hackaton.meow.web.rest.DataPointDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AirResource {

    private final RecordService service;

    public AirResource(RecordService service) {
        this.service = service;
    }

    @GetMapping("/air/iqa")
    public ResponseEntity<List<DataPointDTO>> getIqa(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",42),new DataPointDTO("04h",35),new DataPointDTO("08h",55),new DataPointDTO("12h",85),new DataPointDTO("16h",88),new DataPointDTO("20h",60));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",52),new DataPointDTO("Sem 2",65),new DataPointDTO("Sem 3",70),new DataPointDTO("Sem 4",58));
            case "mois"    -> List.of(new DataPointDTO("Jan",45),new DataPointDTO("Fév",48),new DataPointDTO("Mar",55),new DataPointDTO("Avr",60),new DataPointDTO("Mai",65),new DataPointDTO("Jun",70),new DataPointDTO("Jul",75),new DataPointDTO("Aoû",72),new DataPointDTO("Sep",65),new DataPointDTO("Oct",58),new DataPointDTO("Nov",50),new DataPointDTO("Déc",42));
            default        -> List.of(new DataPointDTO("Lun",55),new DataPointDTO("Mar",48),new DataPointDTO("Mer",62),new DataPointDTO("Jeu",70),new DataPointDTO("Ven",65),new DataPointDTO("Sam",40),new DataPointDTO("Dim",38));
        });
    }

    @GetMapping("/air/co2")
    public ResponseEntity<List<DataPointDTO>> getCo2(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",395),new DataPointDTO("04h",388),new DataPointDTO("08h",410),new DataPointDTO("12h",450),new DataPointDTO("16h",440),new DataPointDTO("20h",415));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",408),new DataPointDTO("Sem 2",418),new DataPointDTO("Sem 3",425),new DataPointDTO("Sem 4",412));
            case "mois"    -> List.of(new DataPointDTO("Jan",400),new DataPointDTO("Fév",402),new DataPointDTO("Mar",408),new DataPointDTO("Avr",415),new DataPointDTO("Mai",420),new DataPointDTO("Jun",425),new DataPointDTO("Jul",428),new DataPointDTO("Aoû",426),new DataPointDTO("Sep",418),new DataPointDTO("Oct",412),new DataPointDTO("Nov",405),new DataPointDTO("Déc",398));
            default        -> List.of(new DataPointDTO("Lun",410),new DataPointDTO("Mar",405),new DataPointDTO("Mer",420),new DataPointDTO("Jeu",435),new DataPointDTO("Ven",425),new DataPointDTO("Sam",398),new DataPointDTO("Dim",392));
        });
    }

    @GetMapping("/air/pm25")
    public ResponseEntity<List<DataPointDTO>> getPm25(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",8),new DataPointDTO("04h",5),new DataPointDTO("08h",12),new DataPointDTO("12h",22),new DataPointDTO("16h",24),new DataPointDTO("20h",15));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",15),new DataPointDTO("Sem 2",20),new DataPointDTO("Sem 3",22),new DataPointDTO("Sem 4",17));
            case "mois"    -> List.of(new DataPointDTO("Jan",18),new DataPointDTO("Fév",20),new DataPointDTO("Mar",16),new DataPointDTO("Avr",12),new DataPointDTO("Mai",10),new DataPointDTO("Jun",8),new DataPointDTO("Jul",7),new DataPointDTO("Aoû",8),new DataPointDTO("Sep",11),new DataPointDTO("Oct",15),new DataPointDTO("Nov",18),new DataPointDTO("Déc",20));
            default        -> List.of(new DataPointDTO("Lun",14),new DataPointDTO("Mar",12),new DataPointDTO("Mer",18),new DataPointDTO("Jeu",22),new DataPointDTO("Ven",20),new DataPointDTO("Sam",8),new DataPointDTO("Dim",7));
        });
    }

    @GetMapping("/air/evolution")
    public ResponseEntity<List<DataPointDTO>> getEvolution(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",40),new DataPointDTO("04h",33),new DataPointDTO("08h",52),new DataPointDTO("12h",80),new DataPointDTO("16h",84),new DataPointDTO("20h",58));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",49),new DataPointDTO("Sem 2",62),new DataPointDTO("Sem 3",67),new DataPointDTO("Sem 4",55));
            case "mois"    -> List.of(new DataPointDTO("Jan",42),new DataPointDTO("Fév",45),new DataPointDTO("Mar",52),new DataPointDTO("Avr",57),new DataPointDTO("Mai",62),new DataPointDTO("Jun",67),new DataPointDTO("Jul",72),new DataPointDTO("Aoû",69),new DataPointDTO("Sep",62),new DataPointDTO("Oct",55),new DataPointDTO("Nov",47),new DataPointDTO("Déc",39));
            default        -> List.of(new DataPointDTO("Lun",52),new DataPointDTO("Mar",45),new DataPointDTO("Mer",58),new DataPointDTO("Jeu",66),new DataPointDTO("Ven",62),new DataPointDTO("Sam",38),new DataPointDTO("Dim",35));
        });
    }
}
