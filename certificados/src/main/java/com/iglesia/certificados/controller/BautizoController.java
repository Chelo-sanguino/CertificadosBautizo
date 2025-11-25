package com.iglesia.certificados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // Importante para leer el ID de la URL
import org.springframework.web.bind.annotation.PostMapping;

import com.iglesia.certificados.model.Bautizo;
import com.iglesia.certificados.repository.BautizoRepository;
import com.iglesia.certificados.service.PdfService; // Importamos tu nuevo servicio

import jakarta.servlet.http.HttpServletResponse; // Para enviar la respuesta como archivo
import java.io.IOException;
import java.util.Optional;

@Controller
public class BautizoController {

    @Autowired
    private BautizoRepository bautizoRepository;

    // --- NUEVA INYECCIÓN: El servicio de PDF ---
    @Autowired
    private PdfService pdfService;

    // MOSTRAR FORMULARIO
    @GetMapping("/bautizo/nuevo")
    public String mostrarFormularioDeBautizo(Model model) {
        model.addAttribute("bautizo", new Bautizo());
        return "form-bautizo"; 
    }

    // MOSTRAR FORMULARIO DE EDICIÓN (Nuevo extra útil)
    @GetMapping("/bautizo/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Optional<Bautizo> bautizo = bautizoRepository.findById(id);
        if (bautizo.isPresent()) {
            model.addAttribute("bautizo", bautizo.get());
            return "form-bautizo"; // Reutilizamos el mismo formulario
        } else {
            return "redirect:/bautizos";
        }
    }

    // GUARDAR DATOS
    @PostMapping("/bautizo/guardar")
    public String guardarBautizo(@ModelAttribute Bautizo bautizo) {
        bautizoRepository.save(bautizo);
        return "redirect:/bautizos"; // Al guardar, volvemos a la lista
    }

    // LISTAR TODOS
    @GetMapping("/bautizos")
    public String mostrarListaDeBautizos(Model model) {
        model.addAttribute("bautizos", bautizoRepository.findAll());
        return "lista-bautizos";
    }
    
    // --- NUEVO MÉTODO: DESCARGAR PDF ---
    @GetMapping("/bautizo/{id}/pdf")
    public void descargarPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        // 1. Buscamos el bautizo por ID
        Optional<Bautizo> bautizoOptional = bautizoRepository.findById(id);
        
        if (bautizoOptional.isPresent()) {
            Bautizo bautizo = bautizoOptional.get();

            // 2. Configuramos la respuesta para que el navegador sepa que es un PDF
            response.setContentType("application/pdf");
            
            // Esta línea hace que el navegador te pregunte "¿Dónde guardar?"
            // El nombre del archivo será: certificado_1.pdf, certificado_2.pdf, etc.
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=certificado_" + bautizo.getId() + ".pdf";
            response.setHeader(headerKey, headerValue);

            // 3. Llamamos al servicio para que "dibuje" el PDF en la descarga
            pdfService.generarCertificadoPdf(response, bautizo);
        }
    }
}