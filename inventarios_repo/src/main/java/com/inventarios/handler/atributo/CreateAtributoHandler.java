package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.CreateAtributoAbstractHandler;
import com.inventarios.model.Atributo;
import com.inventarios.model.Atributos;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.util.List;

public class CreateAtributoHandler extends CreateAtributoAbstractHandler {

  @Override
  public int getAtributoID(Atributo atributo) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      int atributoid = dsl.transactionResult(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        return insertAtributo(transactionalDsl, atributo);
      });
      return atributoid;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        long atributoid = atributo.getId();//updateAtributo(transactionalDsl, atributo);
        System.out.println("atributoid = "+atributo.getId());
        atributo.setId(atributoid);
        System.out.println("WHERE atributoid = "+atributo.getId());
        System.out.println("WHERE getResponsable = "+atributo.getResponsable());
        System.out.println("WHERE getArticulo = "+atributo.getArticulo());
        System.out.println("WHERE getAtributos = "+atributo.getAtributos().get(0));
        //System.out.println("WHERE atributoid = "+atributoid);

        for (Atributos atributos : atributosList) {
          atributos.setAtributo(atributo);
          //atributos.setAtributo(new Atributo(atributo.getId(), atributo.getResponsable(), atributo.getArticulo()));
          //atributos.setAtributo(atributo); // Establecer la relación con el Atributo padre
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  ///@Override
  /*public void save1xx(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        /*insertAtributo(transactionalDsl, atributo);
        System.out.println("WHERE atributoid = "+atributo.getId());
        System.out.println("WHERE getResponsable = "+atributo.getResponsable());
        System.out.println("WHERE getArticulo = "+atributo.getArticulo());
        System.out.println("WHERE getAtributos = "+atributo.getAtributos().get(0));
        //System.out.println("WHERE atributoid = "+atributoid);* /

        for (Atributos atributos : atributosList) {
          atributos.setAtributo(atributo);
          //atributos.setAtributo(new Atributo(atributo.getId(), atributo.getResponsable(), atributo.getArticulo()));
          //atributos.setAtributo(atributo); // Establecer la relación con el Atributo padre
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  private int insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableid"), atributo.getResponsable().getId())
            .set(DSL.field("articuloid"), atributo.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Integer.class));
  }

  private int updateAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.update(ATRIBUTO_TABLE)
          .set(DSL.field("responsableid"), atributo.getResponsable().getId())
          .set(DSL.field("articuloid"), atributo.getArticulo().getId())
          .where(DSL.field("id").eq(atributo.getId())) // Especificar la condición de actualización
          .execute(); // Ejecutar la actualización y devolver el número de filas afectadas
}

  /*private void insertAtributox(DSLContext dsl, Atributo atributo) {
    dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .execute();
  }*/

  private void insertAtributos(DSLContext dsl, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoid"), atributos.getAtributo().getId())
            .set(DSL.field("nombreatributo"), atributos.getNombreatributo())
            .execute();
  }

  /*public void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = insertAtributo(transactionalDsl, atributo);

        for (Atributos atributoItem : atributosList) {
          // Establecer la relación con el atributo usando el constructor que acepta el ID del atributo
          atributoItem.setAtributo(new Atributo(atributoId));
          insertAtributos(transactionalDsl, atributoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("atributoId"))
            .fetchOne()
            .getValue(DSL.field("atributoId", Long.class));
  }

  private void insertAtributos(DSLContext dsl, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributos.getAtributo().getId())
            .set(DSL.field("nombreatributo"), atributos.getNombreatributo())
            .set(DSL.field("descripatributo"), atributos.getDescripatributo())
            .execute();
  }*/
  /*public void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = insertAtributo(transactionalDsl, atributo);

        for (Atributos atributoItem : atributosList) {
          // Establecer la relación con el atributo usando el constructor que acepta el ID del atributo
          atributoItem.setAtributo(new Atributo(atributoId));
          insertAtributos(transactionalDsl, atributoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("atributoId"))
            .fetchOne()
            .getValue(DSL.field("atributoId", Long.class));
  }

  private void insertAtributos(DSLContext dsl, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributos.getAtributo().getId())
            .set(DSL.field("atributo"), atributos.getAtributo())
            .execute();
  }*/
}

  /*public void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = insertAtributo(transactionalDsl, atributo);

        for (Atributos atributos : atributosList) {
          atributos.setAtributo(new Atributo(atributoId)); // Establecer la relación con el atributo
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertAtributos(DSLContext dsl, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributos.getAtributo().getId())
            .set(DSL.field("atributo"), atributos.getAtributo())
            .execute();
  }*/
  /*public void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = insertAtributo(transactionalDsl, atributo);

        for (Atributos atributos : atributosList) {
          insertAtributos(transactionalDsl, atributoId, atributos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertAtributos(DSLContext dsl, Long atributoId, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributoId)
            .set(DSL.field("descripcion"), atributos.getDescripcion())
            .execute();
  }*/
  /*protected void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = insertAtributo(transactionalDsl, atributo);

        for (Atributos atributos : atributosList) {
          insertAtributos(transactionalDsl, atributoId, atributos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertAtributos(DSLContext dsl, Long atributoId, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributoId)
            .set(DSL.field("descripcion"), atributos.getDescripcion())
            .execute();
  }*/
  /*@Override
  protected void save(Atributo atributo, List<Atributos> atributosList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = transactionalDsl.insertInto(ATRIBUTO_TABLE)
                .set(DSL.field("responsableId"), atributo.getResponsable().getId())
                .set(DSL.field("articuloId"), atributo.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        for (Atributos atributos : atributosList) {
          transactionalDsl.insertInto(ATRIBUTOS_TABLE)
                  .set(DSL.field("atributoId"), atributoId)
                  .set(DSL.field("descripcion"), atributos.getDescripcion())
                  .execute();
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  /*protected void save(Atributo atributo, Atributos atributos, Long grupoID) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long atributoId = transactionalDsl.insertInto(ATRIBUTO_TABLE)
                .set(DSL.field("responsableId"), atributo.getResponsable().getId())
                .set(DSL.field("articuloId"), atributo.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        transactionalDsl.insertInto(ATRIBUTOS_TABLE)
                .set(DSL.field("atributoId"), atributoId)
                .set(DSL.field("descripcion"), atributos.getDescripcion())
                .execute();
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/
  /*protected void save(Atributo atributo, Atributos atributos, Long grupoID) {
    DSLContext dsl = DatabaseConnection.getDSL();
    Long atributoId = dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsableId"), atributo.getResponsable().getId())
            .set(DSL.field("articuloId"), atributo.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoId"), atributoId)
            .set(DSL.field("descripcion"), atributos.getDescripcion())
            .execute();
  }*/

    /*
    var dsl = DatabaseConnection.getDSL();
    dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("responsable"), atributo.getResponsable())
            .set(DSL.field("articulo"), atributo.getArticulo())
            .set(DSL.field("grupoId"), grupoID)
            .execute();*/