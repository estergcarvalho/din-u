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
            stmt.setInt(4, categoria.isPreDefinida() ? 1 : 0);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                sucesso = true;
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
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
                int idUsuario = rs.getInt("ID_USUARIO");
                String nome = rs.getString("NOME");
                String tipo = rs.getString("TIPO");
                boolean preDefinida = rs.getInt("PRE_DEFINIDA") == 1;

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

    public List<Categoria> listarCategoriasPorUsuarioETipo(int idUsuario, String tipo) {
        List<Categoria> categorias = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT ID_CATEGORIA, ID_USUARIO, NOME, TIPO, PRE_DEFINIDA FROM T_DIN_CATEGORIAS " +
                "WHERE TIPO = ? AND (ID_USUARIO IS NULL OR ID_USUARIO = ?) " +
                "ORDER BY NOME";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            stmt.setInt(2, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idCategoria = rs.getInt("ID_CATEGORIA");
                int fetchedIdUsuario = rs.getInt("ID_USUARIO");
                String nome = rs.getString("NOME");
                boolean preDefinida = rs.getInt("PRE_DEFINIDA") == 1;

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

}