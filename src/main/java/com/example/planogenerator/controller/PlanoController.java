package com.example.planogenerator.controller;

import com.example.planogenerator.service.GeminiService;
import com.example.planogenerator.service.SupabaseService;
import com.example.planogenerator.dto.RequestDto;
import com.example.planogenerator.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PlanoController {
    @Autowired
    GeminiService geminiService;

    @Autowired
    SupabaseService supabaseService;

    @PostMapping("/gerador")
    public Mono<ResponseEntity<ResponseDto>> generate(@Valid @RequestBody RequestDto req) throws Exception {
        return geminiService.generatePlan(req)
                .flatMap(response -> supabaseService.savePlan(response)
                        .thenReturn(ResponseEntity.ok(response)));
    }
}