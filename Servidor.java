// Subclasse de Usuario: servidor pode manter até 4 empréstimos ativos.
public class Servidor extends Usuario {
    public Servidor(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public int getLimiteEmprestimos() {
        return 4;
    }
}
