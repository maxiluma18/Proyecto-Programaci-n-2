import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public abstract class VueloPublico extends Vuelo {
    protected static int codigoPasajeIncremental = 1;
    protected int[][] cantAsientos;
    protected Map<Integer, Pasaje> pasajerosPorDNI;
    protected double[] precios;
    protected double valorRefrigerio;
    private double recaudacionTotal;
    private static int contadorVuelos = 0;
    private String codigo;

    public VueloPublico(String origen, String destino, String fecha, int tripulantes, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes);
        this.codigo = generarCodigoVuelo() + "-PUB";
        this.pasajerosPorDNI = new HashMap<>();
        this.recaudacionTotal = 0;
    }

    protected double calcularPrecioPasaje(String clase) {
        double precioPasaje = 0;
        if (clase.equals("Turista")) {
            precioPasaje = precios[0];
        } else if (clase.equals("Ejecutivo")) {
            precioPasaje = precios[1];
        } else if (clase.equals("Primera Clase")) {
            precioPasaje = precios[2];
        }
        return precioPasaje * 1.2; // Añade 20% de impuestos
    }

    protected void actualizarRecaudacion(double monto) {
        this.recaudacionTotal += monto;
    }

    public double getRecaudacionTotal() {
        return recaudacionTotal;
    }

    private static String generarCodigoVuelo() {
        contadorVuelos++;
        return String.valueOf(contadorVuelos);
    }

    public String getCodigo() {
        return codigo;
    }

    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0) {
            System.out.println("ACACA");
            return 0;
        }
        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length;
        if (pasajerosPorDNI.size() >= totalAsientos - 1) {
            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            return codPasaje;
        }

        return 0;
    }

    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= cantAsientos[0].length) {
            // Asiento Turista
            if (cantAsientos[0][nroAsiento - 1] == 0) {
                cantAsientos[0][nroAsiento - 1] = 1;
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Turista", aOcupar, codVuelo, codPasaje));
                return true;
            } else {
                return false;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            // Asiento Ejecutivo
            int asientoEjecutivo = nroAsiento - cantAsientos[0].length - 1;
            if (cantAsientos[1][asientoEjecutivo] == 0) {
                cantAsientos[1][asientoEjecutivo] = 1;
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar, codVuelo, codPasaje));
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean tienePasaje(int dni) {

        return pasajerosPorDNI.containsKey(dni);
    }

    public void cancelarPasaje(int dni, int nroAsiento) {
        pasajerosPorDNI.remove(dni);
        int lenCantAsiento0 = cantAsientos[0].length;
        int lenCantAsiento01 = cantAsientos[0].length + cantAsientos[1].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

    public int asignarAsiento(int dni, int nroAsiento, String clase, boolean ocupado) {
        int codPasaje = codigoPasajeIncremental++;
        String codVuelo = this.getCodigo();

        List<String> clasesDisponibles = new ArrayList<>();
        if (clase.equals("Turista")) {
            clasesDisponibles.add("Turista");
            clasesDisponibles.add("Ejecutivo");
            clasesDisponibles.add("Primera Clase");
        } else if (clase.equals("Ejecutivo")) {
            clasesDisponibles.add("Ejecutivo");
            clasesDisponibles.add("Primera Clase");
        } else {
            throw new RuntimeErrorException(null, "Clase inexistente");
        }

        for (String claseActual : clasesDisponibles) {
            int asientoDisponible = encontrarAsientoDisponible(claseActual);
            if (asientoDisponible != -1) {
                if (ocuparAsiento(dni, asientoDisponible, codPasaje, ocupado, codVuelo)) {
                    double precioPasaje = calcularPrecioPasaje(clase) + valorRefrigerio;
                    actualizarRecaudacion(precioPasaje);
                    return codPasaje;
                }
            }
        }

        return -1;
    }

    protected int encontrarAsientoDisponible(String clase) {
        int inicio, fin;
        if (clase.equals("Turista")) {
            inicio = 1;
            fin = cantAsientos[0].length;
        } else if (clase.equals("Ejecutivo")) {
            inicio = cantAsientos[0].length + 1;
            fin = cantAsientos[0].length + cantAsientos[1].length;
        } else { // Primera Clase
            inicio = cantAsientos[0].length + cantAsientos[1].length + 1;
            fin = cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length;
        }

        for (int i = inicio; i <= fin; i++) {
            if (!estaOcupado(i)) {
                return i;
            }
        }
        return -1;
    }

    protected boolean estaOcupado(int nroAsiento) {
        if (nroAsiento <= cantAsientos[0].length) {
            return cantAsientos[0][nroAsiento - 1] == 1;
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            return cantAsientos[1][nroAsiento - cantAsientos[0].length - 1] == 1;
        } else {
            return cantAsientos[2][nroAsiento - cantAsientos[0].length - cantAsientos[1].length - 1] == 1;
        }
    }

    public Map<Integer, Pasaje> getPasajeros() {
        return pasajerosPorDNI;
    }
}
