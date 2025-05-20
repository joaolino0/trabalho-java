package dao;

import model.Emprestimo;
import util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

	private static final DateTimeFormatter FORMATADOR_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public void realizarEmprestimo(int idAluno, int idLivro) {
		String verificarEstoque = "SELECT quantidade_estoque FROM Livros WHERE id_livro = ?";
		String emprestarSQL = "INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo) VALUES (?, ?, ?)";
		String atualizarEstoque = "UPDATE Livros SET quantidade_estoque = quantidade_estoque - 1 WHERE id_livro = ?";

		try (Connection conn = Conexao.getConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement stmtEstoque = conn.prepareStatement(verificarEstoque)) {
				stmtEstoque.setInt(1, idLivro);
				ResultSet rs = stmtEstoque.executeQuery();

				if (!rs.next() || rs.getInt("quantidade_estoque") <= 0) {
					System.out.println("❌ Estoque insuficiente para empréstimo!");
					conn.rollback();
					return;
				}
			}

			try (PreparedStatement stmtEmprestimo = conn.prepareStatement(emprestarSQL)) {
				stmtEmprestimo.setInt(1, idAluno);
				stmtEmprestimo.setInt(2, idLivro);
				stmtEmprestimo.setDate(3, Date.valueOf(LocalDate.now()));
				stmtEmprestimo.executeUpdate();
			}

			try (PreparedStatement stmtAtualizar = conn.prepareStatement(atualizarEstoque)) {
				stmtAtualizar.setInt(1, idLivro);
				stmtAtualizar.executeUpdate();
			}

			conn.commit();
			System.out.println("✅ Empréstimo realizado com sucesso!");

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
		}
	}

	public void registrarDevolucao(int idEmprestimo) {
		String buscarLivro = "SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ?";
		String atualizarEstoque = "UPDATE Livros SET quantidade_estoque = quantidade_estoque + 1 WHERE id_livro = ?";
		String registrarDevolucao = "UPDATE Emprestimos SET data_devolucao = ? WHERE id_emprestimo = ?";

		try (Connection conn = Conexao.getConnection()) {
			conn.setAutoCommit(false);

			int idLivro;

			try (PreparedStatement stmtBuscar = conn.prepareStatement(buscarLivro)) {
				stmtBuscar.setInt(1, idEmprestimo);
				ResultSet rs = stmtBuscar.executeQuery();

				if (!rs.next()) {
					System.out.println("❌ Empréstimo não encontrado.");
					conn.rollback();
					return;
				}

				idLivro = rs.getInt("id_livro");
			}

			try (PreparedStatement stmtEstoque = conn.prepareStatement(atualizarEstoque)) {
				stmtEstoque.setInt(1, idLivro);
				stmtEstoque.executeUpdate();
			}

			try (PreparedStatement stmtDevolucao = conn.prepareStatement(registrarDevolucao)) {
				stmtDevolucao.setDate(1, Date.valueOf(LocalDate.now()));
				stmtDevolucao.setInt(2, idEmprestimo);
				stmtDevolucao.executeUpdate();
			}

			conn.commit();
			System.out.println("✅ Devolução registrada com sucesso!");

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Erro ao registrar devolução: " + e.getMessage());
		}
	}

	public Emprestimo buscarPorId(int idEmprestimo) {
		String sql = "SELECT * FROM Emprestimos WHERE id_emprestimo = ?";
		Emprestimo emprestimo = null;

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, idEmprestimo);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				emprestimo = new Emprestimo(rs.getInt("id_emprestimo"), rs.getInt("id_aluno"), rs.getInt("id_livro"),
						rs.getDate("data_emprestimo").toLocalDate(),
						rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null);
			}

		} catch (SQLException e) {
			System.err.println("Erro ao buscar empréstimo: " + e.getMessage());
		}

		return emprestimo;
	}

	public List<Emprestimo> listarEmprestimosAtivos() {
		List<Emprestimo> lista = new ArrayList<>();
		String sql = "SELECT * FROM Emprestimos WHERE data_devolucao IS NULL";

		try (Connection conn = Conexao.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Emprestimo e = new Emprestimo(rs.getInt("id_emprestimo"), rs.getInt("id_aluno"), rs.getInt("id_livro"),
						rs.getDate("data_emprestimo").toLocalDate(), null);
				lista.add(e);
			}

		} catch (SQLException e) {
			System.err.println("Erro ao listar empréstimos ativos: " + e.getMessage());
		}

		return lista;
	}

	public void listarEmprestimosAtivosDetalhados() {
	    String sql = """
	        SELECT e.id_emprestimo, a.nome_aluno, l.titulo, e.data_emprestimo
	        FROM Emprestimos e
	        JOIN Alunos a ON e.id_aluno = a.id_aluno
	        JOIN Livros l ON e.id_livro = l.id_livro
	        WHERE e.data_devolucao IS NULL
	        """;

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        System.out.println("📋 Empréstimos Ativos:");
	        boolean vazio = true;

	        while (rs.next()) {
	            vazio = false;
	            System.out.printf("ID: %d | Aluno: %s | Livro: %s | Data: %s\n",
	                    rs.getInt("id_emprestimo"),
	                    rs.getString("nome_aluno"),
	                    rs.getString("titulo"),
	                    rs.getDate("data_emprestimo").toLocalDate().format(FORMATADOR_BR)
	            );
	        }

	        if (vazio) {
	            System.out.println("⚠️ Nenhum empréstimo ativo no momento.");
	        }

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar empréstimos ativos: " + e.getMessage());
	    }
	}

	public void listarLivrosEmprestados() {
	    String sql = """
	        SELECT l.titulo, COUNT(*) AS total_emprestado
	        FROM Emprestimos e
	        JOIN Livros l ON e.id_livro = l.id_livro
	        WHERE e.data_devolucao IS NULL
	        GROUP BY l.titulo
	        """;

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        System.out.println("📚 Livros Emprestados:");
	        boolean vazio = true;

	        while (rs.next()) {
	            vazio = false;
	            System.out.printf("Livro: %s | Qtde emprestada: %d\n",
	                    rs.getString("titulo"),
	                    rs.getInt("total_emprestado")
	            );
	        }

	        if (vazio) {
	            System.out.println("⚠️ Nenhum livro emprestado atualmente.");
	        }

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar livros emprestados: " + e.getMessage());
	    }
	}

	public void listarHistoricoEmprestimos() {
	    String sql = """
	        SELECT e.id_emprestimo, a.nome_aluno, l.titulo,
	               e.data_emprestimo, e.data_devolucao
	        FROM Emprestimos e
	        JOIN Alunos a ON e.id_aluno = a.id_aluno
	        JOIN Livros l ON e.id_livro = l.id_livro
	        ORDER BY e.data_emprestimo DESC
	        """;

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        System.out.println("🗃️ Histórico de Empréstimos:");
	        boolean vazio = true;

	        while (rs.next()) {
	            vazio = false;
	            String devolucao = (rs.getDate("data_devolucao") != null)
	                    ? rs.getDate("data_devolucao").toLocalDate().format(FORMATADOR_BR)
	                    : "Não devolvido";

	            System.out.printf("ID: %d | Aluno: %s | Livro: %s | Empréstimo: %s | Devolução: %s\n",
	                    rs.getInt("id_emprestimo"),
	                    rs.getString("nome_aluno"),
	                    rs.getString("titulo"),
	                    rs.getDate("data_emprestimo").toLocalDate().format(FORMATADOR_BR),
	                    devolucao
	            );
	        }

	        if (vazio) {
	            System.out.println("⚠️ Nenhum histórico de empréstimos encontrado.");
	        }

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar histórico: " + e.getMessage());
	    }
	}

}
