package com.crediya.users.users_service.infraestructure.controllers.dto;

import com.crediya.users.users_service.domain.model.User;

import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;

    public void setId(UUID id) { this.id = id;}
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public UUID getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreoElectronico() {return correoElectronico; }

    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico;  }



    //Metodo estático para convertir un objeto de dominio en un DTO
    public static UserResponseDTO fromUser(User User) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(User.getId());
        dto.setNombres(User.getNombres());
        dto.setApellidos(User.getApellidos());
        dto.setCorreoElectronico(User.getCorreoElectronico());
        return dto;
    }
}
