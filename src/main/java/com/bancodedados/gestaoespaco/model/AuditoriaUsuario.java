package com.bancodedados.gestaoespaco.model;

import java.time.LocalDateTime;

public class AuditoriaUsuario {

    private Long id;
    private Long usuarioId;
    private String acao;
    private LocalDateTime dataHora;

    public AuditoriaUsuario() {
        this.dataHora = LocalDateTime.now();
    }

    public AuditoriaUsuario(Long usuarioId, String acao) {
        this.usuarioId = usuarioId;
        this.acao = acao;
        this.dataHora = LocalDateTime.now();
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

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "AuditoriaUsuario{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", acao='" + acao + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditoriaUsuario)) return false;
        AuditoriaUsuario that = (AuditoriaUsuario) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}