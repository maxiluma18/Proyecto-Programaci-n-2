import java.util.HashMap;
import java.util.Map;

public class VueloPublico extends Vuelo {
    private Map<Integer, Cliente> pasajeros;
    private static int contadorVuelos = 0;
    private String codigo;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes) {
        super(origen, destino, fecha, tripulantes);
        this.pasajeros = new HashMap<>();
        this.codigo = generarCodigoVuelo();
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }
}