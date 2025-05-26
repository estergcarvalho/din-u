package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.UsuarioDao;
import br.com.fiap.dinfintech.model.Usuario;
import jakarta.servlet.ServletException; // Use jakarta.servlet
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDao usuarioDAO;

    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. Obter todos os parâmetros do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("password");
        String confirmarSenha = request.getParameter("confirmPassword");

        // Obtenha os campos adicionais.
        // Se você não descomentou eles no JSP, eles virão como null/vazio,
        // então a lógica abaixo vai atribuir valores padrão.
        String sobrenome = request.getParameter("sobrenome");
        String dtNascimento = request.getParameter("dtNascimento");
        String cpf = request.getParameter("cpf");
        String sexo = request.getParameter("sexo");

        // Lógica para atribuir valores padrão se os campos não forem enviados do formulário
        if (sobrenome == null || sobrenome.trim().isEmpty()) {
            sobrenome = "Não Informado";
        }
        if (dtNascimento == null || dtNascimento.trim().isEmpty()) {
            dtNascimento = "01/01/1900"; // Formato esperado pelo Oracle (DD/MM/YYYY ou YYYY-MM-DD dependendo do NLS_DATE_FORMAT)
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            cpf = "000.000.000-00";
        }
        if (sexo == null || sexo.trim().isEmpty()) {
            sexo = "NAO_INFORMADO";
        }


        // 2. Validação básica de campos obrigatórios do formulário (Nome, E-mail, Senha)
        if (nome == null || nome.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            senha == null || senha.trim().isEmpty() ||
            confirmarSenha == null || confirmarSenha.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Nome, E-mail e Senha são campos obrigatórios.");
            request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            request.setAttribute("mensagemErro", "A senha e a confirmação de senha não coincidem.");
            request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
            return;
        }

        // 3. Cria o objeto Usuário
        Usuario novoUsuario = new Usuario(nome, sobrenome, dtNascimento, cpf, sexo, email, senha);

        // 4. Tenta cadastrar o usuário
        var cadastrado = usuarioDAO.cadastrarUsuario(novoUsuario);

        // 5. Redireciona ou exibe mensagem
        if (cadastrado) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?cadastroSucesso=true");
        } else {
            // A mensagem de erro específica para e-mail já cadastrado é tratada no DAO e impressa no console.
            // Aqui, podemos retornar uma mensagem genérica ou uma mais específica se o DAO retornar um código de erro.
            request.setAttribute("mensagemErro", "Erro ao cadastrar usuário. O e-mail informado já pode estar em uso.");
            request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/cadastro").forward(request, response);
    }
}