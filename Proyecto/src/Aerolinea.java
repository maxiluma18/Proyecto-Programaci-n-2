import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class Aerolinea implements IAerolinea {
    private String nombre;
    private String cuit;
    private Map<String, Aeropuerto> aeropuertos;
    private Map<String, Vuelo> Vuelos;
    private Map<Integer, Cliente> clientes;

    public Aerolinea(String nombre, String cuit) {
        this.nombre = nombre;
        this.cuit = cuit;
        this.aeropuertos = new HashMap<>();
        this.clientes = new HashMap<>();
        this.Vuelos = new HashMap<>();
    }

    public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {
        // si ya existe ese aeropuerto en aerpuertos no se hace
        if (aeropuertos.containsKey(nombre)) {
            throw new RuntimeErrorException(null, "Aeropuerto ya existente");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
        }
        if (pais == null || pais.isEmpty()) {
            throw new RuntimeErrorException(null, "el pais no puede ser nulo o vacio");
        }
        if (provincia == null || provincia.isEmpty()) {
            throw new RuntimeErrorException(null, "La provincia no puede ser nulo o vacio");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new RuntimeErrorException(null, "La direccion no puede ser nulo o vacio");
        }
        Aeropuerto nuevoAeropuerto = new Aeropuerto(nombre, pais, provincia, direccion);
        aeropuertos.put(nombre, nuevoAeropuerto);
    }

    public void registrarCliente(int dni, String nombre, String telefono) {
        if (clientes.containsKey(dni)) {
            throw new RuntimeErrorException(null, "Cliente ya existente");
        }
        if (dni <= 0) {
            throw new IllegalArgumentException("El Dni debe ser positivo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El dato nombre debe ser valido");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new IllegalArgumentException("El dato telefono debe ser valido");
        }
        Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
        clientes.put(dni, nuevoCliente);
    }

    public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {

        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        if (!aeropuertos.get(origen).getPais().equals("Argentina") ||
                !aeropuertos.get(destino).getPais().equals("Argentina")) {
            throw new RuntimeException("Los aeropuertos deben ser nacionales");
        }
        if (precios.length != 2 || cantAsientos.length != 2) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
        // IREP DE LO DEMAS

        VueloNacional nuevoVuelo = new VueloNacional(origen, destino, fecha, tripulantes, valorRefrigerio, precios,
                cantAsientos);

        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        if (precios.length != 3 || cantAsientos.length != 3) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
        // IREP DE LO DEMAS
        // agregar mejor irep que este
        if (escalas.length > 0) {
            for (int i = 0; i < escalas.length; i++) {
                boolean escalasFalsa = true;
                if (escalas[i].length() < 3) {
                    escalasFalsa = false;
                }
                if (!escalasFalsa) {
                    throw new RuntimeErrorException(null, "Las escalas son invalidas");
                }
            }
        }
        VueloInternacional nuevoVuelo = new VueloInternacional(origen, destino, fecha, tripulantes, valorRefrigerio,
                cantRefrigerios, precios, cantAsientos, escalas);
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);

        return codigoVuelo;
    }

    public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
            int dniComprador, int[] acompaniantes) {
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        if (!aeropuertos.get(origen).getPais().equals("Argentina") ||
                !aeropuertos.get(destino).getPais().equals("Argentina")) {
            throw new RuntimeException("Los aeropuertos deben ser nacionales");
        }
        if (acompaniantes.length < 0) {
            throw new RuntimeErrorException(null, "Error en los datos");
        }
        if (!clientes.containsKey(dniComprador)) {
            throw new RuntimeException("El cliente no esta registrado");
        }
        // IREP DE LO DEMAS

        VueloPrivado nuevoVuelo = new VueloPrivado(origen, destino, fecha, tripulantes, precio, dniComprador,
                acompaniantes);
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    public Map<Integer, String> asientosDisponibles(String codVuelo) {
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        if (vuelo instanceof VueloInternacional) {
            VueloInternacional vueloInternacional = (VueloInternacional) vuelo;
            System.out.println(vueloInternacional.getAsientosDisponibles());
            return vueloInternacional.getAsientosDisponibles();
        } else if (vuelo instanceof VueloNacional) {
            VueloNacional vueloNacional = (VueloNacional) vuelo;
            return vueloNacional.getAsientosDisponibles();
        } else {
            throw new RuntimeException("El vuelo no tiene acceso a los asientos");
        }
    }

    public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
        if (clientes.get(dni) == null) {
            throw new RuntimeException("El cliente no está registrado");
        }
        if (clientes.get(dni).esPasajero() == true) {
            System.out.println(clientes.get(dni).esPasajero());
            if (aOcupar == true) {
                throw new RuntimeException("El cliente ya tiene asiento designado");
            }
        }
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        int resultado;
        if (vuelo instanceof VueloInternacional) {
            VueloInternacional vueloInternacional = (VueloInternacional) vuelo;
            resultado = vueloInternacional.venderPasaje(dni, nroAsiento, aOcupar, codVuelo);
            if (resultado <= 0) {
                throw new RuntimeException("Hubo un error en la designación del asiento");
            }
            if (clientes.get(dni).esPasajero() == false && aOcupar == true) {
                clientes.get(dni).cambiarEstado(true);
            } else if (aOcupar == false && clientes.get(dni).esPasajero() == false) {
                clientes.get(dni).cambiarEstado(false);
            } else if (aOcupar == false && clientes.get(dni).esPasajero() == true) {
                clientes.get(dni).cambiarEstado(false);
            } else {
                throw new RuntimeException("Hubo un error en la designación del asiento");
            }

        } else if (vuelo instanceof VueloNacional) {
            VueloNacional vueloNacional = (VueloNacional) vuelo;
            resultado = vueloNacional.venderPasaje(dni, nroAsiento, aOcupar, codVuelo);
            if (resultado <= 0) {
                throw new RuntimeException("Hubo un error en la designación del asiento");
            }
            if (clientes.get(dni).esPasajero() == false && aOcupar == true) {
                clientes.get(dni).cambiarEstado(true);
            } else if (aOcupar == false && clientes.get(dni).esPasajero() == false) {
                clientes.get(dni).cambiarEstado(false);
            } else if (aOcupar == false && clientes.get(dni).esPasajero() == true) {
                clientes.get(dni).cambiarEstado(false);
            } else {
                throw new RuntimeException("Hubo un error en la designación del asiento");
            }

        } else {
            throw new RuntimeException("El vuelo no forma parte de las clases definidas");
        }

        return resultado;
    }

    public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {
        List<String> resultado = new ArrayList<>();
        for (Vuelo vuelo : Vuelos.values()) {
            if (vuelo instanceof VueloInternacional) {
                VueloInternacional vueloInternacional = (VueloInternacional) vuelo;

                if (vueloInternacional.getOrigen().equals(origen) &&
                        vueloInternacional.getDestino().equals(destino) && vueloInternacional.fechaValida(Fecha)) {
                    resultado.add(vueloInternacional.toString());
                }
            } else if (vuelo instanceof VueloNacional) {
                VueloNacional vueloNacional = (VueloNacional) vuelo;
                if (vueloNacional.getOrigen().equals(origen) && vueloNacional.getDestino().equals(destino)
                        && vueloNacional.fechaValida(Fecha)) {
                    System.out.println(vueloNacional.toString());
                    resultado.add(vueloNacional.toString());
                }
            }

        }
        return resultado;
    }

    public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
        // * Se borra el pasaje y se libera el lugar para que pueda comprarlo otro
        // * cliente.
        // * IMPORTANTE: Se debe resolver en O(1).
        Cliente cl = clientes.get(dni);
        if (cl == null) {
            throw new RuntimeException("El cliente no está registrado");
        }
        Vuelo v = Vuelos.get(codVuelo);
        if (v == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        if (v instanceof VueloInternacional) {
            VueloInternacional vueloInternacional = (VueloInternacional) v;
            if (vueloInternacional.tienePasaje(dni, nroAsiento)) {
                vueloInternacional.cancelarPasaje(dni, nroAsiento);
                if (cl.esPasajero()) {
                    cl.cambiarEstado(false);
                }
            } else {
                throw new RuntimeException("El pasaje no existe");
            }
        } else if (v instanceof VueloNacional) {
            VueloNacional vueloNacional = (VueloNacional) v;
            if (vueloNacional.tienePasaje(dni, nroAsiento)) {
                vueloNacional.cancelarPasaje(dni, nroAsiento);
                if (cl.esPasajero()) {
                    cl.cambiarEstado(false);
                }
            } else {
                throw new RuntimeException("El pasaje no existe");
            }
        } else {
            throw new RuntimeException("El pasaje no existe o no se tiene acceso");
        }
    }
}
