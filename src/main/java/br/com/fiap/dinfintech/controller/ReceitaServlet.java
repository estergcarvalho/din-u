package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.ReceitaDao;
import br.com.fiap.dinfintech.model.Receita;
import br.com.fiap.dinfintech.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/receitas")
public class ReceitaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ReceitaDao receitaDAO;

    public void init() throws ServletException {
        this.receitaDAO = new ReceitaDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("cadastrar".equals(action)) {
            request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
        } else {
            Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

            if (usuarioLogado == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            List<Receita> receitas = receitaDAO.listarReceitasPorUsuario(usuarioLogado.getIdUsuario());
            request.setAttribute("listaReceitas", receitas);
            request.getRequestDispatcher("/home.jsp").forward(request, response);
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

        String descricao = request.getParameter("descricao");
        String valorStr = request.getParameter("valor");
        String dataReceitaStr = request.getParameter("dataReceita");
        String tipoReceita = request.getParameter("tipoReceita");

        if (descricao == null || descricao.trim().isEmpty() ||
            valorStr == null || valorStr.trim().isEmpty() ||
            dataReceitaStr == null || dataReceitaStr.trim().isEmpty() ||
            tipoReceita == null || tipoReceita.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Todos os campos da receita são obrigatórios.");
            request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                request.setAttribute("mensagemErro", "O valor da receita deve ser positivo.");
                request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("mensagemErro", "Valor da receita inválido.");
            request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
            return;
        }

        LocalDate dataReceita;
        try {
            dataReceita = LocalDate.parse(dataReceitaStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("mensagemErro", "Formato de data inválido. Use AAAA-MM-DD.");
            request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
            return;
        }

        descricao = descricao.trim();
        tipoReceita = tipoReceita.trim();

        Receita novaReceita = new Receita(idUsuario, descricao, valor, dataReceita, tipoReceita);

        boolean cadastrado = receitaDAO.cadastrarReceita(novaReceita);

        if (cadastrado) {
            response.sendRedirect(request.getContextPath() + "/home.jsp?cadastroReceitaSucesso=true");
        } else {
            request.setAttribute("mensagemErro", "Erro ao cadastrar receita. Tente novamente.");
            request.getRequestDispatcher("/cadastroReceita.jsp").forward(request, response);
        }
    }
}