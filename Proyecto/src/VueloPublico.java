import java.util.HashMap;
import java.util.Map;

public abstract class VueloPublico extends Vuelo {
    private static int contadorVuelos = 0;
    private String codigo;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes) {
        super(origen, destino, fecha, tripulantes);
        this.codigo = generarCodigoVuelo() + "-PUB";
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }
}