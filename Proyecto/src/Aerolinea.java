import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.RuntimeErrorException;

public class Aerolinea implements IAerolinea {
    private String nombre;
    private String cuit;
    private Map<String, Aeropuerto> aeropuertos;
    private Map<String, Vuelo> Vuelos;
    private Map<Integer, Cliente> clientes;
    private Map<String, Double> recaudacionPorDestino;

    // Funcion 1
    public Aerolinea(String nombre, String cuit) {
        validacionNombreCuit(nombre, cuit);
        this.nombre = nombre;
        this.cuit = cuit;
        this.aeropuertos = new HashMap<>();
        this.clientes = new HashMap<>();
        this.Vuelos = new HashMap<>();
        this.recaudacionPorDestino = new HashMap<>();
    }

    // Funcion 2
    public void registrarCliente(int dni, String nombre, String telefono) {
        if (clientes.containsKey(dni)) {
            throw new RuntimeErrorException(null, "Cliente ya existente");
        }
        Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
        clientes.put(dni, nuevoCliente);
    }

    // Funcion 3
    public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {

        if (aeropuertos.containsKey(nombre)) {
            throw new RuntimeErrorException(null, "Aeropuerto ya existente");
        }
        Aeropuerto nuevoAeropuerto = new Aeropuerto(nombre, pais, provincia, direccion);
        aeropuertos.put(nombre, nuevoAeropuerto);
    }

