package com.crediya.users.users_service.infraestructure.controllers.dto;

import com.crediya.users.users_service.domain.model.Rol;
import com.crediya.users.users_service.domain.model.User;

import java.util.UUID;

public class RolResponseDTO {

    private UUID id;
    private String nombre;
    private String descripcion;
    private String correoElectronico;

    public void setId(UUID id) { this.id = id;}
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }


    //Metodo estático para convertir un objeto de dominio en un DTO
    public static RolResponseDTO fromRol(Rol rol) {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }
}
