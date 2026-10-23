import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Coordena cadastros, buscas, empréstimos e devoluções.
public class SistemaEmprestimos {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Equipamento> equipamentos = new ArrayList<>();
    private final Historico historico = new Historico();
    private int proximoCodigoEmprestimo = 1;

    public Historico getHistorico() { return historico; }

    public void carregarEquipamentosIniciais() {
        cadastrarEquipamento(new Bola(1, "Futsal"));
        cadastrarEquipamento(new Bola(2, "Volei"));
        cadastrarEquipamento(new Bola(3, "Basquete"));
        cadastrarEquipamento(new Raquete(4));
        cadastrarEquipamento(new Colete(5));
        cadastrarEquipamento(new Bicicleta(6));
    }

    public void cadastrarUsuario(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuario invalido.");
        if (buscarUsuario(usuario.getMatricula()) != null) throw new IllegalArgumentException("Ja existe usuario com esse identificador.");
        usuarios.add(usuario);
    }

    public void cadastrarEquipamento(Equipamento equipamento) {
        if (equipamento == null) throw new IllegalArgumentException("Equipamento invalido.");
        if (buscarEquipamento(equipamento.getCodigo()) != null) throw new IllegalArgumentException("Ja existe equipamento com esse codigo.");
        equipamentos.add(equipamento);
    }

    public Usuario buscarUsuario(String matricula) {
        for (Usuario u : usuarios) {
            if (u.getMatricula().equalsIgnoreCase(matricula)) return u;
        }
        return null;
    }

    public Equipamento buscarEquipamento(int codigo) {
        for (Equipamento e : equipamentos) {
            if (e.getCodigo() == codigo) return e;
        }
        return null;
    }

    public Emprestimo realizarEmprestimo(String matricula, int codigoEquipamento) {
        Usuario usuario = buscarUsuario(matricula);
        if (usuario == null) throw new IllegalArgumentException("Usuario nao encontrado.");
        Equipamento equipamento = buscarEquipamento(codigoEquipamento);
        if (equipamento == null) throw new IllegalArgumentException("Equipamento nao encontrado.");
        usuario.verificarPodeEmprestar();
        if (!equipamento.isDisponivel()) throw new IllegalStateException("Equipamento indisponivel.");

        LocalDateTime agora = LocalDateTime.now();
        Emprestimo emprestimo = new Emprestimo(proximoCodigoEmprestimo++, usuario, equipamento, agora);
        equipamento.marcarEmprestado();
        usuario.adicionarEmprestimoAtivo(emprestimo);
        historico.adicionar(emprestimo);
        return emprestimo;
    }

    public Emprestimo buscarEmprestimoAtivo(int codigo) {
        for (Emprestimo e : historico.registrosInternos()) {
            if (e.getCodigo() == codigo && !e.isDevolvido()) return e;
        }
        return null;
    }

    public void registrarDevolucao(int codigo, boolean danificado, boolean vistoriaAprovada) {
        Emprestimo emprestimo = buscarEmprestimoAtivo(codigo);
        if (emprestimo == null) throw new IllegalArgumentException("Emprestimo ativo nao encontrado.");

        boolean atrasado = emprestimo.isAtrasado();
        emprestimo.registrarDevolucao(danificado, vistoriaAprovada);
        boolean houveDano = emprestimo.isEquipamentoDanificado();
        emprestimo.getEquipamento().marcarDisponivel();
        emprestimo.getUsuario().removerEmprestimoAtivo(emprestimo);

        if (atrasado || houveDano) {
            emprestimo.getUsuario().bloquearPorDias(2);
        }
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
            return;
        }
        for (Usuario u : usuarios) System.out.println(u);
    }

    public void listarEquipamentos() {
        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento cadastrado.");
            return;
        }
        for (Equipamento e : equipamentos) System.out.println(e);
    }

    public void listarEquipamentosDisponiveis() {
        System.out.println("Equipamentos disponiveis:");
        boolean encontrou = false;
        for (Equipamento e : equipamentos) {
            if (e.isDisponivel()) {
                System.out.println(e);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhum equipamento disponivel.");
    }
}
