package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Receita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDao {

    public boolean cadastrarReceita(Receita receita) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;
        PreparedStatement stmtGetId = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlGetNextId = "SELECT SQ_T_DIN_RECEITAS_ID.NEXTVAL FROM DUAL";
            stmtGetId = conn.prepareStatement(sqlGetNextId);
            rs = stmtGetId.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt(1);
                receita.setIdReceita(nextId);
            } else {
                System.err.println("Erro: Não foi possível obter o próximo ID da sequência para receita.");
                return false;
            }
            rs.close();
            stmtGetId.close();
            rs = null;
            stmtGetId = null;

            String sqlReceita = "INSERT INTO T_DIN_RECEITAS (ID_RECEITA, ID_USUARIO, DESCRICAO, VALOR, DATA_RECEITA, TIPO_RECEITA) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlReceita);

            stmt.setInt(1, receita.getIdReceita());
            stmt.setInt(2, receita.getIdUsuario());
            stmt.setString(3, receita.getDescricao());
            stmt.setDouble(4, receita.getValor());
            stmt.setDate(5, java.sql.Date.valueOf(receita.getDataReceita()));
            stmt.setString(6, receita.getTipoReceita());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                String sqlUpdateSaldo = "UPDATE T_DIN_USUARIO SET SALDO = SALDO + ? WHERE ID_USUARIO = ?";
                PreparedStatement stmtUpdateSaldo = conn.prepareStatement(sqlUpdateSaldo);
                stmtUpdateSaldo.setDouble(1, receita.getValor());
                stmtUpdateSaldo.setInt(2, receita.getIdUsuario());
                stmtUpdateSaldo.executeUpdate();
                stmtUpdateSaldo.close();

                sucesso = true;
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback da transação de receita: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Erro ao cadastrar receita: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(null, stmtGetId, null);
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return sucesso;
    }

    public List<Receita> listarReceitasPorUsuario(int idUsuario) {
        List<Receita> receitas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_RECEITA, ID_USUARIO, DESCRICAO, VALOR, DATA_RECEITA, TIPO_RECEITA FROM T_DIN_RECEITAS WHERE ID_USUARIO = ? ORDER BY DATA_RECEITA DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idReceita = rs.getInt("ID_RECEITA");
                String descricao = rs.getString("DESCRICAO");
                double valor = rs.getDouble("VALOR");
                LocalDate dataReceita = rs.getDate("DATA_RECEITA").toLocalDate();
                String tipoReceita = rs.getString("TIPO_RECEITA");

                receitas.add(new Receita(idReceita, idUsuario, descricao, valor, dataReceita, tipoReceita));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar receitas para o usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return receitas;
    }
}