package model;

public class Quadrado extends Forma {
    private double lado;

    public Quadrado(double lado) {
        if (lado < 10 || lado > 40) {
            throw new IllegalArgumentException("O lado deve estar entre 10 e 40 cm.");
        }
        this.lado = lado;
    }

    @Override
    public double calcularArea() {
        return lado * lado;
    }

    public double getLado() {
        return lado;
    }

    public void setLado(double lado) {
        if (lado < 10 || lado > 40) {
            throw new IllegalArgumentException("O lado deve estar entre 10 e 40 cm.");
        }
        this.lado = lado;
    }
}
