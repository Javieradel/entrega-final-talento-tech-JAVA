package com.talento_tech.prueba_final.pedidos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private StatusPedido status;
    private List<PedidoProductoDTO> productos;
}
