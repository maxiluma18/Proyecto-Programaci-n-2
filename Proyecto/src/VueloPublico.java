import java.util.HashMap;
import java.util.Map;

public abstract class VueloPublico extends Vuelo {
    private static int contadorVuelos = 0;
    private String codigo;
    private static int codigoPasajeIncremental = 1;;
    protected int[][] cantAsientos;
    protected Map<Integer, Pasaje> pasajeros;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        this.codigo = generarCodigoVuelo() + "-PUB";
        this.cantAsientos = new int[2][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
        this.pasajeros = new HashMap<>();
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }

    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar) {
        if (nroAsiento <= 0)
            return 0;

        int codPasaje = codigoPasajeIncremental++;

        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar)) {
            return codPasaje;
        }
        return 0;
    }

    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar) {
        if (nroAsiento <= cantAsientos[0].length) {
            // Asiento Turista
            if (cantAsientos[0][nroAsiento - 1] == 0) {
                cantAsientos[0][nroAsiento - 1] = aOcupar ? 1 : 0;
                pasajeros.put(codPasaje, new Pasaje(dni, nroAsiento, "Turista", aOcupar));
                return true;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            // Asiento Ejecutivo
            int asientoEjecutivo = nroAsiento - cantAsientos[0].length - 1;
            if (cantAsientos[1][asientoEjecutivo] == 0) {
                cantAsientos[1][asientoEjecutivo] = aOcupar ? 1 : 0;
                pasajeros.put(codPasaje, new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar));
                return true;
            }
        }
        return false;
    }

    public int getCodigoPasaje() {
        return this.codigoPasajeIncremental;
    }

    public void setCodigoPasaje(int n) {
        this.codigoPasajeIncremental = codigoPasajeIncremental + n;
    }
}
