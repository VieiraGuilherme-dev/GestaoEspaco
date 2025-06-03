package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.EspacoFisico;
import com.bancodedados.gestaoespaco.model.TipoEspaco;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EspacoFisicoRepository {

    private final DataSource dataSource;

    public EspacoFisicoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public EspacoFisico save(EspacoFisico espacoFisico) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (espacoFisico.getId() == null) {
                // INSERT operation
                sql = "INSERT INTO espaco_fisico (nome, localizacao, capacidade, tipo, disponivel) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, espacoFisico.getNome());
                    ps.setString(2, espacoFisico.getLocalizacao());
                    ps.setInt(3, espacoFisico.getCapacidade());
                    ps.setString(4, espacoFisico.getTipo().name()); // Store enum as String
                    ps.setBoolean(5, espacoFisico.isDisponivel());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            espacoFisico.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // UPDATE operation
                sql = "UPDATE espaco_fisico SET nome = ?, localizacao = ?, capacidade = ?, tipo = ?, disponivel = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, espacoFisico.getNome());
                    ps.setString(2, espacoFisico.getLocalizacao());
                    ps.setInt(3, espacoFisico.getCapacidade());
                    ps.setString(4, espacoFisico.getTipo().name()); // Store enum as String
                    ps.setBoolean(5, espacoFisico.isDisponivel());
                    ps.setLong(6, espacoFisico.getId());
                    ps.executeUpdate();
                }
            }
        }
        return espacoFisico;
    }

    public Optional<EspacoFisico> findById(Long id) throws SQLException {
        String sql = "SELECT id, nome, localizacao, capacidade, tipo, disponivel FROM espaco_fisico WHERE id = ?";
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

    public List<EspacoFisico> findAll() throws SQLException {
        List<EspacoFisico> espacos = new ArrayList<>();
        String sql = "SELECT id, nome, localizacao, capacidade, tipo, disponivel FROM espaco_fisico";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                espacos.add(mapRow(rs));
            }
        }
        return espacos;
    }

    public List<EspacoFisico> findByDisponivel(boolean disponivel) throws SQLException {
        List<EspacoFisico> espacos = new ArrayList<>();
        String sql = "SELECT id, nome, localizacao, capacidade, tipo, disponivel FROM espaco_fisico WHERE disponivel = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponivel);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    espacos.add(mapRow(rs));
                }
            }
        }
        return espacos;
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM espaco_fisico WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM espaco_fisico WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private EspacoFisico mapRow(ResultSet rs) throws SQLException {
        EspacoFisico espaco = new EspacoFisico();
        espaco.setId(rs.getLong("id"));
        espaco.setNome(rs.getString("nome"));
        espaco.setLocalizacao(rs.getString("localizacao"));
        espaco.setCapacidade(rs.getInt("capacidade"));
        espaco.setTipo(TipoEspaco.valueOf(rs.getString("tipo")));
        espaco.setDisponivel(rs.getBoolean("disponivel"));
        return espaco;
    }
}