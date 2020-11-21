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
        compras.consulta("fabricante");
        System.out.println("------------------------------------------------------------\n");
        
        String camposFabricante[] = {"idFabricante","nombre","direccion","telefono","email","paginaWeb","contacto","RFC"};
        String valoresFabricante[] = {"1","Troncoso","Naucalpan, Mex", "55555555","troncoso@mail.com","troncoso.com","Juan Lopez","AADS981004KU"};  
        String valoresFabricante2[] = {"2","IKEA","CDMX", "55111111","famsa@mail.com","famsa.com","Pedro Perez","PPDS901107AF"}; 
        String valoresFabricante3[] = {"3","Casa de las lomas","Tlalnepantla", "55773355","casa@mail.com","casadelaslomas.com","Jose Juarez","JJDS901107AF"}; 
        
        compras.inserta("fabricante", camposFabricante, valoresFabricante); 
        compras.inserta("fabricante", camposFabricante, valoresFabricante2);
        compras.inserta("fabricante", camposFabricante, valoresFabricante3);
        compras.consulta("fabricante");
        System.out.println("------------------------------------------------------------\n");
        
        String camposCategoria[] = {"idCategoriaProducto","nombre","descripcion"};
        String valoresCategoria[] = {"1","Salas","Asientos de 3 piezas, distintos diseños"};          
        String valoresCategoria2[] = {"2","Comedores","Juego de sillas y mesa"};  
        String valoresCategoria3[] = {"3","Camas","Base para cama"};  
        
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria); 
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria2);
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria3);
        
        compras.consulta("categoriaproducto");
        System.out.println("------------------------------------------------------------\n");   
                        
        String camposProducto[] = {"idProducto","nombre","descripcion","precioUnitario","existencias","idCategoriaProducto"};
        String valoresProducto[]  = {"1","Sala modular kalia azul","Sala esquinera de estilo contemporáneo, 2 sillones","22000","4","1"};          
        String valoresProducto2[] = {"2","Sala derby new marfil","Sala Tapizada en piel natural","44000","6","1"};  
        String valoresProducto3[] = {"3","Antecomedor capuccino","Moderno antecomedor, mesa con cubierta de cristal","13200","2","2"}; 
        String valoresProducto4[] = {"4","Comedor oleg nogal","Comedor estilo nórdico, fabricado en madera de pino","25400","5","2"};          
        String valoresProducto5[] = {"5","Cama Nuria gris","King size, madera","23000","1","3"};
        String valoresProducto6[] = {"6","Cama odisey velvet","King size, madera, chapa natural","15600","2","3"}; 
        
        compras.inserta("producto", camposProducto, valoresProducto); 
        compras.inserta("producto", camposProducto, valoresProducto2);
        compras.inserta("producto", camposProducto, valoresProducto3);
        compras.inserta("producto", camposProducto, valoresProducto4);
        compras.inserta("producto", camposProducto, valoresProducto5);
        compras.inserta("producto", camposProducto, valoresProducto6);
        
        compras.consulta("producto");
        System.out.println("------------------------------------------------------------\n");  
        
        compras.elimina("fabricante","idFabricante" , "1");
        compras.elimina("fabricante","idFabricante" , "2");
        compras.elimina("fabricante","idFabricante" , "3");
        
        compras.elimina("categoriaproducto","idCategoriaProducto" , "1");
        compras.elimina("categoriaproducto","idCategoriaProducto" , "2");
        compras.elimina("categoriaproducto","idCategoriaProducto" , "3");   
        
        compras.elimina("producto","idProducto" , "1");
        compras.elimina("producto","idProducto" , "2");
        compras.elimina("producto","idProducto" , "3");
        compras.elimina("producto","idProducto" , "4");
        compras.elimina("producto","idProducto" , "5");
        compras.elimina("producto","idProducto" , "6");
        
//        compras.consulta("Producto");
//        String campos[] = {"idProducto", "nombre", "descripcion", "precioUnitario", "existencias", "idCategoriaProducto"};
//        String valores[] = {"3", "Escultura de perro azteca", "Escultura en cobre del clásico perro azteca", "250.15", "999", "1"};
//        compras.inserta("Producto", campos, valores);
//        compras.consulta("Producto");
//        campos = new String[]{"idProducto", "nombre", "descripcion", "precioUnitario"};
//        valores = new String[]{"3", "Escultura en miniatura de perro azteca", "Escultura mini en bronce del clásico perro azteca", "295.85"};
//        compras.actualiza("Producto", campos, valores, "3");
//        compras.consulta("Producto");
//        compras.elimina("Producto", "idProducto", "3");
//        compras.consulta("Producto");
        compras.cierra();
        
        
        
    }    
}
