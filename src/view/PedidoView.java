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
        formaComboBox = new JComboBox<>();
        carregarSabores(); // Load existing flavors into formaComboBox
        panel.add(formaLabel);
        panel.add(formaComboBox);

        btnAdicionarPizza = new JButton("Adicionar Pizza");
        btnAdicionarPizza.addActionListener(e -> abrirTelaAdicionarPizza());
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

    // Método para alterar o status do pedido
    private void alterarStatusPedido(String pedidoInfo) {
        if (pedidoInfo != null) {
            // Obtendo o ID do pedido a partir da string de info (ex: "Pedido ID: 1 - Cliente: João - Status: aberto")
            String[] partes = pedidoInfo.split(" - ");
            int pedidoId = Integer.parseInt(partes[0].replace("Pedido ID: ", ""));

            // Encontrando o pedido correspondente
            Pedido pedido = pedidoRepository.buscarPedidoPorId(pedidoId);

            if (pedido != null) {
                // Atualizando o status
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

    // Método para salvar o pedido
    private void salvarPedido() {
        clienteSelecionado = (String) clienteComboBox.getSelectedItem();

        if (clienteSelecionado == null || clienteSelecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!");
            return;
        }

        // Criando o cliente com base na seleção
        Cliente cliente = new Cliente(clienteSelecionado, "email@exemplo.com", "12345678");

        // Criando o pedido no repositório
        Pedido novoPedido = pedidoRepository.criarPedido(cliente);

        // Adicionando pizzas ao pedido
        pizzasNoPedido.forEach(novoPedido::adicionarPizza);

        // Limpar as pizzas após salvar o pedido
        pizzasNoPedido.clear();

        JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso para o cliente: " + clienteSelecionado);
    }

    // Método para adicionar a pizza ao pedido
    public void adicionarPizzaAoPedido(Pizza pizza) {
        pizzasNoPedido.add(pizza);
        JOptionPane.showMessageDialog(this, "Pizza adicionada ao pedido!");
    }

    // Método para abrir a tela de adicionar pizza
    private void abrirTelaAdicionarPizza() {
        // Criando a janela para adicionar pizza
        JFrame adicionarPizzaFrame = new JFrame("Adicionar Pizza");
        adicionarPizzaFrame.setSize(300, 200);
        adicionarPizzaFrame.setLayout(new BorderLayout());

        // Campo para o sabor da pizza
        JTextField saborField = new JTextField();
        adicionarPizzaFrame.add(new JLabel("Digite o sabor da pizza:"), BorderLayout.NORTH);
        adicionarPizzaFrame.add(saborField, BorderLayout.CENTER);

        // Botão para adicionar pizza
        JButton btnAdicionar = new JButton("Adicionar Pizza");
        btnAdicionar.addActionListener(e -> {
            String sabor = saborField.getText();
            if (sabor.isEmpty()) {
                JOptionPane.showMessageDialog(adicionarPizzaFrame, "O sabor não pode ser vazio!");
            } else {
                // Criando uma nova pizza com a forma, lista de sabores e preço
                Forma forma = new Circulo(10); // Exemplo de forma
                List<String> sabores = new ArrayList<>();
                sabores.add(sabor); // Adiciona o sabor digitado à lista

                double precoPorCm2 = 5.00; // Exemplo de preço por cm²
                Pizza novaPizza = new Pizza(forma, sabores, precoPorCm2); // Use o construtor correto
                adicionarPizzaAoPedido(novaPizza);
                adicionarPizzaFrame.dispose();  // Fecha a janela
            }
        });
        adicionarPizzaFrame.add(btnAdicionar, BorderLayout.SOUTH);

        adicionarPizzaFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoView(new ClienteRepository(), new SaborRepository(), new PedidoRepository()));
    }
}
