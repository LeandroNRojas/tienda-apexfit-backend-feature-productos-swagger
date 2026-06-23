package com.tiendapexfit.productos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío!")
    private String nombre;

    @NotBlank(message = "El producto debe contener una descripción!")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo!")
    @Positive(message = "El precio debe ser un valor mayor a cero!")
    private Double precio;

    @NotBlank(message = "El producto debe tener una marca!")
    private String marca;

    //En lugar de enviar el objeto Categoría completo
    //la respuesta enviara solo los datos necesarios.
    @NotNull(message = "El ID de la categoría es obligatorio!")
    private Long categoriaId;
    private String categoriaNombre;

}
