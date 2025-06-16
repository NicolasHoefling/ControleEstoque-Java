package br.com.controleestoque.gerenciador;

import br.com.controleestoque.model.Fornecedor;
import br.com.controleestoque.util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorFornecedor {
    public void cadastrarFornecedor(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (nome, cnpj, telefone) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Fornecedor> listarFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor(rs.getString("nome"),
                        rs.getString("cnpj"), rs.getString("telefone"));
                fornecedores.add(fornecedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fornecedores;
    }
}
