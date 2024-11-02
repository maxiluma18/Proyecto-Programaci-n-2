import javax.management.RuntimeErrorException;

public class Cliente {
    private int dni;
    private String nombre;
    private String telefono;

    public Cliente(int dni, String nombre, String telefono) {
        if (validacion(dni, nombre, telefono)) {
            this.dni = dni;
            this.nombre = nombre;
            this.telefono = telefono;
        } else {
            throw new RuntimeErrorException(null, "Error en la validacion");
        }
    }

    // agregar validacion e irep con las 4 variables
    public boolean validacion(int dni, String nombre, String telefono) {
        if (dni <= 0 || nombre.isEmpty() || telefono.isEmpty()) {
            return false;
        }
        return true;
    }
}