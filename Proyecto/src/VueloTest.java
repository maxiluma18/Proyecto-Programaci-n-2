import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VueloTest {
    public static void main(String[] args) {
        Aerolinea aerolinea = new Aerolinea("Aerolineas Argentina", "30-12345678-9");

        // Test 1: Registrar aeropuertos
        try {
            aerolinea.registrarAeropuerto("Ezeiza", "Argentina", "Buenos Aires", "Direccion 1");
            aerolinea.registrarAeropuerto("Aeroparque", "Argentina", "Buenos Aires", "Direccion 2");
            aerolinea.registrarAeropuerto("JFK", "Argentina", "Buenos Aires", "Direccion 2");
            System.out.println("Aeropuertos registrados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar aeropuertos: " + e.getMessage());
        }

        // Test 2: Registrar clientes
        try {
            aerolinea.registrarCliente(12345678, "Juan Perez", "123-4567");
            aerolinea.registrarCliente(87654321, "Maria Gomez", "987-6543");
            System.out.println("Clientes registrados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar clientes: " + e.getMessage());
        }

        // Test 3: Registrar vuelo nacional
        String codigoVueloNacional = null;
        try {
            double[] preciosNacional = { 1500.0, 2500.0 };
            int[] cantAsientosNacional = { 10, 20 };
            codigoVueloNacional = aerolinea.registrarVueloPublicoNacional("Ezeiza", "Aeroparque", "15/11/2024", 5,
                    500.0, preciosNacional, cantAsientosNacional);
            System.out.println("Vuelo nacional registrado con código: " + codigoVueloNacional);
        } catch (Exception e) {
            System.out.println("Error al registrar vuelo nacional: " + e.getMessage());
        }

        // Test 4: Registrar vuelo internacional
        String codigoVueloInternacional = null;
        try {
            double[] preciosInternacional = { 3000.0, 4500.0, 6000.0 };
            int[] cantAsientosInternacional = { 15, 25, 30 };
            String[] escalas = { "GRU", "JFK" };
            codigoVueloInternacional = aerolinea.registrarVueloPublicoInternacional("Ezeiza", "JFK", "20/11/2024", 6,
                    700.0, 2, preciosInternacional, cantAsientosInternacional, escalas);
            System.out.println("Vuelo internacional registrado con código: " + codigoVueloInternacional);
        } catch (Exception e) {
            System.out.println("Error al registrar vuelo internacional: " + e.getMessage());
        }

        // Test 5: Consultar asientos disponibles en vuelo nacional
        try {
            Map<Integer, String> asientosDisponibles = aerolinea.asientosDisponibles(codigoVueloNacional);
            System.out.println("Asientos disponibles en el vuelo nacional: " + asientosDisponibles);
        } catch (Exception e) {
            System.out.println("Error al consultar asientos disponibles: " + e.getMessage());
        }

        // Test 6: Vender pasaje en vuelo nacional
        try {
            int resultado = aerolinea.venderPasaje(12345678, codigoVueloNacional, 1, true);
            System.out.println("Pasaje vendido exitosamente, asiento asignado: " + resultado);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Error al vender pasaje: " + e.getMessage());
        }

        // Test 7: Cancelar vuelo nacional y verificar reprogramación de pasajeros
        try {
            List<String> pasajerosReprogramados = aerolinea.cancelarVuelo(codigoVueloNacional);
            System.out.println("Pasajeros reprogramados: " + pasajerosReprogramados);
        } catch (Exception e) {
            System.out.println("Error al cancelar vuelo: " + e.getMessage());
        }
        double Total = aerolinea.totalRecaudado(codigoVueloNacional);
        System.out.println("DINERO TOTAL" + Total);

    }
}
