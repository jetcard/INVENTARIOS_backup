package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

public class Comun implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Responsable responsable;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Tipo tipo;
    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Grupo grupo;

    private String descripcomun;
    private String descripcortacomun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getDescripcomun() {
        return descripcomun;
    }

    public void setDescripcomun(String descripcomun) {
        this.descripcomun = descripcomun;
    }

    public String getDescripcortacomun() {
        return descripcortacomun;
    }

    public void setDescripcortacomun(String descripcortacomun) {
        this.descripcortacomun = descripcortacomun;
    }
}
