import util.ConnectionFactory;
import java.sql.Connection;
import model.Aluno;
import dao.AlunoDAO;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso! 🎉");
        } catch (Exception e) {
            System.out.println("Erro na conexão: " + e.getMessage());
        }
    }
}