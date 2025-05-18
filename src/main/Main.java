package main;

import dao.AlunoDAO;
import dao.LivroDAO;
import dao.EmprestimoDAO;
import model.Aluno;
import model.Livro;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AlunoDAO alunoDAO = new AlunoDAO();
        LivroDAO livroDAO = new LivroDAO();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

        int opcao;

        do {
            System.out.println("\n📚 MENU BIBLIOTECA ESCOLAR");
            System.out.println("[1] Cadastrar aluno");
            System.out.println("[2] Cadastrar livro");
            System.out.println("[3] Realizar empréstimo");
            System.out.println("[4] Registrar devolução");
            System.out.println("[5] Relatórios");
            System.out.println("[0] Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();
                    System.out.print("Data de nascimento (DD-MM-AAAA): ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate nascimento = LocalDate.parse(scanner.nextLine(), formatter);

                    Aluno aluno = new Aluno(nome, matricula, nascimento);
                    alunoDAO.inserir(aluno);
                    System.out.println("✅ Aluno cadastrado com ID: " + aluno.getIdAluno());
                }

                case 2 -> {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Ano de publicação: ");
                    int ano = Integer.parseInt(scanner.nextLine());
                    System.out.print("Quantidade em estoque: ");
                    int qtd = Integer.parseInt(scanner.nextLine());

                    Livro livro = new Livro(titulo, autor, ano, qtd);
                    livroDAO.inserir(livro);
                    System.out.println("✅ Livro cadastrado com ID: " + livro.getIdLivro());
                }

                case 3 -> {
                    System.out.print("ID do aluno: ");
                    int idAluno = Integer.parseInt(scanner.nextLine());
                    System.out.print("ID do livro: ");
                    int idLivro = Integer.parseInt(scanner.nextLine());

                    emprestimoDAO.realizarEmprestimo(idAluno, idLivro);
                }

                case 4 -> {
                    System.out.print("ID do empréstimo: ");
                    int idEmprestimo = Integer.parseInt(scanner.nextLine());
                    emprestimoDAO.registrarDevolucao(idEmprestimo);
                }

                case 5 -> {
                    System.out.println("\n📊 RELATÓRIOS:");
                    System.out.println("[1] Empréstimos ativos");
                    System.out.println("[2] Livros emprestados");
                    System.out.println("[3] Histórico de empréstimos");
                    System.out.print("Escolha: ");
                    int rel = Integer.parseInt(scanner.nextLine());

                    switch (rel) {
                        case 1 -> emprestimoDAO.listarEmprestimosAtivosDetalhados();
                        case 2 -> emprestimoDAO.listarLivrosEmprestados();
                        case 3 -> emprestimoDAO.listarHistoricoEmprestimos();
                        default -> System.out.println("❌ Opção inválida.");
                    }
                }

                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("❌ Opção inválida.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}
