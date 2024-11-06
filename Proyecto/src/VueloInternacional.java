
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class VueloInternacional extends VueloPublico {
    private double valorRefrigerio;
    private int cantRefrigerios;
    private String[] escalas;

    public VueloInternacional(String origen, String destino, String fecha, int tripulantes,
            double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos,
            String[] escalas) {
        super(origen, destino, fecha, tripulantes, cantAsientos);
        validacionPreciosCantAsientosInternacional(precios, cantAsientos);
        validacionCantRefrigeriosInternacional(cantRefrigerios);
        this.valorRefrigerio = valorRefrigerio;
        this.cantRefrigerios = cantRefrigerios;
        this.escalas = escalas.length > 0 ? escalas : null;
        this.precios = precios;
        this.cantAsientos = new int[3][];
        this.cantAsientos[0] = new int[cantAsientos[0]];
        this.cantAsientos[1] = new int[cantAsientos[1]];
        this.cantAsientos[2] = new int[cantAsientos[2]];

    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0) {
            return 0;
        }
        int totalAsientos = cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length;
        if (pasajerosPorCodPasaje.size() >= totalAsientos) {
            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        String clase = determinarClase(nroAsiento);

        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    @Override
    protected String determinarClase(int nroAsiento) {
        if (nroAsiento <= cantAsientos[0].length) {
            return "Turista";
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            return "Ejecutivo";
        } else {
            return "Primera Clase";
        }
    }

    @Override
    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= cantAsientos[0].length) {
            if (cantAsientos[0][nroAsiento - 1] == 0) {
                cantAsientos[0][nroAsiento - 1] = 1;
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Turista", aOcupar, codVuelo, codPasaje));
                pasajerosPorCodPasaje.put(codPasaje,
                        new Pasaje(dni, nroAsiento, "Turista", aOcupar, codVuelo, codPasaje));
                contadorPasajesPorDNI.put(dni, contadorPasajesPorDNI.getOrDefault(dni, 0) + 1);
                return true;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length) {
            int asientoEjecutivo = nroAsiento - cantAsientos[0].length - 1;
            if (cantAsientos[1][asientoEjecutivo] == 0) {
                cantAsientos[1][asientoEjecutivo] = 1;
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar, codVuelo, codPasaje));
                pasajerosPorCodPasaje.put(codPasaje,
                        new Pasaje(dni, nroAsiento, "Ejecutivo", aOcupar, codVuelo, codPasaje));
                contadorPasajesPorDNI.put(dni, contadorPasajesPorDNI.getOrDefault(dni, 0) + 1);
                return true;
            }
        } else if (nroAsiento <= cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length) {
            int asientoPrimeraClase = nroAsiento - cantAsientos[0].length - cantAsientos[1].length - 1;
            if (cantAsientos[2][asientoPrimeraClase] == 0) {
                cantAsientos[2][asientoPrimeraClase] = 1;
                pasajerosPorDNI.put(dni, new Pasaje(dni, nroAsiento, "Primera Clase", aOcupar, codVuelo, codPasaje));
                pasajerosPorCodPasaje.put(codPasaje,
                        new Pasaje(dni, nroAsiento, "Primera Clase", aOcupar, codVuelo, codPasaje));
                contadorPasajesPorDNI.put(dni, contadorPasajesPorDNI.getOrDefault(dni, 0) + 1);
                return true;
            }
        }
        return false;
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
        int lenCantAsiento012 = cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento012) {
            cantAsientos[2][nroAsiento - lenCantAsiento01 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
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
        int lenCantAsiento012 = cantAsientos[0].length + cantAsientos[1].length + cantAsientos[2].length;
        if (nroAsiento <= lenCantAsiento0) {
            cantAsientos[0][nroAsiento - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento01) {
            cantAsientos[1][nroAsiento - lenCantAsiento0 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(-precioPasaje);
        } else if (nroAsiento <= lenCantAsiento012) {
            cantAsientos[2][nroAsiento - lenCantAsiento01 - 1] = 0;
            String clase = determinarClase(nroAsiento);
            double precioPasaje = (calcularPrecioPasaje(clase) + (valorRefrigerio * cantRefrigerios)) * 1.2;
            actualizarRecaudacion(-precioPasaje);

        } else {
            throw new RuntimeErrorException(null, "No existe el asiento");
        }
    }

    @Override
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
        } else if (clase.equals("Primera Clase")) {
            clasesDisponibles.add("Primera Clase");
        } else {
            throw new RuntimeErrorException(null, "Clase Inexistente");
        }

        for (String claseActual : clasesDisponibles) {
            int asientoDisponible = encontrarAsientoDisponible(claseActual);
            if (asientoDisponible != -1) {
                if (ocuparAsiento(dni, asientoDisponible, codPasaje, ocupado, codVuelo)) {
                    double precioPasaje = (calcularPrecioPasaje(clase) + valorRefrigerio) * 1.2;
                    actualizarRecaudacion(precioPasaje);
                    return codPasaje;
                }
            }
        }

        return -1;
    }

    // GETTERS
    public String[] getEscalas() {
        return this.escalas;
    }

    public String getTipoVuelo() {
        return "INTERNACIONAL";
    }

    @Override
    protected double getClaseSeccion(String clase) {
        double cantRefrigerio = cantRefrigerios * valorRefrigerio;
        if (clase.equals("Turista")) {
            double cant = precios[0] + cantRefrigerio;
            return cant * 1.2;
        } else if (clase.equals("Ejecutivo")) {
            double cant = precios[1] + cantRefrigerio;
            return cant * 1.2;
        } else {
            double cant = precios[2] + cantRefrigerio;
            return cant * 1.2;
        }

    }

    public void validacionPreciosCantAsientosInternacional(double[] precios, int[] cantAsientos) {
        if (precios.length != 3 || cantAsientos.length != 3) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 3");
        }
    }

    public void validacionCantRefrigeriosInternacional(int cantRefrigerios) {
        if (cantRefrigerios != 3) {
            throw new RuntimeException("La cantidad de refrigerios deben ser 3");
        }
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
