package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="responsable")
public class Responsable implements Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String arearesponsable;
  private String nombresyapellidos;
  private String correo;
  private String nombreusuario;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getArearesponsable() {
    return arearesponsable;
  }

  public void setArearesponsable(String arearesponsable) {
    this.arearesponsable = arearesponsable;
  }

  public String getNombresyapellidos() {
    return nombresyapellidos;
  }

  public void setNombresyapellidos(String nombresyapellidos) {
    this.nombresyapellidos = nombresyapellidos;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getNombreusuario() {
    return nombreusuario;
  }

  public void setNombreusuario(String nombreusuario) {
    this.nombreusuario = nombreusuario;
  }
}
