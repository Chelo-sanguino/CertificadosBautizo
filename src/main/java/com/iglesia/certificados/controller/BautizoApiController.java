package com.iglesia.certificados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.iglesia.certificados.model.Bautizo;
import com.iglesia.certificados.repository.BautizoRepository;
import com.iglesia.certificados.service.PdfService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bautizos")
public class BautizoApiController {

    @Autowired
    private BautizoRepository bautizoRepository;

    @Autowired
    private PdfService pdfService;

    // --- R - READ (Con Buscador Mejorado: Nombre, Apellido o CI) ---
    @GetMapping
    public List<Bautizo> obtenerBautizos(@RequestParam(required = false) String buscar) {
        if (buscar != null && !buscar.isEmpty()) {
            // Buscamos el mismo texto en los 3 campos posibles
            return bautizoRepository.findByNombreBautizadoContainingIgnoreCaseOrApellidoBautizadoContainingIgnoreCaseOrCiContainingIgnoreCase(
                buscar, buscar, buscar
            );
        }
        return bautizoRepository.findAll();
    }

    // --- C - CREATE (Con Validación de CI) ---
    @PostMapping
    public ResponseEntity<?> crearBautizo(@RequestBody Bautizo bautizo) {
        
        // 1. Validar CI obligatorio
        if (bautizo.getCi() == null || bautizo.getCi().trim().isEmpty()) {
             return ResponseEntity.badRequest().body("⚠️ Error: El CI / Documento es obligatorio.");
        }
        bautizo.setCi(bautizo.getCi().trim()); // Limpiar espacios

        // 2. Verificación de duplicados por CI
        if (bautizoRepository.existsByCi(bautizo.getCi())) {
            return ResponseEntity.badRequest().body("⚠️ Error: Ya existe una persona registrada con el CI: " + bautizo.getCi());
        }

        // 3. Limpieza de nombres
        if (bautizo.getNombreBautizado() != null) bautizo.setNombreBautizado(bautizo.getNombreBautizado().trim());
        if (bautizo.getApellidoBautizado() != null) bautizo.setApellidoBautizado(bautizo.getApellidoBautizado().trim());

        Bautizo nuevoBautizo = bautizoRepository.save(bautizo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoBautizo);
    }

    // --- D - DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBautizo(@PathVariable Long id) {
        if (bautizoRepository.existsById(id)) {
            bautizoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- R - READ ONE ---
    @GetMapping("/{id}")
    public ResponseEntity<Bautizo> obtenerUno(@PathVariable Long id) {
        return bautizoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // --- U - UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<Bautizo> actualizarBautizo(@PathVariable Long id, @RequestBody Bautizo datos) {
        return bautizoRepository.findById(id).map(existente -> {
            // Actualizar CI solo si cambió y verificar que no choque con otro
            if (!existente.getCi().equals(datos.getCi()) && bautizoRepository.existsByCi(datos.getCi())) {
                throw new RuntimeException("El nuevo CI ya existe");
            }
            existente.setCi(datos.getCi());
            
            existente.setNombreBautizado(datos.getNombreBautizado());
            existente.setApellidoBautizado(datos.getApellidoBautizado());
            existente.setNombrePadre(datos.getNombrePadre());
            existente.setNombreMadre(datos.getNombreMadre());
            existente.setNombrePadrino(datos.getNombrePadrino());
            existente.setNombreMadrina(datos.getNombreMadrina());
            existente.setFechaBautizo(datos.getFechaBautizo());
            existente.setObservaciones(datos.getObservaciones());
            existente.setLibro(datos.getLibro());
            existente.setFolio(datos.getFolio());
            
            return ResponseEntity.ok(bautizoRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- PDF ---
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        Optional<Bautizo> bautizoOptional = bautizoRepository.findById(id);
        if (bautizoOptional.isPresent()) {
            Bautizo bautizo = bautizoOptional.get();
            byte[] pdfBytes = pdfService.generarCertificadoBautizo(bautizo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Certificado_" + bautizo.getCi() + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}