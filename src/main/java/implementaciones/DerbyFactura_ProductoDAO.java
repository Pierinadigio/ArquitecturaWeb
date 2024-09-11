package implementaciones;
import entidades.Factura_Producto;
import interfaces.Factura_ProductoDAO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DerbyFactura_ProductoDAO implements Factura_ProductoDAO {
    private final Connection connection;

    public DerbyFactura_ProductoDAO(Connection connection) {
        this.connection = connection;
    }



    @Override
    public void addFacturaProducto(Factura_Producto fp) {
        String insertSQL = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, fp.getIdFactura());
            pstmt.setInt(2, fp.getIdProducto());
            pstmt.setInt(3, fp.getCantidad());
            pstmt.executeUpdate();
            System.out.println("Factura-Producto agregada con exito to Derby.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo para cargar datos desde CSV
    public void loadFacturas_ProductosFromCSV(String csvFilePath) {
        String insertSQL = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (FileReader reader = new FileReader(csvFilePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("idFactura", "idProducto", "cantidad").withIgnoreHeaderCase().withTrim());
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);

            for (CSVRecord record : parser) {
                int idFactura = Integer.parseInt(record.get("idFactura"));
                int idProducto = Integer.parseInt(record.get("idProducto"));
                int cantidad = Integer.parseInt(record.get("cantidad"));

                pstmt.setInt(1, idFactura);
                pstmt.setInt(2, idProducto);
                pstmt.setInt(3, cantidad);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            connection.commit();
            System.out.println("Datos de Facturas_Productos  cargados desde CSV a Derby.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Factura_Producto> getAllFacturasProductos() {
        String selectSQL = "SELECT * FROM Factura_Producto";
        List<Factura_Producto> factura_productos = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idFactura = rs.getInt("idFactura");
                int idProducto = rs.getInt("idProducto");
                int cantidad = rs.getInt("cantidad");
                factura_productos.add(new Factura_Producto(idFactura, idProducto, cantidad));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factura_productos;
    }

    // Método para borrar la tabla
    public void dropTable()  {
        String sql = "DROP TABLE Factura_Producto";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

