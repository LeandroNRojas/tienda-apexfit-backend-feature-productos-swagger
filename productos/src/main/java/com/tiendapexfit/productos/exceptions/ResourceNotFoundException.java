package com.tiendapexfit.productos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Con esta anotación se le indica a Spring que si esta excepción escapa, devuela un HTTP 404.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    //Const receptor del mensaje personalizado
    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }

}
