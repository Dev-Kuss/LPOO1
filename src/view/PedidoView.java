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
    private JPanel saboresPanel;
    private List<JCheckBox> checkboxesSabores;

    public PedidoView(ClienteRepository clienteRepository, SaborRepository saborRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.saborRepository = saborRepository;
        this.pedidoRepository = pedidoRepository;
        this.pizzasNoPedido = new ArrayList<>();

        setTitle("Adicionar Pizza");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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

        saboresPanel = new JPanel();
        saboresPanel.setLayout(new BoxLayout(saboresPanel, BoxLayout.Y_AXIS));
        saboresPanel.setBorder(BorderFactory.createTitledBorder("Selecione até dois sabores"));
        checkboxesSabores = new ArrayList<>();
        carregarSabores();
        panel.add(saboresPanel);

        btnAdicionarPizza = new JButton("Adicionar Pizza");
        btnAdicionarPizza.addActionListener(e -> {
            Forma forma = criarForma();
            if (forma != null) {
                List<Sabor> saboresSelecionados = new ArrayList<>();
                for (JCheckBox checkBox : checkboxesSabores) {
                    if (checkBox.isSelected()) {
                        String nomeSabor = checkBox.getText().split(" - ")[0];
                        saboresSelecionados.add(saborRepository.listarSabores().stream()
                                .filter(s -> s.getNome().equals(nomeSabor))
                                .findFirst()
                                .orElse(null));
                    }
                }
                if (saboresSelecionados.size() > 2) {
                    JOptionPane.showMessageDialog(this, "Uma pizza pode ter no máximo dois sabores.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (saboresSelecionados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecione pelo menos um sabor para a pizza.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double precoPorCm2 = 5.00;
                Pizza pizza = new Pizza(forma, saboresSelecionados, precoPorCm2);
                adicionarPizzaAoPedido(pizza);
            }
        });
        panel.add(btnAdicionarPizza);

        btnSalvar = new JButton("Salvar Pedido");
        btnSalvar.addActionListener(e -> salvarPedido());
        panel.add(btnSalvar);

        btnGerenciarPedidos = new JButton("Gerenciar Pedidos");
        btnGerenciarPedidos.addActionListener(e -> gerenciarPedidos());
        panel.add(btnGerenciarPedidos);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void carregarClientes() {
        List<String> clientes = clienteRepository.listarClientes().stream()
                .map(Cliente::getNome)
                .toList();
        for (String cliente : clientes) {
            clienteComboBox.addItem(cliente);
        }
    }

    private void carregarSabores() {
        List<Sabor> sabores = saborRepository.listarSabores();
        for (Sabor sabor : sabores) {
            JCheckBox checkBox = new JCheckBox(sabor.getNome() + " - " + sabor.getTipo());
            checkboxesSabores.add(checkBox);
            saboresPanel.add(checkBox);
        }
    }

    private Forma criarForma() {
        String formaSelecionada = (String) formaComboBox.getSelectedItem();
        String dimensaoTexto = dimensaoField.getText().trim();
        if (dimensaoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe as dimensões da pizza.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            double dimensao = Double.parseDouble(dimensaoTexto);
            switch (formaSelecionada) {
                case "Círculo":
                    return new Circulo(dimensao);
                case "Quadrado":
                    return new Quadrado(dimensao);
                case "Triângulo":
                    return new Triangulo(dimensao);
                default:
                    JOptionPane.showMessageDialog(this, "Forma inválida selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, informe um valor numérico válido para as dimensões.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
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
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> gerenciarPedidosFrame.dispose());
        gerenciarPedidosFrame.add(btnFechar, BorderLayout.SOUTH);
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

    public void adicionarPizzaAoPedido(Pizza pizza) {
        pizzasNoPedido.add(pizza);
        JOptionPane.showMessageDialog(this, "Pizza adicionada ao pedido!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoView(new ClienteRepository(), new SaborRepository(), new PedidoRepository()));
    }
}
