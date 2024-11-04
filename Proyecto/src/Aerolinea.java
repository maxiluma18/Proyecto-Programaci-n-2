import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class Aerolinea implements IAerolinea {
    private String nombre;
    private String cuit;
    private Map<String, Aeropuerto> aeropuertos;
    private Map<String, Vuelo> Vuelos;
    private Map<Integer, Cliente> clientes;

    public Aerolinea(String nombre, String cuit) {
        this.nombre = nombre;
        this.cuit = cuit;
        this.aeropuertos = new HashMap<>();
        this.clientes = new HashMap<>();
        this.Vuelos = new HashMap<>();
    }

    //IREPS
    public void validacionDni(int dni){
        if(dni <= 0){
            throw new IllegalArgumentException("El Dni debe ser positivo");
        }
    }
    public void validacionPreciosCantAsientosNacional(double[] precios, int[] cantAsientos){
        if (precios.length != 2 || cantAsientos.length != 2) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
    }
    public void validacionPreciosCantAsientosInternacional(double [] precios, int [] cantAsientos){
        if (precios.length != 3 || cantAsientos.length != 3) {
            throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        }
    }
    public void validacionNombre(String nombre){
        if(nombre == null || nombre.isEmpty() || nombre.length()<=2){
            throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
        }
    }
    public void validacionPais(String pais){
        if (pais == null || pais.isEmpty() || pais.length()<=2) {
            throw new RuntimeErrorException(null, "el pais no puede ser nulo o vacio");
        }
    }
    public void validacionProvincia(String provincia){
        if (provincia == null || provincia.isEmpty() || provincia.length()<=2) {
            throw new RuntimeErrorException(null, "La provincia no puede ser nulo o vacio");
        }
    }
    public void validacionDireccion(String direccion){
        if (direccion == null || direccion.isEmpty() || direccion.length()<=2) {
            throw new RuntimeErrorException(null, "La direccion no puede ser nulo o vacio");
        }
    }
    public void validacionTelefono(String telefono){
        if (telefono == null || telefono.isEmpty() || telefono.length()<10) {
            throw new IllegalArgumentException("El dato telefono debe ser valido");
        }
    }
    public void validacionOrigenDestinoNacional(String origen, String destino){
        if(origen == null || origen.isEmpty() || destino == null || destino.isEmpty()){
            throw new RuntimeException("El origen y destino no pueden ser nulos o vacíos");
        }
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        if (!aeropuertos.get(origen).getPais().equals("Argentina") ||
                !aeropuertos.get(destino).getPais().equals("Argentina")) {
            throw new RuntimeException("Los aeropuertos deben ser nacionales");
        }
    }
    public void validacionOrigenDestinoInternacional(String origen, String destino){
        if(origen == null || origen.isEmpty() || destino == null || destino.isEmpty()){
            throw new RuntimeException("El origen y destino no pueden ser nulos o vacíos");
        }
        if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
            throw new RuntimeException("Origen o destino no registrados");
        }
        if (!aeropuertos.get(origen).getPais().equals("Argentina") ||
            aeropuertos.get(destino).getPais().equals("Argentina")) {
            throw new RuntimeException("El origen debe ser nacional y el destino debe ser internacional");
        }
    }
    public void validacionFecha(String fecha){
        if(fecha == null || fecha.isEmpty()){
            throw new RuntimeErrorException(null, "La fecha no puede ser nula o vacia");
        }
    }
    public void validacionTripulantes(int tripulantes){
        if(tripulantes<=0){
            throw new IllegalArgumentException("La cantidad de tripulantes debe ser positiva");
        }
    }
    public void validacionRefrigerio(double refrigerio){
        if(refrigerio<=0){
            throw new RuntimeException("El valor del refrigerio debe ser positivo");
        }
    }
    public void validacionEscalas(String [] escalas){
        if (escalas.length > 0) {
            for (int i = 0; i < escalas.length; i++) {
                boolean escalasFalsa = true;
                if (escalas[i].length() < 3) {
                    escalasFalsa = false;
                }
                if (!escalasFalsa) {
                    throw new RuntimeErrorException(null, "Las escalas son invalidas");
                }
            }
        }
    }
    public void validacionCantRefrigerios(int cantRefrigerios){
        if(cantRefrigerios <=2 ){
            throw new RuntimeException("La cantidad de refrigerios deben ser 2");
        }
    }
    public void validacionAcompaniantes(int [] acompaniantes){
        if (acompaniantes.length < 0) {
            throw new RuntimeErrorException(null, "Error en los datos");
        }
    }
    public void validacionDniComprador(int dniComprador){
        if (!clientes.containsKey(dniComprador)) {
            throw new RuntimeException("El cliente no esta registrado");
        }
    }
    
    // public void validacionStr(String nombre, String pais, String provincia, String direccion, String telefono){
    //     if(nombre == null || nombre.isEmpty() || nombre.length()<=2){
    //         throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
    //     }
    //     if (pais == null || pais.isEmpty() || pais.length()<=2) {
    //         throw new RuntimeErrorException(null, "el pais no puede ser nulo o vacio");
    //     }
    //     if (provincia == null || provincia.isEmpty() || provincia.length()<=2) {
    //         throw new RuntimeErrorException(null, "La provincia no puede ser nulo o vacio");
    //     }
    //     if (direccion == null || direccion.isEmpty() || direccion.length()<=2) {
    //         throw new RuntimeErrorException(null, "La direccion no puede ser nulo o vacio");
    //     }
    //     if (telefono == null || telefono.isEmpty() || telefono.length()<10) {
    //         throw new IllegalArgumentException("El dato telefono debe ser valido");
    //     }
    // }

    public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {
        // si ya existe ese aeropuerto en aerpuertos no se hace
        if (aeropuertos.containsKey(nombre)) {
            throw new RuntimeErrorException(null, "Aeropuerto ya existente");
        }
        validacionNombre(nombre);
        validacionPais(pais);
        validacionProvincia(provincia);
        validacionDireccion(direccion);
        // if (nombre == null || nombre.isEmpty() || nombre.length()<=2) {
        //     throw new RuntimeErrorException(null, "Nombre de aeropuerto no puede ser nulo o vacio");
        // }
        // if (pais == null || pais.isEmpty() || pais.length()<=2) {
        //     throw new RuntimeErrorException(null, "el pais no puede ser nulo o vacio");
        // }
        // if (provincia == null || provincia.isEmpty() || provincia.length()<=2) {
        //     throw new RuntimeErrorException(null, "La provincia no puede ser nulo o vacio");
        // }
        // if (direccion == null || direccion.isEmpty()|| direccion.length()<=2) {
        //     throw new RuntimeErrorException(null, "La direccion no puede ser nulo o vacio");
        // }
        Aeropuerto nuevoAeropuerto = new Aeropuerto(nombre, pais, provincia, direccion);
        aeropuertos.put(nombre, nuevoAeropuerto);
    }

    public void registrarCliente(int dni, String nombre, String telefono) {
        if (clientes.containsKey(dni)) {
            throw new RuntimeErrorException(null, "Cliente ya existente");
        }
        validacionDni(dni);
        validacionNombre(nombre);
        validacionTelefono(telefono);
        // if (dni <= 0) {
        //     throw new IllegalArgumentException("El Dni debe ser positivo");
        // }
        // if (nombre == null || nombre.isEmpty()) {
        //     throw new IllegalArgumentException("El dato nombre debe ser valido");
        // }
        // if (telefono == null || telefono.isEmpty()) {
        //     throw new IllegalArgumentException("El dato telefono debe ser valido");
        // }
        Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
        clientes.put(dni, nuevoCliente);
    }

    public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, double[] precios, int[] cantAsientos) {
        validacionOrigenDestinoNacional(origen, destino);
        validacionPreciosCantAsientosNacional(precios, cantAsientos);
        validacionFecha(fecha);
        validacionTripulantes(tripulantes);
        validacionRefrigerio(valorRefrigerio);
        // if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
        //     throw new RuntimeException("Origen o destino no registrados");
        // }
        // if (!aeropuertos.get(origen).getPais().equals("Argentina") ||
        //         !aeropuertos.get(destino).getPais().equals("Argentina")) {
        //     throw new RuntimeException("Los aeropuertos deben ser nacionales");
        // }
        // if (precios.length != 2 || cantAsientos.length != 2) {
        //     throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        // }
        // IREP DE LO DEMAS

        VueloNacional nuevoVuelo = new VueloNacional(origen, destino, fecha, tripulantes, valorRefrigerio, precios,
                cantAsientos);

        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {
        validacionOrigenDestinoInternacional(origen, destino);
        validacionFecha(fecha);
        validacionTripulantes(tripulantes);
        validacionRefrigerio(cantRefrigerios);
        validacionPreciosCantAsientosInternacional(precios, cantAsientos);
        validacionEscalas(escalas);
        validacionCantRefrigerios(cantRefrigerios);
        // if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
        //     throw new RuntimeException("Origen o destino no registrados");
        // }
        // if (precios.length != 3 || cantAsientos.length != 3) {
        //     throw new RuntimeException("Los arrays de precios y asientos deben tener longitud 2");
        // }
        // IREP DE LO DEMAS
        // agregar mejor irep que este
        // if (escalas.length > 0) {
        //     for (int i = 0; i < escalas.length; i++) {
        //         boolean escalasFalsa = true;
        //         if (escalas[i].length() < 3) {
        //             escalasFalsa = false;
        //         }
        //         if (!escalasFalsa) {
        //             throw new RuntimeErrorException(null, "Las escalas son invalidas");
        //         }
        //     }
        // }
        VueloInternacional nuevoVuelo = new VueloInternacional(origen, destino, fecha, tripulantes, valorRefrigerio,
                cantRefrigerios, precios, cantAsientos, escalas);
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);

        return codigoVuelo;
    }

    public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio, int dniComprador, int[] acompaniantes) {
        validacionOrigenDestinoNacional(origen, destino);
        validacionFecha(fecha);
        validacionTripulantes(tripulantes);
        validacionAcompaniantes(acompaniantes);
        validacionDniComprador(dniComprador);

        // if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
        //     throw new RuntimeException("Origen o destino no registrados");
        // }
        // if (acompaniantes.length < 0) {
        //     throw new RuntimeErrorException(null, "Error en los datos");
        // }
        // if (!clientes.containsKey(dniComprador)) {
        //     throw new RuntimeException("El cliente no esta registrado");
        // }
        // IREP DE LO DEMAS

        VueloPrivado nuevoVuelo = new VueloPrivado(origen, destino, fecha, tripulantes, precio, dniComprador,
                acompaniantes);
        String codigoVuelo = nuevoVuelo.getCodigo();
        Vuelos.put(codigoVuelo, nuevoVuelo);
        return codigoVuelo;
    }

    public Map<Integer, String> asientosDisponibles(String codVuelo) {
        Vuelo vuelo = Vuelos.get(codVuelo);
        if (vuelo == null) {
            throw new RuntimeException("El vuelo no existe");
        }
        if (vuelo instanceof VueloInternacional) {
            VueloInternacional vueloInternacional = (VueloInternacional) vuelo;
            return vueloInternacional.getAsientosDisponibles();
        } else if (vuelo instanceof VueloNacional) {
            VueloNacional vueloNacional = (VueloNacional) vuelo;
            return vueloNacional.getAsientosDisponibles();
        } else {
            throw new RuntimeException("El vuelo no tiene acceso a los asientos");
        }
    }
    public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha){
        List<String> vuelosSimilares = new ArrayList<>();
        validacionFecha(Fecha);
        return vuelosSimilares;
    }
    
}
