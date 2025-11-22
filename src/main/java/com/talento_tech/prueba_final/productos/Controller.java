package com.talento_tech.prueba_final.productos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/v1/api/products")
public class Controller {

    private final ProductoService productoService;

    public Controller(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ProductoDTO createProducto(@RequestBody ProductoDTO productoDTO) {
        return productoService.save(productoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO existingProducto = productoService.findById(id);
        if (existingProducto != null) {
            productoDTO.setId(id);
            return ResponseEntity.ok(productoService.save(productoDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<ProductoDTO> searchProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax
    ) {
        ProductoSearchCriteria criteria = new ProductoSearchCriteria();
        criteria.setNombre(nombre);
        criteria.setDescripcion(descripcion);
        criteria.setPrecioMin(precioMin);
        criteria.setPrecioMax(precioMax);
        return productoService.search(criteria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        ProductoDTO existingProducto = productoService.findById(id);
        if (existingProducto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
