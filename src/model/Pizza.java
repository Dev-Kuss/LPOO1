package model;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private Forma forma;
    private List<Sabor> sabores;
    private double precoPorCm2;

    public Pizza(Forma forma, List<Sabor> saboresSelecionados, double precoPorCm2) {
        if (saboresSelecionados.size() > 2) {
            throw new IllegalArgumentException("Uma pizza pode ter no máximo dois sabores.");
        }
        this.forma = forma;
        this.sabores = new ArrayList<Sabor>(saboresSelecionados); // Specify the type parameter here
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

    public List<Sabor> getSabores() {
        return sabores;
    }

    public void setSabores(List<Sabor> sabores) {
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
