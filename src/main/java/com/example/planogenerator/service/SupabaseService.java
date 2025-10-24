package com.example.planogenerator.service;

import com.example.planogenerator.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SupabaseService {

    private final WebClient.Builder webClientBuilder;

    @Value("${supabase.url}")
    private String url;

    @Value("${supabase.service-role-key}")
    private String serviceRoleKey;

    public SupabaseService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Void> savePlan(ResponseDto plan) {
        String userIdMock = "00000000-0000-0000-0000-000000000000";

        Map<String, Object> payload = Map.of(
                "user_id", userIdMock,
                "tema", plan.getTema(),
                "nivel", plan.getNivel(),
                "idade", plan.getIdade(),
                "duracao", plan.getDuracao(),
                "introducao", plan.getIntroducao(),
                "objetivo_bncc", plan.getObjetivoBNCC(),
                "passo_a_passo", plan.getPassoAPasso(),
                "rubrica", plan.getRubrica()
        );

        System.out.println("Enviando para Supabase: " + payload);

        WebClient serviceClient = webClientBuilder
                .baseUrl(url + "/rest/v1")
                .defaultHeader("apikey", serviceRoleKey)
                .defaultHeader("Authorization", "Bearer " + serviceRoleKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Prefer", "return=representation")
                .build();

        return serviceClient.post()
                .uri("/planos_aula")
                .bodyValue(payload)
                .retrieve()
                // Tratamento de erros para status 4xx e 5xx
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    System.err.println(" Erro 4xx do Supabase. Status: " + response.statusCode() + ". Corpo: " + body);
                                    return Mono.error(new RuntimeException("Erro Supabase (Status " + response.statusCode() + "): " + body));
                                })
                )
                .onStatus(status -> status.is5xxServerError(), response ->
                        Mono.error(new RuntimeException("Erro 5xx Supabase: falha no servidor Supabase."))
                )
                .bodyToMono(Void.class)
                .doOnSuccess(v -> System.out.println("Persistência no Supabase OK. Status 2xx"))
                .then();
    }
}