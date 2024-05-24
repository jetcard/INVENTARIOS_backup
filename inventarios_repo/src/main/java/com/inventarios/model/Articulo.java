package com.inventarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="articulo")
public class Articulo implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombrearticulo;
  private String descriparticulo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombrearticulo() {
    return nombrearticulo;
  }

  public void setNombrearticulo(String nombrearticulo) {
    this.nombrearticulo = nombrearticulo;
  }

  public String getDescriparticulo() {
    return descriparticulo;
  }

  public void setDescriparticulo(String descriparticulo) {
    this.descriparticulo = descriparticulo;
  }
}
