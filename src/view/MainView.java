package view;

import repository.ClienteRepository;
import repository.SaborRepository;
import repository.PedidoRepository;

import javax.swing.*;

public class MainView extends JFrame {
    private ClienteRepository clienteRepository;
    private SaborRepository saborRepository;
    private PedidoRepository pedidoRepository;

    public MainView() {
        clienteRepository = new ClienteRepository();
        saborRepository = new SaborRepository();
        pedidoRepository = new PedidoRepository();

        setTitle("Pizzaria - Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnClientes = new JButton("Gerenciar Clientes");
        btnClientes.setBounds(100, 50, 200, 30);
        add(btnClientes);

        JButton btnSabores = new JButton("Gerenciar Sabores");
        btnSabores.setBounds(100, 100, 200, 30);
        add(btnSabores);

        JButton btnPedidos = new JButton("Gerenciar Pedidos");
        btnPedidos.setBounds(100, 150, 200, 30);
        add(btnPedidos);

        JButton btnPrecos = new JButton("Atualizar Preços");
        btnPrecos.setBounds(100, 200, 200, 30);
        add(btnPrecos);

        btnClientes.addActionListener(e -> new ClienteView(clienteRepository).setVisible(true));
        btnSabores.addActionListener(e -> new SaborView(saborRepository).setVisible(true));
        btnPedidos.addActionListener(e -> new PedidoView(clienteRepository, saborRepository, pedidoRepository).setVisible(true));
        btnPrecos.addActionListener(e -> new AtualizarPrecoView().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}
