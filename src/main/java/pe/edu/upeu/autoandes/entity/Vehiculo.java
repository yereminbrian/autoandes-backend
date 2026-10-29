package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 7)
    private String placa;
    @Column(nullable = false, length = 40)
    private String marca;
    @Column(nullable = false, length = 40)
    private String modelo;
    @Column(nullable = false)
    private Integer anio;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    public Vehiculo() {}
    public void actualizar(String placa, String marca, String modelo, Integer anio, Cliente cliente) {
        this.placa = placa; this.marca = marca; this.modelo = modelo; this.anio = anio; this.cliente = cliente;
    }
    public Long getId() { return id; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAnio() { return anio; }
    public Cliente getCliente() { return cliente; }
}
