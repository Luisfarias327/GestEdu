package br.com.gestedu;

public class Professor {

    private int id;
    private String nome;
    private String areaAtuacao;
    private String contato;

    public Professor() {
    }

    public Professor(int id, String nome, String areaAtuacao, String contato) {
        this.id = id;
        this.nome = nome;
        this.areaAtuacao = areaAtuacao;
        this.contato = contato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}