package com.bancodedados.gestaoespaco.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Solicitacao {

    private Long id;
    private Long usuarioId;
    private Long espacoId;
    private LocalDate dataReserva;
    private LocalTime horaReserva;
    private LocalDateTime dataSolicitacao;
    private String status; // PENDENTE, APROVADA, REJEITADA

    public Solicitacao() {
        this.dataSolicitacao = LocalDateTime.now();
        this.status = "PENDENTE"; // Default status
    }

    public Solicitacao(Long usuarioId, Long espacoId, LocalDate dataReserva, LocalTime horaReserva) {
        this.usuarioId = usuarioId;
        this.espacoId = espacoId;
        this.dataReserva = dataReserva;
        this.horaReserva = horaReserva;
        this.dataSolicitacao = LocalDateTime.now();
        this.status = "PENDENTE"; // Default status
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEspacoId() {
        return espacoId;
    }

    public void setEspacoId(Long espacoId) {
        this.espacoId = espacoId;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Solicitacao{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", espacoId=" + espacoId +
                ", dataReserva=" + dataReserva +
                ", horaReserva=" + horaReserva +
                ", dataSolicitacao=" + dataSolicitacao +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solicitacao)) return false;
        Solicitacao that = (Solicitacao) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}