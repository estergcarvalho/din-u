<%-- src/main/webapp/cadastroMeta.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.fiap.dinfintech.model.Usuario" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Din+ - Cadastrar Meta</title>
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
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 40px;
    }

    .form-card {
      background-color: #fff;
      padding: 40px;
      border-radius: 10px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 500px;
      text-align: center;
    }

    .form-card h2 {
      margin-bottom: 30px;
      color: #333;
      font-size: 28px;
    }

    .form-group {
      margin-bottom: 20px;
      text-align: left;
    }

    .form-group label {
      display: block;
      margin-bottom: 8px;
      color: #555;
      font-weight: bold;
    }

    .form-group input[type="text"],
    .form-group input[type="number"],
    .form-group input[type="date"],
    .form-group select {
      width: calc(100% - 20px);
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 16px;
      box-sizing: border-box;
    }

    .form-group input[type="submit"] {
      background-color: #f7931e;
      color: white;
      padding: 15px 30px;
      border: none;
      border-radius: 5px;
      font-size: 18px;
      font-weight: bold;
      cursor: pointer;
      transition: background-color 0.3s ease;
      width: 100%;
    }

    .form-group input[type="submit"]:hover {
      background-color: #e08010;
    }

    .error-message {
      color: #dc3545;
      margin-top: 15px;
      font-weight: bold;
    }

    .success-message {
      color: #28a745;
      margin-top: 15px;
      font-weight: bold;
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
  </style>
</head>
<body>
<%
  Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

  if (usuarioLogado == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }

  String mensagemErro = (String) request.getAttribute("mensagemErro");
  String mensagemSucesso = (String) request.getAttribute("mensagemSucesso");
%>

<div class="header">
  <a href="<%= request.getContextPath() %>/home.jsp" class="logo">Din+</a>
  <nav class="nav-menu">
    <ul>
      <li><a href="<%= request.getContextPath() %>/home.jsp">Página inicial</a></li>
      <li><a href="<%= request.getContextPath() %>/categorias?action=listar">Categorias</a></li>
      <li><a href="<%= request.getContextPath() %>/metas?action=listar">Metas</a></li>
      <li><a href="<%= request.getContextPath() %>/resumo">Resumo Mensal</a></li>
      <li class="dropdown">
        <a href="#" class="dropbtn">Cadastro <i class="fas fa-caret-down"></i></a>
        <div class="dropdown-content">
          <a href="<%= request.getContextPath() %>/despesas?action=cadastrar">Nova Despesa</a>
          <a href="<%= request.getContextPath() %>/receitas?action=cadastrar">Nova Receita</a>
          <a href="<%= request.getContextPath() %>/metas?action=cadastrar">Nova Meta</a>
          <a href="<%= request.getContextPath() %>/categorias?action=cadastrar">Nova Categoria</a>
        </div>
      </li>
    </ul>
  </nav>
  <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Sair</a>
</div>

<div class="container">
  <div class="form-card">
    <h2>Cadastrar Nova Meta</h2>

    <% if (mensagemErro != null) { %>
    <p class="error-message"><%= mensagemErro %></p>
    <% } %>
    <% if (mensagemSucesso != null) { %>
    <p class="success-message"><%= mensagemSucesso %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/metas" method="post">
      <input type="hidden" name="action" value="cadastrar">
      <div class="form-group">
        <label for="descricao">Descrição da Meta:</label>
        <input type="text" id="descricao" name="descricao" required placeholder="Ex: Juntar para viajar">
      </div>
      <div class="form-group">
        <label for="valorAlvo">Valor Alvo:</label>
        <input type="number" id="valorAlvo" name="valorAlvo" step="0.01" min="0.01" required placeholder="Ex: 5000.00">
      </div>
      <div class="form-group">
        <label for="dataAlvo">Data Alvo:</label>
        <input type="date" id="dataAlvo" name="dataAlvo" required>
      </div>
      <div class="form-group">
        <label for="statusMeta">Status:</label>
        <select id="statusMeta" name="statusMeta" required>
          <option value="Em Progresso">Em Progresso</option>
          <option value="Pendente">Pendente</option>
          <option value="Concluída">Concluída</option>
          <option value="Cancelada">Cancelada</option>
        </select>
      </div>
      <div class="form-group">
        <label for="prioridade">Prioridade:</label>
        <select id="prioridade" name="prioridade" required>
          <option value="Baixa">Baixa</option>
          <option value="Média">Média</option>
          <option value="Alta">Alta</option>
          <option value="Urgente">Urgente</option>
        </select>
      </div>
      <div class="form-group">
        <input type="submit" value="Cadastrar Meta">
      </div>
    </form>
  </div>
</div>

<div class="footer">
  <i class="fas fa-home"></i> Direitos reservados à Din+
</div>
</body>
</html>