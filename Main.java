//Classes para trabalhar com datas e horários;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Classe principal;
public class Main {
    //Scanner compartilhado pelos métodos que precisam ler dados digitados;
    private static Scanner scanner;

    //Objeto central que guarda usuários, equipamentos e o histórico de empréstimos;
    private static final SistemaEmprestimos sistema = new SistemaEmprestimos();

    //Método main;
    public static void main(String[] args) {
        sistema.carregarEquipamentosIniciais();

        try (Scanner entrada = new Scanner(System.in)) {
            scanner = entrada;
            //Enquanto esta variável for true, o menu continuará aparecendo;
            boolean continuar = true;

            while (continuar) {

        //Molda a tela do menu inicial;
        String titulo = "CONTROLE DE EMPRESTIMOS DA ATLETICA";
        String tituloCentralizado =
                " ".repeat((56 - titulo.length()) / 2) + titulo;

        System.out.println();
        System.out.println("╔" + "═".repeat(58) + "╗");

        System.out.printf("║ %-56s ║%n", tituloCentralizado);

        System.out.println("╠" + "═".repeat(58) + "╣");

        System.out.printf("║ %-56s ║%n", "  1 - Cadastrar aluno");
        System.out.printf("║ %-56s ║%n", "  2 - Cadastrar servidor");
        System.out.printf("║ %-56s ║%n", "  3 - Listar usuarios");
        System.out.printf("║ %-56s ║%n", "  4 - Listar equipamentos");
        System.out.printf("║ %-56s ║%n", "  5 - Realizar emprestimo");
        System.out.printf("║ %-56s ║%n", "  6 - Registrar devolucao");
        System.out.printf("║ %-56s ║%n", "  7 - Consultar historico");
        System.out.printf("║ %-56s ║%n", "  8 - Consultar emprestimos ativos de um usuario");
        System.out.printf("║ %-56s ║%n", "  0 - Sair");

        System.out.println("╚" + "═".repeat(58) + "╝");

        //Lê o escolhido e manda para o local desejado;
        int opcao = lerInteiro("Escolha uma opcao: ");

                //O tratamento de erros volta ao menu se algo falhar;
                try {
                    switch (opcao) {
                        case 1 -> cadastrarUsuario(true);
                        case 2 -> cadastrarUsuario(false);
                        case 3 -> sistema.listarUsuarios();
                        case 4 -> sistema.listarEquipamentos();
                        case 5 -> realizarEmprestimo();
                        case 6 -> registrarDevolucao();
                        case 7 -> sistema.getHistorico().mostrarTodos();
                        case 8 -> consultarAtivosUsuario();
                        case 0 -> {
                            continuar = false;
                            System.out.println("Sistema encerrado.");
                        }
                        default -> System.out.println("Opcao invalida. Tente novamente.");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Nao foi possivel concluir a operacao: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Ocorreu um erro. Verifique os dados e tente novamente.");
                }
            }
        }
    }

    //Cadastra um aluno ou servidor, o parâmetro aluno determina o tipo criado de usuário: true para aluno e false para servidor;
    private static void cadastrarUsuario(boolean aluno) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Matricula/identificador: ");
        String matricula = scanner.nextLine().trim();

        //A variável é do tipo usuário, mas recebe um objeto Aluno ou Servidor;
        Usuario usuario;
        if (aluno) {
            usuario = new Aluno(nome, matricula);
        } else {
            usuario = new Servidor(nome, matricula);
        }
        sistema.cadastrarUsuario(usuario);
        System.out.println("Usuario cadastrado com sucesso.");
    }

