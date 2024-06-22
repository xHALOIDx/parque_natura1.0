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
public final class U4AdminAjustes extends javax.swing.JFrame {

    /*|||||||||||||||||||||||||||||||||| Inicia Llamando conexion (conectar)||||||||||||||||||||||||||||||||||*/
    conexion cc = new conexion();
    Connection cn = cc.conexion();
    public BufferedImage foto_perfil;
    String v1_ajuste_registro_capturar_nombre;//Var global
    private Object Session;

    /*|||||||||||||||||||||||||||||||||| Finaliza Llamando conexion (conectar)||||||||||||||||||||||||||||||||||*/
    /**
     * Creates new form v1_ajuste_registro
     */
    public U4AdminAjustes() {
        initComponents();
        mostrarUsuariosRegistrados(tabla_administrar_usuarios);
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
        tabla_administrar_usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = tabla_administrar_usuarios.getSelectedRow();
                txtGetNumDocumento.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 1).toString());
                txtGetNombres.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 2).toString());
                txtGetApellidos.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 3).toString());
                txtGetCorreo.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 4).toString());
                txtGetCelular.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 5).toString());
                txtGetContraseña.setText(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 6).toString());
                // Para la fecha de nacimiento, necesitarás convertir la cadena de texto a un objeto Date
                try {
                    Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 7).toString());
                    txtGetFechaNacimiento.setDate(fechaNacimiento);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                jComboBoxGetTipoUsuario.setSelectedItem(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 8).toString());
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

    public void mostrarUsuariosRegistrados(JTable tabla_administrar_usuarios) {
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
            tabla_administrar_usuarios.setModel(model);
            // Configurar el ancho de las columnas
            tabla_administrar_usuarios.getColumnModel().getColumn(0).setPreferredWidth(30); // ID
            tabla_administrar_usuarios.getColumnModel().getColumn(1).setPreferredWidth(90); // N documento
            tabla_administrar_usuarios.getColumnModel().getColumn(2).setPreferredWidth(100); // Nombres
            tabla_administrar_usuarios.getColumnModel().getColumn(3).setPreferredWidth(100); // apellidos
            tabla_administrar_usuarios.getColumnModel().getColumn(4).setPreferredWidth(190); // correo
            tabla_administrar_usuarios.getColumnModel().getColumn(5).setPreferredWidth(90); // celular
            tabla_administrar_usuarios.getColumnModel().getColumn(6).setPreferredWidth(100); // Contraseña
            tabla_administrar_usuarios.getColumnModel().getColumn(7).setPreferredWidth(90); // fecha nacimiento
            tabla_administrar_usuarios.getColumnModel().getColumn(8).setPreferredWidth(90); // tipo usuario

        } catch (SQLException ex_03) {
            System.out.println("Error al mostrar los visitantes registrados: [mostrarUsuariosRegistrados()]" + ex_03);
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
        public void insertString(FilterBypass fb, int offset, String string,
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
        public void replace(FilterBypass fb, int offset, int length, String string,
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
        public void insertString(FilterBypass fb, int offset, String string,
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
        public void replace(FilterBypass fb, int offset, int length, String string,
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
        String actualizarNumDocumento = txtGetNumDocumento.getText();
        String actualizarNombres = txtGetNombres.getText();
        String actualizarApellidos = txtGetApellidos.getText();
        String actualizarCorreoElectronico = txtGetCorreo.getText();
        String actualizarCelular = txtGetCelular.getText();
        String actualizarContraseña = txtGetContraseña.getText();
        String actualizarFechaNacimiento = new SimpleDateFormat("yyyy/MM/dd").format(txtGetFechaNacimiento.getDate());
        String actualizarTipoUsuario = jComboBoxGetTipoUsuario.getSelectedItem().toString();

        // Obtén el ID del usuario seleccionado en la tabla
        int filaSeleccionada = tabla_administrar_usuarios.getSelectedRow();
        int idUsuario = Integer.parseInt(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 0).toString());

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
            mostrarUsuariosRegistrados(tabla_administrar_usuarios);

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
        int filaSeleccionada = tabla_administrar_usuarios.getSelectedRow();
        int idUsuario = Integer.parseInt(tabla_administrar_usuarios.getValueAt(filaSeleccionada, 0).toString());

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
            mostrarUsuariosRegistrados(tabla_administrar_usuarios);

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
        String registroNumDocumento = txtGetNumDocumento.getText();
        String registroNombres = txtGetNombres.getText();
        String registroApellidos = txtGetApellidos.getText();
        String registroCorreoElectronico = txtGetCorreo.getText();
        String registroCelular = txtGetCelular.getText();
        String registroContraseña = txtGetContraseña.getText();
        String registroFechaNacimiento = new SimpleDateFormat("yyyy/MM/dd").format(txtGetFechaNacimiento.getDate());
        String registroTipoUsuario = jComboBoxGetTipoUsuario.getSelectedItem().toString();

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
            mostrarUsuariosRegistrados(tabla_administrar_usuarios);

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
//--------------------------------------------------------------------------------------------------------| 

//--------------------------------------------------------------------------------------------------------| 
//--------------------------------------------------------------------------------------------------------| 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCPanel1 = new com.bolivia.panel.JCPanel();
        txtGetNumDocumento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtGetContraseña = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        txtGetNombres = new javax.swing.JTextField();
        txtGetCorreo = new javax.swing.JTextField();
        txtGetCelular = new javax.swing.JTextField();
        jPanel_fondo_baner = new javax.swing.JPanel();
        txt_fondo_baner = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtGetApellidos = new javax.swing.JTextField();
        txtGetFechaNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxGetTipoUsuario = new javax.swing.JComboBox<>();
        v1BtnRegistro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_administrar_usuarios = new javax.swing.JTable();
        jLabel_foto_perfil = new javax.swing.JLabel();
        btn_subir_foto = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel_info_usuario = new javax.swing.JPanel();
        v1_ajuste_registro_name_vigilante = new javax.swing.JLabel();
        jCPanel2 = new com.bolivia.panel.JCPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());

        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 490));
        jPanel1.setRequestFocusEnabled(false);

        jCPanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/imagenes/f_n1.jpg"))); // NOI18N
        jCPanel1.setLocationLogo(new java.awt.Point(0, 70));
        jCPanel1.setSideHexagon(0);
        jCPanel1.setSizeMosaic(new java.awt.Dimension(0, 0));
        jCPanel1.setVisibleLogo(false);

        txtGetNumDocumento.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetNumDocumento.setToolTipText("¡Ingrese su numero de documento!");
        txtGetNumDocumento.setPreferredSize(new java.awt.Dimension(120, 26));
        txtGetNumDocumento.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetNumDocumentoCaretUpdate(evt);
            }
        });
        txtGetNumDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGetNumDocumentoActionPerformed(evt);
            }
        });
        txtGetNumDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetNumDocumentoKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Nombres :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Numero cedula :");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Correo :");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Celular :");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Contraseña :");

        txtGetContraseña.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetContraseña.setToolTipText("¡Ingrese su contraseña!");
        txtGetContraseña.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetContraseñaCaretUpdate(evt);
            }
        });
        txtGetContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetContraseñaKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Fecha Nacimiento :");

        txtGetNombres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetNombres.setToolTipText("¡Ingrese sus nombres!");
        txtGetNombres.setPreferredSize(new java.awt.Dimension(120, 26));
        txtGetNombres.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetNombresCaretUpdate(evt);
            }
        });
        txtGetNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetNombresKeyTyped(evt);
            }
        });

        txtGetCorreo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetCorreo.setToolTipText("¡Ingrese su correo electronico!");
        txtGetCorreo.setMinimumSize(new java.awt.Dimension(196, 23));
        txtGetCorreo.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetCorreoCaretUpdate(evt);
            }
        });
        txtGetCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGetCorreoFocusLost(evt);
            }
        });
        txtGetCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetCorreoKeyTyped(evt);
            }
        });

        txtGetCelular.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetCelular.setToolTipText("¡Ingrese su numero celular!");
        txtGetCelular.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetCelularCaretUpdate(evt);
            }
        });
        txtGetCelular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetCelularKeyTyped(evt);
            }
        });

        jPanel_fondo_baner.setBackground(new java.awt.Color(51, 51, 51));

        txt_fondo_baner.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        txt_fondo_baner.setForeground(new java.awt.Color(255, 255, 255));
        txt_fondo_baner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_fondo_baner.setText("ADMINISTRAR - REGISTRO");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x24.png"))); // NOI18N
        jButton1.setToolTipText("Regresar");
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x20.png"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x22.png"))); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel_fondo_banerLayout = new javax.swing.GroupLayout(jPanel_fondo_baner);
        jPanel_fondo_baner.setLayout(jPanel_fondo_banerLayout);
        jPanel_fondo_banerLayout.setHorizontalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondo_banerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_fondo_banerLayout.setVerticalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondo_banerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Apellidos :");

        txtGetApellidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGetApellidos.setToolTipText("¡Ingrese sus apellidos!");
        txtGetApellidos.setPreferredSize(new java.awt.Dimension(120, 26));
        txtGetApellidos.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGetApellidosCaretUpdate(evt);
            }
        });
        txtGetApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGetApellidosKeyTyped(evt);
            }
        });

        txtGetFechaNacimiento.setToolTipText("¡Ingrese su fecha de nacimiento!");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Tipo Usuario :");

        jComboBoxGetTipoUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComboBoxGetTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Administrador" }));
        jComboBoxGetTipoUsuario.setToolTipText("¡Selecione el tipo de usuario!");

        v1BtnRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/Ico_add_aspirante_v3.png"))); // NOI18N
        v1BtnRegistro.setText("REGISTRARSE");
        v1BtnRegistro.setToolTipText("Clic para registrarse");
        v1BtnRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v1BtnRegistroMouseClicked(evt);
            }
        });

        tabla_administrar_usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_administrar_usuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabla_administrar_usuarios.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tabla_administrar_usuariosMouseMoved(evt);
            }
        });
        tabla_administrar_usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_administrar_usuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_administrar_usuarios);

        jLabel_foto_perfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btn_subir_foto.setText("Subir Foto");
        btn_subir_foto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_subir_fotoActionPerformed(evt);
            }
        });

        jButton2.setText("Actualizar Usuario");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("Eliminar Usuario");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jPanel_info_usuario.setBackground(new java.awt.Color(0, 0, 0));

        v1_ajuste_registro_name_vigilante.setForeground(new java.awt.Color(255, 255, 255));
        v1_ajuste_registro_name_vigilante.setText("view_name_vigilante");

        jCPanel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vigilante.png"))); // NOI18N
        jCPanel2.setIconLogo(null);
        jCPanel2.setMaximumSize(new java.awt.Dimension(45, 45));
        jCPanel2.setMinimumSize(new java.awt.Dimension(45, 45));
        jCPanel2.setVisibleLogo(false);

        javax.swing.GroupLayout jCPanel2Layout = new javax.swing.GroupLayout(jCPanel2);
        jCPanel2.setLayout(jCPanel2Layout);
        jCPanel2Layout.setHorizontalGroup(
            jCPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
        jCPanel2Layout.setVerticalGroup(
            jCPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel_info_usuarioLayout = new javax.swing.GroupLayout(jPanel_info_usuario);
        jPanel_info_usuario.setLayout(jPanel_info_usuarioLayout);
        jPanel_info_usuarioLayout.setHorizontalGroup(
            jPanel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_info_usuarioLayout.createSequentialGroup()
                .addComponent(jCPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_ajuste_registro_name_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel_info_usuarioLayout.setVerticalGroup(
            jPanel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_info_usuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_info_usuarioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(v1_ajuste_registro_name_vigilante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jCPanel1Layout = new javax.swing.GroupLayout(jCPanel1);
        jCPanel1.setLayout(jCPanel1Layout);
        jCPanel1Layout.setHorizontalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jCPanel1Layout.createSequentialGroup()
                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jCPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel_foto_perfil, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_subir_foto, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel_info_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCPanel1Layout.createSequentialGroup()
                        .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jCPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtGetNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGetNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGetApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jCPanel1Layout.createSequentialGroup()
                                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jCPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(6, 6, 6)
                                        .addComponent(txtGetCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8)
                                        .addGap(6, 6, 6)
                                        .addComponent(txtGetCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10))
                                    .addGroup(jCPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtGetFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBoxGetTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(v1BtnRegistro)
                                    .addComponent(txtGetContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 158, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jCPanel1Layout.setVerticalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCPanel1Layout.createSequentialGroup()
                .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtGetNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jCPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGetNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGetApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(14, 14, 14)
                        .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGetCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGetCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGetContraseña, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(v1BtnRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxGetTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGetFechaNacimiento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jCPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(jLabel_foto_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_subir_foto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(87, 87, 87)
                        .addComponent(jPanel_info_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtGetNumDocumentoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetNumDocumentoCaretUpdate
//        validarIngreso();
    }//GEN-LAST:event_txtGetNumDocumentoCaretUpdate

    private void txtGetNumDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGetNumDocumentoActionPerformed

    }//GEN-LAST:event_txtGetNumDocumentoActionPerformed

    private void txtGetNumDocumentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetNumDocumentoKeyTyped
        /* -------------------------------Ini Llamar metodo NumberOnlyFilter ------------------------------- */
//Codigo para llamar clase NumberOnlyFilter la cual filtra que se escriba solo numero
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) txtGetNumDocumento.getDocument();
        doc.setDocumentFilter(new NumberOnlyFilter());
        /* ------------------------------- Fin Llamar clase NumberOnlyFilter ------------------------------- */
 /* ------------------------------- Ini Validador (Cantidad Caracteres) ------------------------------- */
//Validador JtexField Cantidad de Caracteres deseados
        if (txtGetNumDocumento.getText().length() == 10) {
            evt.consume();
        }
        /* ------------------------------- Fin Validador (Cantidad Caracteres) ------------------------------- */
    }//GEN-LAST:event_txtGetNumDocumentoKeyTyped

    private void txtGetContraseñaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetContraseñaCaretUpdate
//        validarIngreso();
    }//GEN-LAST:event_txtGetContraseñaCaretUpdate

    private void txtGetContraseñaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetContraseñaKeyTyped

    }//GEN-LAST:event_txtGetContraseñaKeyTyped

    private void txt_r_fechaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txt_r_fechaPropertyChange

    }//GEN-LAST:event_txt_r_fechaPropertyChange

    private void txtGetNombresCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetNombresCaretUpdate

    }//GEN-LAST:event_txtGetNombresCaretUpdate

    private void txtGetNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetNombresKeyTyped
        /* ------------------------------------------------------------------------------------ */
        //Codigo para llamar clase LetterOnlyFilter la cual filtra que se escriba solo caracteres
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) txtGetNombres.getDocument();
        doc.setDocumentFilter(new LetterSpaceFilter());
        /* ------------------------------------------------------------------------------------ */
 /* ------------------------------------------------------------------------------------ */
        //Codigo para validar JtexField de cantidad de caracteres deseados
        if (txtGetNombres.getText().length() == 25) {
            evt.consume();
        }
        /* ------------------------------------------------------------------------------------ */
    }//GEN-LAST:event_txtGetNombresKeyTyped

    private void txtGetCorreoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetCorreoCaretUpdate

    }//GEN-LAST:event_txtGetCorreoCaretUpdate

    private void txtGetCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGetCorreoFocusLost

    }//GEN-LAST:event_txtGetCorreoFocusLost

    private void txtGetCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetCorreoKeyTyped

    }//GEN-LAST:event_txtGetCorreoKeyTyped

    private void txtGetCelularCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetCelularCaretUpdate

    }//GEN-LAST:event_txtGetCelularCaretUpdate

    private void txtGetCelularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetCelularKeyTyped

    }//GEN-LAST:event_txtGetCelularKeyTyped

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        /* ----------------------------------------------- Ini Boton (home)  ----------------------------------------------- */
        metSetV1Ajuste();//Invocar metodo
        //v1_ajuste_registro_capturar_nombre
        v1_ajuste_registro_capturar_nombre = v1_ajuste_registro_name_vigilante.getText();//pedir nombres y guardarlo
        txt_u4_nombre_vigilante.setText(v1_ajuste_registro_capturar_nombre);//Enviar nombre guardado a otra ventana

        /* ----------------------------------------------- Fin Boton (home)  ----------------------------------------------- */
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtGetApellidosCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGetApellidosCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGetApellidosCaretUpdate

    private void txtGetApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetApellidosKeyTyped
        /* ------------------------------------------- Ini Llamar metodo LetterSpaceFilter -------------------------------------------- */
        //Codigo para llamar clase LetterOnlyFilter la cual filtra que se escriba solo caracteres
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) txtGetNombres.getDocument();
        doc.setDocumentFilter(new LetterSpaceFilter());
        /* ------------------------------------------- Fin Llamar clase LetterSpaceFilter -------------------------------------------- */
 /* ------------------------------------------- Ini Validador (Cantidad Caracteres) -------------------------------------------- */
        //Validador JtexField Cantidad de Caracteres deseados
        if (txtGetApellidos.getText().length() == 25) {
            evt.consume();
        }
        /* -------------------------------------------- Fin Validador (Cantidad Caracteres) ------------------------------------------- */
    }//GEN-LAST:event_txtGetApellidosKeyTyped

    private void v1BtnRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v1BtnRegistroMouseClicked
        metRegistrarUsuario();//Invocar metodo
    }//GEN-LAST:event_v1BtnRegistroMouseClicked

    private void btn_subir_fotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_subir_fotoActionPerformed
        subirFoto();
    }//GEN-LAST:event_btn_subir_fotoActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        actualizarUsuario();
    }//GEN-LAST:event_jButton2MouseClicked

    private void tabla_administrar_usuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_administrar_usuariosMouseClicked
    }//GEN-LAST:event_tabla_administrar_usuariosMouseClicked

    private void tabla_administrar_usuariosMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_administrar_usuariosMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_administrar_usuariosMouseMoved

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        eliminarUsuario();
    }//GEN-LAST:event_jButton3MouseClicked

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
            java.util.logging.Logger.getLogger(U4AdminAjustes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjustes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjustes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(U4AdminAjustes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new U4AdminAjustes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btn_subir_foto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.bolivia.panel.JCPanel jCPanel1;
    private com.bolivia.panel.JCPanel jCPanel2;
    private javax.swing.JComboBox<String> jComboBoxGetTipoUsuario;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_foto_perfil;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_fondo_baner;
    private javax.swing.JPanel jPanel_info_usuario;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tabla_administrar_usuarios;
    public static javax.swing.JTextField txtGetApellidos;
    public static javax.swing.JTextField txtGetCelular;
    public static javax.swing.JPasswordField txtGetContraseña;
    public static javax.swing.JTextField txtGetCorreo;
    private com.toedter.calendar.JDateChooser txtGetFechaNacimiento;
    public static javax.swing.JTextField txtGetNombres;
    public static javax.swing.JTextField txtGetNumDocumento;
    private javax.swing.JLabel txt_fondo_baner;
    private javax.swing.JButton v1BtnRegistro;
    public static javax.swing.JLabel v1_ajuste_registro_name_vigilante;
    // End of variables declaration//GEN-END:variables
}
