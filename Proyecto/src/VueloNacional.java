import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class VueloNacional extends VueloPublico {
    private double valorRefrigerio;

    public VueloNacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, double[] precios, int[] cantAsientos) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        validacionPreciosCantAsientosNacional(precios, cantAsientos);
        this.valorRefrigerio = valorRefrigerio;
        this.precios = precios;
        this.cantAsientos = new int[2][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0)
            return 0;

        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length;
        if (pasajerosPorDNI.size() >= totalAsientos) {
            return 0;
        }

        int codPasaje = codigoPasajeIncremental++;
        String clase = determinarClase(nroAsiento);
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    @Override
    protected String determinarClase(int nroAsiento) {
        return nroAsiento <= cantAsientos[0].length ? "Turista" : "Ejecutivo";
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento) {
        int codPasaje = pasajerosPorDNI.get(dni).getCodPasaje();
        pasajerosPorCodPasaje.remove(codPasaje);
        int cantidadPasajes = contadorPasajesPorDNI.get(dni);
        if (cantidadPasajes == 0) {
            pasajerosPorDNI.remove(dni);
            contadorPasajesPorDNI.remove(dni);
        } else {
            contadorPasajesPorDNI.put(dni, cantidadPasajes - 1);
        }
        int lenCantAsiento0 = cantAsientos[0].length;
        int lenCantAsiento01 = cantAsientos[0].length + cantAsientos[1].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

    @Override
    public void cancelarPasaje(int dni, int nroAsiento, int codPasaje) {
        pasajerosPorCodPasaje.remove(codPasaje);
        int cantidadPasajes = contadorPasajesPorDNI.get(dni);
        if (cantidadPasajes == 1) {
            pasajerosPorDNI.remove(dni);
            contadorPasajesPorDNI.remove(dni);
        } else {
            contadorPasajesPorDNI.put(dni, cantidadPasajes - 1);
        }
        int lenCantAsiento0 = cantAsientos[0].length;
        int lenCantAsiento01 = cantAsientos[0].length + cantAsientos[1].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

    // GETTERS
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

    public void validacionRefrigerio(double refrigerio) {
        if (refrigerio <= 0) {
            throw new RuntimeException("El valor del refrigerio debe ser positivo");
        }
    }

    public String getTipoVuelo() {
        return "NACIONAL";
    }

    public void validacionPreciosCantAsientosNacional(double[] precios, int[] cantAsientos) {
        if (precios.length != 2 || cantAsientos.length != 2) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
    }

    @Override
    protected double getClaseSeccion(String clase) {
        double cantRefrigerio = valorRefrigerio;
        if (clase.equals("Turista")) {
            double cant = precios[0] + cantRefrigerio;
            return cant * 1.2;
        } else {
            double cant = precios[1] + cantRefrigerio;
            return cant * 1.2;
        }
    }
}
