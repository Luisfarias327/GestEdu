
package br.com.gestedu;

public class Turma {

    private int id;
    private String nome;
    private int anoLetivo;

    public Turma() {
    }

    public Turma(int id, String nome, int anoLetivo) {
        this.id = id;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
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

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }
}