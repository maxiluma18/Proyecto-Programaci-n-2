
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class VueloInternacional extends VueloPublico {
    private double valorRefrigerio;
    private int cantRefrigerios;
    private String[] escalas;

    public VueloInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos,
            String[] escalas) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        validacionPreciosCantAsientosInternacional(precios, cantAsientos);
        validacionCantRefrigeriosInternacional(cantRefrigerios);
        this.valorRefrigerio = valorRefrigerio;
        this.cantRefrigerios = cantRefrigerios;
        this.escalas = escalas.length > 0 ? escalas : null;
        this.precios = precios;
        this.cantAsientos = cantAsientos;

        this.totalAsientos = cantAsientos[0] + cantAsientos[1] + cantAsientos[2];
        inicializarAsientos(cantAsientos);
    }

    @Override
    protected void inicializarAsientos(int[] cantAsientos) {
        for (int i = 1; i <= totalAsientos; i++) {
            asientos.put(i, null);
        }
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
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.2; // Ajuste de precio
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        System.out.println("ERROR 4");
        return 0;
    }

    @Override
    protected String determinarClase(int nroAsiento) {
        // Determina la clase del asiento basado en el número de asiento
        if (nroAsiento <= cantAsientos[0]) { // Asientos en clase Turista
            return "Turista";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1])) { // Asientos en clase Ejecutiva
            return "Ejecutivo";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1] + cantAsientos[2])) { // Asientos en Primera Clase
            return "Primera Clase";
        } else {
            throw new IllegalArgumentException("Número de asiento fuera de rango o clase no válida.");
        }
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

    @Override
    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) == null) {
            Pasaje nuevoPasaje = new Pasaje(dni, nroAsiento, determinarClase(nroAsiento), aOcupar, codVuelo, codPasaje);
            asientos.put(nroAsiento, nuevoPasaje);
            pasajeros.computeIfAbsent(dni, k -> new ArrayList<>()).add(nroAsiento);
            return true;
        }
        return false;
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        List<Integer> asientosOcupados = pasajeros.get(dni);
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null) {
            asientos.put(nroAsiento, null);
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.2; // Ajuste de precio
            actualizarRecaudacion(-precioPasaje);
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
        } else if (clase.equals("Primera Clase")) {
            clasesDisponibles.add("Primera Clase");
        } else {
            throw new RuntimeErrorException(null, "Clase Inexistente");
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

    // GETTERS
    public String[] getEscalas() {
        return this.escalas;
    }

    public String getTipoVuelo() {
        return "INTERNACIONAL";
    }

    @Override
    protected double getClaseSeccion(String clase) {
        double cantRefrigerio = cantRefrigerios * valorRefrigerio;
        if (clase.equals("Turista")) {
            double cant = precios[0] + cantRefrigerio;
            return cant * 1.2;
        } else if (clase.equals("Ejecutivo")) {
            double cant = precios[1] + cantRefrigerio;
            return cant * 1.2;
        } else if (clase.equals("Primera Clase")) {
            double cant = precios[2] + cantRefrigerio;
            return cant * 1.2;
        } else {
            throw new IllegalArgumentException("Clase inexistente");
        }
    }

    @Override
    protected double calcularPrecioPasaje(String clase) {
        double precioPasaje = 0;
        if (clase.equals("Turista")) {
            precioPasaje = precios[0];
        } else if (clase.equals("Ejecutivo")) {
            precioPasaje = precios[1];
        } else if (clase.equals("Primera Clase")) {
            precioPasaje = precios[2];
        }
        return precioPasaje * 1.2;
    }

    public void validacionPreciosCantAsientosInternacional(double[] precios, int[] cantAsientos) {
        if (precios.length != 3 || cantAsientos.length != 3) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 3");
        }
    }

    public void validacionCantRefrigeriosInternacional(int cantRefrigerios) {
        if (cantRefrigerios != 3) {
            throw new RuntimeException("La cantidad de refrigerios deben ser 3");
        }
    }

}
