# 💰 FinTracker

**FinTracker** é uma API REST para **gerenciamento de finanças pessoais**, projetada para oferecer controle detalhado sobre receitas, despesas e orçamentos. Criado como um projeto pessoal, o FinTracker busca otimizar a organização financeira, fornecendo **relatórios detalhados, alertas e exportação de dados**.

## 🚀 Tecnologias Utilizadas

- **Java 17** + **Spring Boot**
- **Spring Security** para autenticação
- **Spring Data JPA** para persistência de dados
- **MySQL** como banco de dados
- **Apache POI** para exportação de relatórios em Excel
- **Spring Scheduler** para automação de processos
- **JUnit** e **Mockito** para testes automatizados

---

## 🔥 Funcionalidades

### 📌 **Cadastro e Gerenciamento de Transações**
✔ Interface para **registro de receitas e despesas**  
✔ Cada transação pode ter **descrição, categoria e data**  
✔ Possibilidade de **editar e excluir** transações  

### 📊 **Categorização e Controle de Gastos**
✔ As transações podem ser **categorizadas**  
✔ Definição de **cotas de gastos** por categoria  
✔ Notificação caso **a cota seja ultrapassada**  

### 📅 **Relatórios Inteligentes**
✔ Geração de **relatórios financeiros mensais, trimestrais e anuais**  
✔ **Dashboard** com visão geral de despesas e receitas  
✔ **Relatórios de atingimento de cotas**, categorizando gastos  

### 📂 **Exportação de Dados**
✔ Exportação de transações e relatórios em **Excel, JSON e CSV**  
✔ Geração de **arquivos automatizados** via **Apache POI**  

### 🔄 **Agendamento e Automação**
✔ **Spring Scheduler** para atualização de dados financeiros  
✔ **Tabela materializada** para otimizar cálculos de cotas e totais  

### 🔑 **Segurança e Controle de Acesso**
✔ **Spring Security** para autenticação e autorização  
✔ Contexto de usuário gerenciado via **UserContext**  
✔ Cada usuário tem **acesso isolado aos seus dados financeiros**  

---

## 📖 Estrutura do Projeto

📂 **fintracker** (root)  
┣ 📂 `src/main/java/br/com/fintracker`  
┃ ┣ 📂 `controller` *(Lida com as requisições HTTP)*  
┃ ┣ 📂 `service` *(Regras de negócio e validações)*  
┃ ┣ 📂 `repository` *(Camada de persistência com JPA/Hibernate)*  
┃ ┣ 📂 `model` *(Entidades do banco de dados)*  
┃ ┣ 📂 `dto` *(Transferência de dados entre camadas)*  
┃ ┣ 📂 `infra/security` *(Autenticação e contexto de usuário)*  
┃ ┣ 📂 `scheduler` *(Tarefas agendadas para cálculos financeiros)*  
┣ 📂 `src/test/java/br/com/fintracker` *(Testes unitários e de integração)*  
┣ 📄 `application.yml` *(Configurações da API)*  
┣ 📄 `pom.xml` *(Dependências do projeto Maven)*  

---

## 🛠️ Como Executar o Projeto  

1️⃣ **Clone o repositório:**  
```sh
git clone https://github.com/seu-usuario/fintracker.git
cd fintracker
