package com.crediya.users.users_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private LocalDate fechaNacimiento;
    private String documentoIdentidad;

    // Metodo de validación a nivel de dominio - HU - Criterios - item 2
    public boolean isValid() {
        return nombres != null && !nombres.trim().isEmpty() &&
                apellidos != null && !apellidos.trim().isEmpty() &&
                correoElectronico != null && !correoElectronico.trim().isEmpty() &&
                salarioBase != null && salarioBase.compareTo(BigDecimal.ZERO) > 0;
    }
}
