import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
        this.cantAsientos = new int[2][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
    }

    public Map<Integer, String> getAsientosDisponibles() {
        Map<Integer, String> asientos = new HashMap<>();
        for (int i = 0; i < cantAsientos[0].length; i++) {
            if (cantAsientos[0][i] == 0) {
                asientos.put(i + 1, "Turista");
            }
        }
        int baseEjecutivo = cantAsientos[0].length;
        for (int i = 0; i < cantAsientos[1].length; i++) {
            if (cantAsientos[1][i] == 0) {
                asientos.put(baseEjecutivo + i + 1, "Ejecutivo");
            }
        }
        return asientos;
    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0)
            return 0;

        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length;
        if (pasajerosPorDNI.size() >= totalAsientos - 1) {
            return 0;
        }

        int codPasaje = codigoPasajeIncremental++;
        String clase = determinarClase(nroAsiento);
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            double precioPasaje = calcularPrecioPasaje(clase) + valorRefrigerio;
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    private String determinarClase(int nroAsiento) {
        return nroAsiento <= cantAsientos[0].length ? "Turista" : "Ejecutivo";
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        int codigoPasaje = pasajerosPorDNI.get(dni).getCodPasaje();
        pasajerosPorDNI.remove(dni);
        int lenCantAsiento0 = cantAsientos[0].length;
        int lenCantAsiento01 = cantAsientos[0].length + cantAsientos[1].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = calcularPrecioPasaje(clase) + valorRefrigerio;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = calcularPrecioPasaje(clase) + valorRefrigerio;
            actualizarRecaudacion(-precioPasaje);
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }
}
