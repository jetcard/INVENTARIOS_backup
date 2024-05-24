package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="atributo")
public class Atributo  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Responsable responsable;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Articulo articulo;

    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atributos> atributos;

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

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public List<Atributos> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributos> atributos) {
        this.atributos = atributos;
    }

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Dependencia dependencia;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Articulo articulo;

    private Long dependenciaId;
    private Long articuloId;

    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL)
    private List<Atributos> atributos; // Cambiado a lista de objetos Atributos

    public Atributo() {
        // Constructor sin argumentos
    }

    // Constructor que acepta dependencia y articulo
    public Atributo(Dependencia dependencia, Articulo articulo) {
        this.dependencia = dependencia;
        this.articulo = articulo;
    }

    // Constructor que acepta solo el ID
    public Atributo(Long id) {
        this.id = id;
    }

    / *
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Atributo atributo;* /

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Long getDependenciaId() {
        return dependenciaId;
    }

    public void setDependenciaId(Long dependenciaId) {
        this.dependenciaId = dependenciaId;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(Long articuloId) {
        this.articuloId = articuloId;
    }

    public List<Atributos> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributos> atributos) {
        this.atributos = atributos;
    }*/
}
