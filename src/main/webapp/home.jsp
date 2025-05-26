<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.fiap.dinfintech.model.Usuario" %>
<%@ page import="br.com.fiap.dinfintech.model.Despesa" %>
<%@ page import="br.com.fiap.dinfintech.dao.DespesaDao" %>
<%@ page import="br.com.fiap.dinfintech.model.Receita" %>
<%@ page import="br.com.fiap.dinfintech.dao.ReceitaDao" %>
<%@ page import="br.com.fiap.dinfintech.dao.UsuarioDao" %> <%-- **MUDANÇA 1: NOVO IMPORT** --%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %> <%@ page import="java.util.HashMap" %> <%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Din+ - Página Inicial</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <style>
    body {
      font-family: 'Inter', sans-serif;
      margin: 0;
      background-color: #f0f2f5;
      color: #333;
    }
    .header {
      background-color: #f7931e;
      color: white;
      padding: 15px 50px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    .header .logo {
      font-size: 24px;
      font-weight: bold;
      text-decoration: none;
      color: white;
    }
    .nav-menu ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
    }
    .nav-menu li {
      margin-right: 30px;
    }
    .nav-menu a {
      color: white;
      text-decoration: none;
      font-size: 16px;
      padding: 5px 0;
      position: relative;
    }
    .nav-menu a:hover::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: -3px;
      width: 100%;
      height: 2px;
      background-color: white;
    }
    .nav-menu .dropdown {
      position: relative;
    }
    .nav-menu .dropdown-content {
      display: none;
      position: absolute;
      background-color: #f7931e;
      min-width: 160px;
      box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
      z-index: 1;
      border-radius: 5px;
      padding-top: 10px;
    }
    .nav-menu .dropdown-content a {
      color: white;
      padding: 12px 16px;
      text-decoration: none;
      display: block;
      text-align: left;
    }
    .nav-menu .dropdown-content a:hover {
      background-color: #e08010;
      border-radius: 5px;
    }
    .nav-menu .dropdown:hover .dropdown-content {
      display: block;
    }
    .logout-btn {
      background-color: transparent;
      border: none;
      color: white;
      font-size: 16px;
      cursor: pointer;
      text-decoration: none;
      padding: 0;
    }
    .main-content {
      padding: 40px 50px;
      max-width: 1200px;
      margin: 20px auto;
    }
    .saldo-card {
      background-color: #555;
      color: white;
      padding: 30px;
      border-radius: 10px;
      text-align: center;
      margin-bottom: 40px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    .saldo-card h3 {
      margin-top: 0;
      font-size: 18px;
      color: #ccc;
    }

    .saldo-card .saldo-valor {
      font-size: 48px;
      font-weight: bold;
      margin: 10px 0;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .saldo-card .saldo-valor.positivo {
      color: #28a745;
    }
    .saldo-card .saldo-valor.negativo {
      color: #dc3545;
    }
    .saldo-card .saldo-valor.zero {
      color: #f7931e;
    }

    .saldo-card .saldo-valor i {
      font-size: 24px;
      margin-left: 10px;
      cursor: pointer;
      color: #aaa;
    }
    .section-title {
      font-size: 24px;
      margin-bottom: 25px;
      color: #333;
      text-align: center;
    }
    .list-section {
      margin-bottom: 40px;
    }
    .list-container {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
      overflow: hidden;
      max-width: 700px;
      margin: 0 auto;
    }
    .list-item {
      display: flex;
      align-items: center;
      padding: 15px 25px;
      border-bottom: 1px solid #eee;
    }
    .list-item:last-child {
      border-bottom: none;
    }
    .list-item i {
      font-size: 24px;
      color: #777;
      margin-right: 15px;
      width: 30px;
      text-align: center;
    }
    .list-item .item-info {
      flex-grow: 1;
    }
    .list-item .item-info p {
      margin: 0;
      font-size: 16px;
      color: #555;
    }
    .list-item .item-info .data {
      font-size: 14px;
      color: #888;
    }
    .list-item .item-valor {
      font-size: 18px;
      font-weight: bold;
    }
    .list-item.despesa .item-valor {
      color: #dc3545;
    }
    .list-item.receita .item-valor {
      color: #28a745;
    }
    .categoria-section {
      margin-bottom: 40px;
    }
    .categorias-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 25px;
    }
    .categoria-card {
      background-color: #fff;
      padding: 25px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
      text-align: center;
      transition: transform 0.2s ease-in-out;
    }
    .categoria-card:hover {
      transform: translateY(-5px);
    }
    .categoria-card i {
      font-size: 36px;
      color: #f7931e;
      margin-bottom: 15px;
    }
    .categoria-card h4 {
      margin: 0;
      font-size: 18px;
      color: #555;
    }
    .categoria-card p {
      font-size: 22px;
      font-weight: bold;
      color: #333;
      margin-top: 10px;
    }
    .center-message {
      text-align: center;
      margin: 50px 0;
    }
    .center-message img {
      width: 80px;
      margin-bottom: 15px;
    }
    .center-message p {
      font-size: 18px;
      color: #555;
      max-width: 500px;
      margin: 0 auto;
    }
    .footer {
      background-color: #f7931e;
      color: white;
      text-align: center;
      padding: 20px 0;
      margin-top: 50px;
    }
    .footer i {
      margin-right: 8px;
    }

    .message-container {
      max-width: 700px;
      margin: 20px auto;
      padding: 15px;
      border-radius: 5px;
      text-align: center;
      font-weight: bold;
    }
    .message-success {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }
    .message-error {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }
  </style>
</head>
<body>
<%
  Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

  if (usuarioLogado == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }

  UsuarioDao usuarioDao = new UsuarioDao();
  double saldoAtual = usuarioDao.buscarSaldoUsuario(usuarioLogado.getIdUsuario());

  usuarioLogado.setSaldo(saldoAtual);
  session.setAttribute("usuarioLogado", usuarioLogado);

  DespesaDao despesaDAO = new DespesaDao();
  List<Despesa> despesasDoUsuario = despesaDAO.listarDespesasPorUsuario(usuarioLogado.getIdUsuario());

  Map<String, Double> totalDespesasPorCategoria = despesaDAO.calcularTotalDespesasPorCategoria(usuarioLogado.getIdUsuario());

  ReceitaDao receitaDAO = new ReceitaDao();
  List<Receita> receitasDoUsuario = receitaDAO.listarReceitasPorUsuario(usuarioLogado.getIdUsuario());

  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  String cadastroDespesaSucesso = request.getParameter("cadastroDespesaSucesso");
  String cadastroReceitaSucesso = request.getParameter("cadastroReceitaSucesso");

  Map<String, String> iconesCategorias = new HashMap<>();
  iconesCategorias.put("Alimentação", "fas fa-utensils");
  iconesCategorias.put("Moradia", "fas fa-home");
  iconesCategorias.put("Transporte", "fas fa-car");
  iconesCategorias.put("Saúde", "fas fa-pills");
  iconesCategorias.put("Educação", "fas fa-book");
  iconesCategorias.put("Lazer", "fas fa-gamepad");
  iconesCategorias.put("Contas Residenciais", "fas fa-file-invoice-dollar");
  iconesCategorias.put("Outras Despesas", "fas fa-ellipsis-h");
  iconesCategorias.put("Compras", "fas fa-shopping-cart");
  iconesCategorias.put("Mercado", "fas fa-shopping-basket");
  iconesCategorias.put("Serviços", "fas fa-tools");
%>

<div class="header">
  <a href="<%= request.getContextPath() %>/home.jsp" class="logo">Din+</a>
  <nav class="nav-menu">
    <ul>
      <li><a href="<%= request.getContextPath() %>/home.jsp">Página inicial</a></li>
      <li><a href="#">Carteira</a></li>
      <li><a href="#">Estatísticas</a></li>
      <li><a href="<%= request.getContextPath() %>/metas?action=listar">Metas</a></li>
      <li class="dropdown">
        <a href="#" class="dropbtn">Cadastro <i class="fas fa-caret-down"></i></a>
        <div class="dropdown-content">
          <a href="<%= request.getContextPath() %>/despesas?action=cadastrar">Nova Despesa</a> <%-- **MUDANÇA 4: ATUALIZADO PARA SERVLET** --%>
          <a href="<%= request.getContextPath() %>/receitas?action=cadastrar">Nova Receita</a> <%-- **MUDANÇA 4: ATUALIZADO PARA SERVLET** --%>
          <a href="<%= request.getContextPath() %>/metas?action=cadastrar">Nova Meta</a>
          <a href="<%= request.getContextPath() %>/categorias?action=cadastrar">Nova Categoria</a>
        </div>
      </li>
      <li><a href="<%= request.getContextPath() %>/categorias?action=listar">Categorias</a></li>
    </ul>
  </nav>
  <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Sair</a>
</div>

<div class="main-content">

  <% if ("true".equals(cadastroDespesaSucesso)) { %>
  <div class="message-container message-success">
    Despesa cadastrada com sucesso!
  </div>
  <% } %>
  <% if ("true".equals(cadastroReceitaSucesso)) { %>
  <div class="message-container message-success">
    Receita cadastrada com sucesso!
  </div>
  <% } %>

  <div class="saldo-card">
    <h3>Saldo da Conta</h3>
    <%
      String saldoClass = "saldo-valor";
      if (saldoAtual > 0) {
        saldoClass += " positivo";
      } else if (saldoAtual < 0) {
        saldoClass += " negativo";
      } else {
        saldoClass += " zero";
      }
    %>
    <div class="<%= saldoClass %>">
      R$ <%= String.format("%.2f", saldoAtual).replace(",", "x").replace(".", ",").replace("x", ".") %> <%-- **MUDANÇA 7: EXIBIÇÃO DO SALDO REAL** --%>
      <i class="fas fa-eye"></i>
    </div>
  </div>

  <div class="categoria-section">
    <h2 class="section-title">Gastos por categoria</h2>
    <div class="categorias-grid">
      <% if (totalDespesasPorCategoria != null && !totalDespesasPorCategoria.isEmpty()) { %>
      <% for (Map.Entry<String, Double> entry : totalDespesasPorCategoria.entrySet()) {
        String categoriaNome = entry.getKey();
        Double totalGasto = entry.getValue();
        String icone = iconesCategorias.getOrDefault(categoriaNome, "fas fa-tag");
      %>
      <div class="categoria-card">
        <i class="<%= icone %>"></i>
        <h4><%= categoriaNome %></h4>
        <p>R$<%= String.format("%.2f", totalGasto).replace(",", "x").replace(".", ",").replace("x", ".") %></p>
      </div>
      <% } %>
      <% } else { %>
      <div class="center-message">
        <i class="fas fa-chart-pie" style="font-size: 50px; color: #ccc;"></i>
        <p>Nenhum gasto registrado por categoria ainda.</p>
      </div>
      <% } %>
    </div>
  </div>

  <div class="list-section">
    <h2 class="section-title">Despesas recentes</h2>
    <div class="list-container">
      <% if (despesasDoUsuario != null && !despesasDoUsuario.isEmpty()) { %>
      <% for (Despesa despesa : despesasDoUsuario) { %>
      <div class="list-item despesa">
        <i class="fas fa-money-bill-wave-alt"></i>
        <div class="item-info">
          <p><%= despesa.getDescricao() %></p>
          <span class="data"><%= despesa.getDataDespesa().format(dateFormatter) %> - <%= despesa.getCategoria().getNome() %></span> </div>
        <span class="item-valor">- R$<%= String.format("%.2f", despesa.getValor()).replace(",", "x").replace(".", ",").replace("x", ".") %></span>
      </div>
      <% } %>
      <% } else { %>
      <div class="list-item">
        <div class="item-info">
          <p>Nenhuma despesa cadastrada ainda.</p>
        </div>
      </div>
      <% } %>
    </div>
  </div>

  <div class="list-section">
    <h2 class="section-title">Receitas recentes</h2>
    <div class="list-container">
      <% if (receitasDoUsuario != null && !receitasDoUsuario.isEmpty()) { %>
      <% for (Receita receita : receitasDoUsuario) { %>
      <div class="list-item receita">
        <i class="fas fa-dollar-sign"></i>
        <div class="item-info">
          <p><%= receita.getDescricao() %></p>
          <span class="data"><%= receita.getDataReceita().format(dateFormatter) %> - <%= receita.getTipoReceita() %></span>
        </div>
        <span class="item-valor">+ R$<%= String.format("%.2f", receita.getValor()).replace(",", "x").replace(".", ",").replace("x", ".") %></span>
      </div>
      <% } %>
      <% } else { %>
      <div class="list-item">
        <div class="item-info">
          <p>Nenhuma receita cadastrada ainda.</p>
        </div>
      </div>
      <% } %>
    </div>
  </div>

  <div class="center-message">
    <i class="fas fa-money-bill-wave" style="font-size: 50px; color: #f7931e; margin-bottom: 15px;"></i>
    <p>Organize seu dinheiro, conquiste seus objetivos.</p>
  </div>

</div>
<div class="footer">
  <i class="fas fa-home"></i> Direitos reservados à Din+
</div>
</body>
</html>