import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AerolineaTest {

	private Aerolinea aerolinea;

	@Before
	public void setUp() {
		aerolinea = new Aerolinea("AeroArg", "30-12345678-9");

		// Registrar aeropuertos nacionales (Argentina)
		aerolinea.registrarAeropuerto("Aeroparque", "Argentina", "Buenos Aires", "Av. Costanera Rafael Obligado");
		aerolinea.registrarAeropuerto("Ezeiza", "Argentina", "Buenos Aires", "Autopista Riccheri km 33.5");
		aerolinea.registrarAeropuerto("Cordoba", "Argentina", "Córdoba", "Ruta Nacional 20");
		aerolinea.registrarAeropuerto("Mendoza", "Argentina", "Mendoza", "Ruta Nacional 40");
		aerolinea.registrarAeropuerto("Bariloche", "Argentina", "Río Negro", "Ruta Nacional 237");
		aerolinea.registrarAeropuerto("Salta", "Argentina", "Salta", "Ruta Nacional 51");
		aerolinea.registrarAeropuerto("Iguazu", "Argentina", "Misiones", "Ruta Nacional 12");
		aerolinea.registrarAeropuerto("Neuquen", "Argentina", "Neuquén", "Ruta Nacional 22");
		aerolinea.registrarAeropuerto("Ushuaia", "Argentina", "Tierra del Fuego", "Av. Maipú 500");
		aerolinea.registrarAeropuerto("Tucuman", "Argentina", "Tucumán", "Ruta Nacional 9");

		// Registrar aeropuertos internacionales (Europa y América)
		aerolinea.registrarAeropuerto("Charles de Gaulle", "Francia", "Val-d'Oise", "95700 Roissy-en-France");
		aerolinea.registrarAeropuerto("JFK", "Estados Unidos", "Nueva York", "Queens, NY 11430");
		aerolinea.registrarAeropuerto("Guarulhos", "Brasil", "São Paulo", "Rod. Hélio Smidt, s/n - Cumbica, Guarulhos");
		aerolinea.registrarAeropuerto("Jorge Chavez", "Perú", "Callao", "Av. Elmer Faucett s/n");
		aerolinea.registrarAeropuerto("Barajas", "España", "Madrid", "28042 Madrid");
		aerolinea.registrarAeropuerto("Fiumicino", "Italia", "Región del Lacio", "Via dell'Aeroporto di Fiumicino");
		aerolinea.registrarAeropuerto("Heathrow", "Reino Unido", "Greater London", "Longford TW6");
	}

	@Test
	public void ej02_registrarCliente_clienteRegistrado() {
		aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");
		// No hay excepciones, por lo tanto, test pasa
	}

	@Test(expected = RuntimeException.class)
	public void ej02_registrarClienteYaRegistrado_exception() {
		aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");
		aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");
		// No hay excepciones, por lo tanto, test pasa
	}

	@Test(expected = RuntimeException.class)
	public void ej03_registrarAeropuertoExistente_exception() {
		aerolinea.registrarAeropuerto("Mendoza", "Argentina", "Mendoza", "Ruta Nacional 40");
	}

	@Test(expected = RuntimeException.class)
	public void ej04_registrarVueloPublicoNacional_destinoNoRegistrado_exception() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		aerolinea.registrarVueloPublicoNacional("Aeroparque", "Jujuy", "15/12/2025", 6, 2000, precios, cantAsientos);
	}

	@Test
	public void ej04_registrarVueloPublicoNacional_vueloRegistrado() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 6, 2000,
				precios, cantAsientos);
		assertNotNull(codVuelo);
		assertTrue(codVuelo.endsWith("-PUB"));
	}

	@Test
	public void ej05_registrarVueloPublicoInternacional_sinEscalas_vueloRegistrado() {
		double[] precios = { 15000.0, 30000.0, 50000.0 };
		int[] cantAsientos = { 200, 30, 10 };
		String[] escalas = {};
		String codVuelo = aerolinea.registrarVueloPublicoInternacional("Ezeiza", "Barajas", "10/01/2025", 8, 2000, 3,
				precios,
				cantAsientos, escalas);
		assertNotNull(codVuelo);
		assertTrue(codVuelo.endsWith("-PUB"));
	}

	@Test
	public void ej05_registrarVueloPublicoInternacional_conEscalas_vueloRegistrado() {
		double[] precios = { 15000.0, 30000.0, 50000.0 };
		int[] cantAsientos = { 200, 30, 10 };
		String[] escalas = { "JFK", "Charles de Gaulle" };
		String codVuelo = aerolinea.registrarVueloPublicoInternacional("Ezeiza", "Barajas", "15/01/2025", 8, 2000, 3,
				precios, cantAsientos, escalas);
		assertNotNull(codVuelo);
		assertTrue(codVuelo.endsWith("-PUB"));
	}

	@Test(expected = RuntimeException.class)
	public void ej06_venderVueloPrivado_fechaAnterior_exception() {
		int[] acompaniantes = { 12345679, 12345680 };
		aerolinea.VenderVueloPrivado("Ezeiza", "Tierra del Fuego", "10/10/2023", 5, 500000.0, 12345678, acompaniantes);
	}

	@Test
	public void ej06_venderVueloPrivado_vueloRegistrado() {
		int[] acompaniantes = { 12345679, 12345680 };
		aerolinea.registrarCliente(12345678, "Athos", "011-2345-6789");
		aerolinea.registrarCliente(12345679, "Porthos", "011-2345-6779");
		aerolinea.registrarCliente(12345680, "Aramis", "011-2345-6769");

		String codVuelo = aerolinea.VenderVueloPrivado("Ezeiza", "Ushuaia", "10/12/2025", 5, 500000.0,
				12345678, acompaniantes);
		assertNotNull(codVuelo);
		assertTrue(codVuelo.endsWith("-PRI"));
	}

	@Test
	public void ej07_asientosDisponibles_vueloExistente_asientosDisponibles() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/01/2025", 5, 2500,
				precios, cantAsientos);
		Map<Integer, String> asientos = aerolinea.asientosDisponibles(codVuelo);
		assertNotNull(asientos);
		assertEquals(170, asientos.size());
	}

	@Test(expected = RuntimeException.class)
	public void ej08_venderPasaje_clienteNoRegistrado_exception() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 5, 2000,
				precios, cantAsientos);
		aerolinea.venderPasaje(87654321, codVuelo, 1, true);
	}

	@Test
	public void ej08_venderPasaje_clienteRegistrado_pasajeVendido() {
		aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 6, 2000,
				precios, cantAsientos);
		int codPasaje = aerolinea.venderPasaje(12345678, codVuelo, 1, true);
		assertTrue(codPasaje > 0);
	}

	@Test
	public void ej11_consultarVuelosSimilares_vuelosNoEncontrados() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 5, 3000, precios,
				cantAsientos);
		List<String> vuelos = aerolinea.consultarVuelosSimilares("Aeroparque", "Bariloche", "10/12/2025");
		assertNotNull(vuelos);
		assertFalse(vuelos.isEmpty());
	}

	@Test
	public void ej13_cancelarVuelo_cambiaDeVueloAlPasajero_devuelveListadoConElCodVuelo() {
		int dni = 12345678;
		String nombre = "Juan Perez";
		String telefono = "011-1234-5678";
		aerolinea.registrarCliente(dni, nombre, telefono);
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 6, 2000,
				precios, cantAsientos);
		int codPasaje = aerolinea.venderPasaje(dni, codVuelo, 1, true);
		assertTrue(codPasaje > 0);

		String codVueloNuevo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "16/12/2025", 6, 2000,
				precios, cantAsientos);

		String registroEsperado = String.format("%d - %s - %s - %s", dni, nombre, telefono, codVueloNuevo);
		List<String> resultado = aerolinea.cancelarVuelo(codVuelo);

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(registroEsperado, resultado.get(0));
	}

	@Test
	public void ej14_totalRecaudado_sinVuelo_OK() {
		assertEquals(0.0, aerolinea.totalRecaudado("Bariloche"), 0.1);
	}

	@Test
	public void ej14_totalRecaudado_venderSoloUnVueloPrivado_OK() {
		aerolinea.registrarCliente(98765432, "Carlos Sanchez", "011-2345-6789");

		// Registrar 39 acompañantes
		int[] acompaniantes = new int[39]; // Se simula que viaja con 39 acompañantes
		for (int i = 0; i < acompaniantes.length; i++) {
			acompaniantes[i] = 10000000 + i; // Simplemente como ejemplo
			aerolinea.registrarCliente(acompaniantes[i], "Acompañante " + (i + 1), "011-1234-" + (5670 + i)); // Registrar
																												// acompañante
		}

		// El cliente compra un vuelo privado de Aeroparque a Bariloche para 40 personas
		String fechaVueloPrivado = "10/11/2025";
		double precioVueloPrivado = 500000.0;
		// porque Necesita 3 aviones para poder llevar a los 40 pasajeros y se le suma
		// el 30% de impuestos.
		double recaudacionEsperada = 1950000.0;

		// Vender el vuelo privado
		String codVueloPrivado = aerolinea.VenderVueloPrivado("Aeroparque", "Bariloche", fechaVueloPrivado, 4,
				precioVueloPrivado, 98765432, acompaniantes);

		assertEquals(recaudacionEsperada, aerolinea.totalRecaudado("Bariloche"), 0.1);
	}

	@Test
	public void ej14_totalRecaudado_venderSoloUnVueloInternacional_OK() {
		double[] precios = { 15000.0, 30000.0, 50000.0 };
		int[] cantAsientos = { 100, 30, 10 };
		String[] escalas = { "JFK", "Charles de Gaulle" };
		String codVuelo = aerolinea.registrarVueloPublicoInternacional("Ezeiza", "Barajas", "15/12/2025", 8, 2000, 3,
				precios, cantAsientos, escalas);

		for (int i = 1; i < 141; i++) {
			int dni = 10000000 + i; // Simplemente como ejemplo
			aerolinea.registrarCliente(dni, "Pasajero " + (i + 1), "011-1234-" + (5670 + i)); // Registrar acompañante
			aerolinea.venderPasaje(dni, codVuelo, i, false);
		}

		// Al estar vendido por completo,
		// se suman el costo de todos los asiento que cambia segun la sección..
		// mas el costo de los refrigerios para todos los pasajeros
		// 140 pasajeros * 3 refrigerios * 2000 cada refrigerio
		// y por ultimo sumo el 20 porciento de impuestos.
		double recaudacionEsperada = 4488000.0;
		assertEquals(recaudacionEsperada, aerolinea.totalRecaudado("Barajas"), 0.1);
	}

	@Test
	public void ej15_detalleVuelo_OK() {
		double[] precios = { 5000.0, 10000.0 };
		int[] cantAsientos = { 150, 20 };
		String codVuelo = aerolinea.registrarVueloPublicoNacional("Aeroparque", "Bariloche", "15/12/2025", 6, 2000,
				precios, cantAsientos);

		String esperado = String.format("%s - Aeroparque - Bariloche - 15/12/2025 - NACIONAL", codVuelo);
		assertEquals(esperado, aerolinea.detalleDeVuelo(codVuelo));

		// ----------------------

		double[] preciosInt = { 15000.0, 30000.0, 50000.0 };
		int[] cantAsientosInt = { 200, 30, 10 };
		String[] escalas = {};
		String codVueloInt = aerolinea.registrarVueloPublicoInternacional("Ezeiza", "Barajas", "10/02/2025", 8, 2000, 3,
				preciosInt,
				cantAsientosInt, escalas);

		String esperadoInt = String.format("%s - Ezeiza - Barajas - 10/02/2025 - INTERNACIONAL", codVueloInt);
		assertEquals(esperadoInt, aerolinea.detalleDeVuelo(codVueloInt));

		// -----------------------

		aerolinea.registrarCliente(98765432, "Carlos Sanchez", "011-2345-6789");
		int[] acompaniantes = {};
		String codVueloPrivado = aerolinea.VenderVueloPrivado("Aeroparque", "Bariloche", "07/01/2025", 4, 450000,
				98765432, acompaniantes);

		String esperadoPrivado = String.format("%s - Aeroparque - Bariloche - 07/01/2025 - PRIVADO (1)",
				codVueloPrivado);
		assertEquals(esperadoPrivado, aerolinea.detalleDeVuelo(codVueloPrivado));
	}

}
