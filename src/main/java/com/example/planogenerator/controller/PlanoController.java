package com.example.planogenerator.controller;
import com.example.planogenerator.service.GeminiService;
import com.example.planogenerator.service.SupabaseService;
import com.example.planogenerator.dto.RequestDto;
import com.example.planogenerator.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api")
public class PlanoController {
  @Autowired
  GeminiService geminiService;
  @Autowired
  SupabaseService supabaseService;
  @PostMapping("/generate")
  public ResponseEntity<?> generate(@Valid @RequestBody RequestDto req) throws Exception {
    ResponseDto response = geminiService.generatePlan(req);
    supabaseService.savePlan(response);
    return ResponseEntity.ok(response);
  }
}