    // Funcion 4
    public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        Aeropuerto aeropuertoOrigen = aeropuertos.get(origen);
        Aeropuerto aeropuertoDestino = aeropuertos.get(destino);
        if (aeropuertoOrigen.esIgual(aeropuertoDestino)) {
            throw new RuntimeErrorException(null, "Origen y destino no pueden ser iguales");
        }
        if (!aeropuertoOrigen.esNacional() || !aeropuertoDestino.esNacional()) {
            throw new RuntimeException("Los aeropuertos deben ser nacionales");
        }
        if (!fechaValida(fecha)) {
            throw new RuntimeErrorException(null, "Fecha invalida");
        }
        VueloNacional nuevoVuelo = new VueloNacional(origen, destino, fecha, tripulantes, valorRefrigerio, precios,
                cantAsientos);
        recaudacionPorDestino.put(destino,
                recaudacionPorDestino.getOrDefault(destino, 0.0) + nuevoVuelo.getRecaudacionTotal());
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    // Funcion 5
    public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        validacionEscalas(escalas);
        if (!fechaValida(fecha)) {
            throw new RuntimeErrorException(null, "Fecha invalida");
        }
        Aeropuerto aeropuertoOrigen = aeropuertos.get(origen);
        Aeropuerto aeropuertoDestino = aeropuertos.get(destino);
        if (aeropuertoOrigen.esIgual(aeropuertoDestino)) {
            throw new RuntimeErrorException(null, "Origen y destino no pueden ser iguales");
        }
        VueloInternacional nuevoVuelo = new VueloInternacional(origen, destino, fecha, tripulantes, valorRefrigerio,
                cantRefrigerios, precios, cantAsientos, escalas);
        recaudacionPorDestino.put(destino,
                recaudacionPorDestino.getOrDefault(destino, 0.0) + nuevoVuelo.getRecaudacionTotal());
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    // Funcion 6 y 10
    public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
            int dniComprador, int[] acompaniantes) {
        validacionAcompaniantes(acompaniantes);
        validacionDniComprador(dniComprador);
        if (!fechaValida(fecha)) {
            throw new RuntimeErrorException(null, "Fecha invalida");
        }
        Aeropuerto aeropuertoOrigen = aeropuertos.get(origen);
        Aeropuerto aeropuertoDestino = aeropuertos.get(destino);
        if (aeropuertoOrigen.esIgual(aeropuertoDestino)) {
            throw new RuntimeErrorException(null, "Origen y destino no pueden ser iguales");
        }
        if (!aeropuertoOrigen.esNacional() || !aeropuertoDestino.esNacional()) {
            throw new RuntimeException("Los aeropuertos deben ser nacionales");
        }
        VueloPrivado nuevoVuelo = new VueloPrivado(origen, destino, fecha, tripulantes, precio, dniComprador,
                acompaniantes);
        recaudacionPorDestino.put(destino,
                recaudacionPorDestino.getOrDefault(destino, 0.0) + nuevoVuelo.getRecaudacionTotal());
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    // Funcion 7
    public Map<Integer, String> asientosDisponibles(String codVuelo) {
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        return vuelo.getAsientosDisponibles();
    }

    public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
        if (clientes.get(dni) == null) {
            throw new RuntimeException("El cliente no está registrado");
        }
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        int resultado = vuelo.venderPasaje(dni, nroAsiento, aOcupar, codVuelo);
        if (resultado <= 0) {
            throw new RuntimeException("Hubo un error en la designación del asiento");
        }
        double precioPasaje = vuelo.getClaseSeccion(vuelo.determinarClase(nroAsiento));
        recaudacionPorDestino.put(vuelo.getDestino(),
                recaudacionPorDestino.getOrDefault(vuelo.getDestino(), 0.0) + precioPasaje);
        return resultado;
    }

    // Funcion 11
    public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {
        List<String> resultado = new ArrayList<>();
        for (Vuelo vuelo : Vuelos.values()) {
            if (vuelo.esSimilar(origen, destino, Fecha)) {
                resultado.add(vuelo.getCodigo());
            }
        }
        return resultado;
    }

    // Funcion 12-A Complejidad O(1)
    public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
        Cliente cl = clientes.get(dni);
        if (cl == null) {
            throw new RuntimeException("El cliente no está registrado");
        }
        Vuelo v = Vuelos.get(codVuelo);
        if (v == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        v.cancelarPasaje(dni, nroAsiento);
        String clase = v.determinarClase(nroAsiento);
        double precioPasaje = v.getClaseSeccion(clase);
        recaudacionPorDestino.put(v.getDestino(),
                recaudacionPorDestino.getOrDefault(v.getDestino(), 0.0) - precioPasaje);
    }

    // Funcion 12-B NO Complejidad O(1)
    public void cancelarPasaje(int dni, int codPasaje) {
        Cliente cl = clientes.get(dni);
        if (cl == null) {
            throw new RuntimeException("El cliente no está registrado");
        }
        for (Vuelo vuelo : Vuelos.values()) {
            Pasaje pasaje = vuelo.getPasajePorCodigo(codPasaje);
            if (pasaje != null && pasaje.getDni() == dni) {
                int nroAsiento = pasaje.getNroAsiento();
                vuelo.cancelarPasaje(dni, nroAsiento);
                String clase = vuelo.determinarClase(nroAsiento);
                double precioPasaje = vuelo.getClaseSeccion(clase);
                recaudacionPorDestino.put(vuelo.getDestino(),
                        recaudacionPorDestino.get(vuelo.getDestino()) - precioPasaje);
                return;
            }
        }
        throw new RuntimeException("No se encontró el pasaje con el código proporcionado");
    }

    // Funcion 13
    public List<String> cancelarVuelo(String codVuelo) {
        List<String> listaPasajerosReprogramados = new ArrayList<>();
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        if (!(vuelo instanceof VueloPublico)) {
            throw new RuntimeException("No se puede cancelar un vuelo privado");
        }
        VueloPublico vueloPublico = (VueloPublico) vuelo;
        Map<Integer, Pasaje> pasajerosVuelo = new HashMap<>(vueloPublico.getAsientos());
        if (pasajerosVuelo.size() > 0) {
            Iterator<Vuelo> iterator = Vuelos.values().iterator();
            while (iterator.hasNext()) {
                Vuelo v = iterator.next();
                if (v != vueloPublico) {

                    listaPasajerosReprogramados.addAll(
                            vueloPublico.reprogramarPasajeros(v, pasajerosVuelo, clientes, recaudacionPorDestino));
                }
            }
            recaudacionPorDestino.put(vueloPublico.getDestino(),
                    recaudacionPorDestino.get(vueloPublico.getDestino())
                            - vueloPublico.getRecaudacionTotal());
            Vuelos.remove(codVuelo);
        } else {
            throw new RuntimeException("El vuelo no tiene pasajes vendidos");
        }
        return listaPasajerosReprogramados;
    }

    // Funcion 14
    public double totalRecaudado(String destino) {
        if (recaudacionPorDestino.get(destino) == null) {
            return 0.0;
        }
        double valor = recaudacionPorDestino.get(destino);
        return valor;
    }

    // Funcion 15
    public String detalleDeVuelo(String codVuelo) {
        if (codVuelo == null || codVuelo.isEmpty()) {
            throw new RuntimeErrorException(null, "codVuelo no puede ser nulo o vacio");
        }
        Vuelo v = Vuelos.get(codVuelo);
        if (v == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        return crearSBVuelo(codVuelo, v.getOrigen(), v.getDestino(), v.getFecha(), v.getTipoVuelo());
    }

    // *Funciones Auxiliares---------------------

    public String crearSBVuelo(String codVuelo, String origen, String destino, String fecha, String tipoDeVuelo) {
        StringBuilder sb = new StringBuilder();
        sb.append(codVuelo).append(" - ").append(origen).append(" - ").append(destino).append(" - ").append(fecha)
                .append(" - ").append(tipoDeVuelo);
        return sb.toString();
    }

    public boolean fechaValida(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/uuuu");
        LocalDate fechaVuelo = LocalDate.parse(fecha, formatter);
        LocalDate fechaHoy = LocalDate.now();
        return fechaVuelo.isAfter(fechaHoy);
    }

    // * IREPS------------------

    public void validacionEscalas(String[] escalas) {
        if (escalas.length > 0) {
            for (int i = 0; i < escalas.length; i++) {
                boolean escalasFalsa = true;
                if (escalas[i].length() < 3 && !aeropuertos.containsKey(escalas[i])) {
                    escalasFalsa = false;
                }
                if (!escalasFalsa) {
                    throw new RuntimeErrorException(null, "Las escalas son invalidas");
                }
            }
        }
    }

    public void validacionCantRefrigerios(int cantRefrigerios) {
        if (cantRefrigerios != 2) {
            throw new RuntimeException("La cantidad de refrigerios deben ser 2");
        }
    }

    public void validacionAcompaniantes(int[] acompaniantes) {
        for (int i = 0; i < acompaniantes.length; i++) {
            if (!clientes.containsKey(acompaniantes[i])) {
                throw new RuntimeException("El acompaniante no existe");
            }
        }
    }

    public void validacionDniComprador(int dniComprador) {
        if (!clientes.containsKey(dniComprador)) {
            throw new RuntimeException("El cliente no esta registrado");
        }
    }

    public void validacionNombreCuit(String nombre, String cuit) {
        if (nombre.isEmpty() || cuit.isEmpty() || nombre.length() < 3 || cuit.length() < 10) {
            throw new RuntimeException("El nombre y el cuit no pueden estar vacios");
        }
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Aerolinea: ").append(nombre).append("\n");
        sb.append("Cuit: ").append(cuit).append("\n");
        sb.append("Clientes: ").append(clientes.size()).append("\n");
        sb.append("Vuelos: ").append(Vuelos.size()).append("\n");
        return sb.toString();
    }
}