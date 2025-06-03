package com.bancodedados.gestaoespaco.service;

import com.bancodedados.gestaoespaco.model.TipoUsuario;
import com.bancodedados.gestaoespaco.model.Usuario;
import com.bancodedados.gestaoespaco.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario criarUsuario(String nome, String email, String senha, TipoUsuario tipo) {
        try {
            if (usuarioRepository.existsByEmail(email)) {
                throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
            }
            Usuario novoUsuario = new Usuario(nome, email, senha, tipo);
            return usuarioRepository.save(novoUsuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, String nome, String email, String senha, TipoUsuario tipo) {
        try {
            Usuario usuarioExistente = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

            // Check if email is being changed to an existing one by another user
            if (!usuarioExistente.getEmail().equalsIgnoreCase(email)) {
                if (usuarioRepository.existsByEmail(email)) {
                    throw new RuntimeException("Já existe outro usuário cadastrado com este e-mail: " + email);
                }
            }

            usuarioExistente.setNome(nome);
            usuarioExistente.setEmail(email);
            usuarioExistente.setSenha(senha); // In a real app, handle password hashing
            usuarioExistente.setTipo(tipo);

            return usuarioRepository.save(usuarioExistente);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        try {
            return usuarioRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        try {
            return usuarioRepository.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        try {
            return usuarioRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os usuários: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarUsuariosPorTipo(TipoUsuario tipo) {
        try {
            return usuarioRepository.findByTipo(tipo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários por tipo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletarUsuario(Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new RuntimeException("Usuário não encontrado com ID: " + id);
            }
            usuarioRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }
}