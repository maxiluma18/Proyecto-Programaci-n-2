import javax.management.RuntimeErrorException;

public class Aeropuerto {
    private String nombre;
    private String pais;
    private String provincia;
    private String direccion;

    public Aeropuerto(String nombre, String pais, String provincia, String direccion) {
        validacionNombre(nombre);
        validacionPais(pais);
        validacionProvincia(provincia);
        validacionDireccion(direccion);
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
        this.direccion = direccion;
    }

    public void validacionNombre(String nombre) {
        if (nombre == null || nombre.isEmpty() || nombre.length() <= 2) {
            throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
        }
    }

    public void validacionPais(String pais) {
        if (pais == null || pais.isEmpty() || pais.length() <= 2) {
            throw new RuntimeErrorException(null, "el pais no puede ser nulo o vacio");
        }
    }

    public void validacionProvincia(String provincia) {
        if (provincia == null || provincia.isEmpty() || provincia.length() <= 2) {
            throw new RuntimeErrorException(null, "La provincia no puede ser nulo o vacio");
        }
    }

    public void validacionDireccion(String direccion) {
        if (direccion == null || direccion.isEmpty() || direccion.length() <= 2) {
            throw new RuntimeErrorException(null, "La direccion no puede ser nulo o vacio");
        }
    }

    public boolean esIgual(Aeropuerto otro) {
        return this.nombre.equals(otro.getNombre()) && this.pais.equals(otro.getPais());
    }

    public boolean esNacional() {
        return this.pais.equals("Argentina");
    }

    // GETTERS
    public String getPais() {
        return pais;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getDireccion() {
        return direccion;
    }
}
