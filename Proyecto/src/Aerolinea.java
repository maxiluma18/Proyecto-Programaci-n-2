import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class Aerolinea {
    private String nombre;
    private String cuit;
    private Map<String, Aeropuerto> aeropuertos;

    private Map<Integer, Cliente> clientes;

    public Aerolinea(String nombre, String cuit) {
        this.nombre = nombre;
        this.cuit = cuit;
        this.aeropuertos = new HashMap<>();
        this.clientes = new HashMap<>();
    }

    public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {
        // si ya existe ese aeropuerto en aerpuertos no se hace
        if (aeropuertos.containsKey(direccion)) {
            throw new RuntimeErrorException(null, "Aeropuerto ya existente");
        }
        Aeropuerto nuevoAeropuerto = new Aeropuerto(nombre, pais, provincia, direccion);
        aeropuertos.put(direccion, nuevoAeropuerto);
    }

    public void registrarCliente(int dni, String nombre, String telefono) {
        if (clientes.containsKey(dni)) {
            throw new RuntimeErrorException(null, "Cliente ya existente");
        }
        Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
        clientes.put(dni, nuevoCliente);
    }
}
