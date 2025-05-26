<%-- src/main/webapp/cadastro.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro - Din+</title>
    <style>
        /* CSS INLINE PARA A TELA DE CADASTRO */
        /* Cores principais: #f7931e (laranja), #fff (branco), #333 (preto/cinza escuro para texto) */

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* Fonte mais moderna */
            background-color: #f0f2f5; /* Cor de fundo suave */
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            color: #333; /* Cor de texto padrão para o projeto */
        }

        /* Container Principal para Telas de Autenticação (Login/Cadastro) */
        .auth-container {
            display: flex; /* Habilita flexbox para as colunas */
            background-color: #fff;
            border-radius: 12px; /* Cantos mais arredondados */
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15); /* Sombra mais suave e pronunciada */
            overflow: hidden; /* Garante que os cantos arredondados funcionem */
            width: 950px; /* Largura total ligeiramente maior */
            max-width: 95vw;
            min-height: 580px;
        }

        .left-panel {
            background-color: #f7931e;
            color: white;
            padding: 50px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            flex: 1;
            position: relative;
            overflow: hidden;
        }

        .left-panel::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.1);
            z-index: 0;
        }

        .left-panel > * {
            position: relative;
            z-index: 1;
        }


        .left-panel .logo {
            font-size: 38px;
            font-weight: bold;
            margin-bottom: 25px;
            letter-spacing: 1.5px;
        }

        .left-panel p {
            font-size: 17px;
            line-height: 1.6;
            margin-bottom: 35px;
            max-width: 300px;
        }

        .left-panel .btn-entrar {
            background-color: white;
            color: #f7931e;
            padding: 14px 40px;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            font-size: 17px;
            font-weight: bold;
            text-decoration: none;
            transition: all 0.3s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .left-panel .btn-entrar:hover {
            background-color: #eee;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
        }

        .right-panel {
            background-color: #fff;
            padding: 50px;
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center;
            text-align: center;
        }

        .right-panel .logo {
            font-size: 45px;
            font-weight: bold;
            color: #f7931e;
            margin-bottom: 15px;
        }

        .right-panel h3 {
            font-size: 28px;
            color: #333;
            margin-bottom: 40px;
            font-weight: 600;
        }

        .form-group {
            margin-bottom: 25px;
            text-align: left;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-size: 15px;
            font-weight: 500;
        }

        .form-group input[type="text"],
        .form-group input[type="email"],
        .form-group input[type="password"],
        .form-group select {
            width: calc(100% - 24px);
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input:focus,
        .form-group select:focus {
            border-color: #f7931e;
            box-shadow: 0 0 8px rgba(247, 147, 30, 0.4);
            outline: none;
        }

        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 25px;
        }

        .form-row .form-group {
            flex: 1;
            margin-bottom: 0;
        }

        .right-panel .btn-submit {
            background-color: #f7931e;
            color: white;
            padding: 16px 35px;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            font-size: 19px;
            font-weight: bold;
            width: 100%;
            margin-top: 30px;
            transition: background-color 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .right-panel .btn-submit:hover {
            background-color: #e08010;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
        }

        .message.error {
            color: #dc3545;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-weight: 500;
            font-size: 14px;
        }

        .message.success {
            color: #28a745;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group.hidden-field {
            display: none;
        }
    </style>
</head>
<body>
<div class="auth-container"> <div class="left-panel"> <div class="logo">Din+</div>
    <p>Seja bem-vindo a Din+!<br>Aqui você encontra uma maneira simples e fácil de começar a acompanhar suas finanças e criar seu planejamento financeiro personalizado!</p>
    <p>Já possui conta?</p>
    <a href="<%= request.getContextPath() %>/login.jsp" class="btn-entrar">Entrar</a>
</div>

    <div class="right-panel"> <div class="logo">Din+</div>
        <h3>Cadastro</h3>

        <% String mensagemErro = (String) request.getAttribute("mensagemErro"); %>
        <% String mensagemSucesso = (String) request.getAttribute("mensagemSucesso"); %>

        <% if (mensagemErro != null && !mensagemErro.isEmpty()) { %>
        <p class="message error"><%= mensagemErro %></p>
        <% } %>
        <% if (mensagemSucesso != null && !mensagemSucesso.isEmpty()) { %>
        <p class="message success"><%= mensagemSucesso %></p>
        <% } %>

        <form action="<%= request.getContextPath() %>/cadastro" method="post">
            <div class="form-group">
                <input type="text" id="nome" name="nome" placeholder="Nome" required>
            </div>

            <div class="form-group">
                <input type="email" id="email" name="email" placeholder="E-mail" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <input type="password" id="password" name="password" placeholder="senha" required>
                </div>
                <div class="form-group">
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirme a senha" required>
                </div>
            </div>

            <div class="form-group">
                <input type="text" id="sobrenome" name="sobrenome" placeholder="Sobrenome" required>
            </div>
            <div class="form-group">
                <input type="text" id="dtNascimento" name="dtNascimento" placeholder="DD/MM/AAAA" required>
            </div>
            <div class="form-group">
                <input type="text" id="cpf" name="cpf" placeholder="CPF (somente números)" required>
            </div>
            <div class="form-group">
                <select id="sexo" name="sexo" required>
                    <option value="">Selecione o Sexo</option>
                    <option value="MASCULINO">Masculino</option>
                    <option value="FEMININO">Feminino</option>
                    <option value="OUTRO">Outro</option>
                    <option value="NAO_INFORMADO">Prefiro não informar</option>
                </select>
            </div>

            <button type="submit" class="btn-submit">Entrar</button>
        </form>
    </div>
</div>
</body>
</html>