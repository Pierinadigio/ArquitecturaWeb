package implementaciones;
import entidades.Cliente;
import interfaces.ClienteDAO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DerbyClienteDAO implements ClienteDAO {
    private final Connection connection;

    public DerbyClienteDAO(Connection connection) {
        this.connection = connection;
    }


    public void addCliente(Cliente c) {
        String insertSQL = "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, c.getIdCliente());
            pstmt.setString(2, c.getNombre());
            pstmt.setString(3, c.getEmail());
            pstmt.executeUpdate();
            System.out.println("Tabla Cliente creada con exito en Derby.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Cliente> getAllClientes() {
        String selectSQL = "SELECT * FROM Cliente";
        List<Cliente> customers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idCliente = rs.getInt("idCliente");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                customers.add(new Cliente(idCliente, nombre, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    //Punto 4 Integrador1
    public List<Cliente> getClientesPorFacturacion() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT c.idCliente, c.nombre, SUM(fp.cantidad * p.valor) AS recaudacion " +
                "FROM Cliente c " +
                "JOIN Factura f ON c.idCliente = f.idCliente " +
                "JOIN Factura_Producto fp ON f.idFactura = fp.idFactura " +
                "JOIN Producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY c.idCliente, c.nombre " +
                "ORDER BY recaudacion DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idCliente = rs.getInt("idCliente");
                String nombre = rs.getString("nombre");
                float recaudacion = rs.getFloat("recaudacion");

                Cliente c = new Cliente(idCliente, nombre, recaudacion);
                clientes.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Método para borrar la tabla
    public void dropTable()  {
        String sql = "DROP TABLE Cliente";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
