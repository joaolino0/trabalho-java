package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // Configurações do banco de dados (use a senha que você definiu!)
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "B1bl1otec@2025"; // ⚠️ Sua senha aqui!

    // Metodo para obter a conexão
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
