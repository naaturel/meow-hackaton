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
public class EauResource {

    private final RecordService recordService;

    public EauResource(RecordService service) {
        this.recordService = service;
    }


}
