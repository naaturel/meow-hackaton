package com.hackaton.meow.web.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ElectriciteResource {

    @GetMapping("/electricite/consommation")
    public ResponseEntity<List<DataPointDTO>> getConsommation(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",80),new DataPointDTO("04h",60),new DataPointDTO("08h",120),new DataPointDTO("12h",350),new DataPointDTO("16h",310),new DataPointDTO("20h",480));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",2800),new DataPointDTO("Sem 2",3100),new DataPointDTO("Sem 3",2950),new DataPointDTO("Sem 4",3200));
            case "mois"    -> List.of(new DataPointDTO("Jan",1800),new DataPointDTO("Fév",1650),new DataPointDTO("Mar",1500),new DataPointDTO("Avr",1350),new DataPointDTO("Mai",1200),new DataPointDTO("Jun",1100),new DataPointDTO("Jul",1050),new DataPointDTO("Aoû",1080),new DataPointDTO("Sep",1250),new DataPointDTO("Oct",1450),new DataPointDTO("Nov",1650),new DataPointDTO("Déc",1900));
            default        -> List.of(new DataPointDTO("Lun",420),new DataPointDTO("Mar",380),new DataPointDTO("Mer",450),new DataPointDTO("Jeu",410),new DataPointDTO("Ven",390),new DataPointDTO("Sam",310),new DataPointDTO("Dim",280));
        });
    }

    @GetMapping("/electricite/pic")
    public ResponseEntity<List<DataPointDTO>> getPic(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",55),new DataPointDTO("04h",35),new DataPointDTO("08h",140),new DataPointDTO("12h",380),new DataPointDTO("16h",330),new DataPointDTO("20h",510));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",520),new DataPointDTO("Sem 2",490),new DataPointDTO("Sem 3",510),new DataPointDTO("Sem 4",480));
            case "mois"    -> List.of(new DataPointDTO("Jan",210),new DataPointDTO("Fév",195),new DataPointDTO("Mar",180),new DataPointDTO("Avr",165),new DataPointDTO("Mai",145),new DataPointDTO("Jun",130),new DataPointDTO("Jul",125),new DataPointDTO("Aoû",130),new DataPointDTO("Sep",155),new DataPointDTO("Oct",175),new DataPointDTO("Nov",200),new DataPointDTO("Déc",220));
            default        -> List.of(new DataPointDTO("Lun",480),new DataPointDTO("Mar",350),new DataPointDTO("Mer",420),new DataPointDTO("Jeu",395),new DataPointDTO("Ven",440),new DataPointDTO("Sam",260),new DataPointDTO("Dim",210));
        });
    }

    @GetMapping("/electricite/repartition")
    public ResponseEntity<List<DataPointDTO>> getRepartition(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("Résidentiel", 38),
            new DataPointDTO("Industriel",  30),
            new DataPointDTO("Commercial",  22),
            new DataPointDTO("Transport",   10)
        ));
    }

    @GetMapping("/electricite/evolution")
    public ResponseEntity<List<DataPointDTO>> getEvolution(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",75),new DataPointDTO("04h",52),new DataPointDTO("08h",115),new DataPointDTO("12h",345),new DataPointDTO("16h",305),new DataPointDTO("20h",475));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",2750),new DataPointDTO("Sem 2",3050),new DataPointDTO("Sem 3",2900),new DataPointDTO("Sem 4",3150));
            case "mois"    -> List.of(new DataPointDTO("Jan",1780),new DataPointDTO("Fév",1630),new DataPointDTO("Mar",1480),new DataPointDTO("Avr",1330),new DataPointDTO("Mai",1180),new DataPointDTO("Jun",1080),new DataPointDTO("Jul",1030),new DataPointDTO("Aoû",1060),new DataPointDTO("Sep",1230),new DataPointDTO("Oct",1430),new DataPointDTO("Nov",1630),new DataPointDTO("Déc",1880));
            default        -> List.of(new DataPointDTO("Lun",410),new DataPointDTO("Mar",370),new DataPointDTO("Mer",440),new DataPointDTO("Jeu",400),new DataPointDTO("Ven",380),new DataPointDTO("Sam",300),new DataPointDTO("Dim",270));
        });
    }
}
