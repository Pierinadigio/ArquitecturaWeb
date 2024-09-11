package implementaciones;
import entidades.Factura;

import interfaces.FacturaDAO;
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

public class MySQLFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MySQLFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    /*
    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Factura (idFactura INT, idCliente INT, PRIMARY KEY(idFactura))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Tabla Factura creada en MySQL con exito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void addFactura(Factura factura) {
        String insertSQL = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, factura.getIdFactura());
            pstmt.setInt(2, factura.getIdCliente());
            pstmt.executeUpdate();
            System.out.println("Factura añadida a MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo para cargar datos desde CSV

    @Override
    public List<Factura> getAllFacturas() {
        String selectSQL = "SELECT * FROM Factura";
        List<Factura> facturas = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idFactura = rs.getInt("idFactura");
                int idCliente = rs.getInt("idCliente");
                facturas.add(new Factura(idFactura, idCliente));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturas;
    }

    // Método para borrar la tabla
    public void dropTable()  {
        String sql = "DROP TABLE IF EXISTS Factura";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
