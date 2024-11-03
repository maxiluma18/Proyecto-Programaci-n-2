public class Pasaje {
    private int dni;
    private int nroAsiento;
    private String clase;
    private boolean ocupado;
    private String codVuelo;
    private int codPasaje;

    public Pasaje(int dni, int nroAsiento, String clase, boolean ocupado, String codVuelo, int codPasaje) {
        this.dni = dni;
        this.nroAsiento = nroAsiento;
        this.clase = clase;
        this.ocupado = ocupado;
        this.codVuelo = codVuelo;
        this.codPasaje = codPasaje;
    }

    public int getDni() {
        return dni;
    }

    public int getNroAsiento() {
        return nroAsiento;
    }

    public String getClase() {
        return clase;
    }

    public boolean getOcupado() {
        return ocupado;
    }

    public String getcodVuelo() {
        return codVuelo;
    }

    public int getCodPasaje() {
        return codPasaje;
    }
}