package br.com.controleestoque.model;

public class ProdutoPerecivel extends Produto {
    private String dataValidade;

    public ProdutoPerecivel(int codigo, String nome, double preco, int quantidade, String dataValidade) {
        super(codigo, nome, preco, quantidade);
        this.dataValidade = dataValidade;
    }

    public String getDataValidade() { return dataValidade; }

    @Override
    public void exibirDetalhes() {
        System.out.println("Código: " + getCodigo() + ", Nome: " + getNome() + ", Preço: " + getPreco() +
                           ", Quantidade: " + getQuantidade() + ", Validade: " + dataValidade);
    }
}