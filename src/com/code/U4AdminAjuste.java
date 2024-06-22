//=============================================================================================================
package com.code;

import com.metodos.conexion;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static com.code.U3AdminOpciones.txt_u4_nombre_vigilante;

/**
 *
 * @author luise
 */
public class U4AdminAjuste extends javax.swing.JFrame {
    /*|||||||||||||||||||||||||||||||||| Inicia Llamando conexion (conectar)||||||||||||||||||||||||||||||||||*/
    conexion cc = new conexion();
    Connection cn = cc.conexion();
    public BufferedImage foto_perfil;
    String v1_ajuste_registro_capturar_nombre;//Var global
    private Object Session;

    /*|||||||||||||||||||||||||||||||||| Finaliza Llamando conexion (conectar)||||||||||||||||||||||||||||||||||*/

    public U4AdminAjuste() {
        initComponents();
        metJTableVerUsuarios(tblU4AdminAjuste);
        //--------------------------------------------------------------------------------------------------------|
        //METODO 01 Establece el título de la ventana principal
        this.setTitle("Sicovp Login Admon > Opciones > Ajustes");
        //--------------------------------------------------------------------------------------------------------| 
        //METODO 02 centrar la ventana actual del programa
        this.setLocationRelativeTo(null);
        //--------------------------------------------------------------------------------------------------------|    
        // Metodo 03 Confirmacion cerrar ventana actual: 
        //Configuramos la acción que sucede al hacer clic en el botón "X" de la ventana
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        // Agregamos un "window listener" que detecta cuando la ventana está siendo cerrada
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                // Mostramos un cuadro de diálogo para confirmar si el usuario desea cerrar la ventana
                int confirmed = JOptionPane.showConfirmDialog(null, "¿Estás seguro que deseas cerrar la ventana?", "Confirmación de cierre", JOptionPane.YES_NO_OPTION);
                // Si el usuario confirma que desea cerrar la ventana
                if (confirmed == JOptionPane.YES_OPTION) {
                    // Cerramos la ventana utilizando el método dispose()
                    dispose();
                }
            }
        });
        //--------------------------------------------------------------------------------------------------------|
        //Metodo 04 selecionar usuario en la tabla y mostrar la informacion en la UI
        tblU4AdminAjuste.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = tblU4AdminAjuste.getSelectedRow();
                txtU4NumeroCedula.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 1).toString());
                txtU4Nombres.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 2).toString());
                txtU4Apellidos.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 3).toString());
                txtU4Correo.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 4).toString());
                txtU4Celular.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 5).toString());
                txtU4Contraseña.setText(tblU4AdminAjuste.getValueAt(filaSeleccionada, 6).toString());
                // Para la fecha de nacimiento, necesitarás convertir la cadena de texto a un objeto Date
                try {
                    Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(tblU4AdminAjuste.getValueAt(filaSeleccionada, 7).toString());
                    txtU4FechaNacimiento.setDate(fechaNacimiento);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                cmbU4TipoUsuario.setSelectedItem(tblU4AdminAjuste.getValueAt(filaSeleccionada, 8).toString());
            }
        });
        //--------------------------------------------------------------------------------------------------------|
    }
    
    //--------------------------------------------------------------------------------------------------------| 
    public void enviarCorreo(String proveedor, String remitente, String contraseña, String destinatario, String asunto, String mensaje) {
        // Configuración de la conexión al servidor de correo
        Properties propiedades = new Properties();
        if (proveedor.equalsIgnoreCase("gmail")) {
            propiedades.put("mail.smtp.host", "smtp.gmail.com");
        } else if (proveedor.equalsIgnoreCase("hotmail")) {
            propiedades.put("mail.smtp.host", "smtp-mail.outlook.com");
        }
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");

        // Crear una nueva sesión con una autenticación
        javax.mail.Session sesion = javax.mail.Session.getInstance(propiedades, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contraseña);
            }
        });

        try {
            // Crear un nuevo mensaje de correo
            Message mensajeCorreo = new MimeMessage(sesion);
            mensajeCorreo.setFrom(new InternetAddress(remitente));
            mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensajeCorreo.setSubject(asunto);
            mensajeCorreo.setText(mensaje);

            // Enviar el mensaje
            Transport.send(mensajeCorreo);

            System.out.println("Correo enviado exitosamente");
        } catch (MessagingException ex33) {
            System.out.println("Error al enviar correo: " + ex33);
        }
    }
