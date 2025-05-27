<%-- src/main/webapp/listaMetas.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.fiap.dinfintech.model.Usuario" %>
<%@ page import="br.com.fiap.dinfintech.model.Meta" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Din+ - Minhas Metas</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <style>
    body {
      font-family: 'Inter', sans-serif;
      margin: 0;
      background-color: #f0f2f5;
      color: #333;
      display: flex;
      flex-direction: column;
      min-height: 100vh;
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

    .container {
      flex-grow: 1;
      padding: 20px;
      max-width: 1200px;
      width: 90%;
      margin: 20px auto;
      background-color: #fff;
      border-radius: 10px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }

    .meta-card {
      background-color: #fff;
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 15px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
      transition: transform 0.2s ease-in-out;
      margin-bottom: 15px;
      width: 100%;
      box-sizing: border-box;
    }

    .meta-card:hover {
      transform: translateY(-5px);
    }

    .meta-card h3 {
      color: #333;
      margin-top: 0;
      margin-bottom: 5px;
      font-size: 18px;
      font-weight: bold;
    }

    .meta-card p {
      margin: 5px 0;
      font-size: 14px;
      color: #555;
    }

    .meta-card p strong {
      color: #333;
      font-weight: bold;
    }

    .progress-bar-container {
      background-color: #e0e0e0;
      border-radius: 5px;
      height: 10px;
      margin: 10px 0;
      overflow: hidden;
    }

    .progress-bar {
      background-color: #f7931e;
      height: 100%;
      border-radius: 5px;
      width: 0%;
      transition: width 0.5s ease-in-out;
    }

    .meta-card .status {
      font-weight: bold;
      padding: 5px 10px;
      border-radius: 4px;
      display: inline-block;
      margin-top: 10px;
    }

    .status.em-progresso { background-color: #ffc107; color: #333; }
    .status.concluida { background-color: #28a745; color: white; }
    .status.pendente { background-color: #007bff; color: white; }
    .status.cancelada { background-color: #dc3545; color: white; }

    .no-metas {
      text-align: center;
      font-size: 18px;
      color: #777;
      padding: 50px 0;
    }
    .footer {
      background-color: #f7931e;
      color: white;
      text-align: center;
      padding: 20px 0;
      margin-top: auto;
    }
    .footer i {
      margin-right: 8px;
    }

    .actions {
      display: flex;
      gap: 10px;
    }
    .actions a {
      color: #777;
      font-size: 18px;
      cursor: pointer;
      text-decoration: none;
      transition: color 0.2s ease-in-out;
    }
    .actions a:hover {
      color: #f7931e;
    }
    .actions .delete-btn:hover {
      color: #dc3545;
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

  List<Meta> listaMetas = (List<Meta>) request.getAttribute("listaMetas");
  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>

<div class="header">
  <a href="<%= request.getContextPath() %>/home.jsp" class="logo">Din+</a>
  <nav class="nav-menu">
    <ul>
      <li><a href="<%= request.getContextPath() %>/home.jsp">Página inicial</a></li>
      <li><a href="<%= request.getContextPath() %>/categorias?action=listar">Categorias</a></li>
      <li><a href="<%= request.getContextPath() %>/metas?action=listar">Metas</a></li>
      <li class="dropdown">
        <a href="#" class="dropbtn">Cadastro <i class="fas fa-caret-down"></i></a>
        <div class="dropdown-content">
          <a href="<%= request.getContextPath() %>/despesas?action=cadastrar">Nova Despesa</a>
          <a href="#">Nova Receita</a>
          <a href="<%= request.getContextPath() %>/metas?action=cadastrar">Nova Meta</a>
        </div>
      </li>
    </ul>
  </nav>
  <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Sair</a>
</div>

<div class="container">
  <h2>Minhas Metas</h2>

  <% if (listaMetas != null && !listaMetas.isEmpty()) { %>
  <div>
    <% for (Meta meta : listaMetas) { %>
    <div class="meta-card">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
        <h3><%= meta.getDescricao() %></h3>
        <div class="actions">
          <a href="<%= request.getContextPath() %>/metas?action=editar&id=<%= meta.getIdMeta() %>" title="Editar Meta"><i class="fas fa-edit"></i></a>
          <a href="<%= request.getContextPath() %>/metas?action=excluir&id=<%= meta.getIdMeta() %>" class="delete-btn" title="Excluir Meta" onclick="return confirm('Tem certeza que deseja excluir esta meta?');"><i class="fas fa-trash-alt"></i></a>
        </div>
      </div>
      <%
        double percentualConcluido = 0;
        if (meta.getValorAlvo() > 0) {
          percentualConcluido = (meta.getValorAtual() / meta.getValorAlvo()) * 100;
          if (percentualConcluido > 100) {
            percentualConcluido = 100;
          }
        }
        String valorAtualFormatado = String.format("%,.2f", meta.getValorAtual()); // Com centavos
        String valorAlvoFormatado = String.format("%,.2f", meta.getValorAlvo()); // Com centavos
      %>
      <p>R$ <%= valorAtualFormatado %> <span style="color: #777;"> (<%= String.format("%.0f", percentualConcluido) %>%)</span></p>
      <div class="progress-bar-container">
        <div class="progress-bar" style="width: <%= String.format("%.0f", percentualConcluido) %>%;"></div>
      </div>
      <p style="text-align: right; color: #777;">R$ <%= valorAlvoFormatado %></p>

      <div style="font-size: 12px; color: #999; margin-top: 10px; border-top: 1px solid #eee; padding-top: 5px;">
        <p>Criada em: <%= meta.getDataCriacao().format(dateFormatter) %></p>
        <p>Data Alvo: <%= (meta.getDataAlvo() != null) ? meta.getDataAlvo().format(dateFormatter) : "Não definida" %></p>
        <p>Prioridade: <%= meta.getPrioridade() %></p>
        <p>Status: <span class="status <%= meta.getStatusMeta().toLowerCase().replace(" ", "-") %>"><%= meta.getStatusMeta() %></span></p>
      </div>

    </div>
    <% } %>
  </div>
  <% } else { %>
  <p class="no-metas">Você ainda não possui metas cadastradas. <a href="<%= request.getContextPath() %>/metas?action=cadastrar">Que tal criar uma agora?</a></p>
  <% } %>
</div>

<div class="footer">
  <i class="fas fa-home"></i> Direitos reservados à Din+
</div>
</body>
</html>