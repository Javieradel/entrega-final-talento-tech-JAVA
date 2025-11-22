package com.talento_tech.prueba_final.pedidos;

import java.util.List;

public interface PedidoService {
    PedidoDTO createPedido(PedidoDTO pedidoDTO);
    PedidoDTO updatePedidoStatus(Long pedidoId, StatusPedido status);
    PedidoDTO findById(Long id);
    List<PedidoDTO> findAll();
}
