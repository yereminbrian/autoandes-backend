package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_orden", uniqueConstraints = @UniqueConstraint(name = "uq_detalle_orden_servicio", columnNames = {"orden_id", "servicio_id"}))
public class DetalleOrden {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenServicio orden;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;
    @Column(nullable = false)
    private Integer cantidad;
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    protected DetalleOrden() {}
    public DetalleOrden(Servicio servicio, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.servicio = servicio; this.cantidad = cantidad; this.precioUnitario = precioUnitario; this.subtotal = subtotal;
    }
    void asignarOrden(OrdenServicio orden) { this.orden = orden; }
    public Long getId() { return id; }
    public OrdenServicio getOrden() { return orden; }
    public Servicio getServicio() { return servicio; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
