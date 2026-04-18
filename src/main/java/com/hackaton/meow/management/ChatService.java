package com.hackaton.meow.management;

import com.hackaton.meow.domain.ChatRequest.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SYSTEM_PROMPT = """
        Tu es un assistant intelligent intégré dans un tableau de bord IoT de supervision.
        Tu aides les utilisateurs à comprendre leurs données de capteurs : électricité, gaz,
        température, eau, vent, qualité de l'air et capteurs industriels.
        Réponds en français, de façon concise et claire.
        Si des données de capteurs sont fournies dans le contexte, utilise-les pour répondre
        avec précision aux questions de l'utilisateur.
        """;

    @Value("${openai.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(List<Message> userMessages, String context) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Clé API OpenAI non configurée. Ajoutez `openai.api-key` dans application.yml.";
        }

        String systemContent = SYSTEM_PROMPT;
        if (context != null && !context.isBlank()) {
            systemContent += "\n\nDONNÉES CAPTEURS EN TEMPS RÉEL :\n" + context;
        }

        List<Map<String, String>> messages = new java.util.ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemContent));
        for (Message m : userMessages) {
            messages.add(Map.of("role", m.role(), "content", m.content()));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                OPENAI_URL, new HttpEntity<>(body, headers), Map.class);

            List choices = (List) response.getBody().get("choices");
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Erreur lors de l'appel à OpenAI : " + e.getMessage();
        }
    }
}
