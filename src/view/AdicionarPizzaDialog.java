package view;

import model.Pizza;
import model.Sabor;
import model.Forma;
import model.Circulo;
import model.Quadrado;
import model.Triangulo;
import repository.SaborRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdicionarPizzaDialog extends JDialog {
    private final PedidoView pedidoView;
    private final SaborRepository saborRepository;
    private JList<String> listaSabores;
    private DefaultListModel<String> listModel;
    private JButton btnAdicionar;
    private JComboBox<String> formaComboBox;
    private JTextField dimensaoField;

    public AdicionarPizzaDialog(PedidoView pedidoView, SaborRepository saborRepository) {
        this.pedidoView = pedidoView;
        this.saborRepository = saborRepository;

        setTitle("Adicionar Pizza");
        setSize(400, 300);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        carregarSabores();

        listaSabores = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listaSabores);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // ComboBox para escolher a forma da pizza
        panel.add(new JLabel("Forma:"));
        formaComboBox = new JComboBox<>(new String[] { "Círculo", "Quadrado", "Triângulo" });
        panel.add(formaComboBox);

        // Campo de texto para informar a dimensão da pizza
        panel.add(new JLabel("Dimensão (cm):"));
        dimensaoField = new JTextField();
        panel.add(dimensaoField);

        add(panel, BorderLayout.NORTH);

        btnAdicionar = new JButton("Adicionar ao Pedido");
        btnAdicionar.addActionListener(e -> adicionarPizza());
        add(btnAdicionar, BorderLayout.SOUTH);

        setLocationRelativeTo(pedidoView);
    }

    private void carregarSabores() {
        List<Sabor> sabores = saborRepository.listarSabores();
        for (Sabor sabor : sabores) {
            listModel.addElement(sabor.getNome());
        }
    }

    private void adicionarPizza() {
        List<String> saboresSelecionados = listaSabores.getSelectedValuesList();
        if (saboresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um sabor!");
            return;
        }

        // Obtém a dimensão informada pelo usuário
        String dimensaoTexto = dimensaoField.getText();
        double dimensao = 0;

        try {
            // Tenta converter o texto da dimensão para um número
            dimensao = Double.parseDouble(dimensaoTexto);
        } catch (NumberFormatException e) {
            // Caso ocorra erro na conversão, exibe uma mensagem de erro
            JOptionPane.showMessageDialog(this, "Dimensão inválida!");
            return;
        }

        Forma forma = null;
        String formaSelecionada = (String) formaComboBox.getSelectedItem();

        // Verifica se foi selecionada uma forma válida
        if (formaSelecionada == null || formaSelecionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escolha uma forma válida!");
            return;
        }

        try {
            // Cria a forma de acordo com a seleção do usuário
            switch (formaSelecionada) {
                case "Círculo":
                    forma = new Circulo(dimensao);
                    break;
                case "Quadrado":
                    forma = new Quadrado(dimensao);
                    break;
                case "Triângulo":
                    forma = new Triangulo(dimensao);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Forma inválida!");
                    return;
            }
        } catch (IllegalArgumentException e) {
            // Se ocorrer erro na criação da forma, exibe a mensagem de erro
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria a nova pizza com a forma, sabores e preço por cm²
        double precoPorCm2 = 5.00;  // Preço fixo por cm² (ajustável conforme necessário)
        Pizza novaPizza = new Pizza(forma, saboresSelecionados, precoPorCm2);

        // Chama o método para adicionar a pizza ao pedido
        pedidoView.adicionarPizzaAoPedido(novaPizza);

        // Fecha o dialog
        dispose();
    }
}
