package com.talento_tech.prueba_final.productos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoCriteriaRepository productoCriteriaRepository;


    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoCriteriaRepository productoCriteriaRepository) {
        this.productoRepository = productoRepository;
        this.productoCriteriaRepository = productoCriteriaRepository;
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        if (productoDTO.getId() == null) {
            Stock stock = new Stock();
            stock.setCantidad(productoDTO.getStock() != null ? productoDTO.getStock() : 0);
            producto.setStock(stock);
            stock.setProducto(producto);
        } else {
            Producto existing = productoRepository.findById(productoDTO.getId()).orElseThrow(() -> new RuntimeException("Producto not found"));
            producto.setStock(existing.getStock());
        }
        producto = productoRepository.save(producto);
        return toDTO(producto);
    }

    @Override
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductoDTO findById(Long id) {
        return productoRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> search(ProductoSearchCriteria searchCriteria) {
        return productoCriteriaRepository.findAll(searchCriteria).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void updateStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto not found: " + productoId));
        Stock stock = producto.getStock();
        if (stock == null) {
            stock = new Stock();
            stock.setProducto(producto);
            producto.setStock(stock);
        }
        stock.setCantidad(cantidad);
        productoRepository.save(producto);
    }

    private ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        if (producto.getStock() != null) {
            dto.setStock(producto.getStock().getCantidad());
        } else {
            dto.setStock(0);
        }
        return dto;
    }

    private Producto toEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        return producto;
    }
}