//--------------------------------------------------------------------------------------------------------| 
// Método para abrir el explorador de archivos y guardar la imagen seleccionada
    public void subirFoto() {
        JFileChooser fileChooser = new JFileChooser();

        // Configura el filtro para que solo se puedan seleccionar archivos de imagen
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Se ha seleccionado un archivo de imagen
            File selectedFile = fileChooser.getSelectedFile();

            // Lee la imagen del archivo y la guarda en la variable global
            try {
                foto_perfil = ImageIO.read(selectedFile);

                // Ajusta la imagen al tamaño del JLabel
                Image scaledImage = foto_perfil.getScaledInstance(jLabel_foto_perfil.getWidth(), jLabel_foto_perfil.getHeight(), Image.SCALE_SMOOTH);
                jLabel_foto_perfil.setIcon(new ImageIcon(scaledImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//--------------------------------------------------------------------------------------------------------|    
//--------------------------------------------------------------------------------------------------------|    
//METODO 04 ICONO BARRA DE TAREAS

    @Override
    public Image getIconImage() {
        // Obtener la imagen del icono usando el recurso del archivo ico_barratareas_1.png
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("com/iconos/ico_barratareas_1.png"));
        // Devolver la imagen del icono para ser usada como el icono predeterminado para la ventana
        return retValue;
    }
//--------------------------------------------------------------------------------------------------------| 
//--------------------------------------------------------------------------------------------------------| 

    public void metJTableVerUsuarios(JTable tblU4AdminAjuste) {
        try {
            // Consulta para obtener los visitantes registrados
            String consulta_02 = "SELECT id_usuario, num_documento, nombres, apellidos, correo_electronico , celular, contraseña, fecha_nacimiento, tipo_usuario FROM ta1_usuarios";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta_02);

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("N° de Documento");
            model.addColumn("Nombres");
            model.addColumn("Apellidos");
            model.addColumn("Correo");
            model.addColumn("Celular");
            model.addColumn("Contraseña");
            model.addColumn("f nacimiento");
            model.addColumn("tipo user");

            // Llenar el modelo con los datos de la consulta
            while (rs.next()) {
                Object[] row = new Object[11];
                row[0] = rs.getInt("id_usuario");
                row[1] = rs.getString("num_documento");
                row[2] = rs.getString("nombres");
                row[3] = rs.getString("apellidos");
                row[4] = rs.getString("correo_electronico");
                row[5] = rs.getString("celular");
                row[6] = rs.getString("contraseña");
                row[7] = rs.getString("fecha_nacimiento");
                row[8] = rs.getString("tipo_usuario");
                model.addRow(row);
            }
            // Asignar el modelo a la tabla
            tblU4AdminAjuste.setModel(model);
            // Configurar el ancho de las columnas
            tblU4AdminAjuste.getColumnModel().getColumn(0).setPreferredWidth(30); // ID
            tblU4AdminAjuste.getColumnModel().getColumn(1).setPreferredWidth(90); // N documento
            tblU4AdminAjuste.getColumnModel().getColumn(2).setPreferredWidth(100); // Nombres
            tblU4AdminAjuste.getColumnModel().getColumn(3).setPreferredWidth(100); // apellidos
            tblU4AdminAjuste.getColumnModel().getColumn(4).setPreferredWidth(190); // correo
            tblU4AdminAjuste.getColumnModel().getColumn(5).setPreferredWidth(90); // celular
            tblU4AdminAjuste.getColumnModel().getColumn(6).setPreferredWidth(100); // Contraseña
            tblU4AdminAjuste.getColumnModel().getColumn(7).setPreferredWidth(90); // fecha nacimiento
            tblU4AdminAjuste.getColumnModel().getColumn(8).setPreferredWidth(90); // tipo usuario

        } catch (SQLException ex_03) {
            System.out.println("Error al mostrar los visitantes registrados: [metJTableVerUsuarios()]" + ex_03);
        }

    }
//--------------------------------------------------------------------------------------------------------|
//--------------------------------------------------------------------------------------------------------|
    // Metodo validar jTextField solo acepte numeros (sin espacios)

    public class NumberOnlyFilter extends DocumentFilter {
        // Método que se llama al momento de insertar un nuevo carácter

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    newStr = "";
                    // Muestra un mensaje de error indicando que solo se aceptan números
                    JOptionPane.showMessageDialog(null, "Solo se aceptan números", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            // Inserta el nuevo string si solo contiene números
            super.insertString(fb, offset, newStr, attr);
        }

        // Método que se llama al momento de reemplazar un carácter existente
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string,
                AttributeSet attrs) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    newStr = "";
                    // Muestra un mensaje de error indicando que solo se aceptan números
                    JOptionPane.showMessageDialog(null, "Solo se aceptan números", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            // Reemplaza el string si solo contiene números
            super.replace(fb, offset, length, newStr, attrs);
        }
    }
//--------------------------------------------------------------------------------------------------------|
//--------------------------------------------------------------------------------------------------------|
    // Metodo validar jTextField solo acepte caracteres (sin espacios)

    public class LetterOnlyFilter extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isLetter(string.charAt(i))) {
                    newStr = "";
                    JOptionPane.showMessageDialog(null, "Solo se aceptan caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            super.insertString(fb, offset, newStr, attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string,
                AttributeSet attrs) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isLetter(string.charAt(i))) {
                    newStr = "";
                    JOptionPane.showMessageDialog(null, "Solo se aceptan caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            super.replace(fb, offset, length, newStr, attrs);
        }
    }

