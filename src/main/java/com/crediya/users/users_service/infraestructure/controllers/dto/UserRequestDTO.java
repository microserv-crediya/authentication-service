package com.crediya.users.users_service.infraestructure.controllers.dto;

import com.crediya.users.users_service.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class UserRequestDTO {

    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private LocalDate fechaNacimiento;

    public void setNombres(String nombres) {   this.nombres = nombres;  }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase;  }

    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono;  }
    public String getCorreoElectronico() { return correoElectronico; }
    public BigDecimal getSalarioBase() { return salarioBase; }

    // Método para convertir el DTO en un objeto de dominio
    public User toUser() {
        User User = new User();
        User.setNombres(this.nombres);
        User.setApellidos(this.apellidos);
        User.setDireccion(this.direccion);
        User.setTelefono(this.telefono);
        User.setCorreoElectronico(this.correoElectronico);
        User.setSalarioBase(this.salarioBase);
        User.setFechaNacimiento(this.fechaNacimiento);
        return User;
    }
}
