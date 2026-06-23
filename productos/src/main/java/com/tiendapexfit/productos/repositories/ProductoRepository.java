package com.tiendapexfit.productos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
/*import org.springframework.stereotype.Repository;*/
import com.tiendapexfit.productos.entities.Producto;

/*@Repository*/
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //Query Method para buscar suplementos filtrados por marca
    List<Producto> findByMarca(String marca);

    //Query Method para buscar suplementos que pertenezcan a una categoría especifica
    List<Producto> findByCategoriaId(Long categoriaId);
    

}
