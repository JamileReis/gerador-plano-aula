package com.example.planogenerator.service;

import com.example.planogenerator.dto.RequestDto;
import com.example.planogenerator.dto.ResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String model = "gemini-2.5-flash";

    public GeminiService(ObjectMapper mapper, WebClient.Builder webClientBuilder) {
        this.mapper = mapper;
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }
    public Mono<ResponseDto> generatePlan(RequestDto req) throws Exception {
        String prompt = buildPrompt(req);
        String payload = buildGeminiPayload(prompt);

        return webClient.post()
                // Uso da chave na URI, que foi corrigida para o endpoint generateContent
                .uri("/v1beta/models/" + model + ":generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::extractText)
                .map(this::tryParseJson)
                .map(node -> {
                    // Mapeia o JSON parsado para o DTO de resposta usando Getters
                    return new ResponseDto(
                            node.path("introducao").asText(),
                            node.path("objetivoBNCC").asText(),
                            mapper.convertValue(node.path("passoAPasso"), mapper.getTypeFactory().constructCollectionType(List.class, String.class)),
                            mapper.convertValue(node.path("rubrica"), mapper.getTypeFactory().constructCollectionType(List.class, Map.class)),
                            req.getTema(), req.getNivel(), req.getIdade(), req.getDuracao()
                    );
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Erro na comunicação com Gemini ou parsing: " + e.getMessage())));
    }

    String buildPrompt(RequestDto req) throws Exception {
        String schema = "{\n  \"type\":\"object\",\n  \"properties\":{\n    \"introducao\":{\"type\":\"string\"},\n    \"objetivoBNCC\":{\"type\":\"string\"},\n    \"passoAPasso\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\n    \"rubrica\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"properties\":{\"criterio\":{\"type\":\"string\"},\"peso\":{\"type\":\"string\"}}}}\n  },\n  \"required\":[\"introducao\",\"objetivoBNCC\",\"passoAPasso\",\"rubrica\"]\n}\n";
        String p = "Gere um plano de aula em JSON seguindo este schema: " + schema + "\nTema: " + req.getTema() + "\nNivel: " + req.getNivel() + "\nIdade: " + req.getIdade() + "\nDuração: " + req.getDuracao() + " minutos\nBNCC: " + req.getBncc() + "\nRecursos: " + (req.getRecursos()==null?"":req.getRecursos()) + "\nResposta: apenas o JSON";
        return p;
    }

    String buildGeminiPayload(String prompt) {
        return String.format("""
            {
              "contents": [{
                "parts": [{"text": "%s"}]
              }],
              "config": {
                "temperature": 0.7,
                "responseMimeType": "application/json"
              }
            }
            """, prompt.replace("\"", "\\\"").replace("\n", "\\n"));
    }

    String extractText(JsonNode root) {
        JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
        if (textNode.isTextual()) return textNode.asText();
        return root.toString();
    }

    JsonNode tryParseJson(String text) {
        try {
            return mapper.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter resposta da IA para JSON: " + text, e);
        }
    }
}