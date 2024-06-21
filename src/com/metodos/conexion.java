package com.metodos;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class conexion {

    private static final Logger logger = Logger.getLogger(conexion.class.getName());
    private Connection conect = null;

    public Connection conexion() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conect = DriverManager.getConnection("jdbc:mariadb://localhost:3306/db_parnatura_v2", "root", "230301");
            logger.log(Level.INFO, "Conexión a la base de datos establecida correctamente.");
        } catch (ClassNotFoundException | SQLException code_db_001) {
            logger.log(Level.SEVERE, "Error al conectar a la base de datos: ", code_db_001);
        }
        return conect;
    }
}
