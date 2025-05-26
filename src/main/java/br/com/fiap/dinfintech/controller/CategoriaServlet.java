package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.CategoriaDao;
import br.com.fiap.dinfintech.model.Categoria;
import br.com.fiap.dinfintech.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/categorias")
public class CategoriaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CategoriaDao categoriaDao;

    public void init() throws ServletException {
        this.categoriaDao = new CategoriaDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if ("cadastrar".equals(action)) {
            // Encaminha para a página de formulário de cadastro de categoria
            request.getRequestDispatcher("/cadastroCategoria.jsp").forward(request, response);
        } else if ("listar".equals(action)) {
            // Lista todas as categorias disponíveis para o usuário logado
            List<Categoria> categorias = categoriaDao.listarCategoriasPorUsuarioETipo(usuarioLogado.getIdUsuario(), null); // Pega todos os tipos
            request.setAttribute("listaCategorias", categorias);
            request.getRequestDispatcher("/listaCategorias.jsp").forward(request, response);
        } else {
            // Ação padrão: listar categorias
            response.sendRedirect(request.getContextPath() + "/categorias?action=listar");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int idUsuario = usuarioLogado.getIdUsuario();

        // 1. Obter parâmetros do formulário
        String nome = request.getParameter("nome");
        String tipo = request.getParameter("tipo");

        if (nome == null || nome.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Nome e Tipo da categoria são obrigatórios.");
            request.getRequestDispatcher("/cadastroCategoria.jsp").forward(request, response);
            return;
        }

        nome = nome.trim();
        tipo = tipo.trim().toUpperCase();

        Categoria novaCategoria = new Categoria(idUsuario, nome, tipo);

        boolean cadastrado = categoriaDao.cadastrarCategoria(novaCategoria);

        if (cadastrado) {
            request.setAttribute("mensagemSucesso", "Categoria cadastrada com sucesso!");
            response.sendRedirect(request.getContextPath() + "/categorias?action=listar&cadastroSucesso=true");
        } else {
            request.setAttribute("mensagemErro", "Erro ao cadastrar categoria. Verifique se o nome já existe para este tipo.");
            request.getRequestDispatcher("/cadastroCategoria.jsp").forward(request, response);
        }
    }
}