//--------------------------------------------------------------------------------------------------------|
//--------------------------------------------------------------------------------------------------------|
    // Metodo validar jTextField solo acepte caracteres (con espacios)
    public class LetterSpaceFilter extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isLetter(string.charAt(i)) && string.charAt(i) != ' ') {
                    newStr = "";
                    JOptionPane.showMessageDialog(null, "Solo se aceptan caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            super.insertString(fb, offset, newStr, attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string,
                AttributeSet attrs) throws BadLocationException {
            String newStr = string;
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isLetter(string.charAt(i)) && string.charAt(i) != ' ') {
                    newStr = "";
                    JOptionPane.showMessageDialog(null, "Solo se aceptan caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            super.replace(fb, offset, length, newStr, attrs);
        }
    }

//--------------------------------------------------------------------------------------------------------| 
//--------------------------------------------------------------------------------------------------------| 
    public void actualizarUsuario() {
        // Obtén los datos del formulario
        String actualizarNumDocumento = txtU4NumeroCedula.getText();
        String actualizarNombres = txtU4Nombres.getText();
        String actualizarApellidos = txtU4Apellidos.getText();
        String actualizarCorreoElectronico = txtU4Correo.getText();
        String actualizarCelular = txtU4Celular.getText();
        String actualizarContraseña = txtU4Contraseña.getText();
        String actualizarFechaNacimiento = new SimpleDateFormat("yyyy/MM/dd").format(txtU4FechaNacimiento.getDate());
        String actualizarTipoUsuario = cmbU4TipoUsuario.getSelectedItem().toString();

        // Obtén el ID del usuario seleccionado en la tabla
        int filaSeleccionada = tblU4AdminAjuste.getSelectedRow();
        int idUsuario = Integer.parseInt(tblU4AdminAjuste.getValueAt(filaSeleccionada, 0).toString());

        try {
            // Consulta SQL para actualizar el usuario
            String consultaSQL = "UPDATE ta1_usuarios SET num_documento = ?, nombres = ?, apellidos = ?, correo_electronico = ?, celular = ?, contraseña = ?, fecha_nacimiento = ?, tipo_usuario = ? WHERE id_usuario = ?";

            PreparedStatement pst = cn.prepareStatement(consultaSQL);
            pst.setString(1, actualizarNumDocumento);
            pst.setString(2, actualizarNombres);
            pst.setString(3, actualizarApellidos);
            pst.setString(4, actualizarCorreoElectronico);
            pst.setString(5, actualizarCelular);
            pst.setString(6, actualizarContraseña);
            pst.setString(7, actualizarFechaNacimiento);
            pst.setString(8, actualizarTipoUsuario);
            pst.setInt(9, idUsuario);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableVerUsuarios(tblU4AdminAjuste);

            JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente");

            // Enviar un correo al usuario
            String proveedor = "hotmail"; // o "hotmail"
            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
            String contraseña = "yuceivfuepcfkiem";
            String destinatario = actualizarCorreoElectronico;
            String asunto = "Ser Seguridad - Parque Natura";
            String mensaje = "[Ser Seguridad Ltda] le informa que su usuario ha sido actualizado.";
            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);

        } catch (SQLException ex_05) {
            System.out.println("Error al actualizar usuario: " + ex_05);
        }
    }

