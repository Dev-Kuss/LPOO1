package repository;

import java.util.ArrayList;
import java.util.List;

import model.Sabor;

public class SaborRepository {
    private List<Sabor> sabores;

    public SaborRepository() {
        this.sabores = new ArrayList<>();
        sabores.add(new Sabor("Mussarela", "Simples"));
        sabores.add(new Sabor("Calabresa", "Simples"));
        sabores.add(new Sabor("Frango com Catupiry", "Especial"));
        sabores.add(new Sabor("Quatro Queijos", "Premium"));
    }

    public void adicionarSabor(Sabor sabor) {
        sabores.add(sabor);
    }

    public void editarSabor(int index, Sabor sabor) {
        if (index < 0 || index >= sabores.size()) {
            throw new IllegalArgumentException("Índice inválido para edição.");
        }
        sabores.set(index, sabor);
    }

    public void excluirSabor(int index) {
        if (index < 0 || index >= sabores.size()) {
            throw new IllegalArgumentException("Índice inválido para exclusão.");
        }
        sabores.remove(index);
    }

    public List<Sabor> listarSabores() {
        return new ArrayList<>(sabores);
    }
}
