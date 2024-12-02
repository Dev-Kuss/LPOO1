package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import repository.*;

public class PedidoView extends JFrame {

    private JComboBox<String> formaComboBox;
    private JTextField dimensaoField, areaField;
    private JButton btnAdicionarPizza, btnSalvar, btnGerenciarPedidos;
    private JComboBox<String> clienteComboBox;
    private JPanel panel;

    private ClienteRepository clienteRepository;
    private SaborRepository saborRepository;
    private PedidoRepository pedidoRepository;
    private List<Pizza> pizzasNoPedido;
    private String clienteSelecionado;

    public PedidoView(ClienteRepository clienteRepository, SaborRepository saborRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.saborRepository = saborRepository;
        this.pedidoRepository = pedidoRepository;
        this.pizzasNoPedido = new ArrayList<>();

        setTitle("Adicionar Pizza");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Adicionando o comboBox para selecionar o cliente
        JLabel clienteLabel = new JLabel("Escolha o Cliente:");
        clienteComboBox = new JComboBox<>();
        carregarClientes();
        panel.add(clienteLabel);
        panel.add(clienteComboBox);

        JLabel formaLabel = new JLabel("Escolha a forma da pizza:");
        formaComboBox = new JComboBox<>(new String[] { "Círculo", "Quadrado", "Triângulo" });
        panel.add(formaLabel);
        panel.add(formaComboBox);

        JLabel dimensaoLabel = new JLabel("Informe as dimensões da pizza:");
        dimensaoField = new JTextField();
        panel.add(dimensaoLabel);
        panel.add(dimensaoField);

        JLabel areaLabel = new JLabel("Área da Pizza (cm²):");
        areaField = new JTextField();
        areaField.setEditable(false);
        panel.add(areaLabel);
        panel.add(areaField);

        btnAdicionarPizza = new JButton("Adicionar Pizza");
        btnAdicionarPizza.addActionListener(e -> {
            // Create a list of selected flavors
            List<String> saboresSelecionados = List.of("Sabor Exemplo 1", "Sabor Exemplo 2"); // Replace with actual selected flavors
            Forma forma = new Circulo(15); // Example: create a circle with radius 15
            double precoPorCm2 = 5.00; // Example price per cm²
            adicionarPizzaAoPedido(new Pizza(forma, saboresSelecionados, precoPorCm2));
        });
        panel.add(btnAdicionarPizza);

        btnSalvar = new JButton("Salvar Pedido");
        btnSalvar.addActionListener(e -> salvarPedido());
        panel.add(btnSalvar);

        // Botão para Gerenciar Pedidos
        btnGerenciarPedidos = new JButton("Gerenciar Pedidos");
        btnGerenciarPedidos.addActionListener(e -> gerenciarPedidos());
        panel.add(btnGerenciarPedidos);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void carregarClientes() {
        List<String> clientes = clienteRepository.listarClientes().stream()
                .map(c -> c.getNome())
                .toList();
        for (String cliente : clientes) {
            clienteComboBox.addItem(cliente);
        }
    }

    private void carregarSabores() {
        List<Sabor> sabores = saborRepository.listarSabores();
        for (Sabor sabor : sabores) {
            formaComboBox.addItem(sabor.getNome());
        }
    }

    private void gerenciarPedidos() {
        JFrame gerenciarPedidosFrame = new JFrame("Gerenciar Pedidos");
        gerenciarPedidosFrame.setSize(400, 300);
        gerenciarPedidosFrame.setLayout(new BorderLayout());

        JList<String> listaPedidos = new JList<>();
        DefaultListModel<String> pedidosListModel = new DefaultListModel<>();

        List<Pedido> pedidos = pedidoRepository.listarPedidos();
        for (Pedido pedido : pedidos) {
            pedidosListModel.addElement("Pedido ID: " + pedido.getId() + " - Cliente: " + pedido.getCliente().getNome() + " - Status: " + pedido.getStatus());
        }

        listaPedidos.setModel(pedidosListModel);
        gerenciarPedidosFrame.add(new JScrollPane(listaPedidos), BorderLayout.CENTER);

        // Botão de fechar
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> gerenciarPedidosFrame.dispose());
        gerenciarPedidosFrame.add(btnFechar, BorderLayout.SOUTH);

        // Botão para alterar o status
        JButton btnAlterarStatus = new JButton("Alterar Status");
        btnAlterarStatus.addActionListener(e -> alterarStatusPedido(listaPedidos.getSelectedValue()));
        gerenciarPedidosFrame.add(btnAlterarStatus, BorderLayout.NORTH);

        gerenciarPedidosFrame.setVisible(true);
    }

    private void alterarStatusPedido(String pedidoInfo) {
        if (pedidoInfo != null) {
            String[] partes = pedidoInfo.split(" - ");
            int pedidoId = Integer.parseInt(partes[0].replace("Pedido ID: ", ""));

            Pedido pedido = pedidoRepository.buscarPedidoPorId(pedidoId);

            if (pedido != null) {
                String novoStatus = JOptionPane.showInputDialog(this, "Digite o novo status (aberto, finalizado, cancelado):");
                if (novoStatus != null) {
                    try {
                        pedidoRepository.alterarEstadoPedido(pedido.getId(), novoStatus);
                        JOptionPane.showMessageDialog(this, "Status atualizado para: " + novoStatus);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void salvarPedido() {
        clienteSelecionado = (String) clienteComboBox.getSelectedItem();

        if (clienteSelecionado == null || clienteSelecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!");
            return;
        }

        Cliente cliente = new Cliente(clienteSelecionado, "email@exemplo.com", "12345678");
        Pedido novoPedido = pedidoRepository.criarPedido(cliente);

        pizzasNoPedido.forEach(novoPedido::adicionarPizza);

        pizzasNoPedido.clear();

        JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso para o cliente: " + clienteSelecionado);
    }

    // Agora o método recebe uma Pizza como parâmetro
    public void adicionarPizzaAoPedido(Pizza pizza) {
        pizzasNoPedido.add(pizza);
        JOptionPane.showMessageDialog(this, "Pizza adicionada ao pedido!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoView(new ClienteRepository(), new SaborRepository(), new PedidoRepository()));
    }
}