//--------------------------------------------------------------------------------------------------------|  
//--------------------------------------------------------------------------------------------------------|  
    public void eliminarUsuario() {
        // Obtén el ID del usuario seleccionado en la tabla
        int filaSeleccionada = tblU4AdminAjuste.getSelectedRow();
        int idUsuario = Integer.parseInt(tblU4AdminAjuste.getValueAt(filaSeleccionada, 0).toString());

        try {
            // Consulta SQL para obtener el correo electrónico del usuario
            String consultaCorreo = "SELECT correo_electronico FROM ta1_usuarios WHERE id_usuario = ?";
            PreparedStatement pstCorreo = cn.prepareStatement(consultaCorreo);
            pstCorreo.setInt(1, idUsuario);
            ResultSet rs = pstCorreo.executeQuery();
            if (!rs.next()) {
                System.out.println("No se encontró el usuario con ID: " + idUsuario);
                return;
            }
            String correoUsuario = rs.getString("correo_electronico");

            // Consulta SQL para eliminar el usuario
            String consultaSQL = "DELETE FROM ta1_usuarios WHERE id_usuario = ?";
            PreparedStatement pst = cn.prepareStatement(consultaSQL);
            pst.setInt(1, idUsuario);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableVerUsuarios(tblU4AdminAjuste);

            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente");

            // Enviar un correo al usuario
            String proveedor = "hotmail"; // o "hotmail"
            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
            String contraseña = "yuceivfuepcfkiem";
            String destinatario = correoUsuario;
            String asunto = "Ser Seguridad - Parque Natura";
            String mensaje = "Ser Seguridad Informa que se ha eliminado tu usuario.";
            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);

        } catch (SQLException ex) {
            System.out.println("Error al eliminar usuario: " + ex);
        }
    }

