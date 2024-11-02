import javax.management.RuntimeErrorException;

public class Cliente {
    private int dni;
    private String nombre;
    private String telefono;
    private Boolean esPasajero = false;

    public Cliente(int dni, String nombre, String telefono) {

        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public boolean esPasajero() {
        return this.esPasajero;
    }

    public void cambiarEstado() {
        this.esPasajero = !this.esPasajero;
    }

    public String getNombre() {
        return this.nombre;
    }
}
