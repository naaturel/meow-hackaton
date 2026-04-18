package com.hackaton.meow.web.rest.endpoints;

import com.hackaton.meow.management.SSEService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api")
public class SSEController {

    private final SSEService sseService;

    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("/sse/subscribe")
    public SseEmitter subscribe() {
        return sseService.subscribe();
    }
}