    //Registra um empréstimo após verificar usuário e equipamento;
    private static void realizarEmprestimo() {
        System.out.print("Matricula/identificador do usuario: ");
        String matricula = scanner.nextLine().trim();
        sistema.listarEquipamentosDisponiveis();
        int codigo = lerInteiro("Codigo do equipamento: ");

        Emprestimo emprestimo = sistema.realizarEmprestimo(matricula, codigo);
        System.out.println("Emprestimo registrado.");
        System.out.println("Codigo do emprestimo: " + emprestimo.getCodigo());
        System.out.println("Devolucao prevista: " + emprestimo.getDataPrevistaDevolucao()
                .toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    //Registra a devolução de um empréstimo que ainda esteja ativo e, no caso da bicicleta, também realiza uma vistoria;
    private static void registrarDevolucao() {
        int codigo = lerInteiro("Codigo do emprestimo: ");
        Emprestimo emprestimo = sistema.buscarEmprestimoAtivo(codigo);
        if (emprestimo == null) {
            System.out.println("Nao foi encontrado emprestimo ativo com esse codigo.");
            return;
        }

        boolean danificado = lerSimNao("O equipamento foi devolvido danificado? (s/n): ");
        boolean vistoriaAprovada = true;
        if (emprestimo.getEquipamento().exigeVistoria()) {
            System.out.println("Vistoria da bicicleta:");
            boolean pneu = lerSimNao("Pneu em boas condicoes? (s/n): ");
            boolean freio = lerSimNao("Freio em boas condicoes? (s/n): ");
            boolean corrente = lerSimNao("Corrente em boas condicoes? (s/n): ");
            //A vistoria só é aprovada quando pneu, freio e corrente estão em boas condições;
            vistoriaAprovada = pneu && freio && corrente;
            if (!vistoriaAprovada) {
                danificado = true;
            }
        }

        sistema.registrarDevolucao(codigo, danificado, vistoriaAprovada);
        System.out.println("Devolucao registrada.");
        if (emprestimo.isAtrasado()) {
            System.out.println("A devolucao ocorreu com atraso.");
        } else {
            System.out.println("Devolucao dentro do prazo.");
        }
        if (emprestimo.isAtrasado() || danificado) {
            System.out.println("O usuario foi bloqueado por 2 dias devido ao atraso e/ou dano.");
        }
    }

    //Procura um usuário e mostra os empréstimos que ele ainda não devolveu;
    private static void consultarAtivosUsuario() {
        System.out.print("Matricula/identificador do usuario: ");
        String matricula = scanner.nextLine().trim();
        Usuario usuario = sistema.buscarUsuario(matricula);
        if (usuario == null) {
            System.out.println("Usuario nao encontrado.");
            return;
        }
        System.out.println("Emprestimos ativos de " + usuario.getNome() + ":");
        usuario.mostrarEmprestimosAtivos();
    }

    //Repete a pergunta até que o usuário digite um número certo;
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            try {
                //Converte o texto string em um número inteiro;
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

    //Lê respostas sim/não e devolve true para sim ou false para não;
    private static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) return true;
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) return false;
            System.out.println("Resposta invalida. Digite s ou n.");
        }
    }
}

//Superclasse abstrata dos usuários;
abstract class Usuario {
    //Classe base para os tipos Aluno e Servidor, concentra regras comuns aos usuários;
    private String nome;
    private String matricula;
    private LocalDate dataFimBloqueio;

    //Guarda apenas os empréstimos que ainda não foram devolvidos;
    private final List<Emprestimo> emprestimosAtivos = new ArrayList<>();

    //O construtor valida os dados antes de criar o usuário;
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

    //Cada tipo de usuário define seu próprio limite de empréstimos;
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

    //Verifica se o usuário ainda está dentro do período de bloqueio;
    public boolean estaBloqueado() {
        return dataFimBloqueio != null && LocalDate.now().isBefore(dataFimBloqueio.plusDays(1));
    }

    public void bloquearPorDias(int dias) {
        //A data final do bloqueio é calculada a partir da data de hoje;
        if (dias <= 0) throw new IllegalArgumentException("O bloqueio deve ser de pelo menos um dia.");
        LocalDate fim = LocalDate.now().plusDays(dias);
        if (dataFimBloqueio == null || fim.isAfter(dataFimBloqueio)) dataFimBloqueio = fim;
    }

    //Confere se o usuário pode pegar outro equipamento;
    public void verificarPodeEmprestar() {
        //Antes de emprestar, verifica bloqueio e quantidade de empréstimos em aberto;
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
        //Se a lista estiver vazia, informa isso, caso contrário mostra cada empréstimo;
        if (emprestimosAtivos.isEmpty()) {
            System.out.println("Nenhum emprestimo ativo.");
            return;
        }
        for (Emprestimo e : emprestimosAtivos) System.out.println(e);
    }

    @Override
    public String toString() {
        //Retorna os dados do usuário;
        return getClass().getSimpleName() + " | " + nome + " | Identificador: " + matricula
                + " | Ativos: " + emprestimosAtivos.size() + "/" + getLimiteEmprestimos()
                + (estaBloqueado() ? " | BLOQUEADO ate " + dataFimBloqueio : "");
    }
}

//Aluno herda os atributos e comportamentos comuns do usuário;
class Aluno extends Usuario {
    public Aluno(String nome, String matricula) { super(nome, matricula); }
    @Override
    public int getLimiteEmprestimos() { return 2; }
}

