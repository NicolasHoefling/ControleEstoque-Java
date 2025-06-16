package br.com.controleestoque.gerenciador;

import br.com.controleestoque.model.Produto;
import br.com.controleestoque.model.ProdutoPerecivel;
import br.com.controleestoque.model.ProdutoNaoPerecivel;
import br.com.controleestoque.util.ConexaoBD;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorProduto {
    public boolean produtoJaExiste(int codigo) {
        String sql = "SELECT 1 FROM produto WHERE codigo = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se encontrar algum resultado, já existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Em caso de erro, assume que não existe
        }
    }

    public void cadastrarProdutoPerecivel(ProdutoPerecivel produto) {
        if (produtoJaExiste(produto.getCodigo())) {
            JOptionPane.showMessageDialog(null, "Erro: Produto com código " + produto.getCodigo() + " já existe!");
            return;
        }

        String sqlProduto = "INSERT INTO produto (codigo, nome, preco, quantidade) VALUES (?, ?, ?, ?)";
        String sqlPerecivel = "INSERT INTO produto_perecivel (codigo, nome, preco, quantidade, data_validade) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
             PreparedStatement stmtPerecivel = conn.prepareStatement(sqlPerecivel)) {

            stmtProduto.setInt(1, produto.getCodigo());
            stmtProduto.setString(2, produto.getNome());
            stmtProduto.setDouble(3, produto.getPreco());
            stmtProduto.setInt(4, produto.getQuantidade());
            stmtProduto.executeUpdate();

            stmtPerecivel.setInt(1, produto.getCodigo());
            stmtPerecivel.setString(2, produto.getNome());
            stmtPerecivel.setDouble(3, produto.getPreco());
            stmtPerecivel.setInt(4, produto.getQuantidade());
            stmtPerecivel.setString(5, produto.getDataValidade());
            stmtPerecivel.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Produto perecível cadastrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarProdutoNaoPerecivel(ProdutoNaoPerecivel produto) {
        if (produtoJaExiste(produto.getCodigo())) {
            JOptionPane.showMessageDialog(null, "Erro: Produto com código " + produto.getCodigo() + " já existe!");
            return;
        }

        String sqlProduto = "INSERT INTO produto (codigo, nome, preco, quantidade) VALUES (?, ?, ?, ?)";
        String sqlNaoPerecivel = "INSERT INTO produto_nao_perecivel (codigo, nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
             PreparedStatement stmtNaoPerecivel = conn.prepareStatement(sqlNaoPerecivel)) {

            stmtProduto.setInt(1, produto.getCodigo());
            stmtProduto.setString(2, produto.getNome());
            stmtProduto.setDouble(3, produto.getPreco());
            stmtProduto.setInt(4, produto.getQuantidade());
            stmtProduto.executeUpdate();

            stmtNaoPerecivel.setInt(1, produto.getCodigo());
            stmtNaoPerecivel.setString(2, produto.getNome());
            stmtNaoPerecivel.setDouble(3, produto.getPreco());
            stmtNaoPerecivel.setInt(4, produto.getQuantidade());
            stmtNaoPerecivel.setString(5, produto.getCategoria());
            stmtNaoPerecivel.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Produto não perecível cadastrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerProduto(int codigo) {
        String sqlDeletePerecivel = "DELETE FROM produto_perecivel WHERE codigo = ?";
        String sqlDeleteNaoPerecivel = "DELETE FROM produto_nao_perecivel WHERE codigo = ?";
        String sqlDeleteMovimentacao = "DELETE FROM movimentacao_estoque WHERE produto_codigo = ?";
        String sqlDeleteProduto = "DELETE FROM produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmtPerecivel = conn.prepareStatement(sqlDeletePerecivel);
             PreparedStatement stmtNaoPerecivel = conn.prepareStatement(sqlDeleteNaoPerecivel);
             PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlDeleteMovimentacao);
             PreparedStatement stmtProduto = conn.prepareStatement(sqlDeleteProduto)) {

            // 🔹 Remove primeiro qualquer registro relacionado na tabela de produtos perecíveis
            stmtPerecivel.setInt(1, codigo);
            stmtPerecivel.executeUpdate();

            // 🔹 Remove qualquer registro relacionado na tabela de produtos não perecíveis
            stmtNaoPerecivel.setInt(1, codigo);
            stmtNaoPerecivel.executeUpdate();

            // 🔹 Remove todas as movimentações associadas ao produto
            stmtMovimentacao.setInt(1, codigo);
            stmtMovimentacao.executeUpdate();

            // 🔹 Agora podemos remover o produto sem erro
            stmtProduto.setInt(1, codigo);
            stmtProduto.executeUpdate();

            System.out.println("Produto e todas suas referências foram removidos com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarProduto(int codigo, String novoNome, double novoPreco, int novaQuantidade) {
        String sqlUpdateProduto = "UPDATE produto SET nome = ?, preco = ?, quantidade = ? WHERE codigo = ?";
        String sqlUpdatePerecivel = "UPDATE produto_perecivel SET nome = ?, preco = ?, quantidade = ? WHERE codigo = ?";
        String sqlUpdateNaoPerecivel = "UPDATE produto_nao_perecivel SET nome = ?, preco = ?, quantidade = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmtProduto = conn.prepareStatement(sqlUpdateProduto);
             PreparedStatement stmtPerecivel = conn.prepareStatement(sqlUpdatePerecivel);
             PreparedStatement stmtNaoPerecivel = conn.prepareStatement(sqlUpdateNaoPerecivel)) {

            // 🔹 Atualiza o produto na tabela principal
            stmtProduto.setString(1, novoNome);
            stmtProduto.setDouble(2, novoPreco);
            stmtProduto.setInt(3, novaQuantidade);
            stmtProduto.setInt(4, codigo);
            stmtProduto.executeUpdate();

            // 🔹 Atualiza na tabela produto_perecivel
            stmtPerecivel.setString(1, novoNome);
            stmtPerecivel.setDouble(2, novoPreco);
            stmtPerecivel.setInt(3, novaQuantidade);
            stmtPerecivel.setInt(4, codigo);
            stmtPerecivel.executeUpdate();

            // 🔹 Atualiza na tabela produto_nao_perecivel
            stmtNaoPerecivel.setString(1, novoNome);
            stmtNaoPerecivel.setDouble(2, novoPreco);
            stmtNaoPerecivel.setInt(3, novaQuantidade);
            stmtNaoPerecivel.setInt(4, codigo);
            stmtNaoPerecivel.executeUpdate();

            System.out.println("Produto atualizado em todas as tabelas com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.codigo, p.nome, p.preco, p.quantidade, pp.data_validade, pnp.categoria " +
                     "FROM produto p " +
                     "LEFT JOIN produto_perecivel pp ON p.codigo = pp.codigo " +
                     "LEFT JOIN produto_nao_perecivel pnp ON p.codigo = pnp.codigo";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto;
                if (rs.getString("data_validade") != null) {
                    produto = new ProdutoPerecivel(rs.getInt("codigo"), rs.getString("nome"),
                            rs.getDouble("preco"), rs.getInt("quantidade"), rs.getString("data_validade"));
                } else {
                    produto = new ProdutoNaoPerecivel(rs.getInt("codigo"), rs.getString("nome"),
                            rs.getDouble("preco"), rs.getInt("quantidade"), rs.getString("categoria"));
                }
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
}