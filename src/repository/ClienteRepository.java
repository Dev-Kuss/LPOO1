package repository;

import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteRepository {
    private List<Cliente> clientes;

    public ClienteRepository() {
        this.clientes = new ArrayList<>();
        clientes.add(new Cliente("João", "Rua A, 123", "99999-9999"));
        clientes.add(new Cliente("Maria", "Rua B, 456", "98888-8888"));
        clientes.add(new Cliente("Carlos", "Rua C, 789", "97777-7777"));
        
    }

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void removerCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }
}