//Servidor também herda de usuário, mas possui um limite diferente de empréstimos;
class Servidor extends Usuario {
    // Também herda de Usuario, mas pode manter até 4 empréstimos ativos.
    public Servidor(String nome, String matricula) { super(nome, matricula); }
    @Override
    public int getLimiteEmprestimos() { return 4; }
}

//Superclasse abstrata de todos os equipamentos;
abstract class Equipamento {
    //Classe base dos equipamentos, cada tipo informa prazo e vistoria;
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

    //Retorna a data e hora limite de devolução para cada tipo de equipamento;
    public abstract LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada);

    //Informa se o equipamento precisa de uma vistoria;
    public abstract boolean exigeVistoria();

    public void marcarEmprestado() {
        //Um equipamento emprestado fica indisponível para outros usuários;
        if (!disponivel) throw new IllegalStateException("Equipamento ja esta emprestado.");
        disponivel = false;
    }

    public void marcarDisponivel() { disponivel = true; }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | " + nome + " | " + (disponivel ? "Disponivel" : "Emprestado");
    }
}

//Bola é um tipo específico de Equipamento;
class Bola extends Equipamento {
    //Bolas devem ser devolvidas até o fim do dia;
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

//Colete: assim como a bola, tem devolução prevista para o final do mesmo dia;
class Colete extends Equipamento {
    public Colete(int codigo) { super(codigo, "Colete"); }
    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.toLocalDate().atTime(LocalTime.MAX);
    }
    @Override
    public boolean exigeVistoria() { return false; }
}

//Raquete de tênis de mesa: prazo de devolução de dois dias;
class Raquete extends Equipamento {
    public Raquete(int codigo) { super(codigo, "Raquete de tenis de mesa"); }
    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.plusDays(2);
    }
    @Override
    public boolean exigeVistoria() { return false; }
}

//Bicicleta: prazo de sete dias e exige vistoria;
class Bicicleta extends Equipamento {
    public Bicicleta(int codigo) { super(codigo, "Bicicleta"); }
    @Override
    public LocalDateTime calcularPrazoDevolucao(LocalDateTime retirada) {
        return retirada.plusDays(7);
    }
    @Override
    public boolean exigeVistoria() { return true; }
}

//Representa uma transação de empréstimo, ligando um usuário a um equipamento;
class Emprestimo {
    private final int codigo;
    private final Usuario usuario;
    private final Equipamento equipamento;
    private final LocalDateTime dataEmprestimo;
    private final LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private boolean devolvido;
    private boolean equipamentoDanificado;
    private boolean vistoriaAprovada = true;

    //Ao criar o empréstimo, calcula automaticamente o prazo conforme o equipamento;
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

    //Compara a data de devolução com o prazo previsto;
    public boolean isAtrasado() {
        LocalDateTime referencia = devolvido ? dataDevolucao : LocalDateTime.now();
        return referencia.isAfter(dataPrevistaDevolucao);
    }

    //Salva a data real da devolução e o resultado da verificação do equipamento;
    public void registrarDevolucao(boolean danificado, boolean vistoriaAprovada) {
        if (devolvido) {
            throw new IllegalStateException("Este emprestimo ja foi devolvido.");
        }
        if (!vistoriaAprovada) {
            danificado = true;
        }
        this.dataDevolucao = LocalDateTime.now();
        this.devolvido = true;
        this.equipamentoDanificado = danificado;
        this.vistoriaAprovada = vistoriaAprovada;
    }

    @Override
    public String toString() {
        //Resume as informações principais para exibir no histórico;
        return "Emprestimo #" + codigo + " | " + equipamento.getNome() + " (codigo " + equipamento.getCodigo() + ")"
                + " | Usuario: " + usuario.getNome() + " [" + usuario.getMatricula() + "]"
                + " | Retirada: " + dataEmprestimo.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " | Prevista: " + dataPrevistaDevolucao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " | " + (devolvido ? "Devolvido em " + dataDevolucao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "ATIVO")
                + (equipamentoDanificado ? " | DANO REGISTRADO" : "");
    }
}

//Guarda todos os empréstimos, tanto os ativos quanto os já devolvidos;
class Historico {
    private final List<Emprestimo> registros = new ArrayList<>();

    public void adicionar(Emprestimo emprestimo) { registros.add(emprestimo); }
    List<Emprestimo> registrosInternos() { return registros; }

