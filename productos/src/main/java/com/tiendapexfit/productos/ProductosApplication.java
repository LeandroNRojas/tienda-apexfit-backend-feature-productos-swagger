package com.tiendapexfit.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API de Productos - Tienda ApexFit", version = "1.0", description = "Microservicios para la gestión de los productos y suplementos"))
public class ProductosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosApplication.class, args);
    }

}
