package com.paokentin.app.paokentin.repository.impl;

import com.paokentin.app.paokentin.domain.model.Pao;
import com.paokentin.app.paokentin.repository.PaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaoRepositoryImpl implements PaoRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public Pao save(Pao pao) {
        String sql = "INSERT INTO Pao (nome, descricao, tempo_preparo_minutos, cor_hex) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pao.getNome());
            ps.setString(2, pao.getDescricao());
            ps.setInt(3, pao.getTempoPreparoMinutos());
            ps.setString(4, pao.getCorHex());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar o pão, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pao.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao criar o pão, não foi possível obter o ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o pão no banco de dados", e);
        }
        return pao;
    }

    @Override
    public Pao update(Pao pao) {
        // Atualizaremos todos os campos para o pão com o ID correspondente.
        String sql = "UPDATE Pao SET nome = ?, descricao = ?, tempo_preparo_minutos = ?, cor_hex = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pao.getNome());
            ps.setString(2, pao.getDescricao());
            ps.setInt(3, pao.getTempoPreparoMinutos());
            ps.setString(4, pao.getCorHex());
            ps.setInt(5, pao.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar o pão, ID não encontrado: " + pao.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o pão no banco de dados", e);
        }
        return pao;
    }

    @Override
    public Optional<Pao> findById(Integer id) {
        String sql = "SELECT id, nome, descricao, tempo_preparo_minutos, cor_hex FROM Pao WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pao pao = new Pao();
                    pao.setId(rs.getInt("id"));
                    pao.setNome(rs.getString("nome"));
                    pao.setDescricao(rs.getString("descricao"));
                    pao.setTempoPreparoMinutos(rs.getInt("tempo_preparo_minutos"));
                    pao.setCorHex(rs.getString("cor_hex"));
                    return Optional.of(pao);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o pão por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pao> findAll() {
        List<Pao> paes = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, tempo_preparo_minutos, cor_hex FROM Pao ORDER BY nome";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pao pao = new Pao();
                pao.setId(rs.getInt("id"));
                pao.setNome(rs.getString("nome"));
                pao.setDescricao(rs.getString("descricao"));
                pao.setTempoPreparoMinutos(rs.getInt("tempo_preparo_minutos"));
                // 4. Mapeia a coluna cor_hex do banco para o objeto Pao
                pao.setCorHex(rs.getString("cor_hex"));
                paes.add(pao);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os pães", e);
        }
        return paes;
    }
}