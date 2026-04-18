package com.hackaton.meow.web.rest.endpoints;

import com.hackaton.meow.management.ChatService;
import com.hackaton.meow.domain.ChatRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request.messages(), request.context());
    }
}
