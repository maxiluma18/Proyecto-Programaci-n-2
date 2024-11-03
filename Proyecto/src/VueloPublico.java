import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public abstract class VueloPublico extends Vuelo {
    private static int contadorVuelos = 0;
    private String codigo;
    protected static int codigoPasajeIncremental = 1;;
    protected int[][] cantAsientos;
    protected Map<Integer, Pasaje> pasajeros;
    protected Map<Integer, Pasaje> pasajerosPorDNI;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        this.codigo = generarCodigoVuelo() + "-PUB";
        this.cantAsientos = new int[2][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
        this.pasajeros = new HashMap<>();
        this.pasajerosPorDNI = new HashMap<>();
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }

    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0) {
            System.out.println("ACACA");
            return 0;
        }
        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length;
        if (pasajeros.size() >= totalAsientos - 1) {
            System.out.println("LLNO");
            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            return codPasaje;
        }
        System.out.println("ACACA343");
        return 0;
    }

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
        }
        return false;
    }

    public boolean tienePasaje(int dni, int nroAsiento) {
        return pasajerosPorDNI.containsKey(dni) && pasajerosPorDNI.get(dni).getNroAsiento() == nroAsiento;
    }

    public void cancelarPasaje(int dni, int nroAsiento) {
        int codigoPasaje = pasajerosPorDNI.get(dni).getCodPasaje();
        pasajerosPorDNI.remove(dni);
        pasajeros.remove(codigoPasaje);
        if (nroAsiento <= cantAsientos[0].length) {
            cantAsientos[0][nroAsiento - 1] = 0;
        } else if (nroAsiento <= cantAsientos[1].length) {
            cantAsientos[1][nroAsiento - 1] = 0;
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

}
