# 📦 Controle de Estoque (Java + MySQL)

Sistema simples de controle de estoque desenvolvido em Java, utilizando banco de dados MySQL via XAMPP. Permite:

- ✅ Cadastro de produtos perecíveis e não perecíveis
- ✅ Validação para não duplicar códigos de produto
- ✅ Edição e exclusão de produtos
- ✅ Listagem de todos os produtos com tipo identificado

---

## Integrantes:
- Nícolas Hoefling de C.
- Mauricio Alano
- João Pedro Girardi
- Igor
- João Miguel

## 📁 Estrutura do Projeto

- `model/`: Contém as classes de domínio (`Produto`, `ProdutoPerecivel`, `ProdutoNaoPerecivel`)
- `gerenciador/`: Contém a lógica de cadastro, edição e listagem
- `util/`: Contém a classe de conexão com banco de dados (`ConexaoBD`)


## 🖥️ Tecnologias Utilizadas

- Java 11+
- MySQL (via XAMPP)
- JDBC (driver: `mysql-connector-java`)
- IDE: IntelliJ IDEA (ou Eclipse)

---

## ⚙️ Como Executar

### 1. Instalar o XAMPP

Baixe e instale o [XAMPP](https://www.apachefriends.org/index.html) se ainda não tiver.

> Após a instalação, **abra o painel do XAMPP** e ative:
>
> ✅ `Apache`  
> ✅ `MySQL`

---

### 2. Criar o Banco de Dados

Abra o `phpMyAdmin` (disponível em `http://localhost/phpmyadmin/`) e execute o seguinte SQL:

```sql
CREATE DATABASE controle_estoque;

USE controle_estoque;

CREATE TABLE produto (
  codigo INT PRIMARY KEY,
  nome VARCHAR(100),
  preco DOUBLE,
  quantidade INT
);

CREATE TABLE produto_perecivel (
  codigo INT PRIMARY KEY,
  nome VARCHAR(100),
  preco DOUBLE,
  quantidade INT,
  data_validade VARCHAR(20)
);

CREATE TABLE produto_nao_perecivel (
  codigo INT PRIMARY KEY,
  nome VARCHAR(100),
  preco DOUBLE,
  quantidade INT,
  categoria VARCHAR(50)
);

CREATE TABLE movimentacao_estoque (
  id INT AUTO_INCREMENT PRIMARY KEY,
  produto_codigo INT,
  tipo_movimentacao VARCHAR(50),
  quantidade INT,
  data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3 No arquivo src/br/com/controleestoque/util/ConexaoBD.java, ajuste:

String url = "jdbc:mysql://localhost:3306/controle_estoque";
String usuario = "root";
String senha = ""; // Adicione sua senha aqui, se necessário
