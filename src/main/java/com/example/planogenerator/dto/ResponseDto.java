package com.example.planogenerator.dto;


import java.util.List;
import java.util.Map;

public class ResponseDto {
    private String introducao;
    private String objetivoBNCC;
    private List<String> passoAPasso;
    private List<Map<String, String>> rubrica;
    private String tema;
    private String nivel;
    private String idade;
    private Integer duracao;

    public ResponseDto() {}

    public ResponseDto(String introducao, String objetivoBNCC, List<String> passoAPasso, List<Map<String, String>> rubrica, String tema, String nivel, String idade, Integer duracao) {
        this.introducao = introducao;
        this.objetivoBNCC = objetivoBNCC;
        this.passoAPasso = passoAPasso;
        this.rubrica = rubrica;
        this.tema = tema;
        this.nivel = nivel;
        this.idade = idade;
        this.duracao = duracao;
    }

    public void setIntroducao(Object introducao) {
        this.introducao = (String) introducao;
    }

    public void setObjetivoBNCC(String objetivoBNCC) {
        this.objetivoBNCC = objetivoBNCC;
    }

    public void setPassoAPasso(List<String> passoAPasso) {
        this.passoAPasso = passoAPasso;
    }

    public void setRubrica(List<Map<String, String>> rubrica) {
        this.rubrica = rubrica;
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

    public String getIntroducao() {
        return introducao;
    }

    public String getObjetivoBNCC() {
        return objetivoBNCC;
    }

    public List<String> getPassoAPasso() {
        return passoAPasso;
    }

    public List<Map<String, String>> getRubrica() {
        return rubrica;
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


}

