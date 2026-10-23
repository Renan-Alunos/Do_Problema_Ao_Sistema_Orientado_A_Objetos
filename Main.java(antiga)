import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Classe principal: é por aqui que o programa começa a ser executado.
// Para executar: javac *.java && java Main
public class Main {
    private static Scanner scanner;
    private static final SistemaEmprestimos sistema = new SistemaEmprestimos();

    public static void main(String[] args) {
        sistema.carregarEquipamentosIniciais();

        try (Scanner entrada = new Scanner(System.in)) {
            scanner = entrada;
            boolean continuar = true;

            while (continuar) {
                String titulo = "CONTROLE DE EMPRESTIMOS DA ATLETICA";
                String tituloCentralizado = " ".repeat((56 - titulo.length()) / 2) + titulo;

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

                int opcao = lerInteiro("Escolha uma opcao: ");

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

    private static void cadastrarUsuario(boolean aluno) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Matricula/identificador: ");
        String matricula = scanner.nextLine().trim();

        Usuario usuario;
        if (aluno) {
            usuario = new Aluno(nome, matricula);
        } else {
            usuario = new Servidor(nome, matricula);
        }
        sistema.cadastrarUsuario(usuario);
        System.out.println("Usuario cadastrado com sucesso.");
    }

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

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

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
