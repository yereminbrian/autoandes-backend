package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
public class Servicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 7)
    private String codigo;
    @Column(nullable = false, length = 80)
    private String nombre;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin;
    @Column(nullable = false)
    private Boolean activo = true;
    public Servicio() {}
    public void actualizar(String codigo, String nombre, BigDecimal precio, Integer duracionMin) {
        this.codigo = codigo; this.nombre = nombre; this.precio = precio; this.duracionMin = duracionMin;
    }
    public void cambiarEstado(Boolean activo) { this.activo = activo; }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public Integer getDuracionMin() { return duracionMin; }
    public Boolean getActivo() { return activo; }
}
