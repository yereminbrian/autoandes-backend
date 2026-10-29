package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_servicio")
public class OrdenServicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 9)
    private String numero;
    @Column(nullable = false)
    private LocalDateTime fecha;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;
    @Column(nullable = false)
    private Integer kilometraje;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoOrden estado;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "usuario_registro", nullable = false, length = 30)
    private String usuarioRegistro;
    @Column(name = "motivo_anulacion", length = 200)
    private String motivoAnulacion;
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();
    protected OrdenServicio() {}
    public OrdenServicio(String numero, LocalDateTime fecha, Vehiculo vehiculo, Integer kilometraje,
                         BigDecimal total, String usuarioRegistro) {
        this.numero = numero; this.fecha = fecha; this.vehiculo = vehiculo; this.kilometraje = kilometraje;
        this.total = total; this.usuarioRegistro = usuarioRegistro; this.estado = EstadoOrden.REGISTRADA;
    }
    public void agregarDetalle(DetalleOrden detalle) { detalles.add(detalle); detalle.asignarOrden(this); }
    public void anular(String motivo) { estado = EstadoOrden.ANULADA; motivoAnulacion = motivo; }
    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public LocalDateTime getFecha() { return fecha; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public Integer getKilometraje() { return kilometraje; }
    public EstadoOrden getEstado() { return estado; }
    public BigDecimal getTotal() { return total; }
    public String getUsuarioRegistro() { return usuarioRegistro; }
    public String getMotivoAnulacion() { return motivoAnulacion; }
    public List<DetalleOrden> getDetalles() { return detalles; }
}
