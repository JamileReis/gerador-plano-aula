package com.example.planogenerator.service;

import com.example.planogenerator.dto.RequestDto;
import com.example.planogenerator.dto.ResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
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

        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<ResponseDto> generatePlan(RequestDto req) {
        return Mono.fromCallable(() -> {
                    String prompt = buildPrompt(req);
                    String payload = buildGeminiPayload(prompt);
                    return payload;
                })
                .flatMap(payload ->
                        webClient.post()
                                .uri("/v1beta/models/" + model + ":generateContent?key=" + apiKey)
                                .bodyValue(payload)
                                .retrieve()
                                .onStatus(status -> status.isError(), response ->
                                        response.bodyToMono(String.class)
                                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                                        "Erro Gemini API: " + response.statusCode() + " - " + errorBody
                                                )))
                                )
                                .bodyToMono(JsonNode.class)
                                .timeout(Duration.ofSeconds(60))
                                .retry(3)
                )
                .map(this::extractText)
                .map(this::tryParseJson)
                .map(node -> new ResponseDto(
                        node.path("introducao").asText(),
                        node.path("objetivoBNCC").asText(),
                        mapper.convertValue(node.path("passoAPasso"),
                                mapper.getTypeFactory().constructCollectionType(List.class, String.class)),
                        mapper.convertValue(node.path("rubrica"),
                                mapper.getTypeFactory().constructCollectionType(List.class, Map.class)),
                        req.getTema(), req.getNivel(), req.getIdade(), req.getDuracao()
                ))
                .onErrorResume(e -> {
                    System.err.println("Erro no GeminiService: " + e.getMessage());
                    return Mono.error(new RuntimeException("Erro na comunicação com Gemini: " + e.getMessage()));
                });
    }

    private String buildPrompt(RequestDto req) throws Exception {
        String schema = """
            {
              "type":"object",
              "properties":{
                "introducao":{"type":"string"},
                "objetivoBNCC":{"type":"string"},
                "passoAPasso":{"type":"array","items":{"type":"string"}},
                "rubrica":{"type":"array","items":{"type":"object","properties":{"criterio":{"type":"string"},"peso":{"type":"string"}}}}
              },
              "required":["introducao","objetivoBNCC","passoAPasso","rubrica"]
            }""";

        return "Gere um plano de aula em JSON seguindo este schema: " + schema +
                "\nTema: " + req.getTema() +
                "\nNivel: " + req.getNivel() +
                "\nIdade: " + req.getIdade() +
                "\nDuração: " + req.getDuracao() + " minutos" +
                "\nBNCC: " + req.getBncc() +
                "\nRecursos: " + (req.getRecursos() == null ? "" : req.getRecursos()) +
                "\nResposta: apenas o JSON";
    }
    private String buildGeminiPayload(String prompt) {
        try {
            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                    "generationConfig", Map.of(
                            "temperature", 0.7,
                            "responseMimeType", "application/json"
                    )
            );
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao gerar payload JSON", e);
        }
    }
    private String extractText(JsonNode root) {
        JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
        if (textNode.isTextual()) return textNode.asText();
        return root.toString();
    }
    private JsonNode tryParseJson(String text) {
        try {
            text = text
                    .replaceAll("(?i)```json", "")
                    .replaceAll("(?i)```", "")
                    .trim();

            return mapper.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter resposta da IA para JSON: " + text, e);
        }
    }
}