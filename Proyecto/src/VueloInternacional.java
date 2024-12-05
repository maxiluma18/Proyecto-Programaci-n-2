
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        this.cantAsientos = cantAsientos;

        this.totalAsientos = cantAsientos[0] + cantAsientos[1] + cantAsientos[2];
        inicializarAsientos(cantAsientos);
    }

    @Override
    protected void inicializarAsientos(int[] cantAsientos) {
        for (int i = 1; i <= totalAsientos; i++) {
            asientos.put(i, null);
        }
    }

    @Override
    public int venderPasaje(int dni, int nroAsiento, boolean aOcupar, String codVuelo) {
        if (nroAsiento <= 0 || nroAsiento > totalAsientos) {
            return 0;
        }
        if (asientos.get(nroAsiento) != null) {

            return 0;
        }
        if (pasajeros.size() >= totalAsientos) {

            return 0;
        }
        int codPasaje = codigoPasajeIncremental++;
        if (ocuparAsiento(dni, nroAsiento, codPasaje, aOcupar, codVuelo)) {
            double precioPasaje = calcularPrecioPasaje(determinarClase(nroAsiento)) * 1.2;
            actualizarRecaudacion(precioPasaje);
            return codPasaje;
        }
        return 0;
    }

    @Override
    public String determinarClase(int nroAsiento) {
        if (nroAsiento <= 0 || nroAsiento > totalAsientos) {
            throw new IllegalArgumentException("Número de asiento inválido.");
        }

        if (nroAsiento <= cantAsientos[0]) {
            return "Turista";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1])) {
            return "Ejecutivo";
        } else if (nroAsiento <= (cantAsientos[0] + cantAsientos[1] + cantAsientos[2])) {
            return "Primera Clase";
        } else {
            throw new IllegalArgumentException("Número de asiento fuera de rango o clase no válida.");
        }
    }

    protected boolean ocuparAsiento(int dni, int nroAsiento, int codPasaje, boolean aOcupar, String codVuelo) {
        if (asientos.containsKey(nroAsiento) && asientos.get(nroAsiento) == null) {
            Pasaje nuevoPasaje = new Pasaje(dni, nroAsiento, determinarClase(nroAsiento), aOcupar, codVuelo, codPasaje);
            asientos.put(nroAsiento, nuevoPasaje);
            pasajeros.computeIfAbsent(dni, k -> new HashSet<>()).add(nroAsiento); // Cambiado a HashSet
            return true;
        }
        return false;
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

    @Override
    public String detalle(String codVuelo) {
        return super.detalle(codVuelo) + "-" + "INTERNACIONAL";
    }

    // GETTERS
    public String[] getEscalas() {
        return this.escalas;
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
        } else if (clase.equals("Primera Clase")) {
            double cant = precios[2] + cantRefrigerio;
            return cant * 1.2;
        } else {
            throw new IllegalArgumentException("Clase inexistente");
        }
    }

    @Override
    protected double calcularPrecioPasaje(String clase) {
        double precioPasaje = 0;
        if (clase.equals("Turista")) {
            precioPasaje = precios[0];
        } else if (clase.equals("Ejecutivo")) {
            precioPasaje = precios[1];
        } else if (clase.equals("Primera Clase")) {
            precioPasaje = precios[2];
        }
        return precioPasaje * 1.2;
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
}
