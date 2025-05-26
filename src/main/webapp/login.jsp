<%-- src/main/webapp/login.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Din+</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .auth-container {
            display: flex;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            width: 900px;
            max-width: 90vw;
            min-height: 550px;
        }

        .left-panel {
            background-color: #f7931e;
            color: white;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            flex: 1;
        }

        .left-panel .logo {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .left-panel p {
            font-size: 16px;
            line-height: 1.5;
            margin-bottom: 30px;
        }

        .left-panel .btn-entrar {
            background-color: white;
            color: #f7931e;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        .left-panel .btn-entrar:hover {
            background-color: #eee;
        }

        .right-panel {
            background-color: #fff;
            padding: 40px;
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center;
            text-align: center;
        }

        .right-panel .logo {
            font-size: 40px;
            font-weight: bold;
            color: #f7931e;
            margin-bottom: 10px;
        }

        .right-panel h3 {
            font-size: 24px;
            color: #333;
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }

        .form-group input[type="text"],
        .form-group input[type="email"],
        .form-group input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }

        .right-panel .btn-submit {
            background-color: #f7931e;
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            width: 100%;
            margin-top: 20px;
            transition: background-color 0.3s ease;
        }

        .right-panel .btn-submit:hover {
            background-color: #e08010;
        }

        .message.error {
            color: red;
            margin-bottom: 15px;
            font-weight: bold;
            font-size: 14px;
        }

        .message.success {
            color: green;
            margin-bottom: 15px;
            font-weight: bold;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="auth-container"> <div class="right-panel">
    <div class="logo">Din+</div>
    <h3>Login</h3>

    <% String mensagemErro = (String) request.getAttribute("mensagemErro"); %>
    <% String cadastroSucesso = request.getParameter("cadastroSucesso"); %>

    <% if (mensagemErro != null && !mensagemErro.isEmpty()) { %>
    <p class="message error"><%= mensagemErro %></p>
    <% } %>
    <% if (cadastroSucesso != null && cadastroSucesso.equals("true")) { %>
    <p class="message success">Cadastro realizado com sucesso! Faça login.</p>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <input type="email" id="email" name="email" placeholder="E-mail" required>
        </div>
        <div class="form-group">
            <input type="password" id="password" name="password" placeholder="Senha" required>
        </div>
        <button type="submit" class="btn-submit">Entrar</button>
    </form>
</div>

    <div class="left-panel">
        <div class="logo">Din+</div>
        <p>Ainda não possui uma conta?</p>
        <p>Cadastre-se na Din+ e comece a controlar suas finanças hoje mesmo!</p>
        <a href="<%= request.getContextPath() %>/cadastro.jsp" class="btn-entrar">Cadastre-se</a>
    </div>
</div>
</body>
</html>