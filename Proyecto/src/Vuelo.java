
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.management.RuntimeErrorException;

public class Vuelo {
    private String origen;
    private String destino;
    private String fecha;
    private int tripulantes;

    public Vuelo(String origen, String destino, String fecha, int tripulantes) {

        validacionFecha(fecha);
        validacionTripulantes(tripulantes);
        validacionOrigenDestinoInternacional(origen, destino);
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.tripulantes = tripulantes;
    }

    public boolean fechaValida(String fecha) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
        LocalDate fechaParametro = LocalDate.parse(fecha, formatters);
        LocalDate fechaVuelo = LocalDate.parse(this.fecha, formatters);
        LocalDate fechaMas7Dias = fechaParametro.plusDays(7);

        return !fechaVuelo.isBefore(fechaParametro) && !fechaVuelo.isAfter(fechaMas7Dias);
    }

    public void validacionFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            throw new RuntimeErrorException(null, "La fecha no puede ser nula o vacia");
        }
    }

    public void validacionTripulantes(int tripulantes) {
        if (tripulantes <= 0) {
            throw new IllegalArgumentException("La cantidad de tripulantes debe ser positiva");
        }
    }

    public void validacionOrigenDestinoInternacional(String origen, String destino) {
        if (origen == null || origen.isEmpty() || destino == null || destino.isEmpty()) {
            throw new RuntimeException("El origen y destino no pueden ser nulos o vacíos");
        }
    }

    // GETTERS
    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    protected String getFecha() {
        return fecha;
    }

    protected int getTripulantes() {
        return tripulantes;
    }
}
