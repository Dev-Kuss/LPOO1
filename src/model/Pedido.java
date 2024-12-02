package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contadorId = 1;
    private int id;
    private Cliente cliente;
    private List<Pizza> pizzas;
    private String status;

    public Pedido(Cliente cliente) {
        this.id = contadorId++;  
        this.cliente = cliente;
        this.pizzas = new ArrayList<>();
        this.status = "aberto"; 
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || (!status.equals("aberto") && !status.equals("finalizado") && !status.equals("cancelado"))) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: 'aberto', 'finalizado', 'cancelado'.");
        }
        this.status = status;
    }
    public double calcularPrecoTotal() {
        return pizzas.stream().mapToDouble(Pizza::calcularPreco).sum();
    }
    public void adicionarPizza(Pizza pizza) {
        if (pizza != null && !pizzas.contains(pizza)) {
            pizzas.add(pizza);
        }
    }

    public void removerPizza(Pizza pizza) {
        pizzas.remove(pizza);
    }
    
    public List<String> listarSabores() {
        List<String> sabores = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            sabores.addAll(pizza.getSabores());
        }
        return sabores;
    }
}
