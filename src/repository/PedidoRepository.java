package repository;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private final List<Pedido> pedidos;

    public PedidoRepository() {
        this.pedidos = new ArrayList<>();
    }

    public List<Pedido> listarPedidos() {
        return pedidos;
    }

    public Pedido buscarPedidoPorId(int id) {
        return pedidos.stream()
                .filter(pedido -> pedido.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Pedido buscarPedidoPorCliente(Cliente cliente) {
        return pedidos.stream()
                .filter(pedido -> pedido.getCliente().equals(cliente) && pedido.getStatus().equals("aberto"))
                .findFirst()
                .orElse(null);
    }

    public Pedido criarPedido(Cliente cliente) {
        Pedido novoPedido = new Pedido(cliente);
        pedidos.add(novoPedido);
        return novoPedido;
    }

    private int gerarIdPedido() {
        return pedidos.size() + 1;
    }

    public void removerPedido(Pedido pedido) {
        pedidos.remove(pedido);
    }

    public void adicionarPizzaAoPedido(int idPedido, Pizza pizza) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido != null) {
            pedido.adicionarPizza(pizza);
        }
    }

    public void alterarEstadoPedido(int idPedido, String novoEstado) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido != null) {
            pedido.setStatus(novoEstado); 
        }
    }

    public double calcularTotalPedido(int idPedido) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido != null) {
            return pedido.calcularPrecoTotal();
        }
        return 0.0;
    }
    
    public void removerPedidosPorCliente(Cliente cliente) {
    pedidos.removeIf(pedido -> pedido.getCliente().equals(cliente));
}
}
