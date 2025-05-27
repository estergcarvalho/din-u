package br.com.fiap.dinfintech.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static ConnectionManager connectionManager;

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;

        String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
        String user = "RM560895";
        String password = "fiap25";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado! Verifique o classpath.");
            e.printStackTrace();
            throw new SQLException("Driver JDBC não encontrado!", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados! Verifique URL, usuário e senha.");
            e.printStackTrace();
            throw e;
        }

        return connection;
    }

    public void closeConnection(Connection connection, java.sql.PreparedStatement stmt, java.sql.ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão!");
            e.printStackTrace();
        }
    }

    public void closeConnection(Connection connection) {
        closeConnection(connection, null, null);
    }
}
