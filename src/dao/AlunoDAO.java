package dao;

import model.Aluno;
import util.ConnectionFactory;
import java.sql.*;

public class AlunoDAO {
    public void inserir(Aluno aluno) {
        String sql = "INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            stmt.executeUpdate();

            // Recupera o ID gerado automaticamente (opcional)
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setIdAluno(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluno: " + e.getMessage(), e);
        }
    }
}