//--------------------------------------------------------------------------------------------------------|   
//--------------------------------------------------------------------------------------------------------|   
    // INICIO METODO Pedir datos para registrar un nuevo usuario.
    public void metRegistrarUsuario() {
        String registroNumDocumento = txtU4NumeroCedula.getText();
        String registroNombres = txtU4Nombres.getText();
        String registroApellidos = txtU4Apellidos.getText();
        String registroCorreoElectronico = txtU4Correo.getText();
        String registroCelular = txtU4Celular.getText();
        String registroContraseña = txtU4Contraseña.getText();
        String registroFechaNacimiento = new SimpleDateFormat("yyyy/MM/dd").format(txtU4FechaNacimiento.getDate());
        String registroTipoUsuario = cmbU4TipoUsuario.getSelectedItem().toString();

        // Validar que los campos obligatorios estén completos
        if (registroNumDocumento.isEmpty() || registroNombres.isEmpty() || registroApellidos.isEmpty() || registroCorreoElectronico.isEmpty() || registroContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete los campos obligatorios.");
            return;
        }

        // Validar que el correo electrónico tenga un formato válido
        if (!registroCorreoElectronico.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un correo electrónico válido.");
            return;
        }

        // Validar que el número de celular tenga un formato válido
        if (!registroCelular.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número de celular válido.");
            return;
        }

        try {
            // Convierte la imagen en un array de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(foto_perfil, "jpg", baos);
            byte[] imagenEnBytes = baos.toByteArray();

            // Consulta SQL para insertar un nuevo usuario con imagen
            String consultaSQL = "INSERT INTO ta1_usuarios (num_documento, nombres, apellidos, correo_electronico, celular, contraseña, fecha_nacimiento, tipo_usuario, foto_perfil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement pst = cn.prepareStatement(consultaSQL);
            pst.setString(1, registroNumDocumento);
            pst.setString(2, registroNombres);
            pst.setString(3, registroApellidos);
            pst.setString(4, registroCorreoElectronico);
            pst.setString(5, registroCelular);
            pst.setString(6, registroContraseña);
            pst.setString(7, registroFechaNacimiento);
            pst.setString(8, registroTipoUsuario);

            // Aquí agregamos la imagen a la consulta
            pst.setBytes(9, imagenEnBytes);
            pst.executeUpdate();

            // Después de registrar el usuario con éxito...
            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/Ico_bd_ok.png"));
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente",
                    "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);

            // Actualiza la tabla para reflejar los cambios
            metJTableVerUsuarios(tblU4AdminAjuste);

           // Enviar un correo al usuario
            String proveedor = "hotmail"; // o "hotmail"
            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
            String contraseña = "yuceivfuepcfkiem";
            String destinatario = registroCorreoElectronico;
            String asunto = "Ser Seguridad - Parque Natura";
            String mensaje = "[Ser Seguridad Ltda] le informa que su nuevo usuario ha sido creado exitosamente.";
            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);

        } catch (SQLException ex21) {
            System.out.println("Error al registrar nuevo usuario: " + ex21);
        } catch (IOException ex22) {
            System.out.println("Error al cargar la imagen: " + ex22);
        }
    }
