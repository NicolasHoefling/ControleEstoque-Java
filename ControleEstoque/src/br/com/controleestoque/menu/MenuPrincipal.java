package br.com.controleestoque.menu;

import br.com.controleestoque.gerenciador.GerenciadorProduto;
import br.com.controleestoque.gerenciador.GerenciadorFornecedor;
import br.com.controleestoque.gerenciador.GerenciadorMovimentacao;
import br.com.controleestoque.model.Produto;
import br.com.controleestoque.model.ProdutoPerecivel;
import br.com.controleestoque.model.ProdutoNaoPerecivel;
import br.com.controleestoque.model.MovimentacaoEstoque;
import br.com.controleestoque.model.Fornecedor;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private static GerenciadorProduto gerenciadorProduto = new GerenciadorProduto();
    private static GerenciadorFornecedor gerenciadorFornecedor = new GerenciadorFornecedor();
    private static GerenciadorMovimentacao gerenciadorMovimentacao = new GerenciadorMovimentacao();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            exibirMenu();
            int escolha = scanner.nextInt();
            scanner.nextLine(); 

            switch (escolha) {
            case 1 -> cadastrarProduto();
            case 2 -> listarProdutos();
            case 3 -> removerProduto();
            case 4 -> editarProduto();
            case 5 -> cadastrarFornecedor();
            case 6 -> listarFornecedores();
            case 7 -> registrarMovimentacao();
            case 8 -> listarMovimentacoes();
            case 0 -> {
                System.out.println("Saindo do sistema...");
                System.exit(0);
            }
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n### MENU PRINCIPAL ###");
        System.out.println("1 - Cadastrar Produto");
        System.out.println("2 - Listar Produtos");
        System.out.println("3 - Remover Produto");
        System.out.println("4 - Editar Produto");
        System.out.println("5 - Cadastrar Fornecedor");
        System.out.println("6 - Listar Fornecedores");
        System.out.println("7 - Registrar Movimentação de Estoque");
        System.out.println("8 - Listar Movimentações");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarProduto() {
        System.out.print("Código do produto: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço do produto: ");
        double preco = scanner.nextDouble();
        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("O produto é perecível? (S/N): ");
        String resposta = scanner.nextLine();

        Produto produto;
        if (resposta.equalsIgnoreCase("S")) {
            System.out.print("Data de validade (YYYY-MM-DD): ");
            String validade = scanner.nextLine();
            produto = new ProdutoPerecivel(codigo, nome, preco, quantidade, validade);
            gerenciadorProduto.cadastrarProdutoPerecivel((ProdutoPerecivel) produto);
        } else {
            System.out.print("Categoria do produto: ");
            String categoria = scanner.nextLine();
            produto = new ProdutoNaoPerecivel(codigo, nome, preco, quantidade, categoria);
            gerenciadorProduto.cadastrarProdutoNaoPerecivel((ProdutoNaoPerecivel) produto);
        }

    }

    private static void listarProdutos() {
        List<Produto> produtos = gerenciadorProduto.listarProdutos();
        System.out.println("\nLista de Produtos:");
        for (Produto produto : produtos) {
            produto.exibirDetalhes();
        }
    }

    private static void removerProduto() {
        System.out.print("Código do produto a remover: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();
        gerenciadorProduto.removerProduto(codigo);
        System.out.println("Produto removido com sucesso!");
    }
    
    private static void editarProduto() {
        System.out.print("Digite o código do produto a editar: ");
        int codigo = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Novo nome do produto: ");
        String novoNome = scanner.nextLine();

        System.out.print("Novo preço: ");
        double novoPreco = scanner.nextDouble();

        System.out.print("Nova quantidade: ");
        int novaQuantidade = scanner.nextInt();
        scanner.nextLine(); 

        gerenciadorProduto.editarProduto(codigo, novoNome, novoPreco, novaQuantidade);
        System.out.println("Produto atualizado com sucesso!");
    }

    private static void cadastrarFornecedor() {
        System.out.print("Nome do fornecedor: ");
        String nome = scanner.nextLine();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Fornecedor fornecedor = new Fornecedor(nome, cnpj, telefone);
        gerenciadorFornecedor.cadastrarFornecedor(fornecedor);
        System.out.println("Fornecedor cadastrado com sucesso!");
    }

    private static void listarFornecedores() {
        List<Fornecedor> fornecedores = gerenciadorFornecedor.listarFornecedores();
        System.out.println("\nLista de Fornecedores:");
        for (Fornecedor fornecedor : fornecedores) {
            System.out.println("Nome: " + fornecedor.getNome() + ", CNPJ: " + fornecedor.getCnpj() +
                    ", Telefone: " + fornecedor.getTelefone());
        }
    }

    private static void registrarMovimentacao() {
        System.out.print("Código do produto: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();

        Produto produto = new ProdutoNaoPerecivel(codigo, "Desconhecido", 0, 0, "Categoria");

        System.out.print("Tipo de movimentação (ENTRADA/SAÍDA): ");
        String tipo = scanner.nextLine();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(produto, tipo, quantidade);
        gerenciadorMovimentacao.registrarMovimentacao(movimentacao);

        System.out.println("Movimentação registrada com sucesso!");
    }

    private static void listarMovimentacoes() {
        List<MovimentacaoEstoque> movimentacoes = gerenciadorMovimentacao.listarMovimentacoes();
        System.out.println("\nLista de Movimentações:");
        for (MovimentacaoEstoque movimentacao : movimentacoes) {
            movimentacao.exibirMovimentacao();
        }
    }
}