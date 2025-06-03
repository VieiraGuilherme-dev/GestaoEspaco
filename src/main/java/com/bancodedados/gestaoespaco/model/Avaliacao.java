package com.bancodedados.gestaoespaco.model;

import java.time.LocalDateTime;

public class Avaliacao {

    private Long id;
    private Long solicitacaoId;
    private Long gestorId;
    private String status; // APROVADA, REJEITADA
    private String justificativa;
    private LocalDateTime dataAvaliacao;

    public Avaliacao() {
        this.dataAvaliacao = LocalDateTime.now();
    }

    public Avaliacao(Long solicitacaoId, Long gestorId, String status, String justificativa) {
        this.solicitacaoId = solicitacaoId;
        this.gestorId = gestorId;
        this.status = status;
        this.justificativa = justificativa;
        this.dataAvaliacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSolicitacaoId() {
        return solicitacaoId;
    }

    public void setSolicitacaoId(Long solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public Long getGestorId() {
        return gestorId;
    }

    public void setGestorId(Long gestorId) {
        this.gestorId = gestorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", solicitacaoId=" + solicitacaoId +
                ", gestorId=" + gestorId +
                ", status='" + status + '\'' +
                ", justificativa='" + justificativa + '\'' +
                ", dataAvaliacao=" + dataAvaliacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avaliacao)) return false;
        Avaliacao that = (Avaliacao) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}