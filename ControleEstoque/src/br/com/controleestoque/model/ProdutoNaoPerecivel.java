package br.com.controleestoque.model;

public class ProdutoNaoPerecivel extends Produto {
    private String categoria;

    public ProdutoNaoPerecivel(int codigo, String nome, double preco, int quantidade, String categoria) {
        super(codigo, nome, preco, quantidade);
        this.categoria = categoria;
    }

    public String getCategoria() { return categoria; }

    @Override
    public void exibirDetalhes() {
        System.out.println("Código: " + getCodigo() + ", Nome: " + getNome() + ", Preço: " + getPreco() +
                           ", Quantidade: " + getQuantidade() + ", Categoria: " + categoria);
    }
}