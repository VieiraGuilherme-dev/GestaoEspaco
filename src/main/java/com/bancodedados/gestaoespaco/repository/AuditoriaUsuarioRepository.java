package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.AuditoriaUsuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuditoriaUsuarioRepository {

    private final DataSource dataSource;

    public AuditoriaUsuarioRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AuditoriaUsuario save(AuditoriaUsuario auditoria) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (auditoria.getId() == null) {
                // INSERT operation
                sql = "INSERT INTO auditoria_usuario (usuario_id, acao, data_hora) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, auditoria.getUsuarioId());
                    ps.setString(2, auditoria.getAcao());
                    ps.setTimestamp(3, Timestamp.valueOf(auditoria.getDataHora()));
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            auditoria.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // UPDATE operation
                sql = "UPDATE auditoria_usuario SET usuario_id = ?, acao = ?, data_hora = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, auditoria.getUsuarioId());
                    ps.setString(2, auditoria.getAcao());
                    ps.setTimestamp(3, Timestamp.valueOf(auditoria.getDataHora()));
                    ps.setLong(4, auditoria.getId());
                    ps.executeUpdate();
                }
            }
        }
        return auditoria;
    }

    public Optional<AuditoriaUsuario> findById(Long id) throws SQLException {
        String sql = "SELECT id, usuario_id, acao, data_hora FROM auditoria_usuario WHERE id = ?";
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

    public List<AuditoriaUsuario> findAll() throws SQLException {
        List<AuditoriaUsuario> auditorias = new ArrayList<>();
        String sql = "SELECT id, usuario_id, acao, data_hora FROM auditoria_usuario ORDER BY data_hora DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                auditorias.add(mapRow(rs));
            }
        }
        return auditorias;
    }

    public List<AuditoriaUsuario> findByUsuarioId(Long usuarioId) throws SQLException {
        List<AuditoriaUsuario> auditorias = new ArrayList<>();
        String sql = "SELECT id, usuario_id, acao, data_hora FROM auditoria_usuario WHERE usuario_id = ? ORDER BY data_hora DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    auditorias.add(mapRow(rs));
                }
            }
        }
        return auditorias;
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM auditoria_usuario WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM auditoria_usuario WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // Helper method to map a ResultSet row to an AuditoriaUsuario object
    private AuditoriaUsuario mapRow(ResultSet rs) throws SQLException {
        AuditoriaUsuario auditoria = new AuditoriaUsuario();
        auditoria.setId(rs.getLong("id"));
        auditoria.setUsuarioId(rs.getLong("usuario_id"));
        auditoria.setAcao(rs.getString("acao"));
        auditoria.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        return auditoria;
    }
}