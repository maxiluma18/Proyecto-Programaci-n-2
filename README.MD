# Aerolínea BondiJet

## Descripción del Proyecto
Este proyecto es un sistema de gestión para una aerolínea llamada **BondiJet**. Permite registrar aeropuertos, clientes, vuelos nacionales e internacionales, y gestionar la venta de pasajes. Además, incluye funcionalidades para vuelos privados y la consulta de vuelos similares.

---

## Estructura del Proyecto
El proyecto está compuesto por las siguientes clases e interfaces:

- **`IAerolinea`**: Interfaz que define los métodos que debe implementar la clase Aerolínea.
- **`Aerolinea`**: Implementación de la interfaz `IAerolinea`, que contiene la lógica del sistema.
- **`Principal`**: Clase principal que ejecuta el programa y muestra el funcionamiento del sistema.
- **`Aeropuerto`**: Clase que representa un aeropuerto.
- **`Cliente`**: Clase que representa un cliente de la aerolínea.
- **`Vuelo`**: Clase base para los vuelos.
  - **`VueloNacional`**: Clase que extiende `Vuelo` para representar vuelos nacionales.
  - **`VueloInternacional`**: Clase que extiende `Vuelo` para representar vuelos internacionales.
  - **`VueloPrivado`**: Clase que extiende `Vuelo` para representar vuelos privados.
- **`Pasaje`**: Clase que representa un pasaje vendido.

---

## Funcionalidades
1. **Registrar Aeropuertos**: Permite registrar aeropuertos nacionales e internacionales.
2. **Registrar Clientes**: Permite registrar clientes que pueden comprar pasajes.
3. **Registrar Vuelos**:
   - Vuelos nacionales.
   - Vuelos internacionales (con o sin escalas).
   - Vuelos privados.
4. **Vender Pasajes**: Permite la venta de pasajes para vuelos registrados.
5. **Consultar Vuelos Similares**: Permite consultar vuelos similares en una fecha determinada.
6. **Cancelar Pasajes**: Permite cancelar pasajes y liberar asientos.
7. **Cancelar Vuelos**: Permite cancelar un vuelo completo y reprogramar a los pasajeros.
8. **Consultar Detalle de Vuelos**: Permite obtener el detalle de un vuelo específico.
9. **Total Recaudado**: Permite consultar el total recaudado por vuelos a un destino específico.

---

## Ejemplo de Uso
El siguiente código muestra cómo se puede utilizar el sistema:

```java
public class Principal {
    public static void main(String[] args) {
        IAerolinea aerolinea = new Aerolinea("BondiJet", "30-12345678-9");

        // Registrar aeropuertos
        aerolinea.registrarAeropuerto("Aeroparque", "Argentina", "Buenos Aires", "Av. Costanera Rafael Obligado");
        aerolinea.registrarAeropuerto("Ezeiza", "Argentina", "Buenos Aires", "Autopista Riccheri km 33.5");

        // Registrar clientes
        aerolinea.registrarCliente(12345678, "Juan Perez", "011-1234-5678");

        // Registrar un vuelo nacional
        double[] preciosNacional = { 5000.0, 10000.0 };
        int[] cantAsientosNacional = { 150, 20 };
        String codVueloNacional = aerolinea.registrarVueloPublicoNacional(
            "Aeroparque", 
            "Bariloche", 
            "15/11/2025", 
            8, 
            5000, 
            preciosNacional, 
            cantAsientosNacional
        );

        // Vender un pasaje
        int codPasaje1 = aerolinea.venderPasaje(12345678, codVueloNacional, 5, true);
    }
}
```
## Requisitos
1. **Java 8 o superior.**
2. **IDE de Java** (Eclipse, IntelliJ, NetBeans, etc.) para compilar y ejecutar el proyecto.
3. **Librería de pruebas JUnit** para realizar pruebas unitarias.