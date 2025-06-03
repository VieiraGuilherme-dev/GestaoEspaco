package com.bancodedados.gestaoespaco.service;

import com.bancodedados.gestaoespaco.model.AuditoriaUsuario;
import com.bancodedados.gestaoespaco.repository.AuditoriaUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class AuditoriaUsuarioService {

    private final AuditoriaUsuarioRepository auditoriaUsuarioRepository;

    public AuditoriaUsuarioService(AuditoriaUsuarioRepository auditoriaUsuarioRepository) {
        this.auditoriaUsuarioRepository = auditoriaUsuarioRepository;
    }

    @Transactional
    public AuditoriaUsuario registrarAuditoria(Long usuarioId, String acao) {
        AuditoriaUsuario auditoria = new AuditoriaUsuario(usuarioId, acao);
        try {
            return auditoriaUsuarioRepository.save(auditoria);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar auditoria: " + e.getMessage(), e);
        }
    }

    public List<AuditoriaUsuario> listarTodasAuditorias() {
        try {
            return auditoriaUsuarioRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar auditorias: " + e.getMessage(), e);
        }
    }

    public List<AuditoriaUsuario> listarAuditoriasPorUsuario(Long usuarioId) {
        try {
            return auditoriaUsuarioRepository.findByUsuarioId(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar auditorias por usuário: " + e.getMessage(), e);
        }
    }

    public Optional<AuditoriaUsuario> buscarAuditoriaPorId(Long id) {
        try {
            return auditoriaUsuarioRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar auditoria por ID: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletarAuditoria(Long id) {
        try {
            if (!auditoriaUsuarioRepository.existsById(id)) {
                throw new RuntimeException("Registro de auditoria não encontrado com ID: " + id);
            }
            auditoriaUsuarioRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar registro de auditoria: " + e.getMessage(), e);
        }
    }
}