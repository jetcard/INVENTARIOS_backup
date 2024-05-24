package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="tipo")
public class Tipo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombretipo;
  private String descriptipo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombretipo() {
    return nombretipo;
  }

  public void setNombretipo(String nombretipo) {
    this.nombretipo = nombretipo;
  }

  public String getDescriptipo() {
    return descriptipo;
  }

  public void setDescriptipo(String descriptipo) {
    this.descriptipo = descriptipo;
  }
}
