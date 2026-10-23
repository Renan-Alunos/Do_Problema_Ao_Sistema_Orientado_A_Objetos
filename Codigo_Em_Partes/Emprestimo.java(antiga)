import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Representa uma transação de empréstimo entre um usuário e um equipamento.
public class Emprestimo {
    // Atributos que descrevem o empréstimo e seu estado.
    private final int codigo;
    private final Usuario usuario;
    private final Equipamento equipamento;
    private final LocalDateTime dataEmprestimo;
    private final LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private boolean devolvido;
    private boolean equipamentoDanificado;
    private boolean vistoriaAprovada = true;

    public Emprestimo(int codigo, Usuario usuario, Equipamento equipamento, LocalDateTime dataEmprestimo) {
        this.codigo = codigo;
        this.usuario = usuario;
        this.equipamento = equipamento;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = equipamento.calcularPrazoDevolucao(dataEmprestimo);
    }

    public int getCodigo() { return codigo; }
    public Usuario getUsuario() { return usuario; }
    public Equipamento getEquipamento() { return equipamento; }
    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public LocalDateTime getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDateTime getDataDevolucao() { return dataDevolucao; }
    public boolean isDevolvido() { return devolvido; }
    public boolean isEquipamentoDanificado() { return equipamentoDanificado; }
    public boolean isVistoriaAprovada() { return vistoriaAprovada; }

    public boolean isAtrasado() {
        LocalDateTime referencia = devolvido ? dataDevolucao : LocalDateTime.now();
        return referencia.isAfter(dataPrevistaDevolucao);
    }

    public void registrarDevolucao(boolean danificado, boolean vistoriaAprovada) {
        if (devolvido) throw new IllegalStateException("Este emprestimo ja foi devolvido.");
        if (!vistoriaAprovada) danificado = true;
        this.dataDevolucao = LocalDateTime.now();
        this.devolvido = true;
        this.equipamentoDanificado = danificado;
        this.vistoriaAprovada = vistoriaAprovada;
    }

    @Override
    public String toString() {
        return "Emprestimo #" + codigo + " | " + equipamento.getNome() + " (codigo " + equipamento.getCodigo() + ")"
                + " | Usuario: " + usuario.getNome() + " [" + usuario.getMatricula() + "]"
                + " | Retirada: " + dataEmprestimo.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " | Prevista: " + dataPrevistaDevolucao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " | " + (devolvido ? "Devolvido em " + dataDevolucao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "ATIVO")
                + (equipamentoDanificado ? " | DANO REGISTRADO" : "");
    }
}
