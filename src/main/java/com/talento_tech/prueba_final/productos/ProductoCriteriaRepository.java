package com.talento_tech.prueba_final.productos;

import java.util.List;

public interface ProductoCriteriaRepository {
    List<Producto> findAll(ProductoSearchCriteria searchCriteria);
}
