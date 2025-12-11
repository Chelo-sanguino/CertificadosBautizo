package com.iglesia.certificados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iglesia.certificados.model.Bautizo;
import java.util.List;

public interface BautizoRepository extends JpaRepository<Bautizo, Long> {

    // BÚSQUEDA MEJORADA: Busca coincidencia en Nombre, Apellido O CI
    List<Bautizo> findByNombreBautizadoContainingIgnoreCaseOrApellidoBautizadoContainingIgnoreCaseOrCiContainingIgnoreCase(
        String nombre, String apellido, String ci
    );

    // Validación de CI único
    boolean existsByCi(String ci);
}