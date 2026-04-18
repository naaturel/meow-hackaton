package com.hackaton.meow.management;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.hackaton.meow.domain.ChatRequest.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private static final String DEPLOYMENT = "gpt-5.4-nano";
    private static final String ENDPOINT = "https://helmo-hackathon-resource.cognitiveservices.azure.com/";

    private static final String SYSTEM_PROMPT = """
        Tu es un assistant intelligent intégré dans un tableau de bord IoT de supervision.
        Tu aides les utilisateurs à comprendre leurs données de capteurs : électricité, gaz,
        température, eau, vent, qualité de l'air et capteurs industriels.
        Réponds en français, de façon concise et claire.
        Si des données de capteurs sont fournies dans le contexte, utilise-les pour répondre
        avec précision aux questions de l'utilisateur.
        """;

    @Value("${azure.openai.api-key:}")
    private String apiKey;

    public String chat(List<Message> userMessages, String context) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Clé API Azure OpenAI non configurée. Ajoutez `azure.openai.api-key` dans application.yml.";
        }

        String systemContent = SYSTEM_PROMPT;
        if (context != null && !context.isBlank()) {
            systemContent += "\n\nDONNÉES CAPTEURS EN TEMPS RÉEL :\n" + context;
        }

        try {
            OpenAIClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(ENDPOINT)
                .buildClient();

            List<ChatRequestMessage> messages = new ArrayList<>();
            messages.add(new ChatRequestSystemMessage(systemContent));
            for (Message m : userMessages) {
                if (m.role().equals("user")) {
                    messages.add(new ChatRequestUserMessage(m.content()));
                } else {
                    messages.add(new ChatRequestAssistantMessage(m.content()));
                }
            }

            ChatCompletions completions = client.getChatCompletions(
                DEPLOYMENT, new ChatCompletionsOptions(messages));

            return completions.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "Erreur lors de l'appel à Azure OpenAI : " + e.getMessage();
        }
    }
}
