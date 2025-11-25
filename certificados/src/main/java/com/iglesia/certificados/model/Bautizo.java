package com.iglesia.certificados.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate; 

@Entity
public class Bautizo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Campos de la persona bautizada y sus padres/padrinos ---
    private String nombreBautizado;
    private String apellidoBautizado;
    private String nombrePadre;
    private String nombreMadre;
    private String nombrePadrino;
    private String nombreMadrina;
    

    // 1. Fechas y Lugar
    private LocalDate fechaBautizo; // La fecha en que se realizó el sacramento
    private LocalDate fechaNacimiento; // La fecha de nacimiento del bautizado
    private String lugarNacimiento; // Ej: "Ciudad de La Paz, Bolivia"

    // 2. Datos de Registro Eclesiástico
    private String libro; // El número de libro donde se registra el acta
    private String folio; // La página o folio dentro del libro
    private String partida; // El número de acta/partida
    
    // 3. Ministro/Oficiante
    private String ministroOficiante; // El nombre del sacerdote, pastor u oficiante

    // --- CONSTRUCTOR VACÍO (Requerido por JPA) ---
    public Bautizo() {
    }

    // --- Getters y Setters ---

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


    public LocalDate getFechaBautizo() {
        return fechaBautizo;
    }

    public void setFechaBautizo(LocalDate fechaBautizo) {
        this.fechaBautizo = fechaBautizo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getMinistroOficiante() {
        return ministroOficiante;
    }

    public void setMinistroOficiante(String ministroOficiante) {
        this.ministroOficiante = ministroOficiante;
    }
}