package pe.edu.upeu.autoandes.security;

import java.util.*;

public final class PermisosPorRol {
    public static final String SERVICIO_LEER = "SERVICIO_LEER";
    public static final String SERVICIO_ESCRIBIR = "SERVICIO_ESCRIBIR";
    public static final String VEHICULO_LEER = "VEHICULO_LEER";
    public static final String VEHICULO_ESCRIBIR = "VEHICULO_ESCRIBIR";
    public static final String ORDEN_LEER = "ORDEN_LEER";
    public static final String ORDEN_REGISTRAR = "ORDEN_REGISTRAR";
    public static final String ORDEN_ANULAR = "ORDEN_ANULAR";
    public static final String REPORTE_VER = "REPORTE_VER";

    private static final Set<String> TODOS = Set.of(SERVICIO_LEER, SERVICIO_ESCRIBIR,
            VEHICULO_LEER, VEHICULO_ESCRIBIR, ORDEN_LEER, ORDEN_REGISTRAR, ORDEN_ANULAR, REPORTE_VER);

    private static final Map<String, Set<String>> MAPA = Map.of(
        "ADMIN", TODOS,
        "ASESOR", Set.of(SERVICIO_LEER, VEHICULO_LEER, VEHICULO_ESCRIBIR, ORDEN_LEER, ORDEN_REGISTRAR),
        "JEFE_TALLER", Set.of(SERVICIO_LEER, SERVICIO_ESCRIBIR, VEHICULO_LEER, ORDEN_LEER, ORDEN_ANULAR, REPORTE_VER)
    );

    private PermisosPorRol() {}
    public static Set<String> deRoles(Collection<String> roles) {
        Set<String> permisos = new LinkedHashSet<>();
        for (String rol : roles) permisos.addAll(MAPA.getOrDefault(rol, Set.of()));
        return Collections.unmodifiableSet(permisos);
    }
    public static List<String> rolesDe(pe.edu.upeu.autoandes.entity.Usuario usuario) {
        return usuario.getRoles().stream().map(r -> r.getNombre()).sorted().toList();
    }
    public static List<String> permisosDe(pe.edu.upeu.autoandes.entity.Usuario usuario) {
        return deRoles(rolesDe(usuario)).stream().sorted().toList();
    }
}
