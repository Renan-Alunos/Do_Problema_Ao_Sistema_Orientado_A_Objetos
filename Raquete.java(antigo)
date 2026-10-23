import java.time.LocalDateTime;

// Subclasse de Equipamento: raquete tem prazo de devolução de dois dias.
public class Raquete extends Equipamento {
    public Raquete(int codigo) { super(codigo, "Raquete de tenis de mesa"); }

    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.plusDays(2);
    }

    @Override
    public boolean exigeVistoria() { return false; }
}
