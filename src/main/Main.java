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

        System.out.println("Aluno cadastrado com ID: " + aluno.getIdAluno());
    }
}
