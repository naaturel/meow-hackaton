package com.hackaton.meow.web.rest.endpoints;

import java.util.List;

import com.hackaton.meow.web.rest.DataPointDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GazResource {

    @GetMapping("/gaz/consommation")
    public ResponseEntity<List<DataPointDTO>> getConsommation(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",95),new DataPointDTO("04h",70),new DataPointDTO("08h",120),new DataPointDTO("12h",210),new DataPointDTO("16h",185),new DataPointDTO("20h",200));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",9800),new DataPointDTO("Sem 2",10500),new DataPointDTO("Sem 3",9200),new DataPointDTO("Sem 4",10100));
            case "mois"    -> List.of(new DataPointDTO("Jan",42000),new DataPointDTO("Fév",38000),new DataPointDTO("Mar",32000),new DataPointDTO("Avr",22000),new DataPointDTO("Mai",14000),new DataPointDTO("Jun",9000),new DataPointDTO("Jul",7500),new DataPointDTO("Aoû",8000),new DataPointDTO("Sep",15000),new DataPointDTO("Oct",26000),new DataPointDTO("Nov",35000),new DataPointDTO("Déc",44000));
            default        -> List.of(new DataPointDTO("Lun",1400),new DataPointDTO("Mar",1250),new DataPointDTO("Mer",1350),new DataPointDTO("Jeu",1500),new DataPointDTO("Ven",1450),new DataPointDTO("Sam",900),new DataPointDTO("Dim",800));
        });
    }

    @GetMapping("/gaz/pression")
    public ResponseEntity<List<DataPointDTO>> getPression(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",4.2),new DataPointDTO("04h",3.9),new DataPointDTO("08h",4.5),new DataPointDTO("12h",5.8),new DataPointDTO("16h",5.4),new DataPointDTO("20h",5.7));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",4.6),new DataPointDTO("Sem 2",5.1),new DataPointDTO("Sem 3",4.8),new DataPointDTO("Sem 4",5.2));
            case "mois"    -> List.of(new DataPointDTO("Jan",5.5),new DataPointDTO("Fév",5.3),new DataPointDTO("Mar",5.0),new DataPointDTO("Avr",4.8),new DataPointDTO("Mai",4.5),new DataPointDTO("Jun",4.2),new DataPointDTO("Jul",4.0),new DataPointDTO("Aoû",4.1),new DataPointDTO("Sep",4.5),new DataPointDTO("Oct",5.0),new DataPointDTO("Nov",5.2),new DataPointDTO("Déc",5.6));
            default        -> List.of(new DataPointDTO("Lun",4.8),new DataPointDTO("Mar",4.5),new DataPointDTO("Mer",5.0),new DataPointDTO("Jeu",5.3),new DataPointDTO("Ven",5.1),new DataPointDTO("Sam",4.2),new DataPointDTO("Dim",4.0));
        });
    }

    @GetMapping("/gaz/repartition")
    public ResponseEntity<List<DataPointDTO>> getRepartition(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("Résidentiel", 38),
            new DataPointDTO("Industriel",  25),
            new DataPointDTO("Chauffage",   26),
            new DataPointDTO("Commercial",  11)
        ));
    }

    @GetMapping("/gaz/evolution")
    public ResponseEntity<List<DataPointDTO>> getEvolution(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",92),new DataPointDTO("04h",68),new DataPointDTO("08h",116),new DataPointDTO("12h",204),new DataPointDTO("16h",180),new DataPointDTO("20h",194));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",9500),new DataPointDTO("Sem 2",10200),new DataPointDTO("Sem 3",8900),new DataPointDTO("Sem 4",9800));
            case "mois"    -> List.of(new DataPointDTO("Jan",40000),new DataPointDTO("Fév",36000),new DataPointDTO("Mar",30000),new DataPointDTO("Avr",20000),new DataPointDTO("Mai",12000),new DataPointDTO("Jun",7500),new DataPointDTO("Jul",6000),new DataPointDTO("Aoû",6500),new DataPointDTO("Sep",13000),new DataPointDTO("Oct",24000),new DataPointDTO("Nov",33000),new DataPointDTO("Déc",42000));
            default        -> List.of(new DataPointDTO("Lun",1360),new DataPointDTO("Mar",1210),new DataPointDTO("Mer",1310),new DataPointDTO("Jeu",1460),new DataPointDTO("Ven",1410),new DataPointDTO("Sam",870),new DataPointDTO("Dim",770));
        });
    }
}
