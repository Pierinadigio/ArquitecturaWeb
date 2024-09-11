package implementaciones;
import entidades.Producto;
import interfaces.ProductoDAO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DerbyProductoDAO implements ProductoDAO {
    private final Connection connection;

    public DerbyProductoDAO(Connection connection) {
        this.connection = connection;
    }



    @Override
    public void addProducto(Producto producto) {
        String insertSQL = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, producto.getIdProducto());
            pstmt.setString(2, producto.getNombre());
            pstmt.setFloat(3, producto.getValor());
            pstmt.executeUpdate();
            System.out.println("Producto añadido a Derby.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo para cargar datos desde CSV
    public void loadProductosFromCSV(String csvFilePath) {
        String insertSQL = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ? , ?)";
        try (FileReader reader = new FileReader(csvFilePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("idProducto", "nombre", "valor").withIgnoreHeaderCase().withTrim());
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);

            for (CSVRecord record : parser) {
                int idProducto = Integer.parseInt(record.get("idProducto"));
                String nombre = record.get("nombre");
                float valor = Float.parseFloat(record.get("valor"));

                pstmt.setInt(1, idProducto);
                pstmt.setString(2, nombre);
                pstmt.setFloat(3, valor);

                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            System.out.println("Datos de Productos  cargados desde CSV a Derby.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

        @Override
        public List<Producto> getAllProductos() {
            String selectSQL = "SELECT * FROM Producto";
            List<Producto> productos = new ArrayList<>();
            try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idProducto = rs.getInt("idProducto");
                    String nombre = rs.getString("nombre");
                    float valor = rs.getFloat("valor");
                    productos.add(new Producto(idProducto, nombre, valor));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return productos;
        }

    public Producto getProductoConMayorRecaudacion() {
        String query = "SELECT p.idProducto, p.nombre, SUM(fp.cantidad * p.valor) AS total_recaudacion " +
                "FROM Producto p " +
                "JOIN Factura_Producto fp ON fp.idProducto = p.idProducto " +
                "GROUP BY p.idProducto, p.nombre " +
                "ORDER BY total_recaudacion DESC " +
                "FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int idProducto = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                float totalRecaudacion = rs.getFloat("total_recaudacion");
                return new Producto(idProducto, nombre, totalRecaudacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
        // Método para borrar la tabla
        public void dropTable()  {
            String sql = "DROP TABLE Producto";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }  catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
