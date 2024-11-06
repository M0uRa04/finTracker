# FinTracker

Projeto pessoal criado com o objetivo de otimizar e suportar o controle de finanças pessoais.

#### Funcionalidades e Implementações

1. **Interface simples para o cadastro de transações de entradas e saídas**
   - **Solução:** Implementar uma interface minimalista usando JavaFX (se for desktop) ou um framework web como Thymeleaf para facilitar o cadastro. Utilizar formulários com campos básicos de valor, categoria, descrição e data, com validação de dados antes do envio.

2. **Categorização de transações**
   - **Solução:** Criar uma tabela de categorias no banco de dados. Cada transação poderá ser vinculada a uma categoria específica, facilitando a filtragem e o cálculo de totais por categoria.

3. **Descrição das transações**
   - **Solução:** Adicionar um campo de texto para a descrição de cada transação, permitindo que o usuário insira detalhes adicionais sobre a natureza de cada entrada ou saída.

4. **Saldo atual**
   - **Solução:** Implementar uma função que calcula o saldo em tempo real, somando todas as entradas e subtraindo todas as saídas armazenadas no banco de dados.

5. **Cota por categoria**
   - **Solução:** Permitir que o usuário defina uma cota de gastos para cada categoria e armazenar esses valores. Utilizar essas cotas como base para monitoramento.

6. **Alerta caso a cota seja ultrapassada**
   - **Solução:** Implementar uma notificação automática que será acionada quando o valor total de uma categoria exceder a cota definida.

7. **Relatórios mensais, trimestrais e anuais**
   - **Solução:** Implementar consultas no banco de dados que somem as transações por mês, trimestre e ano, retornando esses valores como relatórios.

8. **Exportação para Excel, JSON e CSV**
   - **Solução:** Utilizar bibliotecas Java como Apache POI para exportar dados para Excel e bibliotecas padrão para JSON e CSV, permitindo ao usuário escolher o formato.

9. **Atalhos rápidos para transações frequentes**
   - **Solução:** Oferecer uma lista de transações predefinidas que o usuário pode acessar com um clique (ex.: salário, aluguel) para adicionar essas entradas de forma rápida.

10. **Criação de novas categorias**
     - **Solução:** Oferecer uma forma de cadastro de novas categorias de forma simples.
