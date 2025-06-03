// src/main/java/com/bancodedados/gestaoespaco/controller/UsuarioController.java
package com.bancodedados.gestaoespaco.controller;

import com.bancodedados.gestaoespaco.model.Usuario;
import com.bancodedados.gestaoespaco.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController // Anotação para indicar que é um Controller REST
@RequestMapping("/api/usuarios") // Define o caminho base para todos os endpoints deste controller
@CrossOrigin(origins = "http://localhost:8080") // Permite requisições do seu frontend rodando na porta 8080
// IMPORTANTE: Em produção, ajuste para o domínio real do seu frontend!
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    // Injeção de dependência do UsuarioRepository
    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        try {
            // Verifica se o email já existe antes de salvar
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado!");
            }
            Usuario novoUsuario = usuarioRepository.save(usuario);
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (SQLException e) {
            // Logar a exceção para depuração
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar usuários: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        try {
            return usuarioRepository.findById(id)
                    .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com ID: " + id));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar usuário por ID: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            // Verifica se o usuário existe antes de tentar atualizar
            if (!usuarioRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com ID: " + id);
            }
            // Garante que o ID do objeto seja o ID da URL
            usuario.setId(id);
            Usuario usuarioAtualizado = usuarioRepository.save(usuario); // O método save do seu repositório já lida com update
            return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com ID: " + id);
            }
            usuarioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar usuário: " + e.getMessage());
        }
    }
}