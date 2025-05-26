package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.UsuarioDao;
import br.com.fiap.dinfintech.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login") // Mapeia este Servlet para a URL /login
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDao usuarioDao;

    public void init() throws ServletException {
        this.usuarioDao = new UsuarioDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        // Validação básica de campos vazios
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Email e senha são obrigatórios.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validação do usuário no banco de dados
        Usuario usuario = usuarioDao.validarUsuario(email, senha);

        if (usuario != null) {
            // Login bem-sucedido: Armazena informações na sessão e redireciona
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario); // Armazena o objeto Usuario na sessão
            response.sendRedirect(request.getContextPath() + "/home.jsp"); // Redireciona para a página principal
        } else {
            // Login falhou: Exibe mensagem de erro na página de login
            request.setAttribute("mensagemErro", "Email ou senha inválidos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response); // <-- CORREÇÃO AQUI
        }
    }

    // Se a página de login for acessada via GET, apenas encaminha para o JSP
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
