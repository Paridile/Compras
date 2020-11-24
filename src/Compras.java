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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    
        public void consultaCompra(String id) {
        int i;
        try {
            System.out.println("\nCompras del cliente " + id);
            ps = connection.prepareStatement("SELECT * FROM compra WHERE idCliente = " + id);
            rs = ps.executeQuery();
            System.out.println("Registros almacenados en la tabla: compra");            
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
            System.out.println("------------------------------------------------------------\n"); 
        } catch (SQLException ex) {
            System.err.println("Error al consultar registros de la tabla compra");
            System.err.println(ex.getMessage());
        }        
    }

    public String consultaPrecio(String idProducto) {
        int i;
        String precio = "1";
        try {
            ps = connection.prepareStatement("SELECT precioUnitario FROM producto WHERE idProducto = " + idProducto);
            rs = ps.executeQuery();          
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();           
            rs.next();
            precio = rs.getString(1);
        } catch (SQLException ex) {
            System.err.println("Error al consultar registros de la tabla producto");
            System.err.println(ex.getMessage());
        } 
        return precio;
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
    
    public  String getCurrentDate() {
       SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy       
        Date cDate = new Date();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
       return now;
    }
    
    public  String getCurrentHour() {
        String now;
        Calendar calendario = Calendar.getInstance();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        now = hora + ":" + minutos + ":" + segundos;
        return now;
    }
    
    public void realizaCompra(String idCliente,String idProducto,String cantidad) {
        String camposCompra[]   = {"idCliente","idProducto","cantidad","subtotal","fecha","hora"};
        float subtotal;
        subtotal = Float.parseFloat(consultaPrecio(idProducto)) * Integer.parseInt(cantidad);
        String valoresCompra[]  =  {idCliente,idProducto,cantidad,String.valueOf(subtotal),getCurrentDate(),getCurrentHour()};
        inserta("compra", camposCompra, valoresCompra);        
    }

    public void eliminaRegistros() {
        elimina("compra","idCliente" , "1");
        elimina("compra","idCliente" , "2");
        
        elimina("fabricanteproducto","idFabricante" , "1");
        elimina("fabricanteproducto","idFabricante" , "2");
        elimina("fabricanteproducto","idFabricante" , "3");
        
        elimina("cliente","idCliente" , "1");
        elimina("cliente","idCliente" , "2");
        
        elimina("fabricante","idFabricante" , "1");
        elimina("fabricante","idFabricante" , "2");
        elimina("fabricante","idFabricante" , "3");
        
        elimina("categoriaproducto","idCategoriaProducto" , "1");
        elimina("categoriaproducto","idCategoriaProducto" , "2");
        elimina("categoriaproducto","idCategoriaProducto" , "3");   
        
        elimina("producto","idProducto" , "1");
        elimina("producto","idProducto" , "2");
        elimina("producto","idProducto" , "3");
        elimina("producto","idProducto" , "4");
        elimina("producto","idProducto" , "5");
        elimina("producto","idProducto" , "6");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Compras compras = new Compras();
        compras.conecta();
        
        String camposFabricante[] = {"idFabricante","nombre","direccion","telefono","email","paginaWeb","contacto","RFC"};
        String valoresFabricante[] = {"1","Troncoso","Naucalpan, Mex", "55555555","troncoso@mail.com","troncoso.com","Juan Lopez","AADS981004KU"};  
        String valoresFabricante2[] = {"2","IKEA","CDMX", "55111111","famsa@mail.com","famsa.com","Pedro Perez","PPDS901107AF"}; 
        String valoresFabricante3[] = {"3","Casa de las lomas","Tlalnepantla", "55773355","casa@mail.com","casadelaslomas.com","Jose Juarez","JJDS901107AF"}; 
        
        //Fabricantes insertados por Uriel
        String valoresFabricante4[] = {"4","Pantoma","Naucalpan, Mex", "55555556","pantoma@mail.com","pantoma.com","Lopez Matias","LMDS781005MU"};  
        String valoresFabricante5[] = {"5","Miss Olas","Madero, Acapulco", "5555557","olas.66@mail.com","olasradicales.com","Pablo Flores","PFDS4541105QW"}; 
        String valoresFabricante6[] = {"6","Articfox","Atizapan", "55555558","coloritos777@mail.com","articfox.com","Fernanda Juarez","FJDS981109XD"}; 
        
        compras.inserta("fabricante", camposFabricante, valoresFabricante); 
        compras.inserta("fabricante", camposFabricante, valoresFabricante2);
        compras.inserta("fabricante", camposFabricante, valoresFabricante3);
        
        //Insercion de fabricantes
        compras.inserta("fabricante", camposFabricante, valoresFabricante4); 
        compras.inserta("fabricante", camposFabricante, valoresFabricante5);
        compras.inserta("fabricante", camposFabricante, valoresFabricante6);
        
        compras.consulta("fabricante");
        System.out.println("------------------------------------------------------------\n");
        
        String camposCategoria[] = {"idCategoriaProducto","nombre","descripcion"};
        String valoresCategoria[] = {"1","Salas","Asientos de 3 piezas, distintos diseños"};          
        String valoresCategoria2[] = {"2","Comedores","Juego de sillas y mesa"};  
        String valoresCategoria3[] = {"3","Camas","Base para cama"};  
        
        //Categorias insertados por Uriel
        String valoresCategoria4[] = {"4","Cobijas","Cobijas de tigre"};          
        String valoresCategoria5[] = {"5","Tabas de surf","Tablas para surfear"};  
        String valoresCategoria6[] = {"6","Tintes","BTintes para cabello"};
        
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria); 
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria2);
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria3);
        
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria4); 
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria5);
        compras.inserta("categoriaproducto", camposCategoria, valoresCategoria6);
        
        compras.consulta("categoriaproducto");
        System.out.println("------------------------------------------------------------\n");   
                        
        String camposProducto[] = {"idProducto","nombre","descripcion","precioUnitario","existencias","idCategoriaProducto"};
        String valoresProducto[]  = {"1","Sala modular kalia azul","Sala esquinera de estilo contemporáneo, 2 sillones","22000","4","1"};          
        String valoresProducto2[] = {"2","Sala derby new marfil","Sala Tapizada en piel natural","44000","6","1"};  
        String valoresProducto3[] = {"3","Antecomedor capuccino","Moderno antecomedor, mesa con cubierta de cristal","13200","2","2"}; 
        String valoresProducto4[] = {"4","Comedor oleg nogal","Comedor estilo nórdico, fabricado en madera de pino","25400","5","2"};          
        String valoresProducto5[] = {"5","Cama Nuria gris","King size, madera","23000","5","3"};
        String valoresProducto6[] = {"6","Cama odisey velvet","King size, madera, chapa natural","15600","2","3"}; 
        
        //Productos ingresdos por Uriel
        String valoresProducto7[] = {"7","Cobija tigre","Cobija abrigadora de tigre","520","100","4"};          
        String valoresProducto8[] = {"8","Cobija leon","Cobija abrigadora de leon","540","100","4"};  
        String valoresProducto9[] = {"9","Minitabla","Tabla para niños","2420","100","5"}; 
        String valoresProducto10[] = {"10","Tabla tiburon","Tabla con estampado de tiburon","3640","100","5"};          
        String valoresProducto11[] = {"11","Tinte blanco","Tinte para tener canas","150","100","6"};
        String valoresProducto12[] = {"12","Tinte rojo","Tinte para ser pelirrojo","156","100","6"}; 
        
        compras.inserta("producto", camposProducto, valoresProducto); 
        compras.inserta("producto", camposProducto, valoresProducto2);
        compras.inserta("producto", camposProducto, valoresProducto3);
        compras.inserta("producto", camposProducto, valoresProducto4);
        compras.inserta("producto", camposProducto, valoresProducto5);
        compras.inserta("producto", camposProducto, valoresProducto6);
        
        compras.inserta("producto", camposProducto, valoresProducto7); 
        compras.inserta("producto", camposProducto, valoresProducto8);
        compras.inserta("producto", camposProducto, valoresProducto9);
        compras.inserta("producto", camposProducto, valoresProducto10);
        compras.inserta("producto", camposProducto, valoresProducto11);
        compras.inserta("producto", camposProducto, valoresProducto12);
        
        compras.consulta("producto");
        System.out.println("------------------------------------------------------------\n");  
        
        String valoresProductomod[]   = {"1","Sala bari rojo","Sala esquinera de estilo contemporáneo, 2 sillones","20000","2","1"}; 
        String valoresProductomod2[]  = {"5","Cama Nuria verde","Matrimonial, madera","10000","2","3"}; 
        
        String valoresProductomod3[]   = {"8","Cobija gato","Cobija abrigadora de gato","670","100","4"}; 
        String valoresProductomod4[]  = {"12","Tinte cafe","Tinte para ser verse joven","170","100","6"}; 
        
        compras.actualiza("Producto", camposProducto, valoresProductomod, "1");
        compras.actualiza("Producto", camposProducto, valoresProductomod2, "5");
        
        compras.actualiza("Producto", camposProducto, valoresProductomod3, "8");
        compras.actualiza("Producto", camposProducto, valoresProductomod4, "12");
        
        compras.consulta("producto");
        System.out.println("------------------------------------------------------------\n"); 
        
        compras.elimina("producto","idProducto" , "1");
        compras.elimina("producto","idProducto" , "3");  
        
        compras.elimina("producto","idProducto" , "8");
        compras.elimina("producto","idProducto" , "9");  
        
        compras.consulta("producto");
        System.out.println("------------------------------------------------------------\n"); 
                
        String camposFabricanteProducto[]  = {"idFabricante","idProducto"}; 
        String valoresFabricanteProducto[]   = {"1","2"}; 
        String valoresFabricanteProducto2[]  = {"2","4"}; 
        String valoresFabricanteProducto3[]  = {"3","5"}; 
        String valoresFabricanteProducto4[]  = {"1","6"}; 
        
        String valoresFabricanteProducto5[]  = {"4","7"}; 
        String valoresFabricanteProducto6[]  = {"5","10"}; 
        String valoresFabricanteProducto7[]  = {"6","11"}; 
        String valoresFabricanteProducto8[]  = {"6","12"};
        
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto2);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto3);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto4);
        
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto5);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto6);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto7);
        compras.inserta("fabricanteproducto", camposFabricanteProducto, valoresFabricanteProducto8);
        
        compras.consulta("fabricanteproducto");
        System.out.println("------------------------------------------------------------\n"); 
        
        String camposCliente[]  = {"idCliente","nombreCompleto","RFC","telefono","direccion","email"};
        String valoresCliente[]   = {"1","Gustavo R. Vidriales Mireles","G9BGA996933","5534874587","Privada Pamo No. 287","gustavo@mail.com"};
        String valoresCliente2[]  = {"2","Ezequias Mendoza Martín","A5GGE986933","5535745335","Gorchs No. 345","mendoza@mail.com"};
        
        //Clientes agregados por Uriel
        String valoresCliente3[]  = {"3","Gabriel Montiel Matias","GMMGA786312","5555444412","Privada Pamo No. 27","werever@mail.com"};
        String valoresCliente4[]  = {"4","Federico Lobezno Gonzalez","FLGGE766512","5555444413","Privada Lomos No. 35","fedelobo@mail.com"};
        
        compras.inserta("cliente", camposCliente, valoresCliente);
        compras.inserta("cliente", camposCliente, valoresCliente2);
        
        compras.inserta("cliente", camposCliente, valoresCliente3);
        compras.inserta("cliente", camposCliente, valoresCliente4);
        
        compras.consulta("cliente");
        System.out.println("------------------------------------------------------------\n"); 
        
        
        compras.realizaCompra("1","4","2");  // idCliente idProducto cantidad   
        compras.realizaCompra("1","6","3");
        compras.realizaCompra("1","2","1");
        
        compras.realizaCompra("2","6","2");
        compras.realizaCompra("2","5","4");
        
        compras.realizaCompra("3","7","5");
        compras.realizaCompra("3","11","1");
        
        compras.realizaCompra("4","10","2");
        compras.realizaCompra("4","12","3");
        
        //compras.consulta("compra");
        compras.consultaCompra("1");
        compras.consultaCompra("2");
        
        compras.consultaCompra("3");
        compras.consultaCompra("4");
        
        compras.eliminaRegistros();
                     
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7        
        
        compras.cierra();                        
    }    
}
