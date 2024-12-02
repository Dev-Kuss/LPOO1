
import javax.swing.*;

import view.MainView;

public class App {
    public static void main(String[] args) {
        System.out.println("Bem-vindo à Pizzaria!");

        // Abre o menu principal com interface gráfica
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView(); 
            mainView.setVisible(true);         
        });
    }
}