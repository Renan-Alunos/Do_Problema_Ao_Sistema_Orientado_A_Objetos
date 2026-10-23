import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Superclasse abstrata dos usuários: Aluno e Servidor herdam desta classe.
public abstract class Usuario {
    // Atributos comuns a todos os usuários.
    private String nome;
    private String matricula;
    private LocalDate dataFimBloqueio;
    private final List<Emprestimo> emprestimosAtivos = new ArrayList<>();

    public Usuario(String nome, String matricula) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ficar vazio.");
        }
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador nao pode ficar vazio.");
        }
        this.nome = nome.trim();
        this.matricula = matricula.trim();
    }

    public abstract int getLimiteEmprestimos();

    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }
    public LocalDate getDataFimBloqueio() { return dataFimBloqueio; }
    public int getQuantidadeEmprestimosAtivos() { return emprestimosAtivos.size(); }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome nao pode ficar vazio.");
        this.nome = nome.trim();
    }

    public void setMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) throw new IllegalArgumentException("Identificador nao pode ficar vazio.");
        this.matricula = matricula.trim();
    }

    public boolean estaBloqueado() {
        return dataFimBloqueio != null && LocalDate.now().isBefore(dataFimBloqueio.plusDays(1));
    }

    public void bloquearPorDias(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("O bloqueio deve ser de pelo menos um dia.");
        LocalDate fim = LocalDate.now().plusDays(dias);
        if (dataFimBloqueio == null || fim.isAfter(dataFimBloqueio)) dataFimBloqueio = fim;
    }

    public void verificarPodeEmprestar() {
        if (estaBloqueado()) {
            throw new IllegalStateException("Usuario bloqueado ate " + dataFimBloqueio + ".");
        }
        if (emprestimosAtivos.size() >= getLimiteEmprestimos()) {
            throw new IllegalStateException("Limite de emprestimos simultaneos atingido.");
        }
    }

    public void adicionarEmprestimoAtivo(Emprestimo emprestimo) {
        emprestimosAtivos.add(emprestimo);
    }

    public void removerEmprestimoAtivo(Emprestimo emprestimo) {
        emprestimosAtivos.remove(emprestimo);
    }

    public void mostrarEmprestimosAtivos() {
        if (emprestimosAtivos.isEmpty()) {
            System.out.println("Nenhum emprestimo ativo.");
            return;
        }
        for (Emprestimo e : emprestimosAtivos) System.out.println(e);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " | " + nome + " | Identificador: " + matricula
                + " | Ativos: " + emprestimosAtivos.size() + "/" + getLimiteEmprestimos()
                + (estaBloqueado() ? " | BLOQUEADO ate " + dataFimBloqueio : "");
    }
}
