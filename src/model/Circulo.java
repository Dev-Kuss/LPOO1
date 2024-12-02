package model;

public class Circulo extends Forma {
    private double raio;

    public Circulo(double raio) {
        if (raio < 7 || raio > 23) {
            throw new IllegalArgumentException("O raio deve estar entre 7 e 23 cm.");
        }
        this.raio = raio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        if (raio < 7 || raio > 23) {
            throw new IllegalArgumentException("O raio deve estar entre 7 e 23 cm.");
        }
        this.raio = raio;
    }
}
