/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Héctor Rafael Orozco Aguirre
 */
public class Compras {
    private Connection connection;
    private ResultSet rs;
    private PreparedStatement ps;
    
    public Compras() {
        connection = null;
        rs = null;
        ps = null;
    }
    
    public void conecta() {    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/Compras?serverTimezone=UTC", "root", "root");
            System.out.println("Conexión exitosa a la base de datos Compras en MySQL 8");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error al establecer conexión con la base de datos Compras en MySQL 8");
            System.err.println(ex.getMessage());
        }        
    }
    
    public void consulta(String tabla) {
        int i;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + tabla);
            rs = ps.executeQuery();
            System.out.println("Registros almacenados en la tabla: " + tabla);            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(i = 1; i <= columnCount; i++ ) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println("");            
            while(rs.next()) {
                for(i = 1; i <= columnCount; i++ ) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar registros de la tabla " + tabla);
            System.err.println(ex.getMessage());
        }        
    }
    
    public void inserta(String tabla, String[] campos, String[] valores) {
        int i;
        try {
            Statement st = connection.createStatement();
            String sqlInsert = "INSERT INTO " + tabla + "(";
            for(i = 0; i < campos.length - 1; i++) {
                sqlInsert += campos[i] + ", ";
            }
            sqlInsert += campos[i] + ") VALUES ('";
            for(i = 0; i < valores.length - 1; i++) {
                sqlInsert += valores[i] + "', '";
            }
            sqlInsert += valores[i] + "')";
            st.executeUpdate(sqlInsert);
            System.out.println("Registro insertado en la tabla " + tabla);
        } catch (SQLException ex) {
            System.err.println("Error al insertar datos en la tabla " + tabla);
            System.err.println(ex.getMessage());
        }
    }
    
    public void actualiza(String tabla, String campos[], String valores[], String idValue) {
        int i;
        try {
            Statement st = connection.createStatement();
            String sqlUpdate = "UPDATE " + tabla + " SET ";
            for(i = 0; i < campos.length - 1; i++) {
                sqlUpdate += campos[i] + "= '" + valores[i] + "', ";
            }
            sqlUpdate += campos[campos.length - 1] + "= '" + valores[campos.length - 1] + "' ";
            sqlUpdate += "WHERE " + campos[0] + " = '" + idValue + "'";
            System.out.println("sqlUpdate " + sqlUpdate);
            st.executeUpdate(sqlUpdate);
            System.out.println("Registro modificado en la tabla " + tabla);
        } catch (SQLException ex) {
            System.err.println("Error al modificar datos en la tabla " + tabla);
            System.err.println(ex.getMessage());
        }
    }
    
    public void elimina(String tabla, String idField, String idValue) {
        try {
            Statement st = connection.createStatement();
            String sqlDelete = "DELETE FROM " + tabla + " WHERE "
                               + idField + " = '" + idValue + "'";
            System.out.println("sqlDelete " + sqlDelete);
            st.executeUpdate(sqlDelete);
            System.out.println("Registro eliminado en la tabla " + tabla);
        } catch (SQLException ex) {
            System.err.println("Error al eliminar datos en la tabla " + tabla);
            System.err.println(ex.getMessage());
        }
    }
    
    public void cierra() {
        try {
            connection.close(); 
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión con la base de datos Compras en MySQL 8");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Compras compras = new Compras();
        compras.conecta();
        compras.consulta("Producto");
        String campos[] = {"idProducto", "nombre", "descripcion", "precioUnitario", "existencias", "idCategoriaProducto"};
        String valores[] = {"3", "Escultura de perro azteca", "Escultura en cobre del clásico perro azteca", "250.15", "999", "1"};
        compras.inserta("Producto", campos, valores);
        compras.consulta("Producto");
        campos = new String[]{"idProducto", "nombre", "descripcion", "precioUnitario"};
        valores = new String[]{"3", "Escultura en miniatura de perro azteca", "Escultura mini en bronce del clásico perro azteca", "295.85"};
        compras.actualiza("Producto", campos, valores, "3");
        compras.consulta("Producto");
        compras.elimina("Producto", "idProducto", "3");
        compras.consulta("Producto");
        compras.cierra();
    }    
}
