package com.crediya.users.users_service.infraestructure.controllers.dto.rol;

import com.crediya.users.users_service.domain.model.Rol;


public class RolRequestDTO {

    private String nombre;
    private String descripcion;


    public void setNombre(String nombre) {   this.nombre = nombre;  }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    //Metodo para convertir el DTO en un objeto de dominio
    public Rol toRol() {
        Rol rol = new Rol();
        rol.setNombre(this.nombre);
        rol.setDescripcion(this.descripcion);
        return rol;
    }
}
