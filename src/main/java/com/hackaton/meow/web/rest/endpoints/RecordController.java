package com.hackaton.meow.web.rest.endpoints;

import com.hackaton.meow.domain.Record;
import com.hackaton.meow.management.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class RecordController {

    private RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

    @GetMapping("/water")
    public ResponseEntity<List<com.hackaton.meow.domain.Record>> getWater(@RequestParam(defaultValue = "jour") String periode) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime then;

        switch (periode) {
            case "heure" -> then = now.minusHours(1);
            case "semaine" -> then = now.minusWeeks(1);
            case "mois" -> then = now.minusMonths(1);
            default -> then = now;
        }

        return ResponseEntity.ok(service.getWater(then, now, 50));
    }

    @GetMapping("/gaz")
    public ResponseEntity<List<com.hackaton.meow.domain.Record>> getGaz(@RequestParam(defaultValue = "jour") String periode) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime then;

        switch (periode) {
            case "heure" -> then = now.minusHours(1);
            case "semaine" -> then = now.minusWeeks(1);
            case "mois" -> then = now.minusMonths(1);
            default -> then = now;
        }

        return ResponseEntity.ok(service.getGaz(then, now, 50));
    }

    @GetMapping("/electricity")
    public ResponseEntity<List<Record>> getElectricity(@RequestParam(defaultValue = "jour") String periode) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime then;

        switch (periode) {
            case "heure" -> then = now.minusHours(1);
            case "semaine" -> then = now.minusWeeks(1);
            case "mois" -> then = now.minusMonths(1);
            default -> then = now;
        }

        return ResponseEntity.ok(service.getElectricity(then, now, 50));
    }

}
