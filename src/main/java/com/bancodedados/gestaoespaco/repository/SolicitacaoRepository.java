package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.Solicitacao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SolicitacaoRepository {

    private final DataSource dataSource;

    public SolicitacaoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Solicitacao save(Solicitacao solicitacao) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (solicitacao.getId() == null) {
                // INSERT operation
                sql = "INSERT INTO solicitacao (usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, solicitacao.getUsuarioId());
                    ps.setLong(2, solicitacao.getEspacoId());
                    ps.setDate(3, Date.valueOf(solicitacao.getDataReserva()));
                    ps.setTime(4, Time.valueOf(solicitacao.getHoraReserva()));
                    ps.setTimestamp(5, Timestamp.valueOf(solicitacao.getDataSolicitacao()));
                    ps.setString(6, solicitacao.getStatus());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            solicitacao.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // UPDATE operation
                sql = "UPDATE solicitacao SET usuario_id = ?, espaco_id = ?, data_reserva = ?, hora_reserva = ?, data_solicitacao = ?, status = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, solicitacao.getUsuarioId());
                    ps.setLong(2, solicitacao.getEspacoId());
                    ps.setDate(3, Date.valueOf(solicitacao.getDataReserva()));
                    ps.setTime(4, Time.valueOf(solicitacao.getHoraReserva()));
                    ps.setTimestamp(5, Timestamp.valueOf(solicitacao.getDataSolicitacao()));
                    ps.setString(6, solicitacao.getStatus());
                    ps.setLong(7, solicitacao.getId());
                    ps.executeUpdate();
                }
            }
        }
        return solicitacao;
    }

    public Optional<Solicitacao> findById(Long id) throws SQLException {
        String sql = "SELECT id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status FROM solicitacao WHERE id = ?";
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

    public List<Solicitacao> findAll() throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status FROM solicitacao ORDER BY data_solicitacao DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                solicitacoes.add(mapRow(rs));
            }
        }
        return solicitacoes;
    }

    public List<Solicitacao> findByStatus(String status) throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status FROM solicitacao WHERE status = ? ORDER BY data_solicitacao DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    solicitacoes.add(mapRow(rs));
                }
            }
        }
        return solicitacoes;
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitacao WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM solicitacao WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Solicitacao mapRow(ResultSet rs) throws SQLException {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(rs.getLong("id"));
        solicitacao.setUsuarioId(rs.getLong("usuario_id"));
        solicitacao.setEspacoId(rs.getLong("espaco_id"));
        solicitacao.setDataReserva(rs.getDate("data_reserva").toLocalDate());
        solicitacao.setHoraReserva(rs.getTime("hora_reserva").toLocalTime());
        solicitacao.setDataSolicitacao(rs.getTimestamp("data_solicitacao").toLocalDateTime());
        solicitacao.setStatus(rs.getString("status"));
        return solicitacao;
    }
}