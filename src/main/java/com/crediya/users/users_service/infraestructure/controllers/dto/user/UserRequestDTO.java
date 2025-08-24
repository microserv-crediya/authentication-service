package com.crediya.users.users_service.infraestructure.controllers.dto.user;

import com.crediya.users.users_service.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserRequestDTO {

    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private LocalDate fechaNacimiento;
    private String documentoIdentidad;



    public UserRequestDTO(String nombres, String apellidos, String direccion, String telefono, String correoElectronico, BigDecimal salarioBase, LocalDate fechaNacimiento, String documentoIdentidad) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.fechaNacimiento = fechaNacimiento;
        this.documentoIdentidad = documentoIdentidad;
    }

    public void setNombres(String nombres) {   this.nombres = nombres;  }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase;  }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }


    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono;  }
    public String getCorreoElectronico() { return correoElectronico; }
    public BigDecimal getSalarioBase() { return salarioBase; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }

    //Convertir el DTO en un objeto de dominio
    public User toUser() {
        User user = new User();
        user.setNombres(this.nombres);
        user.setApellidos(this.apellidos);
        user.setDireccion(this.direccion);
        user.setTelefono(this.telefono);
        user.setCorreoElectronico(this.correoElectronico);
        user.setSalarioBase(this.salarioBase);
        user.setFechaNacimiento(this.fechaNacimiento);
        user.setDocumentoIdentidad(this.documentoIdentidad);
        return user;
    }
}
