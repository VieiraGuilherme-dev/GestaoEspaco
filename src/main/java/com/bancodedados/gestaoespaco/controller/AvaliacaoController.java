package com.bancodedados.gestaoespaco.controller;

import com.bancodedados.gestaoespaco.model.Avaliacao;
import com.bancodedados.gestaoespaco.service.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criarAvaliacao(
            @RequestParam Long solicitacaoId,
            @RequestParam Long gestorId,
            @RequestParam String status,
            @RequestParam(required = false) String justificativa) {
        try {
            Avaliacao novaAvaliacao = avaliacaoService.criarAvaliacao(solicitacaoId, gestorId, status, justificativa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    public List<Avaliacao> listarTodasAvaliacoes() {
        return avaliacaoService.listarTodasAvaliacoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarAvaliacaoPorId(@PathVariable Long id) {
        return avaliacaoService.buscarAvaliacaoPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada com ID: " + id));
    }

    @GetMapping("/solicitacao/{solicitacaoId}")
    public ResponseEntity<Avaliacao> buscarAvaliacaoPorSolicitacaoId(@PathVariable Long solicitacaoId) {
        return avaliacaoService.buscarAvaliacaoPorSolicitacaoId(solicitacaoId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada para a Solicitação com ID: " + solicitacaoId));
    }


    @GetMapping("/status/{status}")
    public List<Avaliacao> listarAvaliacoesPorStatus(@PathVariable String status) {
        return avaliacaoService.listarAvaliacoesPorStatus(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizarAvaliacao(
            @PathVariable Long id,
            @RequestParam Long solicitacaoId,
            @RequestParam Long gestorId,
            @RequestParam String status,
            @RequestParam(required = false) String justificativa) {
        try {
            Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(id, solicitacaoId, gestorId, status, justificativa);
            return ResponseEntity.ok(avaliacaoAtualizada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
        try {
            avaliacaoService.deletarAvaliacao(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}