// Subclasse de Usuario: aluno pode manter até 2 empréstimos ativos.
public class Aluno extends Usuario {
    public Aluno(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public int getLimiteEmprestimos() {
        return 2;
    }
}
