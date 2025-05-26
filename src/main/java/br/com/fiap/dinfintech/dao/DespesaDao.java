package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Categoria;
import br.com.fiap.dinfintech.model.Despesa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DespesaDao {

    public boolean cadastrarDespesa(Despesa despesa) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;
        PreparedStatement stmtGetId = null; // <-- NOVO: PreparedStatement para obter o ID

        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            // **** PASSO 1: Obter o próximo ID da sequence ANTES da inserção ****
            String sqlGetNextId = "SELECT SQ_T_DIN_DESPESAS_ID.NEXTVAL FROM DUAL";
            stmtGetId = conn.prepareStatement(sqlGetNextId);
            rs = stmtGetId.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt(1); // Pega o primeiro (e único) resultado
                despesa.setIdDespesa(nextId); // Define o ID gerado na despesa
            } else {
                System.err.println("Erro: Não foi possível obter o próximo ID da sequência para despesa.");
                return false; // Sai se não conseguir o ID
            }
            // Fechar os recursos para obter o ID
            rs.close();
            stmtGetId.close();
            rs = null;
            stmtGetId = null;

            // **** PASSO 2: Inserção da despesa, AGORA INCLUINDO O ID ****
            String sqlDespesa = "INSERT INTO T_DIN_DESPESAS (ID_DESPESA, ID_USUARIO, DESCRICAO, VALOR, DATA_DESPESA, ID_CATEGORIA) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlDespesa); // Não precisa mais de Statement.RETURN_GENERATED_KEYS

            stmt.setInt(1, despesa.getIdDespesa()); // Define o ID que você já obteve
            stmt.setInt(2, despesa.getIdUsuario());
            stmt.setString(3, despesa.getDescricao());
            stmt.setDouble(4, despesa.getValor());
            stmt.setDate(5, java.sql.Date.valueOf(despesa.getDataDespesa()));
            stmt.setInt(6, despesa.getCategoria().getIdCategoria());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                // Não precisa mais de rs = stmt.getGeneratedKeys(); aqui!
                // Atualizar o saldo do usuário
                String sqlUpdateSaldo = "UPDATE T_DIN_USUARIO SET SALDO = SALDO - ? WHERE ID_USUARIO = ?";
                PreparedStatement stmtUpdateSaldo = conn.prepareStatement(sqlUpdateSaldo);
                stmtUpdateSaldo.setDouble(1, despesa.getValor());
                stmtUpdateSaldo.setInt(2, despesa.getIdUsuario());
                stmtUpdateSaldo.executeUpdate();
                stmtUpdateSaldo.close();

                sucesso = true;
            }
            conn.commit();
        } catch (SQLException e) {
            // ... (rollback e tratamento de erro)
        } finally {
            // Assegurar que todos os recursos são fechados
            ConnectionManager.getInstance().closeConnection(null, stmtGetId, null); // Fecha o stmtGetId
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs); // Fecha o stmt principal e rs
        }
        return sucesso;
    }


    public List<Despesa> listarDespesasPorUsuario(int idUsuario) {
        List<Despesa> despesas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CategoriaDao categoriaDao = new CategoriaDao(); // Usaremos CategoriaDao para buscar a categoria completa

        try {
            conn = ConnectionManager.getInstance().getConnection();
            // MUDAR: JOIN com a tabela de categorias para pegar o nome da categoria e ID
            String sql = "SELECT d.ID_DESPESA, d.ID_USUARIO, d.DESCRICAO, d.VALOR, d.DATA_DESPESA, d.ID_CATEGORIA, " +
                "c.NOME AS NOME_CATEGORIA, c.TIPO AS TIPO_CATEGORIA, c.PRE_DEFINIDA " +
                "FROM T_DIN_DESPESAS d " +
                "JOIN T_DIN_CATEGORIAS c ON d.ID_CATEGORIA = c.ID_CATEGORIA " +
                "WHERE d.ID_USUARIO = ? ORDER BY d.DATA_DESPESA DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idDespesa = rs.getInt("ID_DESPESA");
                String descricao = rs.getString("DESCRICAO");
                double valor = rs.getDouble("VALOR");
                LocalDate dataDespesa = rs.getDate("DATA_DESPESA").toLocalDate();

                // Recupera os dados da categoria do ResultSet e cria o objeto Categoria
                int idCategoria = rs.getInt("ID_CATEGORIA");
                String nomeCategoria = rs.getString("NOME_CATEGORIA");
                String tipoCategoria = rs.getString("TIPO_CATEGORIA");
                boolean preDefinida = rs.getInt("PRE_DEFINIDA") == 1; // Oracle usa NUMBER(1) para boolean

                Categoria categoria = new Categoria(idCategoria, idUsuario, nomeCategoria, tipoCategoria, preDefinida);

                despesas.add(new Despesa(idDespesa, idUsuario, descricao, valor, dataDespesa, categoria));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar despesas para o usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return despesas;
    }

    /**
     * NOVO MÉTODO: Calcula o total gasto por categoria para um usuário.
     * @param idUsuario O ID do usuário.
     * @return Um mapa onde a chave é o nome da categoria (String) e o valor é o total gasto (Double).
     */
    public Map<String, Double> calcularTotalDespesasPorCategoria(int idUsuario) {
        Map<String, Double> totalPorCategoria = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT c.NOME AS NOME_CATEGORIA, SUM(d.VALOR) AS TOTAL_GASTO " +
                "FROM T_DIN_DESPESAS d " +
                "JOIN T_DIN_CATEGORIAS c ON d.ID_CATEGORIA = c.ID_CATEGORIA " +
                "WHERE d.ID_USUARIO = ? AND c.TIPO = 'DESPESA' " + // Garante que é uma categoria de despesa
                "GROUP BY c.NOME " +
                "ORDER BY TOTAL_GASTO DESC"; // Ordena para exibir os maiores gastos primeiro
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nomeCategoria = rs.getString("NOME_CATEGORIA");
                double totalGasto = rs.getDouble("TOTAL_GASTO");
                totalPorCategoria.put(nomeCategoria, totalGasto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular total de despesas por categoria para o usuário " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return totalPorCategoria;
    }
}