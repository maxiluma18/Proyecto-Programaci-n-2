import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class VueloInternacional extends VueloPublico {
    private double valorRefrigerio;
    private int cantRefrigerios;
    private String[] escalas;
    private double[] precios;

    public VueloInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios,
            double[] precios, int[] cantAsientos, String[] escalas) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.cantRefrigerios = cantRefrigerios;
        this.escalas = escalas.length > 0 ? escalas : null;
        this.cantAsientos = new int[3][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
        this.cantAsientos[2] = new int[cantAsientos[2]];
        this.precios = precios;
    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0)
            return 0;
        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length;
        if (pasajeros.size() >= totalAsientos - 1) {
            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            return codPasaje;
        }
        return 0;
    }

    @Override
    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= cantAsientos[0].length) {
            // Asiento Turista
            if (cantAsientos[0][nroAsiento - 1] == 0) {
                cantAsientos[0][nroAsiento - 1] = aOcupar ? 1 : 0;
                pasajeros.put(codPasaje, new Pasaje(dni, nroAsiento, "Turista", aOcupar, codVuelo, codPasaje));
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Turista", aOcupar, codVuelo, codPasaje));

                return true;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            // Asiento Ejecutivo
            int asientoEjecutivo = nroAsiento - cantAsientos[0].length - 1;
            if (cantAsientos[1][asientoEjecutivo] == 0) {
                cantAsientos[1][asientoEjecutivo] = aOcupar ? 1 : 0;
                pasajeros.put(codPasaje, new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar, codVuelo, codPasaje));
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar, codVuelo, codPasaje));

                return true;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length) {
            // Asiento Primera Clase
            int asientoPrimeraClase = nroAsiento - cantAsientos[0].length - cantAsientos[1].length - 1;
            if (cantAsientos[2][asientoPrimeraClase] == 0) {
                cantAsientos[2][asientoPrimeraClase] = aOcupar ? 1 : 0;
                pasajeros.put(codPasaje, new Pasaje(dni, nroAsiento, "Primera Clase", aOcupar, codVuelo, codPasaje));
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Primera Clase", aOcupar, codVuelo, codPasaje));
                return true;
            }
        }
        return false;
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        int codigoPasaje = pasajerosPorDNI.get(dni).getCodPasaje();
        pasajerosPorDNI.remove(dni);
        pasajeros.remove(codigoPasaje);
        if (nroAsiento <= cantAsientos[0].length) {
            cantAsientos[0][nroAsiento - 1] = 0;
        } else if (nroAsiento <= cantAsientos[1].length) {
            cantAsientos[1][nroAsiento - 1] = 0;
        } else if (nroAsiento <= cantAsientos[2].length) {
            cantAsientos[2][nroAsiento - 1] = 0;

        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
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
        int basePrimeraClase = cantAsientos[0].length + cantAsientos[1].length;
        for (int i = 0; i < cantAsientos[2].length; i++) {
            if (cantAsientos[2][i] == 0) {
                asientos.put(basePrimeraClase + i + 1, "PrimeraClase");
            }
        }
        return asientos;
    }

}
