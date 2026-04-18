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
public class EauResource {

    @GetMapping("/eau/consommation")
    public ResponseEntity<List<DataPointDTO>> getConsommation(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",120),new DataPointDTO("04h",75),new DataPointDTO("08h",150),new DataPointDTO("12h",350),new DataPointDTO("16h",300),new DataPointDTO("20h",280));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",12500),new DataPointDTO("Sem 2",13200),new DataPointDTO("Sem 3",11800),new DataPointDTO("Sem 4",12900));
            case "mois"    -> List.of(new DataPointDTO("Jan",52000),new DataPointDTO("Fév",48000),new DataPointDTO("Mar",50000),new DataPointDTO("Avr",55000),new DataPointDTO("Mai",60000),new DataPointDTO("Jun",65000),new DataPointDTO("Jul",70000),new DataPointDTO("Aoû",68000),new DataPointDTO("Sep",60000),new DataPointDTO("Oct",54000),new DataPointDTO("Nov",50000),new DataPointDTO("Déc",48000));
            default        -> List.of(new DataPointDTO("Lun",1800),new DataPointDTO("Mar",1650),new DataPointDTO("Mer",1900),new DataPointDTO("Jeu",2100),new DataPointDTO("Ven",2000),new DataPointDTO("Sam",1400),new DataPointDTO("Dim",1200));
        });
    }

    @GetMapping("/eau/debit")
    public ResponseEntity<List<DataPointDTO>> getDebit(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",8),new DataPointDTO("04h",5),new DataPointDTO("08h",10),new DataPointDTO("12h",22),new DataPointDTO("16h",19),new DataPointDTO("20h",18));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",11),new DataPointDTO("Sem 2",13),new DataPointDTO("Sem 3",10),new DataPointDTO("Sem 4",12));
            case "mois"    -> List.of(new DataPointDTO("Jan",10),new DataPointDTO("Fév",9),new DataPointDTO("Mar",10),new DataPointDTO("Avr",11),new DataPointDTO("Mai",12),new DataPointDTO("Jun",13),new DataPointDTO("Jul",14),new DataPointDTO("Aoû",13),new DataPointDTO("Sep",12),new DataPointDTO("Oct",11),new DataPointDTO("Nov",10),new DataPointDTO("Déc",9));
            default        -> List.of(new DataPointDTO("Lun",12),new DataPointDTO("Mar",10),new DataPointDTO("Mer",14),new DataPointDTO("Jeu",16),new DataPointDTO("Ven",15),new DataPointDTO("Sam",9),new DataPointDTO("Dim",8));
        });
    }

    @GetMapping("/eau/repartition")
    public ResponseEntity<List<DataPointDTO>> getRepartition(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(List.of(
            new DataPointDTO("Résidentiel", 42),
            new DataPointDTO("Industriel",  22),
            new DataPointDTO("Agriculture", 22),
            new DataPointDTO("Commercial",  14)
        ));
    }

    @GetMapping("/eau/evolution")
    public ResponseEntity<List<DataPointDTO>> getEvolution(@RequestParam(defaultValue = "jour") String periode) {
        // TODO: remplacer par vraie source de données
        return ResponseEntity.ok(switch (periode) {
            case "heure"   -> List.of(new DataPointDTO("00h",115),new DataPointDTO("04h",72),new DataPointDTO("08h",145),new DataPointDTO("12h",340),new DataPointDTO("16h",290),new DataPointDTO("20h",272));
            case "semaine" -> List.of(new DataPointDTO("Sem 1",12200),new DataPointDTO("Sem 2",12900),new DataPointDTO("Sem 3",11500),new DataPointDTO("Sem 4",12600));
            case "mois"    -> List.of(new DataPointDTO("Jan",50000),new DataPointDTO("Fév",46000),new DataPointDTO("Mar",48000),new DataPointDTO("Avr",53000),new DataPointDTO("Mai",58000),new DataPointDTO("Jun",63000),new DataPointDTO("Jul",68000),new DataPointDTO("Aoû",66000),new DataPointDTO("Sep",58000),new DataPointDTO("Oct",52000),new DataPointDTO("Nov",48000),new DataPointDTO("Déc",46000));
            default        -> List.of(new DataPointDTO("Lun",1750),new DataPointDTO("Mar",1600),new DataPointDTO("Mer",1850),new DataPointDTO("Jeu",2050),new DataPointDTO("Ven",1950),new DataPointDTO("Sam",1350),new DataPointDTO("Dim",1150));
        });
    }
}
