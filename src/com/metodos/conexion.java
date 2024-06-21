package com.metodos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Clase para gestionar la conexión a la base de datos.
 */
public class Conexion {
    private String url;
    private String usuario;
    private String contraseña;
    private Connection conexion;

    /**
     * Constructor que inicializa los parámetros de conexión desde un archivo de propiedades.
     */
    public Conexion() {
        cargarPropiedades();
    }

    /**
     * Método para cargar las propiedades de conexión desde un archivo.
     */
    private void cargarPropiedades() {
        Properties propiedades = new Properties();
        try (InputStream entrada = getClass().getClassLoader().getResourceAsStream("configuracion.properties")) {
            if (entrada == null) {
                System.out.println("No se encuentra el archivo de configuración.");
                return;
            }
            propiedades.load(entrada);
            this.url = propiedades.getProperty("url");
            this.usuario = propiedades.getProperty("usuario");
            this.contraseña = propiedades.getProperty("contraseña");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para establecer la conexión a la base de datos.
     * @return La conexión establecida.
     */
    public Connection conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión establecida.");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return this.conexion;
    }

    /**
     * Método para cerrar la conexión a la base de datos.
     */
    public void cerrarConexion() {
        try {
            if (this.conexion != null && !this.conexion.isClosed()) {
                this.conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
