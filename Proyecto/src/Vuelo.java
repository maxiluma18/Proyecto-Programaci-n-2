
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vuelo {
    private String origen;
    private String destino;
    private String fecha;
    private int tripulantes;

    public Vuelo(String origen, String destino, String fecha, int tripulantes) {
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
