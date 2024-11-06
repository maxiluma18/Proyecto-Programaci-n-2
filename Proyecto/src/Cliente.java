import javax.management.RuntimeErrorException;

public class Cliente {
    private int dni;
    private String nombre;
    private String telefono;
    private Boolean esPasajero = false;

    public Cliente(int dni, String nombre, String telefono) {
        validacionDni(dni);
        validacionNombre(nombre);
        validacionTelefono(telefono);
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public boolean esPasajero() {
        return this.esPasajero;
    }

    public void cambiarEstado(boolean pasajero) {
        this.esPasajero = pasajero;
    }

    public void validacionDni(int dni) {
        if (dni <= 0) {
            throw new IllegalArgumentException("El Dni debe ser positivo");
        }
    }

    public void validacionTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty() || telefono.length() < 8) {
            throw new IllegalArgumentException("El dato telefono debe ser valido");
        }
    }

    public void validacionNombre(String nombre) {
        if (nombre == null || nombre.isEmpty() || nombre.length() <= 2) {
            throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
        }
    }

    // GETTERS
    public String getTelefono() {
        return telefono;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getDni() {
        return this.dni;
    }

}