//--------------------------------------------------------------------------------------------------------|  
//--------------------------------------------------------------------------------------------------------|
    // Este método se encarga de cerrar el JFrame actual, y mostrar otro

    private void metSetV1Ajuste() {
        U3AdminOpciones HALOID = new U3AdminOpciones();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
//--------------------------------------------------------------------------------------------------------| 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel_fondo_baner = new javax.swing.JPanel();
        txt_fondo_baner = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jCPanel1 = new com.bolivia.panel.JCPanel();
        lvlU4AdminAjuste = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtU4NumeroCedula = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtU4Nombres = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtU4Apellidos = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtU4Correo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtU4Celular = new javax.swing.JTextField();
        jLabel_foto_perfil = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        txtU4Contraseña = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtU4FechaNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        cmbU4TipoUsuario = new javax.swing.JComboBox<>();
        btbU4Agregar = new javax.swing.JButton();
        btnU4Modificar = new javax.swing.JButton();
        btbU4Eliminar = new javax.swing.JButton();
        btbU4BuscarFoto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblU4AdminAjuste = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 600));

        jPanel_fondo_baner.setBackground(new java.awt.Color(51, 51, 51));

        txt_fondo_baner.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        txt_fondo_baner.setForeground(new java.awt.Color(255, 255, 255));
        txt_fondo_baner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_fondo_baner.setText("                                             ADMINISTRADOR AJUSTES");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x24.png"))); // NOI18N
        jButton1.setToolTipText("Regresar");
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x20.png"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x22.png"))); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jCPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jCPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jCPanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_foto_perfil_hombre_x512.png"))); // NOI18N

        javax.swing.GroupLayout jCPanel1Layout = new javax.swing.GroupLayout(jCPanel1);
        jCPanel1.setLayout(jCPanel1Layout);
        jCPanel1Layout.setHorizontalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
        jCPanel1Layout.setVerticalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lvlU4AdminAjuste.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lvlU4AdminAjuste.setForeground(new java.awt.Color(255, 255, 255));
        lvlU4AdminAjuste.setText("v1_ajuste_txt_get_name_vigilante");

        javax.swing.GroupLayout jPanel_fondo_banerLayout = new javax.swing.GroupLayout(jPanel_fondo_baner);
        jPanel_fondo_baner.setLayout(jPanel_fondo_banerLayout);
        jPanel_fondo_banerLayout.setHorizontalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondo_banerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lvlU4AdminAjuste)
                .addContainerGap())
        );
        jPanel_fondo_banerLayout.setVerticalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
            .addComponent(lvlU4AdminAjuste, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel_fondo_banerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fondo_baner, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtU4NumeroCedula.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4NumeroCedula.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4NumeroCedula.setText("1096239932");
        txtU4NumeroCedula.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4NumeroCedula.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4NumeroCedula.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4NumeroCedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4NumeroCedulaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4NumeroCedulaFocusLost(evt);
            }
        });
        txtU4NumeroCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4NumeroCedulaActionPerformed(evt);
            }
        });
        txtU4NumeroCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4NumeroCedulaKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText(" Numero Cedula *");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(" Nombres *");

        txtU4Nombres.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4Nombres.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4Nombres.setText("Luis Eduardo   ");
        txtU4Nombres.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4Nombres.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4Nombres.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4Nombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4NombresFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4NombresFocusLost(evt);
            }
        });
        txtU4Nombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4NombresActionPerformed(evt);
            }
        });
        txtU4Nombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4NombresKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText(" Apellidos *");

        txtU4Apellidos.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4Apellidos.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4Apellidos.setText("Morales Florez  ");
        txtU4Apellidos.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4Apellidos.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4Apellidos.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4Apellidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4ApellidosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4ApellidosFocusLost(evt);
            }
        });
        txtU4Apellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4ApellidosActionPerformed(evt);
            }
        });
        txtU4Apellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4ApellidosKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText(" Correo Electronico *");

        txtU4Correo.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4Correo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4Correo.setText("luiseduardo192@hotmail.com");
        txtU4Correo.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4Correo.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4Correo.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4Correo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4CorreoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4CorreoFocusLost(evt);
            }
        });
        txtU4Correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4CorreoActionPerformed(evt);
            }
        });
        txtU4Correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4CorreoKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText(" # Celular *");

        txtU4Celular.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4Celular.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4Celular.setText("3202187288 ");
        txtU4Celular.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4Celular.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4Celular.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4Celular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4CelularFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4CelularFocusLost(evt);
            }
        });
        txtU4Celular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4CelularActionPerformed(evt);
            }
        });
        txtU4Celular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4CelularKeyTyped(evt);
            }
        });

        jLabel_foto_perfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText(" Contraseña *");

        txtU4Contraseña.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtU4Contraseña.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtU4Contraseña.setText("ParqueNatura2024");
        txtU4Contraseña.setMaximumSize(new java.awt.Dimension(200, 26));
        txtU4Contraseña.setMinimumSize(new java.awt.Dimension(200, 26));
        txtU4Contraseña.setPreferredSize(new java.awt.Dimension(200, 26));
        txtU4Contraseña.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtU4ContraseñaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtU4ContraseñaFocusLost(evt);
            }
        });
        txtU4Contraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtU4ContraseñaActionPerformed(evt);
            }
        });
        txtU4Contraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtU4ContraseñaKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(" Fecha Nacimiento *");

        txtU4FechaNacimiento.setToolTipText("¡Ingrese su fecha de nacimiento!");
        txtU4FechaNacimiento.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText(" Tipo de Usuario *");

        cmbU4TipoUsuario.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        cmbU4TipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Administrador" }));
        cmbU4TipoUsuario.setToolTipText("¡Selecione el tipo de usuario!");

        btbU4Agregar.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btbU4Agregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_agregar_usuario_40px.png"))); // NOI18N
        btbU4Agregar.setText("Agregar");
        btbU4Agregar.setPreferredSize(new java.awt.Dimension(123, 38));
        btbU4Agregar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_agregar_usuario_32px.png"))); // NOI18N
        btbU4Agregar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_agregar_usuario_37px.png"))); // NOI18N
        btbU4Agregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btbU4AgregarMouseClicked(evt);
            }
        });
        btbU4Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbU4AgregarActionPerformed(evt);
            }
        });

        btnU4Modificar.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnU4Modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_usuario_40px.png"))); // NOI18N
        btnU4Modificar.setText("Modificar");
        btnU4Modificar.setPreferredSize(new java.awt.Dimension(123, 38));
        btnU4Modificar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_usuario_32px.png"))); // NOI18N
        btnU4Modificar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_usuario_37px.png"))); // NOI18N
        btnU4Modificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnU4ModificarMouseClicked(evt);
            }
        });
        btnU4Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnU4ModificarActionPerformed(evt);
            }
        });

        btbU4Eliminar.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btbU4Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_eliminar_usuario_40px.png"))); // NOI18N
        btbU4Eliminar.setText("Eliminar");
        btbU4Eliminar.setPreferredSize(new java.awt.Dimension(123, 38));
        btbU4Eliminar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_eliminar_usuario_32px.png"))); // NOI18N
        btbU4Eliminar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_eliminar_usuario_37px.png"))); // NOI18N
        btbU4Eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btbU4EliminarMouseClicked(evt);
            }
        });
        btbU4Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbU4EliminarActionPerformed(evt);
            }
        });

        btbU4BuscarFoto.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btbU4BuscarFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_foto_x40.png"))); // NOI18N
        btbU4BuscarFoto.setText("Buscar Foto");
        btbU4BuscarFoto.setPreferredSize(new java.awt.Dimension(123, 38));
        btbU4BuscarFoto.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_foto_x32.png"))); // NOI18N
        btbU4BuscarFoto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_foto_x37.png"))); // NOI18N
        btbU4BuscarFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btbU4BuscarFotoMouseClicked(evt);
            }
        });
        btbU4BuscarFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbU4BuscarFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel_foto_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5)
                            .addComponent(txtU4NumeroCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Correo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Celular, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtU4Contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtU4FechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbU4TipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btbU4Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnU4Modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btbU4Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btbU4BuscarFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9))
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtU4NumeroCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Correo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Celular, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtU4Contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbU4TipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtU4FechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btbU4Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnU4Modificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btbU4Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btbU4BuscarFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel_foto_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblU4AdminAjuste.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblU4AdminAjuste);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        /* ----------------------------------------------- Ini Boton (home)  ----------------------------------------------- */
//        metSetV1Ajuste();//Invocar metodo
//        //v1_ajuste_registro_capturar_nombre
//        v1_ajuste_registro_capturar_nombre = v1_ajuste_registro_name_vigilante.getText();//pedir nombres y guardarlo
//        v1_login_admon_ajuste_name_vigilante.setText(v1_ajuste_registro_capturar_nombre);//Enviar nombre guardado a otra ventana

        /* ----------------------------------------------- Fin Boton (home)  ----------------------------------------------- */
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtU4NumeroCedulaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4NumeroCedulaFocusGained

    }//GEN-LAST:event_txtU4NumeroCedulaFocusGained

    private void txtU4NumeroCedulaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4NumeroCedulaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NumeroCedulaFocusLost

    private void txtU4NumeroCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4NumeroCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NumeroCedulaActionPerformed

    private void txtU4NumeroCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4NumeroCedulaKeyTyped
