package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import repository.*;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteView extends JFrame {
    private JTextField txtNome, txtSobrenome, txtTelefone, txtFiltro;
    private JTable tableClientes;
    private DefaultTableModel model;
    private ClienteRepository clienteRepository;
    private PedidoRepository pedidoRepository;

    public ClienteView(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;


        setTitle("Gerenciamento de Clientes");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 10, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(110, 10, 200, 25);
        add(txtNome);

        JLabel lblSobrenome = new JLabel("Sobrenome:");
        lblSobrenome.setBounds(10, 40, 100, 25);
        add(lblSobrenome);

        txtSobrenome = new JTextField();
        txtSobrenome.setBounds(110, 40, 200, 25);
        add(txtSobrenome);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(10, 70, 100, 25);
        add(lblTelefone);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(110, 70, 200, 25);
        add(txtTelefone);

        JButton btnAdd = new JButton("Adicionar");
        btnAdd.setBounds(320, 10, 100, 25);
        add(btnAdd);

        JButton btnEdit = new JButton("Editar");
        btnEdit.setBounds(320, 40, 100, 25);
        add(btnEdit);

        JButton btnDelete = new JButton("Excluir");
        btnDelete.setBounds(320, 70, 100, 25);
        add(btnDelete);

        JLabel lblFiltro = new JLabel("Filtro (Sobrenome/Telefone):");
        lblFiltro.setBounds(10, 110, 200, 25);
        add(lblFiltro);

        txtFiltro = new JTextField();
        txtFiltro.setBounds(220, 110, 200, 25);
        add(txtFiltro);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(430, 110, 100, 25);
        add(btnFiltrar);

        model = new DefaultTableModel(new String[]{"Nome", "Sobrenome", "Telefone"}, 0);
        tableClientes = new JTable(model);
        JScrollPane scroll = new JScrollPane(tableClientes);
        scroll.setBounds(10, 150, 660, 300);
        add(scroll);

        btnAdd.addActionListener(e -> adicionarCliente());
        btnEdit.addActionListener(e -> editarCliente());
        btnDelete.addActionListener(e -> excluirCliente());
        btnFiltrar.addActionListener(e -> filtrarClientes());

        atualizarTabela();
    }

    private void atualizarTabela() {
        model.setRowCount(0); // Limpa a tabela
        for (Cliente cliente : clienteRepository.listarClientes()) {
            model.addRow(new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getTelefone()});
        }
    }

    private void adicionarCliente() {
        String nome = txtNome.getText();
        String sobrenome = txtSobrenome.getText();
        String telefone = txtTelefone.getText();
        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }
        Cliente cliente = new Cliente(nome, sobrenome, telefone);
        clienteRepository.adicionarCliente(cliente);
        atualizarTabela();
        txtNome.setText("");
        txtSobrenome.setText("");
        txtTelefone.setText("");
    }

    private void editarCliente() {
        int selectedRow = tableClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar!");
            return;
        }
        String telefoneAtual = (String) model.getValueAt(selectedRow, 2);
        Cliente cliente = clienteRepository.listarClientes().stream()
                .filter(c -> c.getTelefone().equals(telefoneAtual))
                .findFirst()
                .orElse(null);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
            return;
        }

        String nome = txtNome.getText();
        String sobrenome = txtSobrenome.getText();
        String telefone = txtTelefone.getText();
        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }
        cliente.setNome(nome);
        cliente.setSobrenome(sobrenome);
        cliente.setTelefone(telefone);
        atualizarTabela();
    }

    private void excluirCliente() {
    int selectedRow = tableClientes.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!");
        return;
    }
    String telefone = (String) model.getValueAt(selectedRow, 2);
    Cliente cliente = clienteRepository.listarClientes().stream()
            .filter(c -> c.getTelefone().equals(telefone))
            .findFirst()
            .orElse(null);
    if (cliente != null) {
        // Exclui os pedidos do cliente
        pedidoRepository.removerPedidosPorCliente(cliente);
        // Exclui o cliente
        clienteRepository.removerCliente(cliente);
        JOptionPane.showMessageDialog(this, "Cliente e seus pedidos foram excluídos com sucesso!");
    } else {
        JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
    }
    atualizarTabela();
}

    private void filtrarClientes() {
        String filtro = txtFiltro.getText().toLowerCase();
        if (filtro.isEmpty()) {
            atualizarTabela();
            return;
        }

        List<Cliente> filtrados = clienteRepository.listarClientes().stream()
                .filter(cliente -> cliente.getSobrenome().toLowerCase().contains(filtro) || cliente.getTelefone().contains(filtro))
                .collect(Collectors.toList());

        model.setRowCount(0); // Limpa a tabela
        for (Cliente cliente : filtrados) {
            model.addRow(new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getTelefone()});
        }
    }

    public static void main(String[] args) {
    ClienteRepository clienteRepository = new ClienteRepository();
    PedidoRepository pedidoRepository = new PedidoRepository();
    SwingUtilities.invokeLater(() -> new ClienteView(clienteRepository, pedidoRepository).setVisible(true));
}
}
