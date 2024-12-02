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

        panel.add(new JLabel("Forma:"));
        formaComboBox = new JComboBox<>(new String[] { "Círculo", "Quadrado", "Triângulo" });
        panel.add(formaComboBox);

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
        List<Sabor> saboresSelecionados = saborRepository.listarSabores();
        if (saboresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um sabor!");
            return;
        }

        String dimensaoTexto = dimensaoField.getText();
        double dimensao = 0;

        try {
            dimensao = Double.parseDouble(dimensaoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dimensão inválida!");
            return;
        }

        Forma forma = null;
        String formaSelecionada = (String) formaComboBox.getSelectedItem();

        if (formaSelecionada == null || formaSelecionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escolha uma forma válida!");
            return;
        }

        try {
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precoPorCm2 = 5.00;
        Pizza novaPizza = new Pizza(forma, saboresSelecionados, precoPorCm2);

        pedidoView.adicionarPizzaAoPedido(novaPizza);

        dispose();
    }
}
