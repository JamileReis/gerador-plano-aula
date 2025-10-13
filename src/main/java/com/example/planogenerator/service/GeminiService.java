package com.example.planogenerator.service;
import com.example.planogenerator.dto.RequestDto;
import com.example.planogenerator.dto.ResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.http.*;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
@Service
public class GeminiService {
  ObjectMapper mapper = new ObjectMapper();
  String apiKey = System.getenv("GEMINI_API_KEY");
  String model = System.getenv().getOrDefault("GEMINI_MODEL","gemini-2.5-pro");
  public ResponseDto generatePlan(RequestDto req) throws Exception {
    String prompt = buildPrompt(req);
    String body = mapper.createObjectNode()
      .put("model", model)
      .set("input", mapper.createObjectNode().put("text", prompt))
      .toString();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.generativeai.google/v1beta2/models/"+model+":generate"))
      .timeout(Duration.ofSeconds(30))
      .header("Content-Type","application/json")
      .header("Authorization","Bearer "+apiKey)
      .POST(BodyPublishers.ofString(body))
      .build();
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (resp.statusCode() >= 400) throw new RuntimeException(resp.body());
    JsonNode root = mapper.readTree(resp.body());
    String text = extractText(root);
    JsonNode parsed = tryParseJson(text);
    ResponseDto out = new ResponseDto();
    if (parsed != null) {
      out.introducao = parsed.path("introducao").asText(null);
      out.objetivoBNCC = parsed.path("objetivoBNCC").asText(null);
      out.passoAPasso = parsed.path("passoAPasso").asText(null);
      out.rubrica = parsed.path("rubrica").asText(null);
    } else {
      out.introducao = text;
    }
    out.tema = req.tema;
    out.nivel = req.nivel;
    out.idade = req.idade;
    return out;
  }
  String buildPrompt(RequestDto req) throws Exception {
    String schema = "{\n  \"type\":\"object\",\n  \"properties\":{\n    \"introducao\":{\"type\":\"string\"},\n    \"objetivoBNCC\":{\"type\":\"string\"},\n    \"passoAPasso\":{\"type\":\"string\"},\n    \"rubrica\":{\"type\":\"string\"}\n  },\n  \"required\":[\"introducao\",\"objetivoBNCC\",\"passoAPasso\",\"rubrica\"]\n}\n";
    String p = "Gere um plano de aula em JSON seguindo este schema: " + schema + "\nTema: " + req.tema + "\nNivel: " + req.nivel + "\nIdade: " + req.idade + "\nDuração: " + req.duracao + " minutos\nBNCC: " + req.bncc + "\nRecursos: " + (req.recursos==null?"":req.recursos) + "\nResposta: apenas o JSON";
    return p;
  }
  String extractText(JsonNode root) {
    if (root.has("candidates") && root.path("candidates").isArray()) return root.path("candidates").get(0).path("content").path(0).path("text").asText();
    if (root.has("output")) return root.path("output").toString();
    return root.toString();
  }
  JsonNode tryParseJson(String text) {
    try { return mapper.readTree(text); } catch (Exception e) { return null; }
  }
}