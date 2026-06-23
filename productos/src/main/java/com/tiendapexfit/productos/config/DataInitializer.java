package com.tiendapexfit.productos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tiendapexfit.productos.entities.Categoria;
import com.tiendapexfit.productos.entities.Producto;
import com.tiendapexfit.productos.repositories.CategoriaRepository;
import com.tiendapexfit.productos.repositories.ProductoRepository;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository catRepo, ProductoRepository prodRepo){
        return args -> {
            //1. Primero verifica si ya existen datos para no duplicarlos en cada reinicio
            if(catRepo.count() == 0){
                logger.info("Base de datos vacía. Iniciando poblamiento automático");

                //2. Crear categorías para Tienda ApexFit
                Categoria proteinas = catRepo.save(new Categoria(null, "Proteínas", "Suplementos de proteína de suero, aislada y vegana para recuperación muscular.", null));
                Categoria creatinas = catRepo.save(new Categoria(null, "Creatinas", "Creatina monohidratada para fuerza, potencia y ATP muscular.", null));
                Categoria preEntrenos = catRepo.save(new Categoria(null, "Pre-Entrenos", "Fórmulas de energía, óxido nítrico y enfoque para el gimnasio.", null));
                
                logger.info("Categorías base registradas con éxito.");

                //3. Crear Productos enlazados a sus respectivas categorías
                prodRepo.save(new Producto(null, "100% Whey Gold Standard 5lbs", "Proteína de suero de leche de alta calidad.", 64990.0, "Optimum Nutrition", proteinas));
                prodRepo.save(new Producto(null, "ISO100 Hydrolyzed 3lbs", "Proteína aislada e hidrolizada cero carbohidratos.", 54990.0, "Dymatize", proteinas));
                
                prodRepo.save(new Producto(null, "Creatine Monohydrate 300g", "Creatina micronizada pura para rendimiento.", 22990.0, "Optimum Nutrition", creatinas));
                prodRepo.save(new Producto(null, "C4 Original 30 Servings", "Pre-Entreno clásico para energía explosiva.", 29990.0, "Cellucor", preEntrenos));
                prodRepo.save(new Producto(null, "Psychotic 30 Servings", "Pre-Entreno de alta estimulación y enfoque.", 31990.0, "Insane Labz", preEntrenos));

                logger.info("Productos base registrados con éxito para prueba.");
            } else {
                logger.info("La base de datos ya cuenta con registros de productos.");
            }
        };
    }

}
