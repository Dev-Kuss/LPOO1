package model;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private Forma forma;
    private List<String> sabores;
    private double precoPorCm2;

    public Pizza(Forma forma, List<String> sabores, double precoPorCm2) {
        if (sabores.size() > 2) {
            throw new IllegalArgumentException("Uma pizza pode ter no máximo dois sabores.");
        }
        this.forma = forma;
        this.sabores = new ArrayList<>(sabores);
        this.precoPorCm2 = precoPorCm2;
    }

    public double calcularPreco() {
        return forma.calcularArea() * precoPorCm2;
    }

    public Forma getForma() {
        return forma;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
    }

    public List<String> getSabores() {
        return sabores;
    }

    public void setSabores(List<String> sabores) {
        if (sabores.size() > 2) {
            throw new IllegalArgumentException("Uma pizza pode ter no máximo dois sabores.");
        }
        this.sabores = sabores;
    }

    public double getPrecoPorCm2() {
        return precoPorCm2;
    }

    public void setPrecoPorCm2(double precoPorCm2) {
        this.precoPorCm2 = precoPorCm2;
    }
}