    public void mostrarTodos() {
        if (registros.isEmpty()) {
            System.out.println("Historico vazio.");
            return;
        }
        for (Emprestimo e : registros) System.out.println(e);
    }
}

//Classe que coordena as operações principais;
class SistemaEmprestimos {
    //Reúne as regras do sistema e coordena a comunicação entre usuários, equipamentos e histórico;
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Equipamento> equipamentos = new ArrayList<>();
    private final Historico historico = new Historico();
    private int proximoCodigoEmprestimo = 1;

    public Historico getHistorico() { return historico; }

    //Cadastra os equipamentos disponíveis assim que começa;
    public void carregarEquipamentosIniciais() {
        cadastrarEquipamento(new Bola(1, "Futsal"));
        cadastrarEquipamento(new Bola(2, "Volei"));
        cadastrarEquipamento(new Bola(3, "Basquete"));
        cadastrarEquipamento(new Raquete(4));
        cadastrarEquipamento(new Colete(5));
        cadastrarEquipamento(new Bicicleta(6));
    }

    //Não permite usuário nulo nem duas pessoas com o mesmo identificador;
    public void cadastrarUsuario(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuario invalido.");
        if (buscarUsuario(usuario.getMatricula()) != null) throw new IllegalArgumentException("Ja existe usuario com esse identificador.");
        usuarios.add(usuario);
    }

    //Não permite equipamento nulo nem códigos repetidos;
    public void cadastrarEquipamento(Equipamento equipamento) {
        if (equipamento == null) throw new IllegalArgumentException("Equipamento invalido.");
        if (buscarEquipamento(equipamento.getCodigo()) != null) throw new IllegalArgumentException("Ja existe equipamento com esse codigo.");
        equipamentos.add(equipamento);
    }

    //Procura pela matrícula e retorna o usuário encontrado, retorna null se não existir;
    public Usuario buscarUsuario(String matricula) {
        for (Usuario u : usuarios) if (u.getMatricula().equalsIgnoreCase(matricula)) return u;
        return null;
    }

    //Procura o equipamento pelo código informado;
    public Equipamento buscarEquipamento(int codigo) {
        for (Equipamento e : equipamentos) if (e.getCodigo() == codigo) return e;
        return null;
    }

    //Valida os dados e registra o novo empréstimo no sistema;
    public Emprestimo realizarEmprestimo(String matricula, int codigoEquipamento) {
        Usuario usuario = buscarUsuario(matricula);
        if (usuario == null) throw new IllegalArgumentException("Usuario nao encontrado.");
        Equipamento equipamento = buscarEquipamento(codigoEquipamento);
        if (equipamento == null) throw new IllegalArgumentException("Equipamento nao encontrado.");
        usuario.verificarPodeEmprestar();
        if (!equipamento.isDisponivel()) throw new IllegalStateException("Equipamento indisponivel.");

        //Registra a data e hora atuais para calcular o prazo de devolução;
        LocalDateTime agora = LocalDateTime.now();
        Emprestimo emprestimo = new Emprestimo(proximoCodigoEmprestimo++, usuario, equipamento, agora);

        //Atualiza o estado do equipamento, a lista de empréstimos do usuário e o histórico;
        equipamento.marcarEmprestado();
        usuario.adicionarEmprestimoAtivo(emprestimo);
        historico.adicionar(emprestimo);
        return emprestimo;
    }

    //Só retorna empréstimos com o código informado que ainda não foram devolvidos;
    public Emprestimo buscarEmprestimoAtivo(int codigo) {
        for (Emprestimo e : historico.registrosInternos()) {
            if (e.getCodigo() == codigo && !e.isDevolvido()) return e;
        }
        return null;
    }

    //Atualiza o empréstimo e libera o equipamento devolvido;
    public void registrarDevolucao(int codigo, boolean danificado, boolean vistoriaAprovada) {
        Emprestimo emprestimo = buscarEmprestimoAtivo(codigo);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Emprestimo ativo nao encontrado.");
        }

        //Guarda se houve atraso antes de finalizar o empréstimo;
        boolean atrasado = emprestimo.isAtrasado();
        emprestimo.registrarDevolucao(danificado, vistoriaAprovada);

        boolean houveDano = emprestimo.isEquipamentoDanificado();

        emprestimo.getEquipamento().marcarDisponivel();
        emprestimo.getUsuario().removerEmprestimoAtivo(emprestimo);

        //A regra prevê bloqueio de dois dias em caso de atraso ou dano;
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
