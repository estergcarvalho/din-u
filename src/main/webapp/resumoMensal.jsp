<%-- src/main/webapp/resumoMensal.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.fiap.dinfintech.model.Usuario" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Din+ - Resumo Mensal</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
    :root {
      --primary-color: #f7931e;
      --primary-dark-color: #e08010;
      --text-color-dark: #333;
      --text-color-medium: #555;
      --background-light: #f0f2f5;
      --card-background: #fff;
      --border-color: #eee;
      --shadow-light: 0 4px 10px rgba(0, 0, 0, 0.08);
      --shadow-medium: 0 8px 20px rgba(0, 0, 0, 0.12);
      --success-color: #28a745;
      --danger-color: #dc3545;
      --info-color: #007bff;
      --border-radius-soft: 8px;
      --border-radius-medium: 12px;
    }

    body {
      font-family: 'Inter', sans-serif;
      margin: 0;
      background-color: var(--background-light);
      color: var(--text-color-dark);
      display: flex;
      flex-direction: column;
      min-height: 100vh;
      line-height: 1.6;
    }

    .header {
      background-color: var(--primary-color);
      color: white;
      padding: 15px 50px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
      position: sticky;
      top: 0;
      z-index: 1000;
    }
    .header .logo {
      font-size: 26px;
      font-weight: 700;
      text-decoration: none;
      color: white;
      letter-spacing: -0.5px;
    }
    .nav-menu ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
    }
    .nav-menu li {
      margin-right: 35px;
    }
    .nav-menu a {
      color: white;
      text-decoration: none;
      font-size: 17px;
      padding: 8px 0;
      position: relative;
      font-weight: 500;
      transition: color 0.3s ease;
    }
    .nav-menu a:hover {
      color: #ffe0b3;
    }
    .nav-menu a::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: 0;
      width: 0;
      height: 2px;
      background-color: white;
      transition: width 0.3s ease;
    }
    .nav-menu a:hover::after {
      width: 100%;
    }
    .nav-menu .dropdown {
      position: relative;
    }
    .nav-menu .dropdown-content {
      display: none;
      position: absolute;
      background-color: var(--primary-color);
      min-width: 180px;
      box-shadow: var(--shadow-light);
      z-index: 1;
      border-radius: var(--border-radius-soft);
      padding: 10px 0;
      top: 100%;
      left: 0;
      opacity: 0;
      transform: translateY(10px);
      transition: opacity 0.3s ease, transform 0.3s ease;
      pointer-events: none;
    }
    .nav-menu .dropdown:hover .dropdown-content {
      display: block;
      opacity: 1;
      transform: translateY(0);
      pointer-events: auto;
    }
    .nav-menu .dropdown-content a {
      color: white;
      padding: 12px 20px;
      text-decoration: none;
      display: block;
      text-align: left;
      font-size: 16px;
      white-space: nowrap;
    }
    .nav-menu .dropdown-content a:hover {
      background-color: var(--primary-dark-color);
      border-radius: 0;
    }
    .logout-btn {
      background-color: transparent;
      color: white;
      font-size: 16px;
      cursor: pointer;
      text-decoration: none;
      padding: 8px 18px;
      border-radius: 25px;
      transition: background-color 0.3s ease, color 0.3s ease;
    }
    .logout-btn:hover {
      background-color: white;
      color: var(--primary-color);
    }

    .container {
      flex-grow: 1;
      padding: 60px 20px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }

    .resumo-card {
      background-color: var(--card-background);
      padding: 50px;
      border-radius: var(--border-radius-medium);
      box-shadow: var(--shadow-medium);
      width: 100%;
      max-width: 700px;
      text-align: center;
      position: relative;
      overflow: hidden;
      margin-bottom: 40px;
    }

    .resumo-card::before {
      content: '';
      position: absolute;
      top: -50px;
      left: -50px;
      width: 200px;
      height: 200px;
      background: radial-gradient(circle, var(--primary-color) 0%, transparent 70%);
      opacity: 0.05;
      border-radius: 50%;
      animation: pulse 4s infinite alternate;
    }

    .resumo-card::after {
      content: '';
      position: absolute;
      bottom: -50px;
      right: -50px;
      width: 200px;
      height: 200px;
      background: radial-gradient(circle, var(--primary-color) 0%, transparent 70%);
      opacity: 0.05;
      border-radius: 50%;
      animation: pulse 4s infinite alternate-reverse;
    }

    @keyframes pulse {
      0% { transform: scale(1); opacity: 0.05; }
      100% { transform: scale(1.1); opacity: 0.1; }
    }

    .resumo-card h2 {
      margin-bottom: 25px;
      color: var(--primary-color);
      font-size: 32px;
      font-weight: 700;
      letter-spacing: -0.8px;
    }

    .period-selector form {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      align-items: center;
      gap: 20px;
      margin-bottom: 40px;
      padding: 20px;
      background-color: #fcfcfc;
      border: 1px solid var(--border-color);
      border-radius: var(--border-radius-soft);
      box-shadow: var(--shadow-light);
    }

    .period-selector label {
      font-size: 16px;
      color: var(--text-color-medium);
      font-weight: 500;
    }

    .period-selector select {
      padding: 12px 27px;
      border: 1px solid #ddd;
      border-radius: var(--border-radius-soft);
      font-size: 17px;
      appearance: none;
      background-color: white;
      background-image: url('data:image/svg+xml;utf8,<svg fill="%23333" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/><path d="M0 0h24v24H0z" fill="none"/></svg>');
      background-repeat: no-repeat;
      background-position: right 10px center;
      background-size: 20px;
      cursor: pointer;
      transition: border-color 0.3s ease, box-shadow 0.3s ease;
    }
    .period-selector select:focus {
      outline: none;
      border-color: var(--primary-color);
      box-shadow: 0 0 0 3px rgba(247, 147, 30, 0.2);
    }

    .period-selector button {
      background-color: var(--primary-color);
      color: white;
      padding: 12px 25px;
      border: none;
      border-radius: var(--border-radius-soft);
      font-size: 17px;
      font-weight: 600;
      cursor: pointer;
      transition: background-color 0.3s ease, transform 0.2s ease;
      box-shadow: 0 3px 8px rgba(247, 147, 30, 0.3);
    }

    .period-selector button:hover {
      background-color: var(--primary-dark-color);
      transform: translateY(-2px);
      box-shadow: 0 5px 12px rgba(247, 147, 30, 0.4);
    }

    .resumo-card h3 {
      font-size: 24px;
      color: var(--text-color-dark);
      margin-bottom: 35px;
      font-weight: 600;
    }

    .summary-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 25px;
      margin-top: 30px;
      justify-content: center;
    }

    .summary-item {
      background-color: var(--card-background);
      border: 1px solid var(--border-color);
      border-radius: var(--border-radius-medium);
      padding: 30px 20px;
      box-shadow: var(--shadow-light);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }
    .summary-item:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
    }

    .summary-item h3 {
      font-size: 18px;
      color: var(--text-color-medium);
      margin-bottom: 12px;
      font-weight: 500;
    }

    .summary-item p {
      font-size: 28px;
      font-weight: 700;
      margin: 0;
      line-height: 1.2;
    }

    .summary-item.receita p {
      color: var(--success-color);
    }

    .summary-item.despesa p {
      color: var(--danger-color);
    }

    .summary-item.saldo p {
      color: var(--info-color);
    }

    .chart-section {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 30px;
      width: 100%;
      max-width: 1000px;
      margin-top: 40px;
    }

    .chart-container {
      background-color: var(--card-background);
      padding: 80px;
      border-radius: var(--border-radius-medium);
      box-shadow: var(--shadow-medium);
      position: relative;
      overflow: hidden;
      height: 350px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: stretch;
    }

    .chart-container h3 {
      font-size: 24px;
      color: var(--primary-color);
      margin-bottom: 20px;
      font-weight: 600;
      text-align: center;
    }

    .chart-container canvas {
      width: 100% !important;
      height: auto !important;
      flex-grow: 1;
    }

    @media (max-width: 768px) {
      .chart-container {
        height: 300px;
      }
    }

    .footer {
      background-color: var(--primary-color);
      color: white;
      text-align: center;
      padding: 20px 0;
      margin-top: auto;
      font-size: 15px;
      box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.15);
    }
    .footer i {
      margin-right: 8px;
    }

    @media (max-width: 992px) {
      .chart-section {
        grid-template-columns: 1fr;
      }
    }

    @media (max-width: 768px) {
      .header {
        padding: 15px 20px;
      }
      .nav-menu ul {
        flex-direction: column;
        align-items: flex-start;
        display: none;
        position: absolute;
        top: 60px;
        left: 0;
        width: 100%;
        background-color: var(--primary-color);
        box-shadow: 0 5px 10px rgba(0,0,0,0.2);
      }
      .nav-menu li {
        margin: 0;
        width: 100%;
      }
      .nav-menu a {
        padding: 15px 20px;
        display: block;
      }
      .nav-menu .dropdown-content {
        position: static;
        box-shadow: none;
        padding: 0;
        min-width: unset;
        opacity: 1;
        transform: none;
        pointer-events: auto;
      }
      .nav-menu .dropdown-content a {
        padding-left: 40px;
      }
      .nav-menu.active ul {
        display: flex;
      }
      .logout-btn {
        margin-left: auto;
      }
      .resumo-card {
        padding: 30px;
        max-width: 95%;
      }
      .period-selector form {
        flex-direction: column;
        gap: 15px;
      }
      .summary-grid {
        grid-template-columns: 1fr;
      }
      .chart-container {
        height: 300px;
      }
    }

    .menu-toggle {
      display: none;
      color: white;
      font-size: 28px;
      cursor: pointer;
    }
    @media (max-width: 768px) {
      .menu-toggle {
        display: block;
      }
      .header .nav-menu {
        flex-grow: 1;
        display: flex;
        justify-content: flex-end;
      }
      .nav-menu ul {
        display: none;
      }
      .nav-menu.active ul {
        display: flex;
      }
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

  int mes = (int) request.getAttribute("mes");
  int ano = (int) request.getAttribute("ano");
  String nomeMes = (String) request.getAttribute("nomeMes");
  double totalReceitas = (double) request.getAttribute("totalReceitas");
  double totalDespesas = (double) request.getAttribute("totalDespesas");
  double saldoFinal = (double) request.getAttribute("saldoFinal");

  Map<String, Double> receitasPorMesAnoMap = (Map<String, Double>) request.getAttribute("receitasPorMesAno");
  Map<String, Double> despesasPorMesAnoMap = (Map<String, Double>) request.getAttribute("despesasPorMesAno");

  List<String> mesesLabels = new ArrayList<>(receitasPorMesAnoMap.keySet());
  List<Double> receitasAnualData = new ArrayList<>(receitasPorMesAnoMap.values());
  List<Double> despesasAnualData = new ArrayList<>(despesasPorMesAnoMap.values());

  NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

  Map<Integer, String> mesesSelect = (Map<Integer, String>) request.getAttribute("meses");
  List<Integer> anos = (List<Integer>) request.getAttribute("anos");
%>

<div class="header">
  <a href="<%= request.getContextPath() %>/home.jsp" class="logo">Din+</a>
  <nav class="nav-menu" id="mainNav">
    <div class="menu-toggle" id="menuToggle"><i class="fas fa-bars"></i></div>
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
  <div class="resumo-card">
    <h2>Resumo Financeiro Mensal</h2>

    <div class="period-selector">
      <form action="<%= request.getContextPath() %>/resumo" method="get">
        <label for="mes">Mês:</label>
        <select id="mes" name="mes">
          <% for (Map.Entry<Integer, String> entry : mesesSelect.entrySet()) { %>
          <option value="<%= entry.getKey() %>" <%= entry.getKey() == mes ? "selected" : "" %>>
            <%= entry.getValue() %>
          </option>
          <% } %>
        </select>

        <label for="ano">Ano:</label>
        <select id="ano" name="ano">
          <% for (Integer a : anos) { %>
          <option value="<%= a %>" <%= a == ano ? "selected" : "" %>>
            <%= a %>
          </option>
          <% } %>
        </select>
        <button type="submit">Visualizar</button>
      </form>
    </div>

    <h3>Dados para <%= nomeMes %> de <%= ano %></h3>

    <div class="summary-grid">
      <div class="summary-item receita">
        <h3>Total de Receitas</h3>
        <p><%= currencyFormat.format(totalReceitas) %></p>
      </div>
      <div class="summary-item despesa">
        <h3>Total de Despesas</h3>
        <p><%= currencyFormat.format(totalDespesas) %></p>
      </div>
      <div class="summary-item saldo">
        <h3>Saldo Final</h3>
        <p><%= currencyFormat.format(saldoFinal) %></p>
      </div>
    </div>
  </div>

  <div class="chart-section">
    <div class="chart-container">
      <h3>Receitas vs. Despesas do Mês (<%= nomeMes %> de <%= ano %>)</h3>
      <canvas id="monthlyChart"></canvas>
    </div>

    <div class="chart-container">
      <h3>Receitas e Despesas por Mês (<%= ano %>)</h3>
      <canvas id="annualChart"></canvas>
    </div>
  </div>

</div>

<div class="footer">
  <i class="fas fa-home"></i> Direitos reservados à Din+
</div>

<script>
  document.getElementById('menuToggle').addEventListener('click', function() {
    var navMenu = document.getElementById('mainNav');
    navMenu.classList.toggle('active');
  });

  const totalReceitasMes = <%= totalReceitas %>;
  const totalDespesasMes = <%= totalDespesas %>;

  const mesesLabels = [
    <% for (String label : mesesLabels) { %>
    "<%= label %>",
    <% } %>
  ];
  const receitasAnualData = [
    <% for (Double val : receitasAnualData) { %>
    <%= val %>,
    <% } %>
  ];
  const despesasAnualData = [
    <% for (Double val : despesasAnualData) { %>
    <%= val %>,
    <% } %>
  ];

  // --- Gráfico do Mês Atual (Receitas vs. Despesas) ---
  const monthlyCtx = document.getElementById('monthlyChart').getContext('2d');
  new Chart(monthlyCtx, {
    type: 'doughnut',
    data: {
      labels: ['Receitas', 'Despesas'],
      datasets: [{
        data: [totalReceitasMes, totalDespesasMes],
        backgroundColor: [
          '#28a745',
          '#dc3545'
        ],
        hoverOffset: 10
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
          labels: {
            font: {
              size: 14,
              family: 'Inter'
            },
            color: '#333'
          }
        },
        tooltip: {
          callbacks: {
            label: function(tooltipItem) {
              const value = tooltipItem.raw;
              return tooltipItem.label + ': ' + new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
            }
          },
          bodyFont: {
            family: 'Inter',
            size: 14
          },
          titleFont: {
            family: 'Inter',
            size: 16
          },
          padding: 10,
          borderRadius: 5,
          displayColors: true
        }
      }
    }
  });

  // --- Gráfico Anual (Receitas e Despesas por Mês) ---
  const annualCtx = document.getElementById('annualChart').getContext('2d');
  new Chart(annualCtx, {
    type: 'line',
    data: {
      labels: mesesLabels,
      datasets: [
        {
          label: 'Receitas',
          data: receitasAnualData,
          borderColor: '#28a745',
          backgroundColor: 'rgba(40, 167, 69, 0.2)',
          fill: true,
          tension: 0.4,
          pointRadius: 5,
          pointHoverRadius: 8
        },
        {
          label: 'Despesas',
          data: despesasAnualData,
          borderColor: '#dc3545',
          backgroundColor: 'rgba(220, 53, 69, 0.2)',
          fill: true,
          tension: 0.4,
          pointRadius: 5,
          pointHoverRadius: 8
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
          labels: {
            font: {
              size: 14,
              family: 'Inter'
            },
            color: '#333'
          }
        },
        tooltip: {
          callbacks: {
            label: function(tooltipItem) {
              const value = tooltipItem.raw;
              return tooltipItem.dataset.label + ': ' + new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
            }
          },
          bodyFont: {
            family: 'Inter',
            size: 14
          },
          titleFont: {
            family: 'Inter',
            size: 16
          },
          padding: 10,
          borderRadius: 5,
          displayColors: true
        }
      },
      scales: {
        x: {
          grid: {
            display: false
          },
          ticks: {
            font: {
              family: 'Inter',
              size: 12
            },
            color: '#555'
          }
        },
        y: {
          beginAtZero: true,
          grid: {
            color: '#e0e0e0'
          },
          ticks: {
            callback: function(value, index, values) {
              return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(value);
            },
            font: {
              family: 'Inter',
              size: 12
            },
            color: '#555'
          }
        }
      }
    }
  });
</script>
</body>
</html>