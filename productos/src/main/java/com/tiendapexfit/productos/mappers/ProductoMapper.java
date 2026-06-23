package com.tiendapexfit.productos.mappers;

import com.tiendapexfit.productos.repositories.CategoriaRepository;
import org.springframework.stereotype.Component;

import com.tiendapexfit.productos.dtos.ProductoDTO;
import com.tiendapexfit.productos.entities.Categoria;
import com.tiendapexfit.productos.entities.Producto;

@Component
public class ProductoMapper {

    private final CategoriaRepository categoriaRepository;

    ProductoMapper(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Aqui se convierte la Entidad(JPA) a DTO (la salida hacia el cliente)
    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setMarca(producto.getMarca());

        //Extraccion de datos
        if (producto.getCategoria() !=null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }

        return dto;

    }

    //Aqui se convierte DTO a Entidad
    public Producto toEntity(ProductoDTO dto){
        if(dto == null){
            return null;
        }

        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());

        //Si el DTO trae un id de categoría, se crea un "cascarón" aqui mismo
        //Evitando que JPA indique que la relación va en null.categoriaRepository
        if (dto.getCategoriaId() != null){
            Categoria categoria = new Categoria();
            categoria.setId(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }

        //La categoria se terminara de asociar en Service usando
        //categoriaId que esta en el DTO.

        return producto;
    }
}
