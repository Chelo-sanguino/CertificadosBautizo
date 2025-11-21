package com.iglesia.certificados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.iglesia.certificados.model.Bautizo; // Importa el modelo
import com.iglesia.certificados.repository.BautizoRepository; // Importa el repositorio

@Controller // 2. Le dice a Spring: "Esta clase maneja peticiones web"
public class BautizoController {

    // 3. Inyección de Dependencias: Le pedimos a Spring que nos "inyecte" 
    //    una instancia del repositorio que acabamos de crear.
    @Autowired
    private BautizoRepository bautizoRepository;

    // 4. MÉTODO PARA MOSTRAR EL FORMULARIO
    //    Cuando alguien visite http://localhost:8080/bautizo/nuevo
    @GetMapping("/bautizo/nuevo")
    public String mostrarFormularioDeBautizo(Model model) {
        // Prepara un objeto Bautizo vacío para rellenar en el formulario
        model.addAttribute("bautizo", new Bautizo());
        
        // 5. Devuelve el nombre de un archivo HTML (que crearemos a continuación)
        return "form-bautizo"; 
    }

    // 6. MÉTODO PARA GUARDAR LOS DATOS
    //    Cuando el formulario se envíe, lo hará a esta URL
    @PostMapping("/bautizo/guardar")
    public String guardarBautizo(@ModelAttribute Bautizo bautizo) {

        System.out.println("¡ENTRÉ AL MÉTODO GUARDAR!");
        System.out.println("Datos recibidos: " + bautizo.getNombreBautizado());
        // 7. Toma el objeto 'bautizo' que vino del formulario
        //    y usa el repositorio para guardarlo en la BD.
        bautizoRepository.saveAndFlush(bautizo);

        // 8. Redirige al usuario de vuelta al formulario (para que pueda añadir otro)
        return "redirect:/bautizo/nuevo";
    }

    // MÉTODO PARA MOSTRAR LA LISTA DE TODOS LOS BAUTIZOS
@GetMapping("/bautizos")
public String mostrarListaDeBautizos(Model model) {

    // 1. Busca TODOS los registros de Bautizo en la BD
    //    usando el repositorio.
    java.util.List<Bautizo> listaBautizos = bautizoRepository.findAll();

    System.out.println("Número de bautizos encontrados: " + listaBautizos.size());
    // 2. "Pasa" esa lista al archivo HTML para que Thymeleaf la pueda usar.
    //    La llamaremos "bautizos" en el HTML.
    model.addAttribute("bautizos", listaBautizos);
    // 3. Devuelve el nombre de un NUEVO archivo HTML que vamos a crear.
    return "lista-bautizos";
    }


}