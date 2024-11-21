
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        validacionPreciosCantAsientosNacional(precios, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;

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
            pasajeros.computeIfAbsent(dni, k -> new ArrayList<>()).add(nroAsiento);
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.2; // Ajuste de precio
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    @Override
    protected String determinarClase(int nroAsiento) {
        // Determina la clase del asiento basado en el número de asiento
        if (nroAsiento <= cantAsientos[0]) { // Asientos en clase Turista
            return "Turista";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1])) { // Asientos en clase Ejecutiva
            return "Ejecutivo";
        } else {
            throw new IllegalArgumentException("Número de asiento fuera de rango o clase no válida.");
        }
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        List<Integer> asientosOcupados = pasajeros.get(dni);
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) != null) {
            asientos.put(nroAsiento, null);
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

    public void validacionRefrigerio(double refrigerio) {
        if (refrigerio <= 0) {
            throw new RuntimeException("El valor del refrigerio debe ser positivo");
        }
    }

    public String getTipoVuelo() {
        return "NACIONAL";
    }

    public void validacionPreciosCantAsientosNacional(double[] precios, int[] cantAsientos) {
        if (precios.length != 2 || cantAsientos.length != 2) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
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
