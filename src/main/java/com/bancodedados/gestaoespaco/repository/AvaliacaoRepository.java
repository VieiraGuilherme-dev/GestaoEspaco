package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.Avaliacao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AvaliacaoRepository {

    private final DataSource dataSource;

    public AvaliacaoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Avaliacao save(Avaliacao avaliacao) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (avaliacao.getId() == null) {
                // INSERT operation
                sql = "INSERT INTO avaliacao (solicitacao_id, gestor_id, status, justificativa, data_avaliacao) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, avaliacao.getSolicitacaoId());
                    ps.setLong(2, avaliacao.getGestorId());
                    ps.setString(3, avaliacao.getStatus());
                    ps.setString(4, avaliacao.getJustificativa());
                    ps.setTimestamp(5, Timestamp.valueOf(avaliacao.getDataAvaliacao()));
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            avaliacao.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // UPDATE operation
                sql = "UPDATE avaliacao SET solicitacao_id = ?, gestor_id = ?, status = ?, justificativa = ?, data_avaliacao = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, avaliacao.getSolicitacaoId());
                    ps.setLong(2, avaliacao.getGestorId());
                    ps.setString(3, avaliacao.getStatus());
                    ps.setString(4, avaliacao.getJustificativa());
                    ps.setTimestamp(5, Timestamp.valueOf(avaliacao.getDataAvaliacao()));
                    ps.setLong(6, avaliacao.getId());
                    ps.executeUpdate();
                }
            }
        }
        return avaliacao;
    }

    public Optional<Avaliacao> findById(Long id) throws SQLException {
        String sql = "SELECT id, solicitacao_id, gestor_id, status, justificativa, data_avaliacao FROM avaliacao WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Avaliacao> findAll() throws SQLException {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT id, solicitacao_id, gestor_id, status, justificativa, data_avaliacao FROM avaliacao ORDER BY data_avaliacao DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                avaliacoes.add(mapRow(rs));
            }
        }
        return avaliacoes;
    }

    public List<Avaliacao> findByStatus(String status) throws SQLException {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT id, solicitacao_id, gestor_id, status, justificativa, data_avaliacao FROM avaliacao WHERE status = ? ORDER BY data_avaliacao DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    avaliacoes.add(mapRow(rs));
                }
            }
        }
        return avaliacoes;
    }

    public Optional<Avaliacao> findBySolicitacaoId(Long solicitacaoId) throws SQLException {
        String sql = "SELECT id, solicitacao_id, gestor_id, status, justificativa, data_avaliacao FROM avaliacao WHERE solicitacao_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, solicitacaoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM avaliacao WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM avaliacao WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // Helper method to map a ResultSet row to an Avaliacao object
    private Avaliacao mapRow(ResultSet rs) throws SQLException {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(rs.getLong("id"));
        avaliacao.setSolicitacaoId(rs.getLong("solicitacao_id"));
        avaliacao.setGestorId(rs.getLong("gestor_id"));
        avaliacao.setStatus(rs.getString("status"));
        avaliacao.setJustificativa(rs.getString("justificativa"));
        avaliacao.setDataAvaliacao(rs.getTimestamp("data_avaliacao").toLocalDateTime());
        return avaliacao;
    }
}