package com.example.planogenerator.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class RequestDto {
  @NotBlank
  public String tema;
  @NotBlank
  public String nivel;
  @NotBlank
  public String idade;
  @NotNull
  public Integer duracao;
  @NotBlank
  public String bncc;
  public String recursos;
}