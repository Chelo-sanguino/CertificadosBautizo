package com.iglesia.certificados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iglesia.certificados.model.Bautizo;
import com.iglesia.certificados.repository.BautizoRepository;
import com.iglesia.certificados.service.PdfService; // Asegúrate de tener tu PdfService

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class BautizoController {

    @Autowired
    private BautizoRepository bautizoRepository;

    @Autowired
    private PdfService pdfService;

    // MOSTRAR FORMULARIO
    @GetMapping("/bautizo/nuevo")
    public String mostrarFormularioDeBautizo(Model model) {
        model.addAttribute("bautizo", new Bautizo());
        return "form-bautizo"; 
    }

    // MOSTRAR FORMULARIO DE EDICIÓN
    @GetMapping("/bautizo/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Optional<Bautizo> bautizo = bautizoRepository.findById(id);
        if (bautizo.isPresent()) {
            model.addAttribute("bautizo", bautizo.get());
            return "form-bautizo";
        } else {
            return "redirect:/bautizos";
        }
    }

    // GUARDAR (CON VALIDACIÓN DE DUPLICADOS)
    @PostMapping("/bautizo/guardar")
    public String guardarBautizo(@ModelAttribute Bautizo bautizo, RedirectAttributes redirectAttributes) {

        // 1. LIMPIEZA OBLIGATORIA: Quitamos espacios al inicio y final
        // Si el usuario escribe "  Adrian  ", lo convertimos en "Adrian" para siempre.
        if (bautizo.getNombreBautizado() != null) {
            bautizo.setNombreBautizado(bautizo.getNombreBautizado().trim());
        }
        if (bautizo.getApellidoBautizado() != null) {
            bautizo.setApellidoBautizado(bautizo.getApellidoBautizado().trim());
        }

        // VALIDACIÓN DE DUPLICADOS
        // Solo verificamos si es un registro nuevo (ID nulo)
        if (bautizo.getId() == null) {
            
            // Imprimimos en la consola para ver qué está pasando (DEBUG)
            System.out.println("--- VERIFICANDO DUPLICADO ---");
            System.out.println("Buscando nombre: [" + bautizo.getNombreBautizado() + "]");
            System.out.println("Buscando apellido: [" + bautizo.getApellidoBautizado() + "]");

            boolean existe = bautizoRepository.existsByNombreBautizadoIgnoreCaseAndApellidoBautizadoIgnoreCase(
                bautizo.getNombreBautizado(), 
                bautizo.getApellidoBautizado()
            );

            System.out.println("¿Existe en BD?: " + existe);

            if (existe) {
                redirectAttributes.addFlashAttribute("error", "⚠️ ¡Error! Ya existe una persona registrada con el nombre '" + bautizo.getNombreBautizado() + " " + bautizo.getApellidoBautizado() + "'.");
                // Devolvemos los datos al formulario
                redirectAttributes.addFlashAttribute("bautizo", bautizo); 
                return "redirect:/bautizo/nuevo";
            }
        }

        bautizoRepository.save(bautizo);
        redirectAttributes.addFlashAttribute("exito", "✅ Registro guardado correctamente.");
        return "redirect:/bautizos";
    }

    // LISTAR (CON BUSCADOR)
    @GetMapping("/bautizos")
    public String mostrarListaDeBautizos(Model model, @RequestParam(value = "palabraClave", required = false) String palabraClave) {
        List<Bautizo> listaBautizos;
        
        // Buscador
        if (palabraClave != null && !palabraClave.isEmpty()) {
            listaBautizos = bautizoRepository.findByNombreBautizadoContainingIgnoreCaseOrApellidoBautizadoContainingIgnoreCase(palabraClave, palabraClave);
        } else {
            listaBautizos = bautizoRepository.findAll();
        }

        model.addAttribute("bautizos", listaBautizos);
        model.addAttribute("palabraClave", palabraClave);
        return "lista-bautizos";
    }
    
    // DESCARGAR PDF
    @GetMapping("/bautizo/{id}/pdf")
    public void descargarPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Bautizo> bautizoOptional = bautizoRepository.findById(id);
        
        if (bautizoOptional.isPresent()) {
            Bautizo bautizo = bautizoOptional.get();
            // para enviar el PDF
            byte[] pdfBytes = pdfService.generarCertificadoBautizo(bautizo);
            
            response.setContentType("application/pdf");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=certificado_" + bautizo.getId() + ".pdf";
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(pdfBytes);
        }
    }
}