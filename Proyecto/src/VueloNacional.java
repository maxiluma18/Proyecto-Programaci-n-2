import java.util.HashMap;
import java.util.Map;

public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;
    private double[] precios;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
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
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar) {
        if (nroAsiento <= 0)
            return 0;

        // traer el codigopasajes de de vuelopublico
        int codPasaje = super.getCodigoPasaje();

        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar)) {
            super.setCodigoPasaje(1);
            return codPasaje;
        }
        return 0;
    }

}
