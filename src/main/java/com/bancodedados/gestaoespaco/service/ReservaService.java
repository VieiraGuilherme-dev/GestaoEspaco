package com.bancodedados.gestaoespaco.service;

import com.bancodedados.gestaoespaco.model.Reserva;
import com.bancodedados.gestaoespaco.model.StatusReserva;
import com.bancodedados.gestaoespaco.repository.ReservaRepository;
import com.bancodedados.gestaoespaco.repository.EspacoFisicoRepository; // Needed for space availability
import com.bancodedados.gestaoespaco.repository.UsuarioRepository; // Needed to check if user exists
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EspacoFisicoRepository espacoFisicoRepository;
    private final UsuarioRepository usuarioRepository; // Assuming you have a UsuarioRepository

    public ReservaService(ReservaRepository reservaRepository, EspacoFisicoRepository espacoFisicoRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.espacoFisicoRepository = espacoFisicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva criarReserva(Long usuarioId, Long espacoId, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        try {

            if (!usuarioRepository.existsById(usuarioId)) {
                throw new RuntimeException("Usuário não encontrado com ID: " + usuarioId);
            }
            if (!espacoFisicoRepository.existsById(espacoId)) {
                throw new RuntimeException("Espaço físico não encontrado com ID: " + espacoId);
            }


            if (dataHoraFim.isBefore(dataHoraInicio) || dataHoraFim.isEqual(dataHoraInicio)) {
                throw new RuntimeException("A data/hora de fim deve ser posterior à data/hora de início.");
            }

            List<Reserva> overlappingReservas = reservaRepository.findOverlappingReservas(espacoId, dataHoraInicio, dataHoraFim, null);
            if (!overlappingReservas.isEmpty()) {
                throw new RuntimeException("Já existe uma reserva para este espaço no período solicitado.");
            }

            Reserva novaReserva = new Reserva(usuarioId, espacoId, dataHoraInicio, dataHoraFim);
            return reservaRepository.save(novaReserva);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar reserva: " + e.getMessage(), e);
        }
    }

    public Optional<Reserva> buscarReserva(Long id) {
        try {
            return reservaRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva por ID: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarReservas() {
        try {
            return reservaRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Reserva aprovarReserva(Long id) {
        try {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));

            if (reserva.getStatus() != StatusReserva.PENDENTE) {
                throw new RuntimeException("A reserva não pode ser aprovada pois não está no status PENDENTE.");
            }

            List<Reserva> overlappingReservas = reservaRepository.findOverlappingReservas(
                    reserva.getEspacoId(),
                    reserva.getDataHoraInicio(),
                    reserva.getDataHoraFim(),
                    reserva.getId() // Exclude current reservation from overlap check
            );
            if (!overlappingReservas.isEmpty()) {
                throw new RuntimeException("Não é possível aprovar a reserva devido a conflito de horários com outra reserva já aprovada ou pendente.");
            }

            reserva.setStatus(StatusReserva.APROVADA);
            return reservaRepository.save(reserva);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao aprovar reserva: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Reserva recusarReserva(Long id) {
        try {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));

            if (reserva.getStatus() != StatusReserva.PENDENTE) {
                throw new RuntimeException("A reserva não pode ser recusada pois não está no status PENDENTE.");
            }

            reserva.setStatus(StatusReserva.REJEITADA);
            return reservaRepository.save(reserva);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao recusar reserva: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Reserva cancelarReserva(Long id) {
        try {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));

            if (reserva.getStatus() == StatusReserva.APROVADA || reserva.getStatus() == StatusReserva.PENDENTE) {
                reserva.setStatus(StatusReserva.CANCELADA);
                return reservaRepository.save(reserva);
            } else {
                throw new RuntimeException("A reserva não pode ser cancelada no status atual: " + reserva.getStatus());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar reserva: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarReservasOrdenadasPorDataHoraInicio() {
        try {
            return reservaRepository.findAll().stream()
                    .sorted(Comparator.comparing(Reserva::getDataHoraInicio))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas ordenadas por data/hora de início: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarReservasOrdenadasPorStatus() {
        try {
            return reservaRepository.findAll().stream()
                    .sorted(Comparator.comparing(Reserva::getStatus))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas ordenadas por status: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarPorStatus(String status) {
        try {
            StatusReserva statusEnum = StatusReserva.valueOf(status.toUpperCase());
            return reservaRepository.findByStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status de reserva inválido: " + status);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas por status: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarPorUsuario(Long usuarioId) {
        try {
            return reservaRepository.findByUsuarioId(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas por usuário: " + e.getMessage(), e);
        }
    }

    public List<Reserva> listarPorEspaco(Long espacoId) {
        try {
            return reservaRepository.findByEspacoId(espacoId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas por espaço: " + e.getMessage(), e);
        }
    }
}