package com.tiendapexfit.productos.services;

import com.tiendapexfit.productos.dtos.ProductoDTO; // Import de DTO agregado
import com.tiendapexfit.productos.entities.Producto;
import com.tiendapexfit.productos.mappers.ProductoMapper;
import com.tiendapexfit.productos.repositories.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Import para transformar las listas con Streams

@Service
public class ProductoService {

    //Instanciar Logger para trazabilidad exigida
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
    
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper; //Inyeccion de Mapper

    //Inyeccion de dependencia por const.
    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper){
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    //1. Obtener todos los productos
    public List<ProductoDTO> obtenerTodo(){
        logger.info("Solicitando listado completo de suplementos");
        // Buscamos las entidades en la BD y las transformamos a DTOs una por una
        return productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    //2. Obtener producto por ID
    public Optional<ProductoDTO> obtenerPorId(Long id){
        logger.info("Buscando suplemento con ID: {}", id);
        // Si lo encuentra en la BD, lo mapea automáticamente a DTO
        return productoRepository.findById(id)
                .map(productoMapper::toDTO);
    }

    //3. Crear un nuevo producto
    public ProductoDTO guardar(ProductoDTO productoDto){
        try{
            logger.info("Registrando nuevo producto: {} de la marca {}", productoDto.getNombre(), productoDto.getMarca());
            
            // Convertimos el DTO que viene del controlador a Entidad para JPA
            Producto producto = productoMapper.toEntity(productoDto);
            
            // Ojo: Si necesitas asociar la categoría real para que no vaya vacía, 
            // se puede hacer mediante el ID que viaja en el DTO en el siguiente paso.
            
            Producto productoGuardado = productoRepository.save(producto);
            logger.info("Producto registrado con exito con ID: {}", productoGuardado.getId());
            
            // Retornamos el DTO final al cliente
            return productoMapper.toDTO(productoGuardado);
        } catch (Exception e) {
            logger.error("Error al guardar datos del producto: {}", e.getMessage());
            throw e;
        }
    }

    //4. Eliminar producto
    public void eliminar(Long id) {
        try {
            logger.warn("Elimando el suplemento ID: {}", id);
            if(productoRepository.existsById(id)){
                productoRepository.deleteById(id);
                logger.info("Suplemento ID: {} eliminado correctamente", id);
            } else { 
                logger.error("No se pudo eliminar el suplemento con ID: {} no existe", id);
            }
        } catch (Exception e) {
            logger.error("Error al intentar eliminar el producto: {}", e.getMessage());
            throw e;
        }
    }

    //5. Buscar por marca
    public List<ProductoDTO> buscarPorMarca(String marca){
        logger.info("Buscando suplementos bajo la marca: {}", marca);
        // Filtramos las entidades por marca y las transformamos a DTOs
        return productoRepository.findByMarca(marca).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    //6. Actualizar producto existente
    public ProductoDTO actualizar(Long id, ProductoDTO dto){
        try{
            logger.info("Actualizando suplemento ID: {}", id);

            //Verificar si el producto existe en la BD
            Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new com.tiendapexfit.productos.exceptions.ResourceNotFoundException("No se puede actualizar: El suplemento con ID " + id + " no existe"));
            
            //Modificar datos de entidad existente con lo que viene del DTO
            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setMarca(dto.getMarca());

            //Manejo de el recipiente de la categoría en caso de cambios
            if (dto.getCategoriaId() != null){
                com.tiendapexfit.productos.entities.Categoria categoria = new com.tiendapexfit.productos.entities.Categoria();
                categoria.setId(dto.getCategoriaId());
                productoExistente.setCategoria(categoria);
            }

            //Guardar cambios en BD
            Producto productoActualizado = productoRepository.save(productoExistente);
            logger.info("Producto con ID: {} actualizado con éxito", productoActualizado.getId());

            //Retornar DTO actualizado a Cliente
            return productoMapper.toDTO(productoActualizado);

        } catch (com.tiendapexfit.productos.exceptions.ResourceNotFoundException e) {
            logger.error("Error al actualizar: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al intentar procesar actualización del producto: {}", e.getMessage());
            throw e;
        }
    }

}
