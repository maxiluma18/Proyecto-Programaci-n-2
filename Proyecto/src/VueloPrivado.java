public class VueloPrivado extends Vuelo {
    private double precio;
    private int dniComprador;
    private int[] acompaniantes;
    private static int contadorVuelos = 0;
    private String codigo;
    private int cantidadPasajerosPorJet = 15;
    private int cantJets;

    public VueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
            int dniComprador, int[] acompaniantes) {
        super(origen, destino, fecha, tripulantes);
        this.precio = precio;
        this.dniComprador = dniComprador;
        this.acompaniantes = acompaniantes;
        this.codigo = generarCodigoVuelo() + "-PRI";
        this.cantJets = cantJets();
    }

    private int cantJets() {
        int cantidad = acompaniantes.length;
        int jets = cantidad / cantidadPasajerosPorJet;
        if (cantidad % cantidadPasajerosPorJet > 0) {
            jets++;
        }
        System.out.println(jets);
        return jets;
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }
}