package com.talento_tech.prueba_final.pedidos;

import com.talento_tech.prueba_final.productos.Producto;
import com.talento_tech.prueba_final.productos.ProductoRepository;
import com.talento_tech.prueba_final.productos.Stock;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoProductoRepository pedidoProductoRepository;
    private final ProductoRepository productoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoProductoRepository pedidoProductoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoProductoRepository = pedidoProductoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    @Override
    public PedidoDTO createPedido(PedidoDTO pedidoDTO) {
        // 1. Validate stock
        for (PedidoProductoDTO ppDTO : pedidoDTO.getProductos()) {
            Producto producto = productoRepository.findById(ppDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto not found: " + ppDTO.getProductoId()));

            if (producto.getStock() == null || producto.getStock().getCantidad() < ppDTO.getCantidad()) {
                throw new RuntimeException("Insufficient stock for product: " + producto.getNombre());
            }
        }

        // 2. Create Pedido entity
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PROCESANDO);
        pedido.setProductos(new ArrayList<>());
        pedido = pedidoRepository.save(pedido);

        // 3. Decrease stock and create PedidoProducto entities
        for (PedidoProductoDTO ppDTO : pedidoDTO.getProductos()) {
            Producto producto = productoRepository.findById(ppDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto not found: " + ppDTO.getProductoId())); // Should not happen after initial check

            // Decrease stock
            Stock stock = producto.getStock();
            stock.setCantidad(stock.getCantidad() - ppDTO.getCantidad());
            productoRepository.save(producto); // Save product, which cascades to stock

            // Create PedidoProducto
            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(ppDTO.getCantidad());
            pedido.getProductos().add(pedidoProducto);
        }

        return toDTO(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoDTO updatePedidoStatus(Long pedidoId, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido not found: " + pedidoId));
        pedido.setStatus(status);
        return toDTO(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoDTO findById(Long id) {
        return pedidoRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pedido not found: " + id));
    }

    @Override
    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setStatus(pedido.getStatus());
        dto.setProductos(pedido.getProductos().stream()
                .map(pp -> {
                    PedidoProductoDTO ppDTO = new PedidoProductoDTO();
                    ppDTO.setProductoId(pp.getProducto().getId());
                    ppDTO.setCantidad(pp.getCantidad());
                    return ppDTO;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
