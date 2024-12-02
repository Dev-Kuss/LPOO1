package view;

import repository.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Pedido;

public class PedidoStatusView extends JFrame {
    private PedidoRepository pedidoRepository;
    private JTable pedidosTable;
    private DefaultTableModel tableModel;

    public PedidoStatusView(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        setTitle("Gestão de Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID Pedido", "Cliente", "Estado", "Total"}, 0);
        pedidosTable = new JTable(tableModel);
        add(new JScrollPane(pedidosTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnAlterarStatus = new JButton("Alterar Status");
        btnAlterarStatus.addActionListener(e -> alterarStatusPedido());
        buttonPanel.add(btnAlterarStatus);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarPedidos();
        setVisible(true);
    }

private void carregarPedidos() {
    pedidoRepository.listarPedidos().forEach(pedido -> {
        tableModel.addRow(new Object[]{
            pedido.getId(),
            pedido.getCliente().getNome(),
            pedido.getStatus(),
            pedido.calcularPrecoTotal()
        });
    });
}

 private void alterarStatusPedido() {
    int selectedRow = pedidosTable.getSelectedRow();
    if (selectedRow != -1) {
        int idPedido = (int) tableModel.getValueAt(selectedRow, 0);
        Pedido pedido = pedidoRepository.buscarPedidoPorId(idPedido);

        if (pedido != null) {
            if ("aberto".equals(pedido.getStatus())) {
                pedido.setStatus("finalizado");
            } else {
                pedido.setStatus("aberto");
            }
            tableModel.setValueAt(pedido.getStatus(), selectedRow, 2);
        }
    }
}

}
