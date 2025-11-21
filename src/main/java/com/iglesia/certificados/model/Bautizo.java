package com.iglesia.certificados.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // 1. Le dice a Spring: "Esta clase ES una tabla en la base de datos"
public class Bautizo {

    @Id // 2. Le dice a Spring: "Este campo es la Clave Primaria (Primary Key)"
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. Le dice a Spring: "Numera este campo automáticamente (1, 2, 3...)"
    private Long id;

    private String nombreBautizado;
    private String apellidoBautizado;
    private String nombrePadre;
    private String nombreMadre;
    private String nombrePadrino;
    private String nombreMadrina;
    // --- CONSTRUCTOR VACÍO (Buena práctica para JPA) ---
    public Bautizo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreBautizado() {
        return nombreBautizado;
    }

    public void setNombreBautizado(String nombreBautizado) {
        this.nombreBautizado = nombreBautizado;
    }

    public String getApellidoBautizado() {
        return apellidoBautizado;
    }

    public void setApellidoBautizado(String apellidoBautizado) {
        this.apellidoBautizado = apellidoBautizado;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }

    public String getNombreMadre() {
        return nombreMadre;
    }

    public void setNombreMadre(String nombreMadre) {
        this.nombreMadre = nombreMadre;
    }

    public String getNombrePadrino() {
        return nombrePadrino;
    }

    public void setNombrePadrino(String nombrePadrino) {
        this.nombrePadrino = nombrePadrino;
    }

    public String getNombreMadrina() {
        return nombreMadrina;
    }

    public void setNombreMadrina(String nombreMadrina) {
        this.nombreMadrina = nombreMadrina;
    }
}