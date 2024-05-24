package com.inventarios.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
  @Table(name="grupo")
  public class Grupo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombregrupo;
    private String descripgrupo;
    @Transient
    private String idAlfanumerico;
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombregrupo() {
    return nombregrupo;
  }

  public void setNombregrupo(String nombregrupo) {
    this.nombregrupo = nombregrupo;
  }

  public String getDescripgrupo() {
    return descripgrupo;
  }

  public void setDescripgrupo(String descripgrupo) {
    this.descripgrupo = descripgrupo;
  }

  public String getIdAlfanumerico() {
    return idAlfanumerico;
  }

  public void setIdAlfanumerico(String idAlfanumerico) {
    this.idAlfanumerico = idAlfanumerico;
  }
}