//        /* ------------------------------------------------------------------------------------ */
//        // Invocar LetterSpaceFilter
//        javax.swing.text.AbstractDocument doc
//        = (javax.swing.text.AbstractDocument) pivtxtget_nombre_visitante.getDocument();
//        doc.setDocumentFilter(new LetterSpaceFilter());
//        /* ------------------------------------------------------------------------------------ */
//        /* ------------------------------------------------------------------------------------ */
//        //Validador JtexField Cantidad de Caracteres deseados
//        if (pivtxtget_nombre_visitante.getText().length() == 35) {
//            evt.consume();
//        }
//        /* ------------------------------------------------------------------------------------ */
    }//GEN-LAST:event_txtU4NumeroCedulaKeyTyped

    private void txtU4NombresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4NombresFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NombresFocusGained

    private void txtU4NombresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4NombresFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NombresFocusLost

    private void txtU4NombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4NombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NombresActionPerformed

    private void txtU4NombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4NombresKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4NombresKeyTyped

    private void txtU4ApellidosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4ApellidosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ApellidosFocusGained

    private void txtU4ApellidosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4ApellidosFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ApellidosFocusLost

    private void txtU4ApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4ApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ApellidosActionPerformed

    private void txtU4ApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4ApellidosKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ApellidosKeyTyped

    private void txtU4CorreoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4CorreoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CorreoFocusGained

    private void txtU4CorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4CorreoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CorreoFocusLost

    private void txtU4CorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4CorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CorreoActionPerformed

    private void txtU4CorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4CorreoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CorreoKeyTyped

    private void txtU4CelularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4CelularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CelularFocusGained

    private void txtU4CelularFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4CelularFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CelularFocusLost

    private void txtU4CelularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4CelularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CelularActionPerformed

    private void txtU4CelularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4CelularKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4CelularKeyTyped

    private void txtU4ContraseñaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4ContraseñaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ContraseñaFocusGained

    private void txtU4ContraseñaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtU4ContraseñaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ContraseñaFocusLost

    private void txtU4ContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtU4ContraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ContraseñaActionPerformed

    private void txtU4ContraseñaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtU4ContraseñaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtU4ContraseñaKeyTyped

    private void btbU4AgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btbU4AgregarMouseClicked
        //Invocar metodos
