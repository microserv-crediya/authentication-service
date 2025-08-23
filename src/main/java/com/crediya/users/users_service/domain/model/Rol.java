package com.crediya.users.users_service.domain.model;

import java.util.UUID;

public class Rol {

    private UUID  id;
    private String nombre;
    private String descripcion;

    //Constructor vacío, necesario para el mapeo con Spring Data
    public Rol() {}

    //Constructor completo
    public Rol(UUID  id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void setId(UUID  id) {  this.id = id;  }
    public void setNombre(String nombre) {   this.nombre = nombre;  }
    public void setDescripcion (String descripcion ) {   this.descripcion  = descripcion;  }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

}
