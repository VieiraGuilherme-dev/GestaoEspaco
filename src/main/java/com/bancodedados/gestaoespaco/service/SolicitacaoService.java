package com.bancodedados.gestaoespaco.service;

import com.bancodedados.gestaoespaco.model.Solicitacao;
import com.bancodedados.gestaoespaco.repository.SolicitacaoRepository;
import com.bancodedados.gestaoespaco.repository.EspacoFisicoRepository;
import com.bancodedados.gestaoespaco.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final EspacoFisicoRepository espacoFisicoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository, EspacoFisicoRepository espacoFisicoRepository, UsuarioRepository usuarioRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.espacoFisicoRepository = espacoFisicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Solicitacao criarSolicitacao(Long usuarioId, Long espacoId, LocalDate dataReserva, LocalTime horaReserva) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                throw new RuntimeException("Usuário não encontrado com ID: " + usuarioId);
            }
            if (!espacoFisicoRepository.existsById(espacoId)) {
                throw new RuntimeException("Espaço físico não encontrado com ID: " + espacoId);
            }

            Solicitacao novaSolicitacao = new Solicitacao(usuarioId, espacoId, dataReserva, horaReserva);
            novaSolicitacao.setDataSolicitacao(LocalDateTime.now());
            novaSolicitacao.setStatus("PENDENTE"); // Initial status

            return solicitacaoRepository.save(novaSolicitacao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar solicitação: " + e.getMessage(), e);
        }
    }

    public List<Solicitacao> listarTodas() {
        try {
            return solicitacaoRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as solicitações: " + e.getMessage(), e);
        }
    }

    public List<Solicitacao> listarPorStatus(String status) {
        try {
            return solicitacaoRepository.findByStatus(status.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar solicitações por status: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Solicitacao aprovarSolicitacao(Long id) {
        try {
            Solicitacao solicitacao = solicitacaoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Solicitação não encontrada com ID: " + id));

            if (!solicitacao.getStatus().equals("PENDENTE")) {
                throw new RuntimeException("A solicitação não pode ser aprovada pois não está no status PENDENTE.");
            }

            solicitacao.setStatus("APROVADA");
            return solicitacaoRepository.save(solicitacao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao aprovar solicitação: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Solicitacao rejeitarSolicitacao(Long id) {
        try {
            Solicitacao solicitacao = solicitacaoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Solicitação não encontrada com ID: " + id));

            if (!solicitacao.getStatus().equals("PENDENTE")) {
                throw new RuntimeException("A solicitação não pode ser rejeitada pois não está no status PENDENTE.");
            }

            solicitacao.setStatus("REJEITADA");
            return solicitacaoRepository.save(solicitacao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao rejeitar solicitação: " + e.getMessage(), e);
        }
    }

    public Optional<Solicitacao> buscarPorId(Long id) {
        try {
            return solicitacaoRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar solicitação por ID: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletarSolicitacao(Long id) {
        try {
            if (!solicitacaoRepository.existsById(id)) {
                throw new RuntimeException("Solicitação não encontrada com ID: " + id);
            }
            solicitacaoRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar solicitação: " + e.getMessage(), e);
        }
    }
}