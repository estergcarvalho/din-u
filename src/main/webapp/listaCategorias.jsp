<%-- src/main/webapp/listaCategorias.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.fiap.dinfintech.model.Usuario" %>
<%@ page import="br.com.fiap.dinfintech.model.Categoria" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Din+ - Minhas Categorias</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body { font-family: 'Inter', sans-serif; margin: 0; background-color: #f0f2f5; color: #333; display: flex; flex-direction: column; min-height: 100vh; }
        .header { background-color: #f7931e; color: white; padding: 15px 50px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
        .header .logo { font-size: 24px; font-weight: bold; text-decoration: none; color: white; }
        .nav-menu ul { list-style: none; margin: 0; padding: 0; display: flex; }
        .nav-menu li { margin-right: 30px; }
        .nav-menu a { color: white; text-decoration: none; font-size: 16px; padding: 5px 0; position: relative; }
        .nav-menu a:hover::after { content: ''; position: absolute; left: 0; bottom: -3px; width: 100%; height: 2px; background-color: white; }
        .nav-menu .dropdown { position: relative; }
        .nav-menu .dropdown-content { display: none; position: absolute; background-color: #f7931e; min-width: 160px; box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); z-index: 1; border-radius: 5px; padding-top: 10px; }
        .nav-menu .dropdown-content a { color: white; padding: 12px 16px; text-decoration: none; display: block; text-align: left; }
        .nav-menu .dropdown-content a:hover { background-color: #e08010; border-radius: 5px; }
        .nav-menu .dropdown:hover .dropdown-content { display: block; }
        .logout-btn { background-color: transparent; border: none; color: white; font-size: 16px; cursor: pointer; text-decoration: none; padding: 0; }

        .container { flex-grow: 1; padding: 40px; max-width: 900px; margin: 20px auto; background-color: #fff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); }
        .container h2 { text-align: center; margin-bottom: 30px; color: #333; font-size: 28px; }

        .add-button {
            display: block;
            width: fit-content;
            margin: 0 auto 20px auto;
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .add-button:hover { background-color: #218838; }

        .category-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .category-table th, .category-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        .category-table th {
            background-color: #f2f2f2;
            color: #333;
            font-weight: bold;
        }
        .category-table tr:nth-child(even) { background-color: #f9f9f9; }
        .category-table tr:hover { background-color: #f1f1f1; }

        .no-categories {
            text-align: center;
            font-size: 18px;
            color: #777;
            padding: 50px 0;
        }
        .footer { background-color: #f7931e; color: white; text-align: center; padding: 20px 0; margin-top: auto; }
        .footer i { margin-right: 8px; }
    </style>
</head>
<body>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Categoria> listaCategorias = (List<Categoria>) request.getAttribute("listaCategorias");
    String cadastroSucesso = request.getParameter("cadastroSucesso");
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
                    <a href="<%= request.getContextPath() %>/categorias?action=cadastrar">Nova Categoria</a>
                </div>
            </li>
            <li><a href="<%= request.getContextPath() %>/categorias?action=listar">Categorias</a></li>
        </ul>
    </nav>
    <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Sair</a>
</div>

<div class="container">
    <h2>Minhas Categorias</h2>

    <% if (cadastroSucesso != null && cadastroSucesso.equals("true")) { %>
    <p class="success-message">Categoria cadastrada com sucesso!</p>
    <% } %>

    <a href="<%= request.getContextPath() %>/categorias?action=cadastrar" class="add-button">
        <i class="fas fa-plus"></i> Adicionar Nova Categoria
    </a>

    <% if (listaCategorias != null && !listaCategorias.isEmpty()) { %>
    <table class="category-table">
        <thead>
        <tr>
            <th>Nome</th>
            <th>Tipo</th>
            <th>Origem</th>
        </tr>
        </thead>
        <tbody>
        <% for (Categoria categoria : listaCategorias) { %>
        <tr>
            <td><%= categoria.getNome() %></td>
            <td><%= categoria.getTipo().equals("DESPESA") ? "Despesa" : "Receita" %></td>
            <td><%= categoria.isPreDefinida() ? "Sistema" : "Personalizada" %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p class="no-categories">Você ainda não possui categorias personalizadas.</p>
    <% } %>
</div>

<div class="footer">
    <i class="fas fa-home"></i> Direitos reservados à Din+
</div>
</body>
</html>