package com.tiendapexfit.productos.services; // OFICIAL: En su subcarpeta legal

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tiendapexfit.productos.dtos.ProductoDTO;
import com.tiendapexfit.productos.entities.Producto;
import com.tiendapexfit.productos.exceptions.ResourceNotFoundException;
import com.tiendapexfit.productos.mappers.ProductoMapper;
import com.tiendapexfit.productos.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoMock;
    private ProductoDTO productoDtoMock;

    @BeforeEach
    void setUp() {
        // Inicialización de objetos simulados para el ambiente de pruebas
        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Creatina Pura");
        productoMock.setPrecio(25000.0);

        productoDtoMock = new ProductoDTO();
        productoDtoMock.setId(1L);
        productoDtoMock.setNombre("Creatina Pura");
        productoDtoMock.setPrecio(25000.0);
    }

    @Test
    @DisplayName("Debería retornar un ProductoDTO cuando el ID existe en la base de datos")
    void obtenerPorIdExitoso() {
        // Arrange (Configurar simulación Mockito)
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoMapper.toDTO(productoMock)).thenReturn(productoDtoMock);

        // Act (Ejecución del método lógico del servicio)
        Optional<ProductoDTO> resultado = productoService.obtenerPorId(1L);

        // Assert (Verificaciones del resultado)
        assertTrue(resultado.isPresent(), "El resultado no debería ser vacío");
        assertEquals("Creatina Pura", resultado.get().getNombre());
        assertEquals(25000.0, resultado.get().getPrecio());

        // Verificación de llamadas
        verify(productoRepository, times(1)).findById(1L);
        verify(productoMapper, times(1)).toDTO(productoMock);
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException cuando el ID no existe para actualizar")
    void actualizarFallidoPorIdInexistente() {
        // Arrange: Simular que la base de datos no encuentra el ID (Optional.empty)
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert: Comprobar que gatille tu excepción personalizada
        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.actualizar(99L, productoDtoMock);
        });

        // Asegurar que la persistencia quedó intacta tras el fallo
        verify(productoRepository, never()).save(any(Producto.class));
    }
}