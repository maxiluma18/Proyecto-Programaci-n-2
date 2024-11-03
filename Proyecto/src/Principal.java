import java.util.List;
import java.util.Map;

public class Principal {
        public static void main(String[] args) {

                // Crear la Aerolínea
                IAerolinea aerolinea = new Aerolinea("BondiJet", "30-12345678-9");

                // Mostrar el estado general del sistema
                System.out.println("\nEstado general del sistema:");
                System.out.println(aerolinea);

                // Registrar aeropuertos nacionales (Argentina)
                aerolinea.registrarAeropuerto("Aeroparque", "Argentina", "Provincia de Buenos Aires",
                                "Av. Costanera Rafael Obligado");
                aerolinea.registrarAeropuerto("Ezeiza", "Argentina", "Provincia de Buenos Aires",
                                "Autopista Riccheri km 33.5");
                aerolinea.registrarAeropuerto("Bariloche", "Argentina", "Provincia de Río Negro", "Ruta Nacional 237");

                // Registrar aeropuertos internacionales (Europa y América)
                aerolinea.registrarAeropuerto("Charles de Gaulle", "Francia", "Departamento de Val-d'Oise",
                                "95700 Roissy-en-France");
                aerolinea.registrarAeropuerto("JFK", "Estados Unidos", "Estado de Nueva York", "Queens, NY 11430");
                aerolinea.registrarAeropuerto("Guarulhos", "Brasil", "São Paulo",
                                "Rod. Hélio Smidt, s/n - Cumbica, Guarulhos");

                // Registrar clientes
                aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");
                aerolinea.registrarCliente(87654321, "Ana Lopez", "011-8765-4321");

                // Mostrar el estado general del sistema
                System.out.println("\nEstado general del sistema:");
                System.out.println(aerolinea);

                // Registrar un vuelo nacional
                double[] preciosNacional = { 5000.0, 10000.0 };
                int[] cantAsientosNacional = { 150, 20 };
                String codVueloNacional = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche",
                                "15/11/2024", 8,
                                5000, preciosNacional, cantAsientosNacional);

                // Registrar un vuelo internacional con escalas
                double[] preciosInternacional = { 20000.0, 40000.0, 60000.0 };
                int[] cantAsientosInternacional = { 200, 50, 10 };
                String[] escalas = { "Guarulhos", "JFK" };
                String codVueloInternacional = aerolinea.registrarVueloPublicoInternacional("Ezeiza",
                                "Charles de Gaulle",
                                "20/11/2024", 12, 6000, 3, preciosInternacional, cantAsientosInternacional, escalas);

                // Vender pasajes
                int codPasaje1 = aerolinea.venderPasaje(12345678, codVueloNacional, 5, true);
                int codPasaje2 = aerolinea.venderPasaje(87654321, codVueloInternacional, 1,
                                false);

                // Mostrar estado del sistema
                System.out.println("===== ESTADO DEL SISTEMA =====");
                System.out.println("Vuelo Nacional Registrado: " + codVueloNacional);
                System.out.println("Vuelo Internacional Registrado: " + codVueloInternacional);
                System.out.println("Pasaje 1 vendido: " + codPasaje1);
                System.out.println("Pasaje 2 vendido: " + codPasaje2);

                // Mostrar detalle de un vuelo
                // String detalleVueloNacional = aerolinea.detalleDeVuelo(codVueloNacional);
                // System.out.println("Detalle del vuelo nacional: " + detalleVueloNacional);

                // String detalleVueloInternacional =
                // aerolinea.detalleDeVuelo(codVueloInternacional);
                // System.out.println("Detalle del vuelo internacional: " +
                // detalleVueloInternacional);

                // Mostrar asientos disponibles de un vuelo
                Map<Integer, String> asientosDisponibles = aerolinea.asientosDisponibles(codVueloNacional);
                System.out.println(
                                "Asientos disponibles para el vuelo " + codVueloNacional + ": " +
                                                asientosDisponibles);

                // Cancelar un pasaje
                aerolinea.cancelarPasaje(12345678, codVueloNacional, 5);
                System.out.println("Pasaje 1 cancelado. Asientos disponibles tras la cancelación: "
                                + aerolinea.asientosDisponibles(codVueloNacional));

                // Consultar vuelos similares
                List<String> vuelosSimilares = aerolinea.consultarVuelosSimilares("Aeroparque", "Bariloche",
                                "15/11/2024");
                System.out.println("Vuelos similares al 15/11/2024 de Aeroparque a Bariloche: " + vuelosSimilares);

                // >>>>>>>> simulacion vuelo privado
                // Registrar cliente principal que contratará el vuelo privado
                aerolinea.registrarCliente(98765432, "Carlos Sanchez", "011-2345-6789");

                // Registrar 39 acompañantes
                int[] acompaniantes = new int[39]; // Se simula que viaja con 39 acompañantes
                for (int i = 0; i < acompaniantes.length; i++) {
                        acompaniantes[i] = 10000000 + i; // Simplemente como ejemplo
                        aerolinea.registrarCliente(acompaniantes[i], "Acompañante " + (i + 1),
                                        "011-1234-" + (5670 + i)); // Registrar
                                                                   // acompañante
                }

                // El cliente compra un vuelo privado de Aeroparque a Bariloche para 40 personas
                String fechaVueloPrivado = "10/11/2024";
                double precioVueloPrivado = 500000.0;

                // Vender el vuelo privado
                String codVueloPrivado = aerolinea.VenderVueloPrivado("Aeroparque", "Bariloche", fechaVueloPrivado, 4,
                                precioVueloPrivado, 98765432, acompaniantes);

                // Detalle del vuelo privado
                String detalleVueloPrivado = aerolinea.detalleDeVuelo(codVueloPrivado);
                System.out.println("Detalle del vuelo privado (3 jets): " + detalleVueloPrivado);
                boolean error = false;
                try {
                        // Consultar asientos disponibles del vuelo privado debe generar un error.
                        aerolinea.asientosDisponibles(codVueloPrivado);
                        error = true;
                } catch (RuntimeException e) {
                        // debería pasar por acá el codigo.
                        // porque asientoDisponible debería lanzar una excepcion
                }
                if (error)
                        throw new RuntimeException(
                                        "Consultar asientos disponibles del vuelo privado debe generar un error.");

                // <<<<<<<< simulacion vuelo privado

                System.out.println(
                                "Total recaudado en vuelos a 'Charles de Gaulle': "
                                                + aerolinea.totalRecaudado("Charles de Gaulle"));
                System.out.println("Total recaudado en vuelos a 'Bariloche': " + aerolinea.totalRecaudado("Bariloche"));

                // Mostrar el estado general del sistema
                System.out.println("\nEstado general del sistema:");
                System.out.println(aerolinea);
        }
}
