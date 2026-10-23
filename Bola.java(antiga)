import java.time.LocalDateTime;
import java.time.LocalTime;

// Subclasse de Equipamento: bola tem prazo até o fim do dia e guarda a modalidade.
public class Bola extends Equipamento {
    private final String modalidade;

    public Bola(int codigo, String modalidade) {
        super(codigo, "Bola de " + modalidade);
        if (modalidade == null || modalidade.trim().isEmpty()) throw new IllegalArgumentException("Modalidade invalida.");
        this.modalidade = modalidade.trim();
    }

    public String getModalidade() { return modalidade; }

    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public boolean exigeVistoria() { return false; }
}
