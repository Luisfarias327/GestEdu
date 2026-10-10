
package br.com.gestedu;

public class NotaTrimestral {

    private int id;
    private double nota;
    private int trimestre;

    public NotaTrimestral() {
    }

    public NotaTrimestral(int id, double nota, int trimestre) {
        this.id = id;
        this.nota = nota;
        this.trimestre = trimestre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(int trimestre) {
        this.trimestre = trimestre;
    }

    public double calcularMedia() {
        return nota;
    }
}