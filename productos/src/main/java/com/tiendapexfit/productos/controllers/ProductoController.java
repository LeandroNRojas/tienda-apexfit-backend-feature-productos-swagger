package com.tiendapexfit.productos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendapexfit.productos.dtos.ProductoDTO;
/*import com.tiendapexfit.productos.entities.Producto;*/
import com.tiendapexfit.productos.exceptions.ResourceNotFoundException;
import com.tiendapexfit.productos.services.ProductoService;

/*import io.micrometer.core.ipc.http.HttpSender.Response;*/
import io.swagger.v3.oas.annotations.tags.Tag;
/*import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.Valid;*/

@RestController
@RequestMapping("/api/productos")
@Tag(name="Productos", description = "Endpoinst para el mantenimiento de inventario")
public class ProductoController {


    private final ProductoService productoService;

    //Inyeccion servicio por constructor
    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    //1. GET - Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodo();
        return ResponseEntity.ok(productos);
    }

    //2. GET - Obtener productos especifico por ID o lanzar 404
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id){
        ProductoDTO productoDto = productoService.obtenerPorId(id)
        .orElseThrow(() -> new ResourceNotFoundException("No se encontró el suplemento con el ID: " + id));
        return ResponseEntity.ok(productoDto);
    }

    //3. POST - Crear nuevo producto recibiendo y devolviendo un DTO
    //@Valid activa las reglas como @NotBlank, @Positive, etc. usadas en la Entidad
    @PostMapping
    public ResponseEntity<ProductoDTO> guardarProducto(@jakarta.validation.Valid @RequestBody ProductoDTO productoDto) {
        ProductoDTO nuevoProducto = productoService.guardar(productoDto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    //4. DELETE - Eliminar producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        //Se usa el método del Service, si no existe, excepción 404
        if (productoService.obtenerPorId(id).isEmpty()){
            throw new ResourceNotFoundException("No se puede eliminar el suplemento con ID " + id+ "no existe");
        }
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //5. GET - Filtro personalizado por Marca
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> filtrarPorMarca(@RequestParam String marca) {
        List<ProductoDTO> productos = productoService.buscarPorMarca(marca);
        return ResponseEntity.ok(productos);
    }

    //6. PUT - Actualizar producto existente por ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @jakarta.validation.Valid @RequestBody ProductoDTO productoDto) {
        ProductoDTO productoActualizado = productoService.actualizar(id, productoDto);
        return ResponseEntity.ok(productoActualizado);
    }

}
