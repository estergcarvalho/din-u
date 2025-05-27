package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.DespesaDao;
import br.com.fiap.dinfintech.dao.CategoriaDao;
import br.com.fiap.dinfintech.model.Despesa;
import br.com.fiap.dinfintech.model.Usuario;
import br.com.fiap.dinfintech.model.Categoria;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@WebServlet("/despesas")
public class DespesaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DespesaDao despesaDao;
    private CategoriaDao categoriaDao;

    public void init() throws ServletException {
        this.despesaDao = new DespesaDao();
        this.categoriaDao = new CategoriaDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "cadastrar":
                mostrarFormularioCadastro(request, response);
                break;
            case "listar":
                response.sendRedirect(request.getContextPath() + "/home.jsp");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "cadastrar";
        }

        switch (action) {
            case "cadastrar":
                cadastrarDespesa(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home.jsp");
        }
    }

    private void mostrarFormularioCadastro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        CategoriaDao categoriaDao = new CategoriaDao();
        List<Categoria> categoriasDespesa = categoriaDao.listarCategoriasPorUsuarioETipo(usuarioLogado.getIdUsuario(), "DESPESA");
        request.setAttribute("categoriasDespesa", categoriasDespesa);

        request.getRequestDispatcher("/cadastroDespesa.jsp").forward(request, response);
    }

    private void cadastrarDespesa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String descricao = request.getParameter("descricao");
            double valor = Double.parseDouble(request.getParameter("valor").replace(",", "."));
            LocalDate dataDespesa = LocalDate.parse(request.getParameter("dataDespesa"));
            int idCategoriaSelecionada = Integer.parseInt(request.getParameter("categoriaId"));

            DespesaDao despesaDAO = new DespesaDao();
            CategoriaDao categoriaDAO = new CategoriaDao();

            Categoria categoria = categoriaDAO.buscarCategoriaPorId(idCategoriaSelecionada);

            if (categoria != null && categoria.getTipo().equals("DESPESA")) {
                Despesa despesa = new Despesa(usuarioLogado.getIdUsuario(), descricao, valor, dataDespesa, categoria);

                if (despesaDAO.cadastrarDespesa(despesa)) {
                    response.sendRedirect(request.getContextPath() + "/home.jsp?cadastroDespesaSucesso=true");
                } else {
                    request.setAttribute("mensagemErro", "Erro ao cadastrar despesa no banco de dados.");
                    mostrarFormularioCadastro(request, response);
                }
            } else {
                request.setAttribute("mensagemErro", "Categoria selecionada inválida ou não é uma categoria de despesa.");
                mostrarFormularioCadastro(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("mensagemErro", "Valor ou categoria inválido. Certifique-se de usar números e selecionar uma categoria.");
            mostrarFormularioCadastro(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado: " + e.getMessage());
            mostrarFormularioCadastro(request, response);
        }
    }
}