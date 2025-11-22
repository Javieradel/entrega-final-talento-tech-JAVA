package com.talento_tech.prueba_final.productos;

import java.util.List;

public interface ProductoService {
    ProductoDTO save(ProductoDTO productoDTO);
    List<ProductoDTO> findAll();
    ProductoDTO findById(Long id);
    void deleteById(Long id);
    List<ProductoDTO> search(ProductoSearchCriteria searchCriteria);
}
