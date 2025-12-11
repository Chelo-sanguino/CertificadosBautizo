package com.iglesia.certificados.model;

import jakarta.persistence.Column;
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

    // NUEVO CAMPO: Cédula de Identidad (Único)
    @Column(unique = true)
    private String ci;

    private String nombreBautizado;
    private String apellidoBautizado;
    private String nombrePadre;
    private String nombreMadre;
    private String nombrePadrino;
    private String nombreMadrina;

    private LocalDate fechaBautizo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    private String libro;
    private String folio;
    private String partida;

    // Constructor vacío
    public Bautizo() {
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }

    public String getNombreBautizado() { return nombreBautizado; }
    public void setNombreBautizado(String nombreBautizado) { this.nombreBautizado = nombreBautizado; }

    public String getApellidoBautizado() { return apellidoBautizado; }
    public void setApellidoBautizado(String apellidoBautizado) { this.apellidoBautizado = apellidoBautizado; }

    public String getNombrePadre() { return nombrePadre; }
    public void setNombrePadre(String nombrePadre) { this.nombrePadre = nombrePadre; }

    public String getNombreMadre() { return nombreMadre; }
    public void setNombreMadre(String nombreMadre) { this.nombreMadre = nombreMadre; }

    public String getNombrePadrino() { return nombrePadrino; }
    public void setNombrePadrino(String nombrePadrino) { this.nombrePadrino = nombrePadrino; }

    public String getNombreMadrina() { return nombreMadrina; }
    public void setNombreMadrina(String nombreMadrina) { this.nombreMadrina = nombreMadrina; }

    public LocalDate getFechaBautizo() { return fechaBautizo; }
    public void setFechaBautizo(LocalDate fechaBautizo) { this.fechaBautizo = fechaBautizo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getLibro() { return libro; }
    public void setLibro(String libro) { this.libro = libro; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getPartida() { return partida; }
    public void setPartida(String partida) { this.partida = partida; }
}