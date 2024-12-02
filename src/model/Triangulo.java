package model;


public class Triangulo extends Forma {
    private double lado;

    public Triangulo(double lado) {
        if (lado < 20 || lado > 60) {
            throw new IllegalArgumentException("O lado deve estar entre 20 e 60 cm.");
        }
        this.lado = lado;
    }

    @Override
    public double calcularArea() {
        return (Math.sqrt(3) / 4) * lado * lado;
    }

    public double getLado() {
        return lado;
    }

    public void setLado(double lado) {
        if (lado < 20 || lado > 60) {
            throw new IllegalArgumentException("O lado deve estar entre 20 e 60 cm.");
        }
        this.lado = lado;
    }
}
