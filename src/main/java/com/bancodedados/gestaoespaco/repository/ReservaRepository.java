package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.Reserva;
import com.bancodedados.gestaoespaco.model.StatusReserva;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservaRepository {

    private final DataSource dataSource;

    public ReservaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Reserva save(Reserva reserva) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (reserva.getId() == null) {
                sql = "INSERT INTO reserva (usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, reserva.getUsuarioId());
                    ps.setLong(2, reserva.getEspacoId());
                    ps.setTimestamp(3, Timestamp.valueOf(reserva.getDataHoraInicio()));
                    ps.setTimestamp(4, Timestamp.valueOf(reserva.getDataHoraFim()));
                    ps.setString(5, reserva.getStatus().name()); // Store enum as String
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            reserva.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                sql = "UPDATE reserva SET usuario_id = ?, espaco_id = ?, data_hora_inicio = ?, data_hora_fim = ?, status = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, reserva.getUsuarioId());
                    ps.setLong(2, reserva.getEspacoId());
                    ps.setTimestamp(3, Timestamp.valueOf(reserva.getDataHoraInicio()));
                    ps.setTimestamp(4, Timestamp.valueOf(reserva.getDataHoraFim()));
                    ps.setString(5, reserva.getStatus().name());
                    ps.setLong(6, reserva.getId());
                    ps.executeUpdate();
                }
            }
        }
        return reserva;
    }

    public Optional<Reserva> findById(Long id) throws SQLException {
        String sql = "SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva WHERE id = ?";
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

    public List<Reserva> findAll() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva ORDER BY data_hora_inicio DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservas.add(mapRow(rs));
            }
        }
        return reservas;
    }

    public List<Reserva> findByStatus(StatusReserva status) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva WHERE status = ? ORDER BY data_hora_inicio DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapRow(rs));
                }
            }
        }
        return reservas;
    }

    public List<Reserva> findByUsuarioId(Long usuarioId) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva WHERE usuario_id = ? ORDER BY data_hora_inicio DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapRow(rs));
                }
            }
        }
        return reservas;
    }

    public List<Reserva> findByEspacoId(Long espacoId) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva WHERE espaco_id = ? ORDER BY data_hora_inicio DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, espacoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapRow(rs));
                }
            }
        }
        return reservas;
    }

    public List<Reserva> findOverlappingReservas(Long espacoId, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Long currentReservaId) throws SQLException {
        List<Reserva> overlaps = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status FROM reserva WHERE espaco_id = ? AND status IN (?, ?) AND ((data_hora_inicio < ? AND data_hora_fim > ?) OR (data_hora_inicio >= ? AND data_hora_inicio < ?) OR (data_hora_fim > ? AND data_hora_fim <= ?))");

        if (currentReservaId != null) {
            sqlBuilder.append(" AND id != ?");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            ps.setLong(paramIndex++, espacoId);
            ps.setString(paramIndex++, StatusReserva.APROVADA.name());
            ps.setString(paramIndex++, StatusReserva.PENDENTE.name()); // Consider pending as potentially blocking
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraFim));
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraInicio));
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraInicio));
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraFim));
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraInicio));
            ps.setTimestamp(paramIndex++, Timestamp.valueOf(dataHoraFim));

            if (currentReservaId != null) {
                ps.setLong(paramIndex++, currentReservaId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    overlaps.add(mapRow(rs));
                }
            }
        }
        return overlaps;
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reserva WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM reserva WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Reserva mapRow(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(rs.getLong("id"));
        reserva.setUsuarioId(rs.getLong("usuario_id"));
        reserva.setEspacoId(rs.getLong("espaco_id"));
        reserva.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
        reserva.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
        reserva.setStatus(StatusReserva.valueOf(rs.getString("status"))); // Convert String back to enum
        return reserva;
    }
}