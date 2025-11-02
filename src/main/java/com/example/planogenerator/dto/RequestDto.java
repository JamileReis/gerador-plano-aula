package com.example.planogenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestDto {
    @NotBlank
    private String tema;

    @NotBlank
    private String nivel;

    @NotBlank
    private String idade;

    @NotNull
    private Integer duracao;

    @NotBlank
    private String bncc;

    private String recursos;
    public RequestDto() {
    }
    public RequestDto(String tema, String nivel, String idade, Integer duracao, String bncc, String recursos) {
        this.tema = tema;
        this.nivel = nivel;
        this.idade = idade;
        this.duracao = duracao;
        this.bncc = bncc;
        this.recursos = recursos;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public void setBncc(String bncc) {
        this.bncc = bncc;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }

    public String getTema() {
        return tema;
    }

    public String getNivel() {
        return nivel;
    }

    public String getIdade() {
        return idade;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public String getBncc() {
        return bncc;
    }

    public String getRecursos() {
        return recursos;
    }

    public Integer duracao() {
        return duracao;
    }

    public String tema() {
        return tema;
    }

    public String nivel() {
        return nivel;
    }

    public String idade() {
        return idade;
    }
}