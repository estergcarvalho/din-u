package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.MetaDao;
import br.com.fiap.dinfintech.model.Meta;
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

@WebServlet("/metas")
public class MetaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MetaDao metaDAO;

    public void init() throws ServletException {
        this.metaDAO = new MetaDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int idUsuario = usuarioLogado.getIdUsuario();

        if ("cadastrar".equals(action)) {
            request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
        } else if ("listar".equals(action)) {
            List<Meta> metas = metaDAO.listarMetasPorUsuario(idUsuario);
            request.setAttribute("listaMetas", metas);
            request.getRequestDispatcher("/listaMetas.jsp").forward(request, response);
        } else if ("editar".equals(action)) {
            try {
                int idMeta = Integer.parseInt(request.getParameter("id"));
                Meta meta = metaDAO.buscarMetaPorId(idMeta);

                if (meta != null && meta.getIdUsuario() == idUsuario) {
                    request.setAttribute("meta", meta);
                    // ***** CORREÇÃO AQUI: REDIRECIONAR PARA edicaoMeta.jsp *****
                    request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                } else {
                    request.setAttribute("mensagemErro", "Meta não encontrada ou você não tem permissão para editá-la.");
                    response.sendRedirect(request.getContextPath() + "/metas?action=listar");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("mensagemErro", "ID de meta inválido.");
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
            }
        } else if ("excluir".equals(action)) {
            try {
                int idMeta = Integer.parseInt(request.getParameter("id"));
                boolean excluido = metaDAO.excluirMeta(idMeta, idUsuario);

                if (excluido) {
                    request.setAttribute("mensagemSucesso", "Meta excluída com sucesso!");
                } else {
                    request.setAttribute("mensagemErro", "Erro ao excluir meta ou você não tem permissão.");
                }
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
            } catch (NumberFormatException e) {
                request.setAttribute("mensagemErro", "ID de meta inválido para exclusão.");
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/metas?action=listar");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int idUsuario = usuarioLogado.getIdUsuario();

        if ("cadastrar".equals(action)) {
            String descricao = request.getParameter("descricao");
            String valorAlvoStr = request.getParameter("valorAlvo");
            String dataAlvoStr = request.getParameter("dataAlvo");
            String statusMeta = request.getParameter("statusMeta");
            String prioridade = request.getParameter("prioridade");

            if (descricao == null || descricao.trim().isEmpty() ||
                valorAlvoStr == null || valorAlvoStr.trim().isEmpty() ||
                dataAlvoStr == null || dataAlvoStr.trim().isEmpty() ||
                statusMeta == null || statusMeta.trim().isEmpty() ||
                prioridade == null || prioridade.trim().isEmpty()) {
                request.setAttribute("mensagemErro", "Todos os campos da meta são obrigatórios.");
                request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
                return;
            }

            double valorAlvo;
            try {
                valorAlvo = Double.parseDouble(valorAlvoStr);
                if (valorAlvo <= 0) {
                    request.setAttribute("mensagemErro", "O valor alvo da meta deve ser positivo.");
                    request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("mensagemErro", "Valor alvo da meta inválido.");
                request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
                return;
            }

            LocalDate dataAlvo;
            try {
                dataAlvo = LocalDate.parse(dataAlvoStr);
            } catch (DateTimeParseException e) {
                request.setAttribute("mensagemErro", "Formato de data inválido. Use AAAA-MM-DD.");
                request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
                return;
            }

            descricao = descricao.trim();
            statusMeta = statusMeta.trim();
            prioridade = prioridade.trim();

            Meta novaMeta = new Meta(idUsuario, descricao, valorAlvo, dataAlvo, statusMeta, prioridade);
            novaMeta.setValorAtual(0.0);
            novaMeta.setDataCriacao(LocalDate.now());

            boolean cadastrado = metaDAO.cadastrarMeta(novaMeta);

            if (cadastrado) {
                response.sendRedirect(request.getContextPath() + "/metas?action=listar&cadastroMetaSucesso=true");
            } else {
                request.setAttribute("mensagemErro", "Erro ao cadastrar meta. Tente novamente.");
                request.getRequestDispatcher("/cadastroMeta.jsp").forward(request, response);
            }
        } else if ("atualizar".equals(action)) {
            String idMetaStr = request.getParameter("idMeta");
            if (idMetaStr == null || idMetaStr.isEmpty()) {
                request.setAttribute("mensagemErro", "ID da meta não fornecido para atualização.");
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
                return;
            }
            int idMeta;
            try {
                idMeta = Integer.parseInt(idMetaStr);
            } catch (NumberFormatException e) {
                request.setAttribute("mensagemErro", "ID da meta inválido para atualização.");
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
                return;
            }

            String descricao = request.getParameter("descricao");
            String valorAlvoStr = request.getParameter("valorAlvo");
            String valorAtualStr = request.getParameter("valorAtual");
            String dataAlvoStr = request.getParameter("dataAlvo");
            String statusMeta = request.getParameter("statusMeta");
            String prioridade = request.getParameter("prioridade");


            if (descricao == null || descricao.trim().isEmpty() ||
                valorAlvoStr == null || valorAlvoStr.trim().isEmpty() ||
                valorAtualStr == null || valorAtualStr.trim().isEmpty() ||
                dataAlvoStr == null || dataAlvoStr.trim().isEmpty() ||
                statusMeta == null || statusMeta.trim().isEmpty() ||
                prioridade == null || prioridade.trim().isEmpty()) {
                request.setAttribute("mensagemErro", "Todos os campos da meta são obrigatórios.");
                Meta metaExistente = metaDAO.buscarMetaPorId(idMeta); // Busca para manter os dados no formulário
                request.setAttribute("meta", metaExistente);
                request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                return;
            }

            double valorAlvo, valorAtual;
            try {
                valorAlvo = Double.parseDouble(valorAlvoStr);
                valorAtual = Double.parseDouble(valorAtualStr);
                if (valorAlvo <= 0) {
                    request.setAttribute("mensagemErro", "O valor alvo da meta deve ser positivo.");
                    Meta metaExistente = metaDAO.buscarMetaPorId(idMeta);
                    request.setAttribute("meta", metaExistente);
                    request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                    return;
                }
                if (valorAtual < 0) {
                    request.setAttribute("mensagemErro", "O valor atual da meta não pode ser negativo.");
                    Meta metaExistente = metaDAO.buscarMetaPorId(idMeta);
                    request.setAttribute("meta", metaExistente);
                    request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                    return;
                }
                // Lógica para auto-completar status para "Concluída" se valorAtual >= valorAlvo
                if (valorAtual >= valorAlvo) {
                    valorAtual = valorAlvo; // Garante que não ultrapasse o alvo visualmente
                    statusMeta = "Concluída"; // Atualiza o status automaticamente
                }
            } catch (NumberFormatException e) {
                request.setAttribute("mensagemErro", "Valor alvo ou valor atual da meta inválido.");
                Meta metaExistente = metaDAO.buscarMetaPorId(idMeta);
                request.setAttribute("meta", metaExistente);
                request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                return;
            }

            LocalDate dataAlvo;
            try {
                dataAlvo = LocalDate.parse(dataAlvoStr);
            } catch (DateTimeParseException e) {
                request.setAttribute("mensagemErro", "Formato de data inválido. Use AAAA-MM-DD.");
                Meta metaExistente = metaDAO.buscarMetaPorId(idMeta);
                request.setAttribute("meta", metaExistente);
                request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
                return;
            }

            descricao = descricao.trim();
            statusMeta = statusMeta.trim();
            prioridade = prioridade.trim();

            Meta metaParaAtualizar = metaDAO.buscarMetaPorId(idMeta);
            if (metaParaAtualizar == null || metaParaAtualizar.getIdUsuario() != idUsuario) {
                request.setAttribute("mensagemErro", "Meta não encontrada ou você não tem permissão para atualizá-la.");
                response.sendRedirect(request.getContextPath() + "/metas?action=listar");
                return;
            }

            metaParaAtualizar.setDescricao(descricao);
            metaParaAtualizar.setValorAlvo(valorAlvo);
            metaParaAtualizar.setValorAtual(valorAtual);
            metaParaAtualizar.setDataAlvo(dataAlvo);
            metaParaAtualizar.setStatusMeta(statusMeta);
            metaParaAtualizar.setPrioridade(prioridade);

            boolean atualizado = metaDAO.atualizarMeta(metaParaAtualizar);

            if (atualizado) {
                response.sendRedirect(request.getContextPath() + "/metas?action=listar&atualizacaoMetaSucesso=true");
            } else {
                request.setAttribute("mensagemErro", "Erro ao atualizar meta. Verifique os dados e tente novamente.");
                request.setAttribute("meta", metaParaAtualizar);
                request.getRequestDispatcher("/edicaoMeta.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/metas?action=listar");
        }
    }
}