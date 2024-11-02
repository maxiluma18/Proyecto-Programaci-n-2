public class Pasaje {
    private int dni;
    private int nroAsiento;
    private String clase;
    private boolean ocupado;

    public Pasaje(int dni, int nroAsiento, String clase, boolean ocupado) {
        this.dni = dni;
        this.nroAsiento = nroAsiento;
        this.clase = clase;
        this.ocupado = ocupado;
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
}