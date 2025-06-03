package com.bancodedados.gestaoespaco.controller;

import com.bancodedados.gestaoespaco.model.Solicitacao;
import com.bancodedados.gestaoespaco.service.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<Solicitacao> criarSolicitacao(
            @RequestParam Long usuarioId,
            @RequestParam Long espacoId,
            @RequestParam LocalDate dataReserva,
            @RequestParam LocalTime horaReserva) {
        try {
            Solicitacao novaSolicitacao = solicitacaoService.criarSolicitacao(usuarioId, espacoId, dataReserva, horaReserva);
            return new ResponseEntity<>(novaSolicitacao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Este catch lida com exceções do serviço (por exemplo, espaço não encontrado, conflito)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<Solicitacao> listarTodas() {
        return solicitacaoService.listarTodas();
    }

    @GetMapping("/status/{status}")
    public List<Solicitacao> listarPorStatus(@PathVariable String status) {
        return solicitacaoService.listarPorStatus(status);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Solicitacao> aprovarSolicitacao(@PathVariable Long id) {
        try {
            Solicitacao solicitacaoAprovada = solicitacaoService.aprovarSolicitacao(id);
            return ResponseEntity.ok(solicitacaoAprovada);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Solicitacao> rejeitarSolicitacao(@PathVariable Long id,
                                                           @RequestParam(required = false) String justificativa) {
        try {
            Solicitacao solicitacaoRejeitada = solicitacaoService.rejeitarSolicitacao(id);
            return ResponseEntity.ok(solicitacaoRejeitada);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscarSolicitacaoPorId(@PathVariable Long id) {
        return solicitacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada com ID: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        try {
            solicitacaoService.deletarSolicitacao(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}