//        metRegistrarIngresoVisitante();
    }//GEN-LAST:event_btbU4AgregarMouseClicked

    private void btbU4AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbU4AgregarActionPerformed

    }//GEN-LAST:event_btbU4AgregarActionPerformed

    private void btnU4ModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnU4ModificarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnU4ModificarMouseClicked

    private void btnU4ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnU4ModificarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnU4ModificarActionPerformed

    private void btbU4EliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btbU4EliminarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btbU4EliminarMouseClicked

    private void btbU4EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbU4EliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btbU4EliminarActionPerformed

    private void btbU4BuscarFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btbU4BuscarFotoMouseClicked
subirFoto();
    }//GEN-LAST:event_btbU4BuscarFotoMouseClicked

    private void btbU4BuscarFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbU4BuscarFotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btbU4BuscarFotoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new U4AdminAjuste().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btbU4Agregar;
    private javax.swing.JButton btbU4BuscarFoto;
    private javax.swing.JButton btbU4Eliminar;
    private javax.swing.JButton btnU4Modificar;
    private javax.swing.JComboBox<String> cmbU4TipoUsuario;
    private javax.swing.JButton jButton1;
    private com.bolivia.panel.JCPanel jCPanel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_foto_perfil;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_fondo_baner;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lvlU4AdminAjuste;
    private javax.swing.JTable tblU4AdminAjuste;
    private javax.swing.JTextField txtU4Apellidos;
    private javax.swing.JTextField txtU4Celular;
    private javax.swing.JTextField txtU4Contraseña;
    private javax.swing.JTextField txtU4Correo;
    private com.toedter.calendar.JDateChooser txtU4FechaNacimiento;
    private javax.swing.JTextField txtU4Nombres;
    private javax.swing.JTextField txtU4NumeroCedula;
    private javax.swing.JLabel txt_fondo_baner;
    // End of variables declaration//GEN-END:variables
}
