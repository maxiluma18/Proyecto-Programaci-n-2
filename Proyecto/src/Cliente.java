public class Cliente {
    int dni;
    String nombre;
    String telefono;

    //constructor
    public Cliente(int dni, String nombre, String telefono){
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }
    public void registrarse(int dni, String nombre, String telefono){
        if (esCliente(dni)){
            System.out.println(dni + " ya esta registrado el dni de este cliente");
        }
        Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
        clientes.add(nuevoCliente);
        System.out.println(nuevoCliente + " registado");
    }

    public boolean esCliente(int dni){
        for(Cliente c: this.clientes){
            if(c.dni == dni){
                return true;
            }
        }
        return false;
    }
}
