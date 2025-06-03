package com.bancodedados.gestaoespaco.controller;

import com.bancodedados.gestaoespaco.model.AuditoriaUsuario;
import com.bancodedados.gestaoespaco.service.AuditoriaUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaUsuarioController {

    private final AuditoriaUsuarioService auditoriaUsuarioService;

    public AuditoriaUsuarioController(AuditoriaUsuarioService auditoriaUsuarioService) {
        this.auditoriaUsuarioService = auditoriaUsuarioService;
    }

    @PostMapping
    public ResponseEntity<AuditoriaUsuario> registrarAuditoria(
            @RequestParam Long usuarioId,
            @RequestParam String acao) {
        try {
            AuditoriaUsuario novaAuditoria = auditoriaUsuarioService.registrarAuditoria(usuarioId, acao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAuditoria);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<AuditoriaUsuario> listarTodasAuditorias() {
        return auditoriaUsuarioService.listarTodasAuditorias();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<AuditoriaUsuario> listarAuditoriasPorUsuario(@PathVariable Long usuarioId) {
        return auditoriaUsuarioService.listarAuditoriasPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaUsuario> buscarAuditoriaPorId(@PathVariable Long id) {
        return auditoriaUsuarioService.buscarAuditoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de auditoria não encontrado com ID: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAuditoria(@PathVariable Long id) {
        try {
            auditoriaUsuarioService.deletarAuditoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}