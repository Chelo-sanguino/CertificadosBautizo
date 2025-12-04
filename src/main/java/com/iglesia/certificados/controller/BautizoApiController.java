package com.iglesia.certificados.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import java.util.Optional;

import com.iglesia.certificados.model.Bautizo;
import com.iglesia.certificados.repository.BautizoRepository;
import com.iglesia.certificados.service.PdfService;



// Esto le dice a Spring que esta clase devuelve DATOS (JSON), no HTML.
@RestController
// 2. RUTA BASE:
// Todas las URLs de esta clase empezarán con "/api"
@RequestMapping("/api/bautizos")
public class BautizoApiController {

    @Autowired
    private BautizoRepository bautizoRepository;

    @Autowired
    private PdfService pdfService;

        // --- NUEVO: Descargar PDF ---
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        Optional<Bautizo> bautizoOptional = bautizoRepository.findById(id);
        
        if (bautizoOptional.isPresent()) {
            Bautizo bautizo = bautizoOptional.get();
            byte[] pdfBytes = pdfService.generarCertificadoBautizo(bautizo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "Certificado_Bautizo_" + bautizo.getNombreBautizado() + "_" + bautizo.getApellidoBautizado() + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- C - CREATE (Crear) ---
    // Escucha en: POST /api/bautizos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve un código "201 Created"
    public Bautizo crearBautizo(@RequestBody Bautizo bautizo) {
        // @RequestBody toma el JSON que envía el cliente y lo convierte en un objeto Bautizo.
         
        return bautizoRepository.save(bautizo); 
    }

    // --- R - READ (Leer Todos) ---
    // Escucha en: GET /api/bautizos
    @GetMapping
    public List<Bautizo> obtenerTodosLosBautizos() {
        // Simplemente devuelve la lista. Spring la convierte a JSON.
        return bautizoRepository.findAll();
    }

    // --- R - READ (Leer Uno por ID) ---
    // Escucha en: GET /api/bautizos/1 (o el ID que sea)
    @GetMapping("/{id}")
    public ResponseEntity<Bautizo> obtenerBautizoPorId(@PathVariable Long id) {
        // ResponseEntity nos permite devolver un 404 "Not Found" si no lo encuentra
        Optional<Bautizo> bautizo = bautizoRepository.findById(id);
        
        if (bautizo.isPresent()) {
            return ResponseEntity.ok(bautizo.get()); // Devuelve 200 OK + el objeto
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    // --- U - UPDATE (Actualizar) ---
    // Escucha en: PUT /api/bautizos/1 (o el ID que sea)
    @PutMapping("/{id}")
    public ResponseEntity<Bautizo> actualizarBautizo(@PathVariable Long id, @RequestBody Bautizo datosNuevos) {
        
        Optional<Bautizo> bautizoOptional = bautizoRepository.findById(id);
        
        if (bautizoOptional.isPresent()) {
            Bautizo bautizoExistente = bautizoOptional.get();
            
            // Actualizamos los campos del objeto existente
            bautizoExistente.setNombreBautizado(datosNuevos.getNombreBautizado());
            bautizoExistente.setApellidoBautizado(datosNuevos.getApellidoBautizado());
            bautizoExistente.setNombrePadre(datosNuevos.getNombrePadre());
            bautizoExistente.setNombreMadre(datosNuevos.getNombreMadre());
            bautizoExistente.setNombrePadrino(datosNuevos.getNombrePadrino());
            bautizoExistente.setNombreMadrina(datosNuevos.getNombreMadrina());
            
            // Guardamos el objeto actualizado
            Bautizo bautizoActualizado = bautizoRepository.save(bautizoExistente);
            return ResponseEntity.ok(bautizoActualizado); // Devuelve 200 OK + el objeto
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    // --- D - DELETE (Eliminar) ---
    // Escucha en: DELETE /api/bautizos/1 (o el ID que sea)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBautizo(@PathVariable Long id) {
        if (bautizoRepository.existsById(id)) {
            bautizoRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content (Éxito)
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }
}
