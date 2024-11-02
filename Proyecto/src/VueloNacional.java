
public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;
    private double[] precios;
    private int[] cantAsientos;
    private static int contadorVuelos = 0;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio,
            double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
        this.cantAsientos = cantAsientos;
    }

}