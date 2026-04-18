package com.hackaton.meow.web.rest.endpoints;

import com.hackaton.meow.management.RecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class RecordController {

    private RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

}
