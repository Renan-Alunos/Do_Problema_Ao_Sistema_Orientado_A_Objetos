import java.util.ArrayList;
import java.util.List;

// Guarda todos os empréstimos, tanto os ativos quanto os já devolvidos.
public class Historico {
    private final List<Emprestimo> registros = new ArrayList<>();

    public void adicionar(Emprestimo emprestimo) { registros.add(emprestimo); }

    // Acesso interno usado pelo sistema para pesquisar empréstimos.
    List<Emprestimo> registrosInternos() { return registros; }

    public void mostrarTodos() {
        if (registros.isEmpty()) {
            System.out.println("Historico vazio.");
            return;
        }
        for (Emprestimo e : registros) System.out.println(e);
    }
}
