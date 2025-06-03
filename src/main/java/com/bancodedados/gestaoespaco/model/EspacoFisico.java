package com.bancodedados.gestaoespaco.model;

public class EspacoFisico {

    private Long id;
    private String nome;
    private String localizacao;
    private int capacidade;
    private TipoEspaco tipo;
    private boolean disponivel;

    public EspacoFisico() {
    }

    public EspacoFisico(String nome, String localizacao, int capacidade, TipoEspaco tipo, boolean disponivel) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
        this.tipo = tipo;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public TipoEspaco getTipo() {
        return tipo;
    }

    public void setTipo(TipoEspaco tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "EspacoFisico{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", capacidade=" + capacidade +
                ", tipo=" + tipo +
                ", disponivel=" + disponivel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EspacoFisico)) return false;
        EspacoFisico that = (EspacoFisico) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}