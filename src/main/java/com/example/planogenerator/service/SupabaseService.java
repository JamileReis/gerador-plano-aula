package com.example.planogenerator.service;
import com.example.planogenerator.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.http.*;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
@Service
public class SupabaseService {
  ObjectMapper mapper = new ObjectMapper();
  String url = System.getenv("SUPABASE_URL");
  String key = System.getenv("SUPABASE_KEY");
  public void savePlan(ResponseDto plan) throws Exception {
    String body = mapper.writeValueAsString(new java.util.HashMap<String,Object>(){{
      put("tema", plan.tema);
      put("nivel", plan.nivel);
      put("idade", plan.idade);
      put("introducao", plan.introducao);
      put("objetivo_bncc", plan.objetivoBNCC);
      put("passo_a_passo", plan.passoAPasso);
      put("rubrica", plan.rubrica);
    }});
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url + "/rest/v1/planos_aula"))
      .timeout(Duration.ofSeconds(20))
      .header("Content-Type","application/json")
      .header("apikey", key)
      .header("Authorization", "Bearer "+key)
      .POST(BodyPublishers.ofString(body))
      .build();
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (resp.statusCode() >= 400) throw new RuntimeException(resp.body());
  }
}