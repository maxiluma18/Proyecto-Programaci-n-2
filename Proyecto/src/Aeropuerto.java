import javax.management.RuntimeErrorException;

public class Aeropuerto {
    private String nombre;
    private String pais;
    private String provincia;
    private String direccion;

    public Aeropuerto(String nombre, String pais, String provincia, String direccion) {
        if (validacion(nombre, pais, provincia, direccion)) {
            this.nombre = nombre;
            this.pais = pais;
            this.provincia = provincia;
            this.direccion = direccion;
        } else {
            throw new RuntimeErrorException(null, "Los datos ingresados no son validos");
        }
    }

    // agregar validacion e irep con las 4 variables
    public boolean validacion(String nombre, String pais, String provincia, String direccion) {
        if (nombre.isEmpty() || pais.isEmpty() || provincia.isEmpty() || direccion.isEmpty()) {
            return false;
        }
        return true;
    }
}
