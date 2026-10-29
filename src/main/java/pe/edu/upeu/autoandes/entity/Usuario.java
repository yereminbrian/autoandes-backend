package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.type.SqlTypes;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "nombrecompleto", nullable = false, length = 120)
    private String nombreCompleto;
    @Convert(converter = NumericBooleanConverter.class)
    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(nullable = false, columnDefinition = "NUMBER(1)")
    private Boolean activo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuarioid"),
            inverseJoinColumns = @JoinColumn(name = "rolid"))
    private Set<Rol> roles = new HashSet<>();
    protected Usuario() {}
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNombreCompleto() { return nombreCompleto; }
    public Boolean getActivo() { return activo; }
    public Set<Rol> getRoles() { return roles; }
    public void setPassword(String password) { this.password = password; }
}
