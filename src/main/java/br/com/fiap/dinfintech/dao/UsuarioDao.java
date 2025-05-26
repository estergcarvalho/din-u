package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    public boolean cadastrarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;
        PreparedStatement stmtGetId = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlGetNextId = "SELECT SQ_T_DIN_USUARIO_ID.NEXTVAL FROM DUAL";
            stmtGetId = conn.prepareStatement(sqlGetNextId);
            rs = stmtGetId.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt(1);
                usuario.setIdUsuario(nextId);
            } else {
                System.err.println("Erro: Não foi possível obter o próximo ID da sequência para usuário.");
                return false;
            }

            rs.close();
            stmtGetId.close();
            rs = null;
            stmtGetId = null;

            String sql = "INSERT INTO T_DIN_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA) VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                sucesso = true;
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
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
        return sucesso;
    }

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

                usuario = new Usuario(idUsuario, nome, emailDb, senhaDb);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email e senha: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return usuario;
    }

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

    public double buscarSaldoUsuario(int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double saldo = 0.0;

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