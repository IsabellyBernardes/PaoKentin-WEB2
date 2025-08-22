package com.paokentin.app.paokentin.repository.impl;

import com.paokentin.app.paokentin.domain.model.Fornada;
import com.paokentin.app.paokentin.domain.model.Pao;
import com.paokentin.app.paokentin.repository.FornadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FornadaRepositoryImpl implements FornadaRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public Fornada save(Fornada fornada) {
        String sql = "INSERT INTO Fornada (pao_id, data_hora_inicio) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, fornada.getPao().getId());
            ps.setTimestamp(2, Timestamp.valueOf(fornada.getDataHoraInicio()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    fornada.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a fornada", e);
        }
        return fornada;
    }

    @Override
    public List<Fornada> findLatestForEachPao() {
        List<Fornada> ultimasFornadas = new ArrayList<>();
        // Query alternativa usando subquery para encontrar o ID da última fornada de cada pão.
        String sql = """
        SELECT
            f.id AS fornada_id,
            f.data_hora_inicio,
            p.id AS pao_id,
            p.nome AS pao_nome,
            p.descricao AS pao_descricao,
            p.tempo_preparo_minutos AS pao_tempo_preparo
        FROM Fornada f
        JOIN Pao p ON f.pao_id = p.id
        WHERE f.id IN (
            SELECT MAX(sub_f.id)
            FROM Fornada sub_f
            GROUP BY sub_f.pao_id
        )
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pao pao = new Pao();
                pao.setId(rs.getInt("pao_id"));
                pao.setNome(rs.getString("pao_nome"));
                pao.setDescricao(rs.getString("pao_descricao"));
                pao.setTempoPreparoMinutos(rs.getInt("pao_tempo_preparo"));

                Fornada fornada = new Fornada();
                fornada.setId(rs.getInt("fornada_id"));
                // Esta é a linha crítica. Vamos garantir que ela funcione.
                fornada.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                fornada.setPao(pao);

                ultimasFornadas.add(fornada);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar últimas fornadas com query alternativa", e);
        }
        return ultimasFornadas;
    }
}