import java.util.List;
import java.util.Map;

/**
 * @author PII
 *
 */

public interface IAerolinea {
/**
	* 1 constructor
	* Aerolinea(String nombre, String CUIT);
*/

	/**
	* - 2
	* Se registran los clientes de la Aerolínea, compren o no pasaje. 
	* Cuando un cliente compre un pasaje es un Cliente (pasajero) y queda registrado en el vuelo correspondiente.
    */
	void registrarCliente(int dni, String nombre, String telefono);	
	
	/**
	* - 3 
	* Se ingresa un aeropuerto con los datos que lo identifican. Estos aeropuertos son los que deberán corresponder
	* al origen y destino de los vuelos.
	* El nombre es único por aeropuerto en todo el mundo.
	*/
	void registrarAeropuerto(String nombre, String pais, String provincia, String direccion);

	/** - 4
	* El origen y destino deben ser aeropuertos con país=”Argentina” y ya registrados en la aerolinea. 
	* La fecha es la fecha de salida del vuelo.
	* Los asientos se considerarán numerados correlativamente empezando con clase Turista y terminando con la clase Ejecutivo.
	* Se cumple que precios.length == cantAsientos.length == 2
	* -  cantAsientos[0] = cantidad total de asientos en clase Turista.
	* -  cantAsientos[1] = cantidad total de asientos en clase Ejecutivo
	*   idem precios.
	* Tripulantes es la cantidad de tripulantes del vuelo.
	* valorRefrigerio es el valor del unico refrigerio que se sirve en el vuelo.
	* 
	* Devuelve el código del Vuelo con el formato: {Nro_vuelo_publico}-PUB. Por ejemplo--> 103-PUB
	* Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	*/
	String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, 
			double[] precios, int[] cantAsientos);


	/** - 5
	* Pueden ser vuelos con escalas o sin escalas. 
	* La fecha es la de salida y debe ser posterior a la actual.  
	* Los asientos se considerarán numerados correlativamente empezando con clase Turista, siguiendo por Ejecutiva 
	* y terminando con Primera clase.
	* 
	* precios.length == cantAsientos.llength == 3
	*    - cantAsientos[0]  = cantidad total de asientos en clase Turista.
	*    - cantAsientos[1]  = cantidad total de asientos en clase Ejecutiva.
	*    - cantAsientos[2]  = cantidad total de asientos en Primera clase.
	*	 idem precios.
	*	 - escalas = nombre del aeropuerto donde hace escala. Si no tiene escalas, esto es un arreglo vacío.
	* Tripulantes es la cantidad de tripulantes del vuelo. 
	* valorRefrigerio es el valor del refrigerio que se sirve en el vuelo.
	* cantRefrigerios es la cantidad de refrigerio que se sirven en el vuelo.
	*
	* Devuelve el código del vuelo.  Con el formato: {Nro_vuelo_publico}-PUB, por ejemplo--> 103-PUB
	* Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	*/
	String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, 
			int cantRefrigerios, double[] precios,  int[] cantAsientos,  String[] escalas);

	
	/** 6 y 10 **** Se reune en esta firma ambos puntos de la especificación.
	 * 
	* Origen y destino son los Aeropuertos de donde parte y al que llega el jet. 
	* Fecha es la fecha de salida y debe ser posterior a la fecha actual.
	* Tripulantes es la cantidad de tripulantes del vuelo. 
	* Precio es el de un(1) jet. 
	* Se supone que se cuenta con todos los jets necesarios para trasladar todos los acompañantes. 
	* Se usara la cantidad de jets (necesarios) para el calculo del costo total del Vuelo.
	* IMPORTANTE; Se toma un sólo código para la compra aunque se necesiten mas de un jet. 
	* No se sirven refrigerios
	* 
	* Devuelve el código del vuelo. Con el formato: {Nro_vuelo_privado}-PRI, por ejemplo: 103-PRI
	*/
	String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,  int dniComprador, int[] acompaniantes);

	/** - 7 
	*  Dado el código del vuelo, devuelve un diccionario con los asientos aún disponibles para la venta 
	*  --> clave:  el número de asiento
	*  --> valor:  la sección a la que pertenecen los asientos.
	*  
	*/
	Map<Integer, String> asientosDisponibles(String codVuelo);
		
	/**
	* 8 y 9 devuelve el codigo del pasaje comprado.
	* Los pasajeros que compran pasajes deben estar registrados como clientes, con todos sus datos, antes de realizar la compra. Devuelve el codigo del pasaje y lanza una excepción si no puede venderlo.
	* aOcupar indica si el asiento que será ocupado por el cliente, o si solo lo compro para viajar más cómodo.
	* Devuelve un código de pasaje único que se genera incrementalmente sin distinguir entre tipos de vuelos.
	*/
	int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar);

	/** - 11. 
	 * devuelve una lista de códigos de vuelos. que estén entre fecha dada y hasta una semana despues. La lista estará vacía si no se encuentran vuelos similares. La Fecha es la fecha de salida.
	*/
	List<String> consultarVuelosSimilares(String origen, String destino, String Fecha);
		
	/** - 12 
	 * Hay 2 posibles firmas para implementar esto: 12A  Y  12B
	 */
	
	/** - 12-A 
	* Se borra el pasaje y se libera el lugar para que pueda comprarlo otro cliente.
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	void cancelarPasaje(int dni, String codVuelo, int nroAsiento);
	
	/** 12-B
	* Se cancela un pasaje dado el codigo de pasaje. 
	* NO es necesario que se resuelva en O(1).
	*/
	void cancelarPasaje(int dni, int codPasaje);

	/** - 13
	* Cancela un vuelo completo conociendo su codigo.
	* Los pasajes se reprograman a vuelos con igual destino, no importa el numero del asiento pero 
	* si a igual seccion o a una mejor, y no importan las escalas.
	* Devuelve los codigos de los pasajes que no se pudieron reprogramar.
	* Los pasajes no reprogramados se eliminan. Y se devuelven los datos de la cancelación, indicando 
	* los pasajeros que se reprogramaron y a qué vuelo,  y los que se cancelaron por no tener lugar.
	* Devuelve una lista de Strings con este formato : “dni - nombre - telefono - [Codigo nuevo vuelo|CANCELADO]”
	* --> Ejemplo: 
	*   . 11111111 - Juan - 33333333 - CANCELADO
	*   . 11234126 - Jonathan - 33333311 - 545-PUB
	*/
	List<String> cancelarVuelo(String codVuelo);
		
	/** - 14
	* devuelve el total recaudado por todos los viajes al destino pasado por parámetro. 
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	double totalRecaudado(String destino);

	/** - 15 
	* Detalle de un vuelo
	* devuelve un texto con el detalle un vuelo en particular.
	* Formato del String: CodigoVuelo - Nombre Aeropuerto de salida - Nombre Aeropuerto de llegada - 
	*                     fecha de salida - [NACIONAL /INTERNACIONAL / PRIVADO + cantidad de jets necesarios].
	* --> Ejemplo:
	*   . 545-PUB - Bariloche - Jujuy - 10/11/2024 - NACIONAL
	*   . 103-PUB - Ezeiza  - Madrid -  15/11/2024 - INTERNACIONAL
	*   . 222-PRI - Ezeiza - Tierra del Fuego - 3/12/2024 - PRIVADO (3)
	*/
	String detalleDeVuelo(String codVuelo);
}