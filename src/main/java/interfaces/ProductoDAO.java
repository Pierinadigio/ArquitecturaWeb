package interfaces;
import entidades.Producto;
import java.util.List;

public interface ProductoDAO {
    void addProducto(Producto p);
    List<Producto> getAllProductos();
    Producto getProductoConMayorRecaudacion();
    void dropTable();
}
