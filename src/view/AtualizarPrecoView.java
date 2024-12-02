package view;

import model.PrecoConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtualizarPrecoView extends JFrame {
    private final JTextField txtSimples;
    private final JTextField txtEspecial;
    private final JTextField txtPremium;
    private final JButton btnSalvar;

    public AtualizarPrecoView() {
        setTitle("Atualizar Preço por cm²");
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Preço por cm² (Simples):"));
        txtSimples = new JTextField();
        txtSimples.setText(String.valueOf(PrecoConfig.getPrecoSimples()));  
        add(txtSimples);

        add(new JLabel("Preço por cm² (Especial):"));
        txtEspecial = new JTextField();
        txtEspecial.setText(String.valueOf(PrecoConfig.getPrecoEspecial()));  
        add(txtEspecial);

        add(new JLabel("Preço por cm² (Premium):"));
        txtPremium = new JTextField();
        txtPremium.setText(String.valueOf(PrecoConfig.getPrecoPremium()));  
        add(txtPremium);

        btnSalvar = new JButton("Salvar");
        add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPrecos();
            }
        });

        setVisible(true);
    }

    private void salvarPrecos() {
        try {
            double precoSimples = Double.parseDouble(txtSimples.getText());
            double precoEspecial = Double.parseDouble(txtEspecial.getText());
            double precoPremium = Double.parseDouble(txtPremium.getText());

            PrecoConfig.setPrecoSimples(precoSimples);
            PrecoConfig.setPrecoEspecial(precoEspecial);
            PrecoConfig.setPrecoPremium(precoPremium);

            JOptionPane.showMessageDialog(this, "Preços atualizados com sucesso!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Insira valores numéricos válidos.");
        }
    }
}
