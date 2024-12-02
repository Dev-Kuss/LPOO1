package view;

import repository.SaborRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import model.Sabor;

public class SaborView extends JFrame {
    private JTextField txtNomeSabor;
    private JComboBox<String> comboTipo;
    private JTable tableSabores;
    private DefaultTableModel model;
    private SaborRepository saborRepository;

    public SaborView(SaborRepository saborRepository) {
        this.saborRepository = saborRepository;

        setTitle("Gerenciamento de Sabores");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome do Sabor:");
        lblNome.setBounds(10, 10, 100, 25);
        add(lblNome);

        txtNomeSabor = new JTextField();
        txtNomeSabor.setBounds(120, 10, 200, 25);
        add(txtNomeSabor);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(10, 40, 100, 25);
        add(lblTipo);

        comboTipo = new JComboBox<>(new String[]{"Simples", "Especial", "Premium"});
        comboTipo.setBounds(120, 40, 200, 25);
        add(comboTipo);

        JButton btnAdd = new JButton("Adicionar");
        btnAdd.setBounds(340, 10, 100, 25);
        add(btnAdd);

        JButton btnEdit = new JButton("Editar");
        btnEdit.setBounds(340, 40, 100, 25);
        add(btnEdit);

        JButton btnDelete = new JButton("Excluir");
        btnDelete.setBounds(340, 70, 100, 25);
        add(btnDelete);

        model = new DefaultTableModel(new String[]{"Nome", "Tipo"}, 0);
        tableSabores = new JTable(model);
        JScrollPane scroll = new JScrollPane(tableSabores);
        scroll.setBounds(10, 110, 560, 200);
        add(scroll);

        btnAdd.addActionListener(e -> adicionarSabor());
        btnEdit.addActionListener(e -> editarSabor());
        btnDelete.addActionListener(e -> excluirSabor());

        carregarTabela();
    }

    private void carregarTabela() {
        model.setRowCount(0);
        List<Sabor> sabores = saborRepository.listarSabores();
        for (Sabor sabor : sabores) {
            model.addRow(new Object[]{sabor.getNome(), sabor.getTipo()});
        }
    }

    private void adicionarSabor() {
        String nome = txtNomeSabor.getText();
        String tipo = (String) comboTipo.getSelectedItem();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do sabor!");
            return;
        }
        saborRepository.adicionarSabor(new Sabor(nome, tipo));
        carregarTabela();
        txtNomeSabor.setText("");
    }

    private void editarSabor() {
        int selectedRow = tableSabores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um sabor para editar!");
            return;
        }
        String nome = txtNomeSabor.getText();
        String tipo = (String) comboTipo.getSelectedItem();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do sabor!");
            return;
        }
        saborRepository.editarSabor(selectedRow, new Sabor(nome, tipo));
        carregarTabela();
    }

    private void excluirSabor() {
        int selectedRow = tableSabores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um sabor para excluir!");
            return;
        }
        saborRepository.excluirSabor(selectedRow);
        carregarTabela();
    }
}
