import java.time.LocalDateTime;

// Subclasse de Equipamento: colete deve ser devolvido até o fim do mesmo dia.
public class Colete extends Equipamento {
    public Colete(int codigo) { super(codigo, "Colete"); }

    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.toLocalDate().atTime(java.time.LocalTime.MAX);
    }

    @Override
    public boolean exigeVistoria() { return false; }
}
