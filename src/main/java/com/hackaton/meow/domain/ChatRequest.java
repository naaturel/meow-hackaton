package com.hackaton.meow.domain;

import java.util.List;

public record ChatRequest(List<Message> messages, String context) {
    public record Message(String role, String content) {}
}
