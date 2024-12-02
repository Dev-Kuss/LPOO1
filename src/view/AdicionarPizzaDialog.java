package view;

import model.Pizza;
import model.Sabor;
import model.Forma;
import model.Circulo;
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

        Forma forma = new Circulo(10);  // Aqui você pode escolher outra forma, como Quadrado ou Triangulo

        // Definindo o preço por cm²
        double precoPorCm2 = 5.00;  // Exemplo: Preço por cm²

        Pizza novaPizza = new Pizza(forma, saboresSelecionados, precoPorCm2);
        pedidoView.adicionarPizzaAoPedido(novaPizza);
        dispose();
    }
}
