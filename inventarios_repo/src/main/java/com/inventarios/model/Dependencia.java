package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
  @Table(name="dependencia")
  public class Dependencia implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombredependencia;
    private String descripdependencia;
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getNombredependencia() {
      return nombredependencia;
    }

    public void setNombredependencia(String nombredependencia) {
      this.nombredependencia = nombredependencia;
    }

    public String getDescripdependencia() {
      return descripdependencia;
    }

    public void setDescripdependencia(String descripdependencia) {
      this.descripdependencia = descripdependencia;
    }
}
