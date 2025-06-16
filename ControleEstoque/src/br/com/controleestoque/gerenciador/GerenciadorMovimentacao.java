package br.com.controleestoque.gerenciador;

import br.com.controleestoque.util.ConexaoBD;
import br.com.controleestoque.model.MovimentacaoEstoque;
import br.com.controleestoque.model.ProdutoNaoPerecivel;
import br.com.controleestoque.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class GerenciadorMovimentacao {
	public void registrarMovimentacao(MovimentacaoEstoque movimentacao) {
	    String sqlBuscaNome = "SELECT nome FROM produto WHERE codigo = ?";
	    String sqlMovimentacao = "INSERT INTO movimentacao_estoque (produto_codigo, nome_produto, tipo_movimentacao, quantidade, data) VALUES (?, ?, ?, ?, ?)";
	    String sqlAtualizaProduto = "UPDATE produto SET quantidade = quantidade + ? WHERE codigo = ?";
	    String sqlVerificaTipo = "SELECT codigo FROM produto_perecivel WHERE codigo = ?";
	    String sqlAtualizaPerecivel = "UPDATE produto_perecivel SET quantidade = quantidade + ? WHERE codigo = ?";
	    String sqlAtualizaNaoPerecivel = "UPDATE produto_nao_perecivel SET quantidade = quantidade + ? WHERE codigo = ?";

	    try (Connection conn = ConexaoBD.conectar();
	         PreparedStatement stmtBuscaNome = conn.prepareStatement(sqlBuscaNome);
	         PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlMovimentacao);
	         PreparedStatement stmtAtualizaProduto = conn.prepareStatement(sqlAtualizaProduto);
	         PreparedStatement stmtVerificaTipo = conn.prepareStatement(sqlVerificaTipo);
	         PreparedStatement stmtAtualizaPerecivel = conn.prepareStatement(sqlAtualizaPerecivel);
	         PreparedStatement stmtAtualizaNaoPerecivel = conn.prepareStatement(sqlAtualizaNaoPerecivel)) {

	        // 🔹 Recupera o nome do produto
	        stmtBuscaNome.setInt(1, movimentacao.getProduto().getCodigo());
	        ResultSet rsNome = stmtBuscaNome.executeQuery();
	        
	        String nomeProduto = "";
	        if (rsNome.next()) {
	            nomeProduto = rsNome.getString("nome");
	            System.out.println("Produto Movimentado: " + nomeProduto); // 🔹 Teste no console
	        } else {
	            System.out.println("Erro: Produto não encontrado no banco.");
	            return;
	        }

	        // 🔹 Registra a movimentação no banco
	        stmtMovimentacao.setInt(1, movimentacao.getProduto().getCodigo());
	        stmtMovimentacao.setString(2, nomeProduto);
	        stmtMovimentacao.setString(3, movimentacao.getTipoMovimentacao());
	        stmtMovimentacao.setInt(4, movimentacao.getQuantidade());
	        stmtMovimentacao.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
	        stmtMovimentacao.executeUpdate();

	        // 🔹 Define ajuste de quantidade
	        int ajusteQuantidade = movimentacao.getTipoMovimentacao().equalsIgnoreCase("ENTRADA") 
	                               ? movimentacao.getQuantidade() 
	                               : -movimentacao.getQuantidade();

	        // 🔹 Atualiza a tabela principal de produtos
	        stmtAtualizaProduto.setInt(1, ajusteQuantidade);
	        stmtAtualizaProduto.setInt(2, movimentacao.getProduto().getCodigo());
	        stmtAtualizaProduto.executeUpdate();

	        // 🔹 Verifica se o produto está na tabela produto_perecivel
	        stmtVerificaTipo.setInt(1, movimentacao.getProduto().getCodigo());
	        ResultSet rsTipo = stmtVerificaTipo.executeQuery();

	        if (rsTipo.next()) {
	            // 🔹 Produto é perecível, então atualiza produto_perecivel
	            stmtAtualizaPerecivel.setInt(1, ajusteQuantidade);
	            stmtAtualizaPerecivel.setInt(2, movimentacao.getProduto().getCodigo());
	            stmtAtualizaPerecivel.executeUpdate();
	        } else {
	            // 🔹 Produto é não perecível, então atualiza produto_nao_perecivel
	            stmtAtualizaNaoPerecivel.setInt(1, ajusteQuantidade);
	            stmtAtualizaNaoPerecivel.setInt(2, movimentacao.getProduto().getCodigo());
	            stmtAtualizaNaoPerecivel.executeUpdate();
	        }

	        System.out.println("Movimentação registrada e estoque atualizado em todas as tabelas!");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    public List<MovimentacaoEstoque> listarMovimentacoes() {
        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
        String sql = "SELECT produto_codigo, nome_produto, tipo_movimentacao, quantidade, data FROM movimentacao_estoque";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new ProdutoNaoPerecivel(
                        rs.getInt("produto_codigo"),
                        rs.getString("nome_produto"), // ✅ Agora busca corretamente o nome salvo
                        0,
                        0,
                        "Categoria"
                );

                MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                        produto,
                        rs.getString("tipo_movimentacao"),
                        rs.getInt("quantidade")
                );

                movimentacoes.add(movimentacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movimentacoes;
    }
}