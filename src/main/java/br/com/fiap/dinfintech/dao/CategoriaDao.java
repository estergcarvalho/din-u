package br.com.fiap.dinfintech.dao;

import br.com.fiap.dinfintech.model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {

    /**
     * Cadastra uma nova categoria no banco de dados.
     * Pode ser uma categoria personalizada de um usuário (idUsuario != null)
     * ou uma categoria pré-definida do sistema (idUsuario == null e preDefinida = true).
     *
     * @param categoria O objeto Categoria a ser cadastrado.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarCategoria(Categoria categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO T_DIN_CATEGORIAS (ID_USUARIO, NOME, TIPO, PRE_DEFINIDA) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Se for uma categoria de usuário, defina o ID_USUARIO; caso contrário, NULL.
            if (categoria.getIdUsuario() > 0) {
                stmt.setInt(1, categoria.getIdUsuario());
            } else {
                stmt.setNull(1, java.sql.Types.NUMERIC);
            }
            stmt.setString(2, categoria.getNome());
            stmt.setString(3, categoria.getTipo());
            stmt.setInt(4, categoria.isPreDefinida() ? 1 : 0); // 1 para true, 0 para false

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                sucesso = true;
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1)); // Define o ID gerado no objeto Categoria
                }
            }
        } catch (SQLException e) {
            // Lidar com erro de PK violada (categoria já existe para o usuário/tipo)
            if (e.getErrorCode() == 1) { // Código de erro para UNIQUE CONSTRAINT (Oracle)
                System.err.println("Erro: Categoria '" + categoria.getNome() + "' do tipo '" + categoria.getTipo() + "' já existe para este usuário ou é pré-definida.");
            } else {
                System.err.println("Erro ao cadastrar categoria: " + e.getMessage());
            }
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return sucesso;
    }

    /**
     * Lista todas as categorias disponíveis para um usuário.
     * Inclui categorias pré-definidas (idUsuario = NULL) e categorias personalizadas do usuário.
     *
     * @param idUsuario O ID do usuário logado.
     * @param tipo Opcional: "DESPESA" ou "RECEITA" para filtrar por tipo. Se nulo, retorna ambos.
     * @return Uma lista de objetos Categoria.
     */
    public Categoria buscarCategoriaPorId(int idCategoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Categoria categoria = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_CATEGORIA, ID_USUARIO, NOME, TIPO, PRE_DEFINIDA FROM T_DIN_CATEGORIAS WHERE ID_CATEGORIA = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCategoria);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("ID_USUARIO"); // Pode ser 0 ou null para categorias pre-definidas
                String nome = rs.getString("NOME");
                String tipo = rs.getString("TIPO");
                boolean preDefinida = rs.getInt("PRE_DEFINIDA") == 1; // Oracle usa NUMBER(1) para boolean

                categoria = new Categoria(idCategoria, idUsuario, nome, tipo, preDefinida);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return categoria;
    }

    /**
     * Lista todas as categorias (pré-definidas e personalizadas) de um usuário para um determinado tipo.
     * Útil para popular dropdowns de cadastro de despesas/receitas.
     * @param idUsuario ID do usuário (pode ser 0 ou um valor específico para categorias pré-definidas/públicas, dependendo da sua regra).
     * @param tipo "DESPESA" ou "RECEITA".
     * @return Lista de categorias.
     */
    public List<Categoria> listarCategoriasPorUsuarioETipo(int idUsuario, String tipo) {
        List<Categoria> categorias = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            // Busca categorias pré-definidas (ID_USUARIO IS NULL) OU categorias do usuário específico
            String sql = "SELECT ID_CATEGORIA, ID_USUARIO, NOME, TIPO, PRE_DEFINIDA FROM T_DIN_CATEGORIAS " +
                "WHERE TIPO = ? AND (ID_USUARIO IS NULL OR ID_USUARIO = ?) " +
                "ORDER BY NOME";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            stmt.setInt(2, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idCategoria = rs.getInt("ID_CATEGORIA");
                int fetchedIdUsuario = rs.getInt("ID_USUARIO"); // Pode ser 0 se for NULL no BD
                String nome = rs.getString("NOME");
                boolean preDefinida = rs.getInt("PRE_DEFINIDA") == 1; // Oracle usa NUMBER(1) para boolean

                // O construtor Categoria(idCategoria, idUsuario, nome, tipo, preDefinida) é ideal aqui
                categorias.add(new Categoria(idCategoria, fetchedIdUsuario, nome, tipo, preDefinida));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias por usuário e tipo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.getInstance().closeConnection(conn, stmt, rs);
        }
        return categorias;
    }
    // Você pode adicionar métodos para atualizar ou deletar categorias aqui, se necessário.
}