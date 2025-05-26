package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Meta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MetaDao {

    public boolean cadastrarMeta(Meta meta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;
        PreparedStatement stmtGetId = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlGetNextId = "SELECT SQ_T_DIN_METAS_ID.NEXTVAL FROM DUAL";
            stmtGetId = conn.prepareStatement(sqlGetNextId);
            rs = stmtGetId.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt(1);
                meta.setIdMeta(nextId);
            } else {
                System.err.println("Erro: Não foi possível obter o próximo ID da sequência para meta.");
                return false;
            }
            rs.close();
            stmtGetId.close();
            rs = null;
            stmtGetId = null;

            String sql = "INSERT INTO T_DIN_METAS (ID_META, ID_USUARIO, DESCRICAO, VALOR_ALVO, VALOR_ATUAL, DATA_CRIACAO, DATA_ALVO, STATUS_META, PRIORIDADE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, meta.getIdMeta());
            stmt.setInt(2, meta.getIdUsuario());
            stmt.setString(3, meta.getDescricao());
            stmt.setDouble(4, meta.getValorAlvo());
            stmt.setDouble(5, meta.getValorAtual());
            stmt.setDate(6, java.sql.Date.valueOf(meta.getDataCriacao()));
            stmt.setDate(7, java.sql.Date.valueOf(meta.getDataAlvo()));
            stmt.setString(8, meta.getStatusMeta());
            stmt.setString(9, meta.getPrioridade());

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
                    System.err.println("Erro ao fazer rollback da transação de meta: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Erro ao cadastrar meta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(null, stmtGetId, null);
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return sucesso;
    }

    public List<Meta> listarMetasPorUsuario(int idUsuario) {
        List<Meta> metas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_META, ID_USUARIO, DESCRICAO, VALOR_ALVO, VALOR_ATUAL, DATA_CRIACAO, DATA_ALVO, STATUS_META, PRIORIDADE FROM T_DIN_METAS WHERE ID_USUARIO = ? ORDER BY DATA_ALVO DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idMeta = rs.getInt("ID_META");
                String descricao = rs.getString("DESCRICAO");
                double valorAlvo = rs.getDouble("VALOR_ALVO");
                double valorAtual = rs.getDouble("VALOR_ATUAL");
                LocalDate dataCriacao = rs.getDate("DATA_CRIACAO").toLocalDate();
                LocalDate dataAlvo = rs.getDate("DATA_ALVO").toLocalDate();
                String statusMeta = rs.getString("STATUS_META");
                String prioridade = rs.getString("PRIORIDADE");

                metas.add(new Meta(idMeta, idUsuario, descricao, valorAlvo, valorAtual, dataCriacao, dataAlvo, statusMeta, prioridade));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar metas para o usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return metas;
    }

    public Meta buscarMetaPorId(int idMeta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Meta meta = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_META, ID_USUARIO, DESCRICAO, VALOR_ALVO, VALOR_ATUAL, DATA_CRIACAO, DATA_ALVO, STATUS_META, PRIORIDADE FROM T_DIN_METAS WHERE ID_META = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMeta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("ID_USUARIO");
                String descricao = rs.getString("DESCRICAO");
                double valorAlvo = rs.getDouble("VALOR_ALVO");
                double valorAtual = rs.getDouble("VALOR_ATUAL");
                LocalDate dataCriacao = rs.getDate("DATA_CRIACAO").toLocalDate();
                LocalDate dataAlvo = rs.getDate("DATA_ALVO").toLocalDate();
                String statusMeta = rs.getString("STATUS_META");
                String prioridade = rs.getString("PRIORIDADE");

                meta = new Meta(idMeta, idUsuario, descricao, valorAlvo, valorAtual, dataCriacao, dataAlvo, statusMeta, prioridade);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar meta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return meta;
    }

    public boolean atualizarMeta(Meta meta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "UPDATE T_DIN_METAS SET DESCRICAO = ?, VALOR_ALVO = ?, VALOR_ATUAL = ?, DATA_ALVO = ?, STATUS_META = ?, PRIORIDADE = ? WHERE ID_META = ? AND ID_USUARIO = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, meta.getDescricao());
            stmt.setDouble(2, meta.getValorAlvo());
            stmt.setDouble(3, meta.getValorAtual());
            stmt.setDate(4, java.sql.Date.valueOf(meta.getDataAlvo()));
            stmt.setString(5, meta.getStatusMeta());
            stmt.setString(6, meta.getPrioridade());
            stmt.setInt(7, meta.getIdMeta());
            stmt.setInt(8, meta.getIdUsuario());

            int linhasAfetadas = stmt.executeUpdate();
            sucesso = linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar meta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, null);
        }
        return sucesso;
    }

    public boolean excluirMeta(int idMeta, int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "DELETE FROM T_DIN_METAS WHERE ID_META = ? AND ID_USUARIO = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMeta);
            stmt.setInt(2, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();
            sucesso = linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir meta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, null);
        }
        return sucesso;
    }
}