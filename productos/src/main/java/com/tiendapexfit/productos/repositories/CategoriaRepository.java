package com.tiendapexfit.productos.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/*import org.springframework.stereotype.Repository;*/
import com.tiendapexfit.productos.entities.Categoria;

/*@Repository*/
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
    //Este es un Query Method para buscar una categoría por su nombre exacto
    Optional<Categoria> findByNombre(String nombre);
}
