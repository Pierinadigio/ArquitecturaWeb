package entidades;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    private float recaudacion;

    // Constructor
    public Cliente(int idCliente, String nombre, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
    }

    public Cliente(int idCliente, String nombre, Float recaudacion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.recaudacion = recaudacion;
    }
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getRecaudacion() {
        return recaudacion; }


}
