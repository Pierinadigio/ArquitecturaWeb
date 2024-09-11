package interfaces;
import entidades.Cliente;

import java.util.List;

public interface ClienteDAO {
    void addCliente(Cliente cliente);
    List<Cliente> getAllClientes();
    List<Cliente> getClientesPorFacturacion();
    void dropTable();
}
