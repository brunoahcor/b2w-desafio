package br.com.b2w.desafio.vo;

public class PlanetaVo {

    private String id;
    private String nome;
    private String clima;
    private String terreno;
    private Integer aparicoes;

    public PlanetaVo(){}

    public PlanetaVo(String nome, String clima, String terreno){
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

    public PlanetaVo(String id, String nome, String clima, String terreno){
        this.id = id;
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return this.clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return this.terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public Integer getAparicoes() {
        return this.aparicoes;
    }

    public void setAparicoes(Integer aparicoes) {
        this.aparicoes = aparicoes;
    }
    
}