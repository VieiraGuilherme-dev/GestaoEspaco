package com.bancodedados.gestaoespaco.service;

import com.bancodedados.gestaoespaco.model.Avaliacao;
import com.bancodedados.gestaoespaco.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Transactional
    public Avaliacao criarAvaliacao(Long solicitacaoId, Long gestorId, String status, String justificativa) {
        Avaliacao novaAvaliacao = new Avaliacao(solicitacaoId, gestorId, status, justificativa);
        try {
            return avaliacaoRepository.save(novaAvaliacao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar avaliação: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Avaliacao atualizarAvaliacao(Long id, Long solicitacaoId, Long gestorId, String status, String justificativa) {
        try {
            Avaliacao avaliacaoExistente = avaliacaoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

            avaliacaoExistente.setSolicitacaoId(solicitacaoId);
            avaliacaoExistente.setGestorId(gestorId);
            avaliacaoExistente.setStatus(status);
            avaliacaoExistente.setJustificativa(justificativa);
            avaliacaoExistente.setDataAvaliacao(LocalDateTime.now()); // Update timestamp on modification

            return avaliacaoRepository.save(avaliacaoExistente);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar avaliação: " + e.getMessage(), e);
        }
    }

    public Optional<Avaliacao> buscarAvaliacaoPorId(Long id) {
        try {
            return avaliacaoRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliação por ID: " + e.getMessage(), e);
        }
    }

    public List<Avaliacao> listarTodasAvaliacoes() {
        try {
            return avaliacaoRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as avaliações: " + e.getMessage(), e);
        }
    }

    public List<Avaliacao> listarAvaliacoesPorStatus(String status) {
        try {
            return avaliacaoRepository.findByStatus(status);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar avaliações por status: " + e.getMessage(), e);
        }
    }

    public Optional<Avaliacao> buscarAvaliacaoPorSolicitacaoId(Long solicitacaoId) {
        try {
            return avaliacaoRepository.findBySolicitacaoId(solicitacaoId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliação por ID de solicitação: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletarAvaliacao(Long id) {
        try {
            if (!avaliacaoRepository.existsById(id)) {
                throw new RuntimeException("Avaliação não encontrada com ID: " + id);
            }
            avaliacaoRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar avaliação: " + e.getMessage(), e);
        }
    }
}