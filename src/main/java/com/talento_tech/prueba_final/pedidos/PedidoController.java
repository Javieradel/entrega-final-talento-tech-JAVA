package com.talento_tech.prueba_final.pedidos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO createdPedido = pedidoService.createPedido(pedidoDTO);
        return ResponseEntity.ok(createdPedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        List<PedidoDTO> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> updatePedidoStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        PedidoDTO updatedPedido = pedidoService.updatePedidoStatus(id, status);
        return ResponseEntity.ok(updatedPedido);
    }
}
