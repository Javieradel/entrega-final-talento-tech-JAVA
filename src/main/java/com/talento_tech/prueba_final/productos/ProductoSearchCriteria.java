package com.talento_tech.prueba_final.productos;

import lombok.Data;

@Data
public class ProductoSearchCriteria {
    private String nombre;
    private String descripcion;
    private Double precioMin;
    private Double precioMax;
}
