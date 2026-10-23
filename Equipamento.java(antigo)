import java.time.LocalDateTime;

// Superclasse abstrata de todos os equipamentos emprestáveis.
public abstract class Equipamento {
    // Atributos comuns a todos os equipamentos.
    private final int codigo;
    private final String nome;
    private boolean disponivel = true;

    public Equipamento(int codigo, String nome) {
        if (codigo <= 0) throw new IllegalArgumentException("Codigo do equipamento deve ser positivo.");
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome do equipamento nao pode ficar vazio.");
        this.codigo = codigo;
        this.nome = nome.trim();
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public boolean isDisponivel() { return disponivel; }

    public abstract LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada);
    public abstract boolean exigeVistoria();

    public void marcarEmprestado() {
        if (!disponivel) throw new IllegalStateException("Equipamento ja esta emprestado.");
        disponivel = false;
    }

    public void marcarDisponivel() { disponivel = true; }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | " + nome + " | " + (disponivel ? "Disponivel" : "Emprestado");
    }
}
