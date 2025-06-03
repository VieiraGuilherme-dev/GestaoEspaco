package com.bancodedados.gestaoespaco.repository;

import com.bancodedados.gestaoespaco.model.TipoUsuario;
import com.bancodedados.gestaoespaco.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final DataSource dataSource;

    public UsuarioRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Usuario save(Usuario usuario) throws SQLException {
        String sql;
        try (Connection conn = dataSource.getConnection()) {
            if (usuario.getId() == null) {
                // INSERT operation
                sql = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenha()); // Em uma aplicação real, use hashing de senha!
                    ps.setString(4, usuario.getTipo().name()); // Store enum as String
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            usuario.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // UPDATE operation
                sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, tipo = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenha()); // Em uma aplicação real, use hashing de senha!
                    ps.setString(4, usuario.getTipo().name()); // Store enum as String
                    ps.setLong(5, usuario.getId());
                    ps.executeUpdate();
                }
            }
        }
        return usuario;
    }

    public Optional<Usuario> findById(Long id) throws SQLException {
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario WHERE id = ?";
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


    public Optional<Usuario> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }


    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        }
        return usuarios;
    }

    public List<Usuario> findByTipo(TipoUsuario tipo) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario WHERE tipo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name()); // Convert enum to String for database
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapRow(rs));
                }
            }
        }
        return usuarios;
    }


    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // Helper method to map a ResultSet row to a Usuario object
    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTipo(TipoUsuario.valueOf(rs.getString("tipo"))); // Convert String back to enum
        return usuario;
    }
}