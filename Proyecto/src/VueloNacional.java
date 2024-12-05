
public class VueloNacional extends VueloPublico {

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        validacionPreciosCantAsientosNacional(precios, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
    }

    @Override
    public String detalle(String codVuelo) {
        return super.detalle(codVuelo) + "-" + "NACIONAL";
    }

    public void validacionRefrigerio(double refrigerio) {
        if (refrigerio <= 0) {
            throw new RuntimeException("El valor del refrigerio debe ser positivo");
        }
    }

    public void validacionPreciosCantAsientosNacional(double[] precios, int[] cantAsientos) {
        if (precios.length != 2 || cantAsientos.length != 2) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
    }

}
