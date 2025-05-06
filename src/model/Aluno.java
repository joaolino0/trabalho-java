package model;

import java.time.LocalDate;

public class Aluno {
    private int idAluno;
    private String nome;
    private String matricula;
    private LocalDate dataNascimento;

    // Construtor, getters e setters
    public Aluno(String nome, String matricula, LocalDate dataNascimento) {
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters (adicione os métodos abaixo)
    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}
