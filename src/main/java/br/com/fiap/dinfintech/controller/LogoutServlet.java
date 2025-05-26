// src/main/java/br/com/fiap/dinfintech/servlet/LogoutServlet.java
package br.com.fiap.dinfintech.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout") // Esta anotação mapeia o Servlet para a URL /logout
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Não cria uma nova sessão se não existir
        if (session != null) {
            session.invalidate(); // Invalida a sessão, removendo todos os atributos
        }
        // Redireciona para a página de login.jsp
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}