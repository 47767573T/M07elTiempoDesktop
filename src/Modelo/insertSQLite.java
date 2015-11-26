package Modelo;

import Control.ControlDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class insertSQLite {

    static String archivoDB = ControlDB.ficheroDB;
    static String tablaPelis = ControlDB.nombreTablaPeliculas;
    static String tablaActores = ControlDB.nombreTablaActores;
    static Connection c;
    static Statement stmt;

    public static void insertTablaPelis(int id, String titulo, String fecha) {
        {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection(archivoDB);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "INSERT INTO "+tablaPelis+" (ID,TITULO,FECHA) "
                            +"VALUES ("+id+",'"+titulo+"','"+fecha+"');";
                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }

    public static void insertTablaActores(int id, String nombre, long actor, String personaje, int idPeli) {
        {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection(archivoDB);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "INSERT INTO "+tablaActores+" (ID,NOMBRE,ID_ACTOR,PERSONAJE,ID_PELICULA) "
                        +"VALUES ("+id+",'"+nombre+"',"+actor+",'"+personaje+"',"+idPeli+");";
                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName()+e.getClass().getMethods() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }
}
