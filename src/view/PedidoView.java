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
    private JPanel pedidoResumoPanel;

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
        setSize(800, 500);
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
                atualizarResumoPedido();
            }
        });
        panel.add(btnAdicionarPizza);

        btnSalvar = new JButton("Salvar Pedido");
        btnSalvar.addActionListener(e -> salvarPedido());
        panel.add(btnSalvar);

        btnGerenciarPedidos = new JButton("Gerenciar Pedidos");
        btnGerenciarPedidos.addActionListener(e -> gerenciarPedidos());
        panel.add(btnGerenciarPedidos);

        pedidoResumoPanel = new JPanel();
        pedidoResumoPanel.setLayout(new BoxLayout(pedidoResumoPanel, BoxLayout.Y_AXIS));
        pedidoResumoPanel.setBorder(BorderFactory.createTitledBorder("Resumo do Pedido"));

        add(panel, BorderLayout.WEST);
        add(new JScrollPane(pedidoResumoPanel), BorderLayout.CENTER);
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

    private void atualizarResumoPedido() {
        pedidoResumoPanel.removeAll();
        pedidoResumoPanel.add(new JLabel("Cliente: " + (clienteSelecionado != null ? clienteSelecionado : "Não selecionado")));
        pedidoResumoPanel.add(new JLabel("Pizzas no Pedido:"));
        double precoTotal = 0.0;
        for (Pizza pizza : pizzasNoPedido) {
            JPanel pizzaPanel = new JPanel();
            pizzaPanel.setLayout(new BoxLayout(pizzaPanel, BoxLayout.Y_AXIS));
            pizzaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            pizzaPanel.add(new JLabel("- Forma: " + pizza.getForma().getClass().getSimpleName()));
            pizzaPanel.add(new JLabel("  Área: " + formatarDecimal(pizza.getForma().calcularArea()) + " cm²"));
            pizzaPanel.add(new JLabel("  Sabores: " + pizza.getSabores().stream().map(Sabor::getNome).toList()));
            pizzaPanel.add(new JLabel("  Preço: R$" + formatarDecimal(pizza.calcularPreco())));
            pedidoResumoPanel.add(pizzaPanel);
            precoTotal += pizza.calcularPreco();
        }
        pedidoResumoPanel.add(new JLabel("Preço Total: R$" + formatarDecimal(precoTotal)));
        pedidoResumoPanel.revalidate();
        pedidoResumoPanel.repaint();
    }


    private void gerenciarPedidos() {
    JFrame gerenciarPedidosFrame = new JFrame("Gerenciar Pedidos");
    gerenciarPedidosFrame.setSize(600, 400);
    gerenciarPedidosFrame.setLayout(new BorderLayout());

    DefaultListModel<String> pedidosListModel = new DefaultListModel<>();
    JList<String> listaPedidos = new JList<>(pedidosListModel);

    List<Pedido> pedidos = pedidoRepository.listarPedidos();
    for (Pedido pedido : pedidos) {
        pedidosListModel.addElement("Pedido ID: " + pedido.getId() + " - Cliente: " + pedido.getCliente().getNome() + " - Status: " + pedido.getStatus());
    }

    JPanel detalhePanel = new JPanel();
    detalhePanel.setLayout(new BoxLayout(detalhePanel, BoxLayout.Y_AXIS));
    JTextArea detalheArea = new JTextArea();
    detalheArea.setEditable(false);
    detalheArea.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido"));
    detalhePanel.add(new JScrollPane(detalheArea));

    listaPedidos.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            String selectedValue = listaPedidos.getSelectedValue();
            if (selectedValue != null) {
                int pedidoId = Integer.parseInt(selectedValue.split(" - ")[0].replace("Pedido ID: ", ""));
                Pedido pedido = pedidoRepository.buscarPedidoPorId(pedidoId);
                if (pedido != null) {
                    StringBuilder detalhes = new StringBuilder();
                    detalhes.append("Cliente: ").append(pedido.getCliente().getNome()).append("\n");
                    detalhes.append("Status: ").append(pedido.getStatus()).append("\n\n");
                    detalhes.append("Pizzas:\n");
                    double precoTotal = 0.0;
                    for (Pizza pizza : pedido.getPizzas()) {
                        detalhes.append("- Forma: ").append(pizza.getForma().getClass().getSimpleName());
                        detalhes.append(", Área: ").append(formatarDecimal(pizza.getForma().calcularArea())).append(" cm²");
                        detalhes.append(", Sabores: ");
                        detalhes.append(pizza.getSabores().stream().map(Sabor::getNome).toList());
                        detalhes.append(", Preço: R$").append(formatarDecimal(pizza.calcularPreco())).append("\n");
                        precoTotal += pizza.calcularPreco();
                    }
                    detalhes.append("\nPreço Total: R$").append(formatarDecimal(precoTotal));
                    detalheArea.setText(detalhes.toString());
                }
            }
        }
    });

    gerenciarPedidosFrame.add(new JScrollPane(listaPedidos), BorderLayout.WEST);
    gerenciarPedidosFrame.add(detalhePanel, BorderLayout.CENTER);

    JButton btnFechar = new JButton("Fechar");
    btnFechar.addActionListener(e -> gerenciarPedidosFrame.dispose());
    gerenciarPedidosFrame.add(btnFechar, BorderLayout.SOUTH);

    JButton btnAlterarStatus = new JButton("Alterar Status");
    btnAlterarStatus.addActionListener(e -> {
        String selectedValue = listaPedidos.getSelectedValue();
        if (selectedValue != null) {
            alterarStatusPedido(selectedValue);
        } else {
            JOptionPane.showMessageDialog(gerenciarPedidosFrame, "Selecione um pedido para alterar o status.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    });
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
        atualizarResumoPedido();
        JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso para o cliente: " + clienteSelecionado);
    }

    public void adicionarPizzaAoPedido(Pizza pizza) {
        pizzasNoPedido.add(pizza);
        JOptionPane.showMessageDialog(this, "Pizza adicionada ao pedido!");
    }
    
    private String formatarDecimal(double valor) {
    return String.format("%.2f", valor);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoView(new ClienteRepository(), new SaborRepository(), new PedidoRepository()));
    }
}
