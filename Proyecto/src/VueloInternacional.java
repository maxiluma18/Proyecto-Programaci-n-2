import java.util.HashMap;
import java.util.Map;

public class VueloInternacional extends VueloPublico {
    private double valorRefrigerio;
    private int cantRefrigerios;
    private double[] precios;
    private int[][] cantAsientos;
    private Map<Integer, Cliente> pasajeros;
    private String[] escalas;

    public VueloInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios, double[] precios,
            int[] cantAsientos, String[] escalas) {
        super(origen, destino, fecha, tripulantes);
        this.valorRefrigerio = valorRefrigerio;
        this.cantRefrigerios = cantRefrigerios;
        this.escalas = escalas.length > 0 ? escalas : null;
        this.precios = agregarPrecios(precios);
        this.cantAsientos = agregarAsientos(cantAsientos);
        this.pasajeros = new HashMap<>();
    }

    private double[] agregarPrecios(double[] precios) {
        double[] nuevosPrecios = new double[3];
        nuevosPrecios[0] = precios[0];
        nuevosPrecios[1] = precios[1];
        nuevosPrecios[2] = precios[2];
        return nuevosPrecios;
    }

    private int[][] agregarAsientos(int[] cantAsientos) {
        int[][] nuevosAsientos = new int[3][];
        nuevosAsientos[0] = new int[cantAsientos[0]];
        nuevosAsientos[1] = new int[cantAsientos[1]];
        nuevosAsientos[2] = new int[cantAsientos[2]];

        for (int i = 0; i < cantAsientos[0]; i++) {
            nuevosAsientos[0][i] = 0;
        }
        for (int i = 0; i < cantAsientos[1]; i++) {
            nuevosAsientos[1][i] = 0;
        }
        for (int i = 0; i < cantAsientos[2]; i++) {
            nuevosAsientos[2][i] = 0;
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
        int basePrimeraClase = cantAsientos[0].length + cantAsientos[1].length;
        for (int i = 0; i < cantAsientos[2].length; i++) {
            if (cantAsientos[2][i] == 0) {
                asientos.put(basePrimeraClase + i + 1, "PrimeraClase");
            }
        }
        return asientos;
    }
}