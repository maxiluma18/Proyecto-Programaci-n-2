
public class VueloInternacional extends VueloPublico {
    private double valorRefrigerio;
    private int cantRefrigerios;
    private double[] precios;
    private int[] cantAsientos;
    private String[] escalas;
    private static int contadorVuelos = 0;

    public VueloInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio,
            int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {
        super(origen, destino, fecha, tripulantes);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
        this.cantAsientos = cantAsientos;
        this.cantRefrigerios = cantRefrigerios;
        if (escalas.length > 0) {
            this.escalas = escalas;
        }
        this.escalas = null;
    }

}