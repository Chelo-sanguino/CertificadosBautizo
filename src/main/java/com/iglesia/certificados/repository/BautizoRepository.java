package com.iglesia.certificados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iglesia.certificados.model.Bautizo;
import java.util.List;

public interface BautizoRepository extends JpaRepository<Bautizo, Long> {

    // Buscador por nombre (Para la barra de búsqueda)
    List<Bautizo> findByNombreBautizadoContainingIgnoreCaseOrApellidoBautizadoContainingIgnoreCase(String nombre, String apellido);

    // NUEVA VALIDACIÓN: Verificar si existe el CI
    boolean existsByCi(String ci);
}