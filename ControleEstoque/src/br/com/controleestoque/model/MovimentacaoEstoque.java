package br.com.controleestoque.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovimentacaoEstoque {
    private int id;
    private Produto produto;
    private String tipoMovimentacao; // "ENTRADA" ou "SAÍDA"
    private int quantidade;
    private LocalDate data;

    public MovimentacaoEstoque(Produto produto, String tipoMovimentacao, int quantidade) {
        this.produto = produto;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.data = LocalDate.now(); // Usa a data atual

        atualizarEstoque();
    }

    private void atualizarEstoque() {
        if (tipoMovimentacao.equalsIgnoreCase("ENTRADA")) {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
        } else if (tipoMovimentacao.equalsIgnoreCase("SAÍDA")) {
            if (produto.getQuantidade() >= quantidade) {
                produto.setQuantidade(produto.getQuantidade() - quantidade);
            }
        }
    }

    // 🔹 Métodos GETTERS que estavam faltando:
    public Produto getProduto() { return produto; }

    public String getTipoMovimentacao() { return tipoMovimentacao; }

    public int getQuantidade() { return quantidade; }

    public LocalDate getData() { return data; }

    public void exibirMovimentacao() {
        System.out.println("Produto: " + produto.getNome() + ", Tipo: " + tipoMovimentacao +
                           ", Quantidade: " + quantidade + ", Data: " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}