import java.util.HashMap;
import java.util.Map;

public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;
    private double[] precios;
    private int[][] cantAsientos;
    private Map<Integer, Cliente> pasajeros;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = agregarPrecios(precios);
        this.cantAsientos = agregarAsientos(cantAsientos);
        this.pasajeros = new HashMap<>();
    }

    private double[] agregarPrecios(double[] precios) {
        double[] nuevosPrecios = new double[2];
        nuevosPrecios[0] = precios[0];
        nuevosPrecios[1] = precios[1];
        return nuevosPrecios;
    }

    private int[][] agregarAsientos(int[] cantAsientos) {
        int[][] nuevosAsientos = new int[2][];
        nuevosAsientos[0] = new int[cantAsientos[0]];
        nuevosAsientos[1] = new int[cantAsientos[1]];

        for (int i = 0; i < cantAsientos[0]; i++) {
            nuevosAsientos[0][i] = 0;
        }
        for (int i = 0; i < cantAsientos[1]; i++) {
            nuevosAsientos[1][i] = 0;
        }

        return nuevosAsientos;
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

}
