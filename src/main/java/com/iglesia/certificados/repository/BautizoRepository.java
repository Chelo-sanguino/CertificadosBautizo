package com.iglesia.certificados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iglesia.certificados.model.Bautizo;

public interface BautizoRepository extends JpaRepository<Bautizo, Long> {
    // JpaRepository 
    // .save()    (para guardar)
    // .findById() (para buscar por ID)
    // .findAll()  (para obtener todos)
    // .delete()  (para borrar)
}
