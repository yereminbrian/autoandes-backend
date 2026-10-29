package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 11)
    private String documento;
    @Column(nullable = false, length = 120)
    private String nombres;
    @Column(length = 15)
    private String telefono;
    protected Cliente() {}
    public Long getId() { return id; }
    public String getDocumento() { return documento; }
    public String getNombres() { return nombres; }
    public String getTelefono() { return telefono; }
}
