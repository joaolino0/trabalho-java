package main;

import model.Aluno;
import dao.AlunoDAO;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Aluno aluno = new Aluno(
                "Pedro Almeida",
                "20240002",
                LocalDate.of(2000, 10, 7)
        );

        AlunoDAO alunoDAO = new AlunoDAO();
        alunoDAO.inserir(aluno);
        System.out.println("✅ Aluno inserido com ID: " + aluno.getIdAluno());



        // --- TESTE 2: BUSCAR ALUNO POR ID ---
        int idDoAluno = aluno.getIdAluno(); // ID gerado na inserção
        Aluno alunoBuscado = alunoDAO.buscarPorId(idDoAluno);

        if (alunoBuscado != null) {
            System.out.println("\n✅ Aluno encontrado:");
            System.out.println("ID: " + alunoBuscado.getIdAluno());
            System.out.println("Nome: " + alunoBuscado.getNome());
            System.out.println("Matrícula: " + alunoBuscado.getMatricula());
            System.out.println("Data de Nascimento: " + alunoBuscado.getDataNascimento());
        } else {
            System.out.println("\n❌ Aluno não encontrado!");
        }

        // --- TESTE 3: ATUALIZAR ALUNO ---
        if (alunoBuscado != null) {
            alunoBuscado.setNome("Janaina Pessoa"); // Novo nome
            alunoBuscado.setMatricula("20240003"); // Nova matrícula
            alunoDAO.atualizar(alunoBuscado);
            System.out.println("\n✅ Aluno atualizado!");
        }


        System.out.println("Aluno cadastrado com ID: " + aluno.getIdAluno());
    }
}
