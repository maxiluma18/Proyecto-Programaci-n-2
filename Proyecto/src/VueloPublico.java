import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

public abstract class VueloPublico extends Vuelo {
    protected static int codigoPasajeIncremental = 1;
    protected Map<Integer, Pasaje> asientos;
    protected int totalAsientos;
    protected int[] cantAsientos;
    protected Map<Integer, Set<Integer>> pasajeros;
    protected double[] precios;
    protected double valorRefrigerio;
    private double recaudacionTotal;
    private static int contadorVuelos = 0;
    private String codigo;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        validacionOrigenDestinoNacional(origen, destino);
        this.codigo = generarCodigoVuelo() + "-PUB";
        this.pasajeros = new HashMap<>();
        this.recaudacionTotal = 0.0;
        this.asientos = new HashMap<>();
        this.totalAsientos = cantAsientos[0] + cantAsientos[1];
        inicializarAsientos(cantAsientos);
        this.pasajeros = new HashMap<>();
        this.cantAsientos = cantAsientos;
    }

    protected double calcularPrecioPasaje(String clase) {
        double precioPasaje = 0;
        if (clase.equals("Turista")) {
            precioPasaje = precios[0];
        } else if (clase.equals("Ejecutivo")) {
            precioPasaje = precios[1];
        }
        return precioPasaje * 1.2;
    }

    protected void inicializarAsientos(int[] cantAsientos) {
        for (int i = 1; i <= totalAsientos; i++) {
            asientos.put(i, null);
        }
    }

    protected void actualizarRecaudacion(double monto) {
        this.recaudacionTotal += monto;

    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0 || nroAsiento > totalAsientos) {
            return 0;
        }
        if (asientos.get(nroAsiento) != null) {
            return 0;
        }
        if (pasajeros.size() >= totalAsientos) {
            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.2;
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    public List<String> reprogramarPasajeros(Vuelo nuevoVuelo, Map<Integer, Pasaje> pasajerosVuelo,
            Map<Integer, Cliente> clientes,
            Map<String, Double> recaudacionPorDestino) {
        List<String> listaPasajerosReprogramados = new ArrayList<>();
        boolean encontroVueloValido = false;

        if (nuevoVuelo.getOrigen().equals(this.getOrigen()) &&
                nuevoVuelo.getDestino().equals(this.getDestino()) &&
                nuevoVuelo.fechaValida(this.getFecha())) {

            encontroVueloValido = true;

            for (Pasaje p : pasajerosVuelo.values()) {
                String telefonoCliente = clientes.get(p.getDni()).getTelefono();
                String nombreCliente = clientes.get(p.getDni()).getNombre();
                String codVueloNuevo = nuevoVuelo.getCodigo();
                int nroAsiento = p.getNroAsiento();

                if (nuevoVuelo.asignarAsiento(p.getDni(), nroAsiento, p.getClase(), p.getOcupado()) > 0) {
                    String clase = nuevoVuelo.determinarClase(nroAsiento);
                    double precioPasaje = nuevoVuelo.getClaseSeccion(clase);
                    recaudacionPorDestino.put(nuevoVuelo.getDestino(),
                            recaudacionPorDestino.getOrDefault(nuevoVuelo.getDestino(), 0.0) + precioPasaje);
                    agregarPasajero(listaPasajerosReprogramados,
                            p.getDni(),
                            nombreCliente,
                            telefonoCliente,
                            codVueloNuevo);
                } else {
                    agregarPasajero(listaPasajerosReprogramados,
                            p.getDni(),
                            nombreCliente,
                            telefonoCliente,
                            "CANCELADO");
                }
            }
        }

        if (!encontroVueloValido) {
            for (Pasaje p : pasajerosVuelo.values()) {
                String telefonoCliente = clientes.get(p.getDni()).getTelefono();
                String nombreCliente = clientes.get(p.getDni()).getNombre();
                agregarPasajero(listaPasajerosReprogramados,
                        p.getDni(),
                        nombreCliente,
                        telefonoCliente,
                        "CANCELADO");
            }
        }

        return listaPasajerosReprogramados;
    }

    @Override
    public boolean esSimilar(String origen, String destino, String fecha) {
        return this.getOrigen().equals(origen) &&
                this.getDestino().equals(destino) &&
                this.fechaValida(fecha);
    }

    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) == null) {
            Pasaje nuevoPasaje = new Pasaje(dni, nroAsiento, determinarClase(nroAsiento), aOcupar, codVuelo, codPasaje);
            asientos.put(nroAsiento, nuevoPasaje);
            pasajeros.computeIfAbsent(dni, k -> new HashSet<>()).add(nroAsiento); // Cambiado a HashSet
            return true;
        }
        return false;
    }

    public boolean tienePasaje(int dni) {
        return pasajeros.containsKey(dni);
    }

    @Override
    public Pasaje getPasajePorCodigo(int codPasaje) {
        for (Pasaje pasaje : asientos.values()) {
            if (pasaje != null && pasaje.getCodPasaje() == codPasaje) {
                return pasaje;
            }
        }
        return null;
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        Set<Integer> asientosOcupados = pasajeros.get(dni);
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null) {
            asientos.put(nroAsiento, null);
            asientosOcupados.remove(nroAsiento);

            if (asientosOcupados.isEmpty()) {
                pasajeros.remove(dni);
            }
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

    @Override
    public int asignarAsiento(int dni, int nroAsiento, String clase, boolean ocupado) {
        int codPasaje = codigoPasajeIncremental++;
        String codVuelo = this.getCodigo();

        List<String> clasesDisponibles = new ArrayList<>();
        if (clase.equals("Turista")) {
            clasesDisponibles.add("Turista");
            clasesDisponibles.add("Ejecutivo");
            clasesDisponibles.add("Primera Clase");
        } else if (clase.equals("Ejecutivo")) {
            clasesDisponibles.add("Ejecutivo");
            clasesDisponibles.add("Primera Clase");
        } else {
            throw new RuntimeErrorException(null, "Clase inexistente");
        }

        for (String claseActual : clasesDisponibles) {
            int asientoDisponible = encontrarAsientoDisponible(claseActual);
            if (asientoDisponible != -1) {
                if (ocuparAsiento(dni, asientoDisponible, codPasaje, ocupado, codVuelo)) {
                    double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
                    actualizarRecaudacion(precioPasaje);
                    return codPasaje;
                }
            }
        }

        return -1;
    }

    protected int encontrarAsientoDisponible(String clase) {
        for (Map.Entry<Integer, Pasaje> entry : asientos.entrySet()) {
            if (entry.getValue() == null && determinarClase(entry.getKey()).equals(clase)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Override
    public Map<Integer, String> getAsientosDisponibles() {
        Map<Integer, String> asientosDisponibles = new HashMap<>();
        for (Map.Entry<Integer, Pasaje> entry : asientos.entrySet()) {
            if (entry.getValue() == null) {
                asientosDisponibles.put(entry.getKey(), determinarClase(entry.getKey()));
            }
        }
        return asientosDisponibles;
    }

    protected boolean estaOcupado(int nroAsiento) {
        return asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null;
    }

    public void validacionOrigenDestinoNacional(String origen, String destino) {
        if (origen == null || origen.isEmpty() || destino == null || destino.isEmpty()) {
            throw new RuntimeException("El origen y destino no pueden ser nulos o vacíos");
        }
    }

    public void agregarPasajero(List<String> lista, int dni, String nombre,
            String telefono, String codVuelo) {
        StringBuilder sb = new StringBuilder();
        sb.append(dni).append(" - ")
                .append(nombre).append(" - ")
                .append(telefono).append(" - ")
                .append(codVuelo);
        lista.add(sb.toString());
    }

    // GETTERS
    public Map<Integer, Set<Integer>> getPasajeros() {
        return pasajeros;
    }

    public Map<Integer, Pasaje> getAsientos() {
        Map<Integer, Pasaje> asientosDisponibles = new HashMap<>();
        for (Map.Entry<Integer, Pasaje> entrada : asientos.entrySet()) {
            if (entrada.getValue() != null) {
                asientosDisponibles.put(entrada.getKey(), entrada.getValue());
            }
        }
        return asientosDisponibles;
    }

    public double getRecaudacionTotal() {
        return recaudacionTotal;

    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public String determinarClase(int nroAsiento) {
        if (nroAsiento <= 0 || nroAsiento > totalAsientos) {
            throw new IllegalArgumentException("Número de asiento inválido.");
        }

        if (nroAsiento <= cantAsientos[0]) {
            return "Turista";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1])) {
            return "Ejecutivo";
        } else {
            throw new IllegalArgumentException("Número de asiento fuera de rango.");
        }
    }

    @Override
    protected double getClaseSeccion(String clase) {
        double cantRefrigerio = valorRefrigerio;
        if (clase.equals("Turista")) {
            double cant = precios[0] + cantRefrigerio;
            return cant * 1.2;
        } else if (clase.equals("Ejecutivo")) {
            double cant = precios[1] + cantRefrigerio;
            return cant * 1.2;
        } else {
            throw new IllegalArgumentException("Clase inexistente");
        }
    }

}