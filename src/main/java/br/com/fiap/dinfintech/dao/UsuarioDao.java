package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importação essencial para Statement.RETURN_GENERATED_KEYS (embora não usemos mais para o ID)

// Outras importações que você pode ter em UsuarioDao
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

public class UsuarioDao {

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario O objeto Usuario a ser cadastrado.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarUsuario(Usuario usuario) { // <-- O RETORNO DEVE SER BOOLEAN!
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;
        PreparedStatement stmtGetId = null; // PreparedStatement para obter o ID da sequência

        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false); // Inicia uma transação

            // **** SEÇÃO 1: Obter o próximo ID da sequence ANTES da inserção ****
            String sqlGetNextId = "SELECT SQ_T_DIN_USUARIO_ID.NEXTVAL FROM DUAL"; // CONFIRME O NOME DA SUA SEQUENCE!
            stmtGetId = conn.prepareStatement(sqlGetNextId);
            rs = stmtGetId.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt(1);
                usuario.setIdUsuario(nextId);
            } else {
                System.err.println("Erro: Não foi possível obter o próximo ID da sequência para usuário.");
                return false; // Retorna false se não conseguir obter o ID
            }
            // Fechar os recursos para obter o ID antes de continuar
            rs.close();
            stmtGetId.close();
            rs = null;
            stmtGetId = null;


            // **** SEÇÃO 2: Inserção do usuário no banco de dados ****
            // Certifique-se de que sua tabela T_DIN_USUARIO tem a coluna SALDO se você for inseri-lo aqui
            // Se SALDO é um campo separado para o qual você terá um método `atualizarSaldo` ou similar,
            // e ele não é parte do cadastro inicial, remova-o da query de INSERT.
            // Para este exemplo, vou assumir que você **não** cadastra o SALDO junto,
            // mas que ele começa como 0 e será atualizado depois.
            // Se você quiser que o SALDO seja 0.0 na criação, a sua tabela T_DIN_USUARIO
            // deve ter a coluna SALDO com um valor padrão (DEFAULT 0.0) ou você pode incluir
            // SALDO na query de INSERT e passar 0.0.
            // Exemplo COM SALDO DEFAULT 0.0 (recomendado para simplicidade no cadastro inicial):
            String sql = "INSERT INTO T_DIN_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA) VALUES (?, ?, ?, ?)";
            // Exemplo SE QUISER INSERIR SALDO INICIALMENTE (precisa de um 5º ?)
            // String sql = "INSERT INTO T_DIN_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, SALDO) VALUES (?, ?, ?, ?, ?)";


            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            // Se você incluiu SALDO na query acima, adicione esta linha:
            // stmt.setDouble(5, usuario.getSaldo()); // Assumindo que Usuario tem getSaldo()

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                sucesso = true;
            }
            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback da transação de usuário: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Erro ao cadastrar usuário em T_DIN_USUARIO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(null, stmtGetId, null);
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return sucesso; // Retorna true ou false
    }

    /**
     * Busca um usuário pelo email e senha para login.
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @return O objeto Usuario se encontrado, ou null caso contrário.
     */
    public Usuario validarUsuario(String email, String senha) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_USUARIO, NOME, EMAIL, SENHA FROM T_DIN_USUARIO WHERE EMAIL = ? AND SENHA = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("ID_USUARIO");
                String nome = rs.getString("NOME");
                String emailDb = rs.getString("EMAIL");
                String senhaDb = rs.getString("SENHA");

                usuario = new Usuario(idUsuario, nome, emailDb, senhaDb); // <-- ISSO ESTÁ CERTO!
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email e senha: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return usuario;
    }

    /**
     * Busca um usuário pelo ID.
     * @param idUsuario O ID do usuário.
     * @return O objeto Usuario se encontrado, ou null caso contrário.
     */
    public Usuario buscarUsuarioPorId(int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_USUARIO, NOME, EMAIL, SENHA FROM T_DIN_USUARIO WHERE ID_USUARIO = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("NOME");
                String emailDb = rs.getString("EMAIL");
                String senhaDb = rs.getString("SENHA");

                Usuario usuario1 = new Usuario();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return usuario;
    }

    /**
     * Busca o saldo atual de um usuário pelo ID.
     * ESTE MÉTODO DEVE ESTAR FORA DE OUTROS MÉTODOS.
     * @param idUsuario O ID do usuário.
     * @return O saldo atual do usuário. Retorna 0.0 se o usuário não for encontrado ou em caso de erro.
     */
    public double buscarSaldoUsuario(int idUsuario) { // <-- O RETORNO DEVE SER DOUBLE!
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double saldo = 0.0; // Valor padrão em caso de não encontrar ou erro

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT SALDO FROM T_DIN_USUARIO WHERE ID_USUARIO = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            if (rs.next()) {
                saldo = rs.getDouble("SALDO");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar saldo do usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return saldo;
    }

    public boolean atualizarSaldoUsuario(int idUsuario, double novoSaldo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "UPDATE T_DIN_USUARIO SET SALDO = ? WHERE ID_USUARIO = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();
            sucesso = linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo do usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, null);
        }
        return sucesso;
    }
}