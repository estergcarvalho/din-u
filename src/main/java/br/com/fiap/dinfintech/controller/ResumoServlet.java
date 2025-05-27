package br.com.fiap.dinfintech.controller;

import br.com.fiap.dinfintech.dao.DespesaDao;
import br.com.fiap.dinfintech.dao.ReceitaDao;
import br.com.fiap.dinfintech.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet("/resumo")
public class ResumoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ReceitaDao receitaDao;
    private DespesaDao despesaDao;

    @Override
    public void init() throws ServletException {
        this.receitaDao = new ReceitaDao();
        this.despesaDao = new DespesaDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int idUsuario = usuarioLogado.getIdUsuario();

        int mesAtual = LocalDate.now().getMonthValue();
        int anoAtual = LocalDate.now().getYear();

        int mes = mesAtual;
        int ano = anoAtual;

        try {
            String mesParam = request.getParameter("mes");
            String anoParam = request.getParameter("ano");

            if (mesParam != null && !mesParam.isEmpty()) {
                mes = Integer.parseInt(mesParam);
            }
            if (anoParam != null && !anoParam.isEmpty()) {
                ano = Integer.parseInt(anoParam);
            }
        } catch (NumberFormatException e) {
            System.err.println("Parâmetro de mês ou ano inválido recebido: " + e.getMessage());
        }

        double totalReceitas = receitaDao.getTotalReceitasPorMes(idUsuario, mes, ano);
        double totalDespesas = despesaDao.getTotalDespesasPorMes(idUsuario, mes, ano);
        double saldoFinal = totalReceitas - totalDespesas;

        Locale localePtBr = new Locale("pt", "BR");

        String nomeMes = Month.of(mes).getDisplayName(TextStyle.FULL, localePtBr);
        nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1); // Capitaliza a primeira letra

        Map<Integer, String> mesesSelect = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            String nome = Month.of(i).getDisplayName(TextStyle.FULL, localePtBr);
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1); // Capitaliza para o select
            mesesSelect.put(i, nome);
        }

        List<Integer> anosDisponiveis = new ArrayList<>();
        for (int i = anoAtual - 5; i <= anoAtual + 1; i++) {
            anosDisponiveis.add(i);
        }

        Map<String, Double> receitasPorMesAno = new LinkedHashMap<>();
        Map<String, Double> despesasPorMesAno = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            String nomeMesCurto = Month.of(i).getDisplayName(TextStyle.SHORT, localePtBr);
            nomeMesCurto = nomeMesCurto.substring(0, 1).toUpperCase() + nomeMesCurto.substring(1);

            double totalReceitasMes = receitaDao.getTotalReceitasPorMes(idUsuario, i, ano);
            double totalDespesasMes = despesaDao.getTotalDespesasPorMes(idUsuario, i, ano);

            receitasPorMesAno.put(nomeMesCurto, totalReceitasMes);
            despesasPorMesAno.put(nomeMesCurto, totalDespesasMes);
        }

        request.setAttribute("mes", mes);
        request.setAttribute("ano", ano);
        request.setAttribute("nomeMes", nomeMes);
        request.setAttribute("totalReceitas", totalReceitas);
        request.setAttribute("totalDespesas", totalDespesas);
        request.setAttribute("saldoFinal", saldoFinal);
        request.setAttribute("meses", mesesSelect);
        request.setAttribute("anos", anosDisponiveis);
        request.setAttribute("receitasPorMesAno", receitasPorMesAno);
        request.setAttribute("despesasPorMesAno", despesasPorMesAno);

        request.getRequestDispatcher("/resumoMensal.jsp").forward(request, response);
    }
}