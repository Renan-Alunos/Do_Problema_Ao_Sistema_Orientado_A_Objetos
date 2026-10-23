import java.time.LocalDateTime;

// Subclasse de Equipamento: bicicleta tem prazo de sete dias e exige vistoria.
public class Bicicleta extends Equipamento {
    public Bicicleta(int codigo) { super(codigo, "Bicicleta"); }

    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.plusDays(7);
    }

    @Override
    public boolean exigeVistoria() { return true; }
}
