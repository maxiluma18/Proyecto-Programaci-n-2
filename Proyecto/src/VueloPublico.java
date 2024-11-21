import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.swing.text.html.HTMLDocument.Iterator;

public abstract class VueloPublico extends Vuelo {
    protected static int codigoPasajeIncremental = 1;
    protected Map<Integer, Pasaje> asientos;
    protected int totalAsientos;
    protected int[] cantAsientos;
    protected Map<Integer, List<Integer>> pasajeros;
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
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.5; // Ajuste de precio
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) == null) { // disponible
            Pasaje nuevoPasaje = new Pasaje(dni, nroAsiento, determinarClase(nroAsiento), aOcupar, codVuelo, codPasaje);
            asientos.put(nroAsiento, nuevoPasaje); // Asignamos el pasaje al asiento
            pasajeros.computeIfAbsent(dni, k -> new ArrayList<>()).add(nroAsiento);
            return true;
        }
        return false;
    }

    public boolean tienePasaje(int dni) {
        return pasajeros.containsKey(dni);
    }

    public void cancelarPasaje(int dni, int nroAsiento) {
        List<Integer> asientosOcupados = pasajeros.get(dni);
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null) {
            asientos.put(nroAsiento, null);
            asientosOcupados.remove(Integer.valueOf(nroAsiento));
            if (asientosOcupados.isEmpty()) {
                pasajeros.remove(dni);
            } else {
                pasajeros.put(dni, asientosOcupados);
            }
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

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
                return entry.getKey(); // Retorna el primer asiento disponible en la clase especificada
            }
        }
        return -1; // Si no se encuentra un asiento disponible
    }

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
        return asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null; // Retorna true si el asiento está
                                                                                     // ocupado
    }

    public void validacionOrigenDestinoNacional(String origen, String destino) {
        if (origen == null || origen.isEmpty() || destino == null || destino.isEmpty()) {
            throw new RuntimeException("El origen y destino no pueden ser nulos o vacíos");
        }
    }

    // GETTERS
    public Map<Integer, List<Integer>> getPasajeros() {
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

    public String getCodigo() {
        return codigo;
    }

    protected String determinarClase(int nroAsiento) {
        // Verifica si el número de asiento es válido
        if (nroAsiento <= 0 || nroAsiento > totalAsientos) {
            throw new IllegalArgumentException("Número de asiento inválido.");
        }
        // Determina la clase del asiento basado en el número de asiento
        if (nroAsiento <= cantAsientos[0]) { // Asientos en clase Turista
            return "Turista";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1])) { // Asientos en clase Ejecutiva
            return "Ejecutivo";
        } else {
            throw new IllegalArgumentException("Número de asiento fuera de rango.");
        }
    }

    protected double getClaseSeccion(String clase) {
        double cantRefrigerio = valorRefrigerio;
        if (clase.equals("Turista")) {
            double cant = precios[0] + cantRefrigerio;
            return cant * 1.2;
        } else {
            double cant = precios[1] + cantRefrigerio;
            return cant * 1.2;
        }
    }

}
