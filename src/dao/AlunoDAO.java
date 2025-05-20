package dao;

import model.Aluno;
import util.Conexao;
import java.sql.*;

public class AlunoDAO {

    // Metodo INSERT
    public void inserir(Aluno aluno) {
        String sql = "INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            stmt.executeUpdate();

            // Recupera o ID gerado automaticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setIdAluno(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluno: " + e.getMessage(), e);
        }
    }

    // Metodo SELECT
    public Aluno buscarPorId(int idAluno) {
        String sql = "SELECT * FROM Alunos WHERE id_aluno = ?";
        Aluno aluno = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno(
                        rs.getString("nome_aluno"),
                        rs.getString("matricula"),
                        rs.getDate("data_nascimento").toLocalDate()
                );
                aluno.setIdAluno(rs.getInt("id_aluno"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluno: " + e.getMessage());
        }

        return aluno;
    }

    // Metodo UPDATE
    public void atualizar(Aluno aluno) {
        String sql = "UPDATE Alunos SET nome_aluno = ?, matricula = ?, data_nascimento = ? WHERE id_aluno = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.setInt(4, aluno.getIdAluno());

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println(linhasAfetadas + " linha(s) atualizada(s).");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    // Metodo DELETE
    public void deletar(int idAluno) {
        String sql = "DELETE FROM Alunos WHERE id_aluno = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);
            int linhasAfetadas = stmt.executeUpdate();
            System.out.println(linhasAfetadas + " linha(s) deletada(s).");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar aluno: " + e.getMessage());
        }
    }
}