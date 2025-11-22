package com.talento_tech.prueba_final.pedidos;

import com.talento_tech.prueba_final.productos.Producto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PedidoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;
}
