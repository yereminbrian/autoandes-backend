package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String nombre;
    protected Rol() {}
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}
