//=============================================================================================================
package com.code;

import com.metodos.conexion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

//=============================================================================================================
/**
 *
 * @author luise
 */
public final class U5Home extends javax.swing.JFrame {

    /*||||||||||||||||||||||||||||||||||||| Inicia Llamando conexion (conectar)||||||||||||||||||||||||||||||||||*/
    conexion cc = new conexion();
    Connection cn = cc.conexion();
    /*||||||||||||||||||||||||||||||||||||| Finaliza Llamando conexion (conectar)|||||||||||||||||||||||||||||||||*/
    //=============================================================================================================
    //Variables para guarda estado de los botones (btn_i_visitante, btn_i_correspondencia, btn_i_carro, btn_buscar, btn_acercade)
    //Ubicados en el panel izquierdo [Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
    private boolean estaVisitanteActivo = false;
    private boolean estaCorrespondenciaActivo = false;
    private boolean estaVehicularActivo = false;
    private boolean estaBuscarActivo = false;
    private boolean estaAcercaDeActivo = false;


    //=============================================================================================================
    public U5Home() {
        initComponents();
        metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);
        metJTableMostrarCorrespondenciaRegistrada(JTableControlCorrespondencia);
        metJTableMostrarIngresoVehicularPropietarios(JTableControlVehicularPropietarios);
        metCargarApartamentosVisitantes();
        metCargarApartamentosCorrespondencia();
        metCargarApartamentosVehicularPropietarios();
        //=============================================================================================================
        //METODO 01 Establece el título de la ventana principal
        this.setTitle("Ser Seguridad SICOVP - Inicio");
        //=============================================================================================================
        //METODO 02 centrar la ventana actual del programa
        this.setLocationRelativeTo(null);
        //=============================================================================================================
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
        //=============================================================================================================
        // Agregar un ActionListener al JComboBox de Apartamentos panel ingreso salida visitantes
        pivjComboBoxget_apto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Obtener el apartamento seleccionado
                String apartamentoSeleccionadoVisitante = (String) pivjComboBoxget_apto.getSelectedItem();
                // Cargar los propietarios del apartamento seleccionado
                cargarPropietariosPorApartamentoVisitantes(apartamentoSeleccionadoVisitante);
            }
        });
        //=============================================================================================================
        // Agregar un ActionListener al JComboBox de Apartamentos panel ingreso salida correspondencia
        pic_get_apartamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Obtener el apartamento seleccionado
                String apartamentoSeleccionadoCorrespondencia = (String) pic_get_apartamento.getSelectedItem();
                // Cargar los propietarios del apartamento seleccionado
                cargarPropietariosPorApartamentoCorrespondencia(apartamentoSeleccionadoCorrespondencia);
            }
        });
        //=============================================================================================================
        // Agregar un ActionListener al JComboBox de Apartamentos panel ingreso salida vehicular propietarios
        piptxtget_get_apto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Obtener el apartamento seleccionado
                String apartamentoSeleccionadoVehicularPropietarios = (String) piptxtget_get_apto.getSelectedItem();
                // Cargar los propietarios del apartamento seleccionado
                cargarPropietariosPorApartamentoVehicularPropietarios(apartamentoSeleccionadoVehicularPropietarios);
            }
        });
        //=============================================================================================================
        /* Constructor, al hacer clic en la tabla se muestra la informacion de la fila selecionada en los campos 
        de la ventana (ingreso visitantes - salida visitantes)*/
        JTableControlVisitantes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = JTableControlVisitantes.getSelectedRow();
                pivtxtget_nombre_visitante.setText(JTableControlVisitantes.getValueAt(filaSeleccionada, 3).toString());
                pivtxtget_numero_documento_visitante.setText(JTableControlVisitantes.getValueAt(filaSeleccionada, 4).toString());
                pivjComboBoxget_apto.setSelectedItem(JTableControlVisitantes.getValueAt(filaSeleccionada, 5).toString());
                pivtxtget_placa.setText(JTableControlVisitantes.getValueAt(filaSeleccionada, 9).toString());
                pivtxtget_motivo_visita.setText(JTableControlVisitantes.getValueAt(filaSeleccionada, 7).toString());
                piv_jComboBox_quien_autoriza.setSelectedItem(JTableControlVisitantes.getValueAt(filaSeleccionada, 6).toString());
            }
        });
        //=============================================================================================================
        /* Constructor, al hacer clic en la tabla se muestra la informacion de la fila selecionada en los campos 
        de la ventana (ingreso correspondencia - salida correspondencia)*/
        JTableControlCorrespondencia.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = JTableControlCorrespondencia.getSelectedRow();
                pic_get_quien_trae.setText(JTableControlCorrespondencia.getValueAt(filaSeleccionada, 2).toString());
                pic_get_tipo_paquete.setSelectedItem(JTableControlCorrespondencia.getValueAt(filaSeleccionada, 4).toString());
                pic_get_numero_guia.setText(JTableControlCorrespondencia.getValueAt(filaSeleccionada, 3).toString());
                pic_get_apartamento.setSelectedItem(JTableControlCorrespondencia.getValueAt(filaSeleccionada, 5).toString());
                pic_get_nombre_residente.setSelectedItem(JTableControlCorrespondencia.getValueAt(filaSeleccionada, 6).toString());
            }
        });
        //=============================================================================================================
        /* Constructor, al hacer clic en la tabla se muestra la informacion de la fila selecionada en los campos 
        de la ventana (INGRESO VEHICULAR PROPIETARIOS - SALIDA VEHICULAR PROPIETARIOS)*/
        JTableControlVehicularPropietarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = JTableControlVehicularPropietarios.getSelectedRow();
                piptxtget_tipo_dueño.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 3).toString());
                piptxtget_tipo_vehiculo.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 4).toString());
                piptxtget_placa_vehicular.setText(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 5).toString());
                piptxtget_get_apto.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 6).toString());
                piptxtget_nombre_propietario.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 7).toString());
                piptxtget_nombre_visitante.setText(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 8).toString());
                piptxtget_quien_autoriza.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 9).toString());
                piptxtget_vehicular_espejo.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 10).toString());
                piptxtget_vehicular_estrellado.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 11).toString());
                piptxtget_vehicular_rayado.setSelectedItem(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 12).toString());
                piptxtget_vehicular_observacion.setText(JTableControlVehicularPropietarios.getValueAt(filaSeleccionada, 13).toString());
            }
        });
        //=============================================================================================================
        /*[CODIGO DE JOPTION>JTEXTFIELD>JBUTTON][PARTE 2] */
        //Este fragmento de código agrega un ActionListener al botón de búsqueda para ejecutar el método de búsqueda con el criterio y valor seleccionados.
        p_i_v_btn_buscar_filtro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filtroSeleccionado = pivtxtget_filtro_opciones.getSelectedItem().toString();
                String valorBusqueda = pivtxtget_filtro.getText();

                if (valorBusqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese un valor para buscar");
                    return;
                }

                switch (filtroSeleccionado) {
                    case "# Documento":
                        metBuscarVisitantePorFiltros("numero_documento_visitante", valorBusqueda);
                        break;
                    case "# Apartamento":
                        metBuscarVisitantePorApartamento(valorBusqueda); // Llama al método específico para buscar por apartamento
                        break;
                    case "Nombre Visitante":
                        metBuscarVisitantePorFiltros("nombre_visitante", valorBusqueda);
                        break;
                    case "Placa Vehicular":
                        metBuscarVisitantePorFiltros("placa_vehicular", valorBusqueda);
                        break;
                    case "Fecha":
                        metBuscarVisitantePorFiltros("fecha", valorBusqueda);  // Ajusta según tu campo de fecha en la base de datos
                        break;
                    case "Estado visitante":
                        metBuscarVisitantePorFiltros("estado_visitante", valorBusqueda);  // Ajusta según tu campo de estado en la base de datos
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Seleccione un criterio de búsqueda válido");
                }
            }
        });

        //=============================================================================================================
    }
    //=============================================================================================================

    /* ++++++++++++++++++++++++++++++++++++++++++++++ INICIO METODOS - LOGICA ++++++++++++++++++++++++++++++++++++++++++++++ */
    //METODO 04 ICONO BARRA DE TAREAS
    // Este método sobrescribe el método getIconImage de la clase JFrame para establecer el icono de la aplicación.
    @Override
    public Image getIconImage() {
        // Obtener la imagen del icono usando el recurso del archivo ico_barratareas_1.png
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("com/iconos/ico_barratareas_1.png"));
        // Devolver la imagen del icono para ser usada como el icono predeterminado para la ventana
        return retValue;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

    /*[CODIGO DE JOPTION>JTEXTFIELD>JBUTTON][PARTE 1] */
    // Método general para buscar visitantes por diferentes criterios
    public void metBuscarVisitantePorFiltros(String criterio, String valor) {
        String consulta = "SELECT * FROM ta4_control_peatonal WHERE " + criterio + " = ?";

        try (PreparedStatement pst = cn.prepareStatement(consulta)) {
            pst.setString(1, valor);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                    String nombreVisitante = rs.getString("nombre_visitante");
                    String numeroDocumento = rs.getString("numero_documento_visitante");
                    String numeroApartamento = rs.getString("numero_apartamento");
                    String placaVehicular = rs.getString("placa_vehicular");
                    String motivoVisita = rs.getString("motivo_visita");
                    String quienAutoriza = rs.getString("quien_autoriza");

                    // Mostrar los datos en la UI
                    pivtxtget_nombre_visitante.setText(nombreVisitante);
                    pivtxtget_numero_documento_visitante.setText(numeroDocumento);
                    pivjComboBoxget_apto.setSelectedItem(numeroApartamento);
                    pivtxtget_placa.setText(placaVehicular);
                    pivtxtget_motivo_visita.setText(motivoVisita);
                    piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);

                    // Actualizar la tabla con los resultados de la búsqueda
//                    actualizarTablaConResultados(rs);
                } else {
                    JOptionPane.showMessageDialog(null, "El visitante nunca ha ingresado");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejo adecuado de la excepción
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Este método se encarga de cerrar el JFrame actual, y mostrar otro

    private void metSetV1Ajuste() {
        U3AdminOpciones HALOID = new U3AdminOpciones();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: VISITANTES > REGISTAR INGRESO VISITANTE

    public void metRegistrarIngresoVisitante() {
        // Obtener los valores de los campos de entrada
        String pivRegistroNombreVisitante = pivtxtget_nombre_visitante.getText().toUpperCase();
        String pivRegistroNumeroDocumento = pivtxtget_numero_documento_visitante.getText().toUpperCase();
        String pivRegistroApto = pivjComboBoxget_apto.getSelectedItem().toString();
        String pivRegistroPlaca = pivtxtget_placa.getText().toUpperCase();
        String pivRegistroMotivoVisita = pivtxtget_motivo_visita.getText().toUpperCase();
        String pivRegistroQuienAutoriza = piv_jComboBox_quien_autoriza.getSelectedItem().toString();
        String pivRegistroVigilante = pivtxtget_vigilante.getText().toUpperCase();

        // obtener la fecha y hora actual del sistema
        LocalDateTime now = LocalDateTime.now();
        String pivRegistroHoraIngreso = now.toString();

        // Establecer valores constantes
        final String pivRegistroTipoUsuario = "Visitante";
        final String pivEstado1 = "Adentro";
        final String pivEstado2 = "Afuera";

        // Validar que los campos obligatorios estén completos
        if (pivRegistroNombreVisitante.isEmpty() || pivRegistroNumeroDocumento.isEmpty() || pivRegistroMotivoVisita.isEmpty() || pivRegistroVigilante.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los campos (Nombre visitante, numero documento, motivo visita) son obligatorios");
            return;
        }
        //Manejo de errores y excepciones, conexion a base de datos y registro
        try {
            String sqlRegistarVisitante = "INSERT INTO ta4_control_peatonal (fecha_hora_ingreso, nombre_visitante, numero_documento_visitante, numero_apartamento, quien_autoriza, motivo_visita, estado_visitante, placa_vehicular, tipo_usuario, nombre_vigilante) VALUES (?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = cn.prepareStatement(sqlRegistarVisitante);
            pst.setString(1, pivRegistroHoraIngreso);
            pst.setString(2, pivRegistroNombreVisitante);
            pst.setString(3, pivRegistroNumeroDocumento);
            pst.setString(4, pivRegistroApto);
            pst.setString(5, pivRegistroQuienAutoriza);
            pst.setString(6, pivRegistroMotivoVisita);
            pst.setString(7, pivEstado1);
            pst.setString(8, pivRegistroPlaca);
            pst.setString(9, pivRegistroTipoUsuario);
            pst.setString(10, pivRegistroVigilante);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);

            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/Ico_bd_ok.png"));
            JOptionPane.showMessageDialog(null, "Visitante " + pivRegistroNombreVisitante + " Registrado",
                    "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);

            // Enviar un correo al usuario
//            String proveedor = "hotmail"; // o "hotmail"
//            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
//            String contraseña = "yuceivfuepcfkiem";
//            String destinatario = registroCorreoElectronico;
//            String asunto = "Ser Seguridad - Parque Natura";
//            String mensaje = "[Ser Seguridad Ltda] le informa que su nuevo usuario ha sido creado exitosamente.";
//            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);
        } catch (SQLException ex) {
            System.out.println("Visitante No Registrado: " + ex);

            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Visitante No Registrado: " + ex,
                    "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);
        }

        // Establecer los campos en blanco
        pivtxtget_nombre_visitante.setText("");
        pivtxtget_numero_documento_visitante.setText("");
        pivjComboBoxget_apto.setSelectedItem(null);
        pivtxtget_placa.setText("");
        pivtxtget_motivo_visita.setText("");
        piv_jComboBox_quien_autoriza.setSelectedItem(null);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: CORRESPONDENCIA > REGISTRAR INGRESO CORRESPONDENCIA

    public void metRegistrarIngresoCorrespondencia() {
        // Obtener los valores de los campos de entrada
        String picQuienTrae = pic_get_quien_trae.getText().toUpperCase();
        String picTipoPaquete = pic_get_tipo_paquete.getSelectedItem().toString();
        String picNumeroGuia = pic_get_numero_guia.getText();
        String picApartamento = pic_get_apartamento.getSelectedItem().toString();
        String picNombreResidente = pic_get_nombre_residente.getSelectedItem().toString();
        String picVigilante = pictxtget_vigilante.getText();

        // obtener la fecha y hora actual del sistema
        LocalDateTime now = LocalDateTime.now();
        String picRegistroHoraIngreso = now.toString();

        // Establecer valores constantes
        final String picEstado1 = "Pendiente";
        final String picEstado2 = "Entregado";

        // Validar que los campos obligatorios estén completos
        if (picQuienTrae.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo ¿quien trae es obligatorio");
            return;
        }
        //Manejo de errores y excepciones, conexion a base de datos y registro
        try {
            String sqlRegistarPaquete = "INSERT INTO ta5_control_correspondencia (fecha_hora_ingreso, quien_trae, tipo_paquete, numero_guia, numero_apartamento, a_nombre_de, estado_paquete, nombre_vigilante) VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement pst = cn.prepareStatement(sqlRegistarPaquete);
            pst.setString(1, picRegistroHoraIngreso);
            pst.setString(2, picQuienTrae);
            pst.setString(3, picTipoPaquete);
            pst.setString(4, picNumeroGuia);
            pst.setString(5, picApartamento);
            pst.setString(6, picNombreResidente);
            pst.setString(7, picEstado1);
            pst.setString(8, picVigilante);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableMostrarCorrespondenciaRegistrada(JTableControlCorrespondencia);

            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/Ico_bd_ok.png"));
            JOptionPane.showMessageDialog(null, "Correspondencia a nombre de : " + picNombreResidente + " Registrada",
                    "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);

            // Enviar un correo al usuario
//            String proveedor = "hotmail"; // o "hotmail"
//            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
//            String contraseña = "yuceivfuepcfkiem";
//            String destinatario = registroCorreoElectronico;
//            String asunto = "Ser Seguridad - Parque Natura";
//            String mensaje = "[Ser Seguridad Ltda] le informa que su nuevo usuario ha sido creado exitosamente.";
//            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);
        } catch (SQLException ex_31) {
            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Correspondencia no registrada metRegistrarIngresoCorrespondencia(): " + ex_31,
                    "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);
        }

        // Establecer los campos en blanco
        pivtxtget_nombre_visitante.setText("");
        pivtxtget_numero_documento_visitante.setText("");
        pivjComboBoxget_apto.setSelectedItem(null);
        pivtxtget_placa.setText("");
        pivtxtget_motivo_visita.setText("");
        piv_jComboBox_quien_autoriza.setSelectedItem(null);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: VEHICULAR PROPIETARIOS > REGISTRAR INGRESO VEHICULAR

    private void metRegistroIngresoVehicularPropietarios() {
        // (ventana control vehicular propietarios)[metodo realizar ingreso vehicular propietarios]
        try {
            // Obtener la fila seleccionada en la tabla
            int filaSeleccionadaVehicular = JTableControlVehicularPropietarios.getSelectedRow();

            // Verificar si se ha seleccionado alguna fila
            if (filaSeleccionadaVehicular == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un vehiculo para realizar el ingreso");
                return;
            }

            // Obtener el ID del control vehicular propietarios de la fila seleccionada
            int idControlVehicular = (int) JTableControlVehicularPropietarios.getValueAt(filaSeleccionadaVehicular, 0);

            // Verificar si el vehiculo ya ha ingresado
            String consultaIngresoVehicular = "SELECT fecha_hora_ingreso FROM ta6_control_vehicular_propietarios WHERE id_control_vehicular_propietarios = ?";
            try (PreparedStatement pstConsultaVehicular = cn.prepareStatement(consultaIngresoVehicular)) {
                pstConsultaVehicular.setInt(1, idControlVehicular);
                ResultSet rsConsulta = pstConsultaVehicular.executeQuery();

                if (rsConsulta.next()) {
                    // Si ya tiene fecha de ingreso, mostrar un mensaje y salir del método
                    if (rsConsulta.getString("fecha_hora_ingreso") != null) {
                        JOptionPane.showMessageDialog(null, "Este vehiculo ya ha ingresado");
                        return;
                    }
                } else {
                    // No se encontró el registro, mostrar un mensaje y salir del método
                    JOptionPane.showMessageDialog(null, "Error al realizar el ingreso del vehiculo");
                    return;
                }
            }

            // Obtener la fecha y hora actual del sistema
            LocalDateTime now = LocalDateTime.now();
            String fechaHoraIngresoVehicular = now.toString();
            String estadoVehiculo = "Adentro";

            // Actualizar el ingreso vehicular en la base de datos.
            String ejecutarIngresoVehicular = "UPDATE ta6_control_vehicular_propietarios SET fecha_hora_ingreso = ?, vehiculo_estado = ? WHERE id_control_vehicular_propietarios = ?";
            try (PreparedStatement pst = cn.prepareStatement(ejecutarIngresoVehicular)) {
                pst.setString(1, fechaHoraIngresoVehicular);
                pst.setString(2, estadoVehiculo);  // Cambiar el estado a "Adentro"
                pst.setInt(3, idControlVehicular);

                int filasActualizadas = pst.executeUpdate();

                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(null, "Ingreso realizado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al realizar el ingreso");
                }
            }

            // Actualizar la tabla con los datos actualizados
            metJTableMostrarIngresoVehicularPropietarios(JTableControlVehicularPropietarios);

        } catch (SQLException ex_realizarIngresoVehicularPropietarios) {
            System.out.println("Error: realizarIngresoVehicularPropietarios() : " + ex_realizarIngresoVehicularPropietarios);
            ex_realizarIngresoVehicularPropietarios.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: VISITANTES > ACTUALIZAR USUARIO VISITANTE
    public void metActualizarVisitante() {
        // Obtén los datos del formulario
        String actualizarNombreVisitante = pivtxtget_nombre_visitante.getText().toUpperCase();
        String actualizarNumeroDocumento = pivtxtget_numero_documento_visitante.getText().toUpperCase();
        String actualizarApartamento = pivjComboBoxget_apto.getSelectedItem().toString();
        String actualizarPlacaVehicular = pivtxtget_placa.getText().toUpperCase();
        String actualizarMotivoVisita = pivtxtget_motivo_visita.getText().toUpperCase();
        String actualizarQuienAutoriza = piv_jComboBox_quien_autoriza.getSelectedItem().toString();

        // Obtén el ID del usuario seleccionado en la tabla
        int filaSeleccionada = JTableControlVisitantes.getSelectedRow();
        int idUsuario = Integer.parseInt(JTableControlVisitantes.getValueAt(filaSeleccionada, 0).toString());

        try {
            // Consulta SQL para actualizar el usuario
            String consultaSQL = "UPDATE ta4_control_peatonal SET nombre_visitante = ?, numero_documento_visitante = ?, numero_apartamento = ?, placa_vehicular = ?, motivo_visita = ?, quien_autoriza = ? WHERE id_control_peatonal = ?";

            PreparedStatement pst = cn.prepareStatement(consultaSQL);
            pst.setString(1, actualizarNombreVisitante);
            pst.setString(2, actualizarNumeroDocumento);
            pst.setString(3, actualizarApartamento);
            pst.setString(4, actualizarPlacaVehicular);
            pst.setString(5, actualizarMotivoVisita);
            pst.setString(6, actualizarQuienAutoriza);
            pst.setInt(7, idUsuario);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);

            JOptionPane.showMessageDialog(null, "Visitante actualizado correctamente");

            // Enviar un correo al usuario
//            String proveedor = "hotmail"; // o "hotmail"
//            String remitente = "luiseduardo192@hotmail.com"; // o "tu_correo@hotmail.com"
//            String contraseña = "yuceivfuepcfkiem";
//            String destinatario = actualizarCorreoElectronico;
//            String asunto = "Ser Seguridad - Parque Natura";
//            String mensaje = "[Ser Seguridad Ltda] le informa que su usuario ha sido actualizado.";
//            enviarCorreo(proveedor, remitente, contraseña, destinatario, asunto, mensaje);
        } catch (SQLException ex_05) {
            System.out.println("Error al actualizar usuario: " + ex_05);
        }
        // Establecer los campos en blanco
        pivtxtget_nombre_visitante.setText("");
        pivtxtget_numero_documento_visitante.setText("");
        pivjComboBoxget_apto.setSelectedItem(null);
        pivtxtget_placa.setText("");
        pivtxtget_motivo_visita.setText("");
        piv_jComboBox_quien_autoriza.setSelectedItem(null);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: VISITANTES > MOSTRAR REGISTROS DE VISITANTES JTABLE

    public void metJTableMostrarVisitantesRegistrados(JTable tablaIngresoVisitantes) {
        try {
            // Consulta para obtener los visitantes registrados
            String consult_db_01 = "SELECT id_control_peatonal, fecha_hora_ingreso, fecha_hora_salida, nombre_visitante, numero_documento_visitante, numero_apartamento, quien_autoriza, motivo_visita, estado_visitante, placa_vehicular, tipo_usuario, nombre_vigilante FROM ta4_control_peatonal WHERE fecha_hora_ingreso;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consult_db_01);

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Fecha hora ingreso");
            model.addColumn("Fecha hora salida");
            model.addColumn("Nombre visitante");
            model.addColumn("# documento");
            model.addColumn("Apto");
            model.addColumn("Quien autoriza");
            model.addColumn("Motivo visita");
            model.addColumn("Estado ");
            model.addColumn("Placa");
            model.addColumn("Tipo");
            model.addColumn("Vigilante");

            // Llenar el modelo con los datos de la consulta
            while (rs.next()) {
                Object[] row = new Object[12];
                row[0] = rs.getInt("id_control_peatonal");
                row[1] = rs.getString("fecha_hora_ingreso");
                row[2] = rs.getString("fecha_hora_salida");
                row[3] = rs.getString("nombre_visitante");
                row[4] = rs.getString("numero_documento_visitante");
                row[5] = rs.getString("numero_apartamento");
                row[6] = rs.getString("quien_autoriza");
                row[7] = rs.getString("motivo_visita");
                row[8] = rs.getString("estado_visitante");
                row[9] = rs.getString("placa_vehicular");
                row[10] = rs.getString("tipo_usuario");
                row[11] = rs.getString("nombre_vigilante");
                model.addRow(row);
            }

            // Asignar el modelo a la tabla
            tablaIngresoVisitantes.setModel(model);

            // Agregar renderizador personalizado para cambiar el color de fondo de las filas
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Obtener el valor de la columna "estado_visitante" en el modelo de la tabla
                    String estadoVisitante = table.getModel().getValueAt(row, 8).toString();

                    // Establecer el color de fondo según el estado del visitante
                    if ("Adentro".equals(estadoVisitante)) {
                        comp.setBackground(Color.ORANGE);
                    } else if ("Afuera".equals(estadoVisitante)) {
                        comp.setBackground(Color.WHITE);
                    } else {
                        // Puedes manejar otros estados o dejar el fondo predeterminado
                        comp.setBackground(table.getBackground());
                    }

                    // Establecer el color de la fuente siempre en negro
                    comp.setForeground(Color.BLACK);

                    // Asegurar que se seleccione toda la fila
                    table.setRowSelectionAllowed(true);

                    // Configurar el color de fondo de la fila seleccionada
                    if (isSelected) {
                        comp.setBackground(table.getSelectionBackground());
                    }

                    return comp;
                }
            };

            // Aplicar el renderizador a todas las columnas
            for (int i = 0; i < tablaIngresoVisitantes.getColumnCount(); i++) {
                tablaIngresoVisitantes.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            // Configurar el ancho de las columnas
            tablaIngresoVisitantes.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
            tablaIngresoVisitantes.getColumnModel().getColumn(1).setPreferredWidth(115); // fecha y hora de ingreso
            tablaIngresoVisitantes.getColumnModel().getColumn(2).setPreferredWidth(115); // fecha y hora salida
            tablaIngresoVisitantes.getColumnModel().getColumn(3).setPreferredWidth(200); // nombre visitante
            tablaIngresoVisitantes.getColumnModel().getColumn(4).setPreferredWidth(70);  // # documento
            tablaIngresoVisitantes.getColumnModel().getColumn(5).setPreferredWidth(35);  // apartamento
            tablaIngresoVisitantes.getColumnModel().getColumn(6).setPreferredWidth(150); // ¿quien autoriza?
            tablaIngresoVisitantes.getColumnModel().getColumn(7).setPreferredWidth(150); // motivo visita
            tablaIngresoVisitantes.getColumnModel().getColumn(8).setPreferredWidth(55); // estado visitante
            tablaIngresoVisitantes.getColumnModel().getColumn(9).setPreferredWidth(60);  // placa vehicular
            tablaIngresoVisitantes.getColumnModel().getColumn(10).setPreferredWidth(65); // tipo usuario
            tablaIngresoVisitantes.getColumnModel().getColumn(11).setPreferredWidth(200); // nombre vigilante

        } catch (SQLException ex_10) {
            // Manejar excepciones al mostrar los visitantes registrados
            System.out.println("Error al mostrar los visitantes registrados --> metJTableMostrarVisitantesRegistrados() : " + ex_10);
        }
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //METODO: VISITANTES > MOSTRAR REGISTROS DE CORRESPONDENCIA JTABLE

    public void metJTableMostrarCorrespondenciaRegistrada(JTable tablaIngresoCorrespondencia) {
        try {
            // Consulta para obtener los visitantes registrados
            String consult_db_02 = "SELECT id_control_correspondencia, fecha_hora_ingreso, numero_guia, quien_trae, numero_guia, tipo_paquete, numero_apartamento, a_nombre_de, fecha_hora_salida, quien_reclama, estado_paquete, observacion, nombre_vigilante FROM ta5_control_correspondencia WHERE fecha_hora_ingreso;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consult_db_02);

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Fecha hora ingreso");
            model.addColumn("Quien lo trae");
            model.addColumn("# Guia");
            model.addColumn("Tipo paquete");
            model.addColumn("Apto");
            model.addColumn("Residente");
            model.addColumn("Fecha hora salida");
            model.addColumn("Quien reclama");
            model.addColumn("Estado");
            model.addColumn("Observación ");
            model.addColumn("Vigilante");

            // Llenar el modelo con los datos de la consulta
            while (rs.next()) {
                Object[] row = new Object[13];
                row[0] = rs.getInt("id_control_correspondencia");
                row[1] = rs.getString("fecha_hora_ingreso");
                row[2] = rs.getString("quien_trae");
                row[3] = rs.getString("numero_guia");
                row[4] = rs.getString("tipo_paquete");
                row[5] = rs.getString("numero_apartamento");
                row[6] = rs.getString("a_nombre_de");
                row[7] = rs.getString("fecha_hora_salida");
                row[8] = rs.getString("quien_reclama");
                row[9] = rs.getString("estado_paquete");
                row[10] = rs.getString("observacion");
                row[11] = rs.getString("nombre_vigilante");
                model.addRow(row);
            }
            // Asignar el modelo a la tabla
            tablaIngresoCorrespondencia.setModel(model);
            // Agregar renderizador personalizado para cambiar el color de fondo de las filas
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Obtener el valor de la columna "estado_visitante" en el modelo de la tabla
                    String estadoVisitante = table.getModel().getValueAt(row, 9).toString();

                    // Establecer el color de fondo según el estado del visitante
                    if ("Pendiente".equals(estadoVisitante)) {
                        comp.setBackground(Color.ORANGE);
                    } else if ("Entregado".equals(estadoVisitante)) {
                        comp.setBackground(Color.WHITE);
                    } else {
                        // Puedes manejar otros estados o dejar el fondo predeterminado
                        comp.setBackground(table.getBackground());
                    }

                    // Establecer el color de la fuente siempre en negro
                    comp.setForeground(Color.BLACK);

                    // Asegurar que se seleccione toda la fila
                    table.setRowSelectionAllowed(true);

                    // Configurar el color de fondo de la fila seleccionada
                    if (isSelected) {
                        comp.setBackground(table.getSelectionBackground());
                    }

                    return comp;
                }
            };

            // Aplicar el renderizador a todas las columnas
            for (int i = 0; i < tablaIngresoCorrespondencia.getColumnCount(); i++) {
                tablaIngresoCorrespondencia.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            // Configurar el ancho de las columnas
            tablaIngresoCorrespondencia.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
            tablaIngresoCorrespondencia.getColumnModel().getColumn(1).setPreferredWidth(115); // fecha y hora de ingreso
            tablaIngresoCorrespondencia.getColumnModel().getColumn(2).setPreferredWidth(150); // Quien lo trae
            tablaIngresoCorrespondencia.getColumnModel().getColumn(3).setPreferredWidth(80); // # Guia
            tablaIngresoCorrespondencia.getColumnModel().getColumn(4).setPreferredWidth(130); // Tipo paquete
            tablaIngresoCorrespondencia.getColumnModel().getColumn(5).setPreferredWidth(50);  // apartamento
            tablaIngresoCorrespondencia.getColumnModel().getColumn(6).setPreferredWidth(170); // Residente
            tablaIngresoCorrespondencia.getColumnModel().getColumn(7).setPreferredWidth(115); // fecha hora salida
            tablaIngresoCorrespondencia.getColumnModel().getColumn(8).setPreferredWidth(170); // quien reclama
            tablaIngresoCorrespondencia.getColumnModel().getColumn(9).setPreferredWidth(80); // Estado correspondencia
            tablaIngresoCorrespondencia.getColumnModel().getColumn(10).setPreferredWidth(130); // observacion
            tablaIngresoCorrespondencia.getColumnModel().getColumn(11).setPreferredWidth(180); // vigilante

        } catch (SQLException ex_11) {
            System.out.println("Error al mostrar los visitantes registrados --> metJTableMostrarVisitantesRegistrados() : " + ex_11);
        }
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // METODO: VISITANTES MOSTRAR REGISTROS VEHICULARES PROPIETARIOS JTABLE

    public void metJTableMostrarIngresoVehicularPropietarios(JTable tablaIngresoVehicular) {
        try {
            // Consulta para obtener los visitantes registrados
            String consult_db_03 = "SELECT id_control_vehicular_propietarios, fecha_hora_ingreso, fecha_hora_salida, tipo_dueño,tipo_vehiculo,placa_vehicular,numero_apartamento,nombre_propietario,nombre_visitante,visitante_autorizado_por,vehiculo_espejo,vehiculo_estrellado,vehiculo_rayado,vehiculo_observacion,vehiculo_estado,nombre_vigilante FROM ta6_control_vehicular_propietarios WHERE fecha_hora_salida;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consult_db_03);

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Id");
            model.addColumn("Fecha Ingreso");
            model.addColumn("Fecha Salida");
            model.addColumn("Tipo Dueño");
            model.addColumn("Tipo Veh");
            model.addColumn("Placa");
            model.addColumn("Apt");
            model.addColumn("Propietario");
            model.addColumn("Nombre Visitante");
            model.addColumn("Autorizado por");
            model.addColumn("Espejo");
            model.addColumn("Estrellado ");
            model.addColumn("Rayado");
            model.addColumn("Observacion");
            model.addColumn("Estado");
            model.addColumn("Vigilante");

            // Llenar el modelo con los datos de la consulta de la base de datos
            while (rs.next()) {
                Object[] row = new Object[16];
                row[0] = rs.getInt("id_control_vehicular_propietarios");
                row[1] = rs.getString("fecha_hora_ingreso");
                row[2] = rs.getString("fecha_hora_salida");
                row[3] = rs.getString("tipo_dueño");
                row[4] = rs.getString("tipo_vehiculo");
                row[5] = rs.getString("placa_vehicular");
                row[6] = rs.getString("numero_apartamento");
                row[7] = rs.getString("nombre_propietario");
                row[8] = rs.getString("nombre_visitante");
                row[9] = rs.getString("visitante_autorizado_por");
                row[10] = rs.getString("vehiculo_espejo");
                row[11] = rs.getString("vehiculo_estrellado");
                row[12] = rs.getString("vehiculo_rayado");
                row[13] = rs.getString("vehiculo_observacion");
                row[14] = rs.getString("vehiculo_estado");
                row[15] = rs.getString("nombre_vigilante");
                model.addRow(row);
            }
            // Asignar el modelo a la tabla
            tablaIngresoVehicular.setModel(model);
            // Agregar renderizador personalizado para cambiar el color de fondo de las filas
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Obtener el valor de la columna "estado_visitante" en el modelo de la tabla
                    String estadoVisitante = table.getModel().getValueAt(row, 14).toString();

                    // Establecer el color de fondo según el estado del visitante
                    if ("Afuera".equals(estadoVisitante)) {
                        comp.setBackground(Color.ORANGE);
                    } else if ("Adentro".equals(estadoVisitante)) {
                        comp.setBackground(Color.WHITE);
                    } else {
                        // Puedes manejar otros estados o dejar el fondo predeterminado
                        comp.setBackground(table.getBackground());
                    }

                    // Establecer el color de la fuente siempre en negro
                    comp.setForeground(Color.BLACK);

                    // Asegurar que se seleccione toda la fila
                    table.setRowSelectionAllowed(true);

                    // Configurar el color de fondo de la fila seleccionada
                    if (isSelected) {
                        comp.setBackground(table.getSelectionBackground());
                    }

                    return comp;
                }
            };

            // Aplicar el renderizador a todas las columnas
            for (int i = 0; i < tablaIngresoVehicular.getColumnCount(); i++) {
                tablaIngresoVehicular.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            // Configurar el ancho de las columnas
            tablaIngresoVehicular.getColumnModel().getColumn(1).setPreferredWidth(120); // fecha y hora de ingreso
            tablaIngresoVehicular.getColumnModel().getColumn(2).setPreferredWidth(120); // fecha y hora de salida
            tablaIngresoVehicular.getColumnModel().getColumn(3).setPreferredWidth(77); // Tipo dueño
            tablaIngresoVehicular.getColumnModel().getColumn(4).setPreferredWidth(60); // Tipo Vehiculo
            tablaIngresoVehicular.getColumnModel().getColumn(5).setPreferredWidth(55);  // Placa
            tablaIngresoVehicular.getColumnModel().getColumn(6).setPreferredWidth(35); // Apto
            tablaIngresoVehicular.getColumnModel().getColumn(7).setPreferredWidth(130); // Propietario
            tablaIngresoVehicular.getColumnModel().getColumn(8).setPreferredWidth(170); // Nombre Visitante
            tablaIngresoVehicular.getColumnModel().getColumn(9).setPreferredWidth(100); // Autorizado por
            tablaIngresoVehicular.getColumnModel().getColumn(10).setPreferredWidth(30); // Espejo
            tablaIngresoVehicular.getColumnModel().getColumn(11).setPreferredWidth(30); // Estrelaldo
            tablaIngresoVehicular.getColumnModel().getColumn(12).setPreferredWidth(30); // Rayado
            tablaIngresoVehicular.getColumnModel().getColumn(13).setPreferredWidth(50); // Observacion
            tablaIngresoVehicular.getColumnModel().getColumn(14).setPreferredWidth(60); // vehiculo estado
            tablaIngresoVehicular.getColumnModel().getColumn(15).setPreferredWidth(180); // vigilante

        } catch (SQLException ex_metJTableMostrarIngresoVehicularPropietarios) {
            System.out.println("Error > metJTableMostrarIngresoVehicularPropietarios(JTable tablaIngresoVehicular) : " + ex_metJTableMostrarIngresoVehicularPropietarios);
            ex_metJTableMostrarIngresoVehicularPropietarios.printStackTrace();
        }
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void metBuscarVisitantePorApartamento(String numApartamento) {
        try {
            // Validar que se haya ingresado un apartamento
            if (numApartamento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese un apartamento");
                return;
            }

            String consulta_buscarVisitantePorApartamento = "SELECT * FROM ta4_control_peatonal WHERE numero_apartamento = ?";
            try (PreparedStatement pst = cn.prepareStatement(consulta_buscarVisitantePorApartamento)) {
                pst.setString(1, numApartamento);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                        String nombreVisitante = rs.getString("nombre_visitante");
                        String numeroDocumento = rs.getString("numero_documento_visitante");
                        String numeroApartamento = rs.getString("numero_apartamento");
                        String placaVehicular = rs.getString("placa_vehicular");
                        String motivoVisita = rs.getString("motivo_visita");
                        String quienAutoriza = rs.getString("quien_autoriza");
                        // ... continuar con los demás campos

                        // Mostrar los datos en la UI
                        pivtxtget_nombre_visitante.setText(nombreVisitante);
                        pivtxtget_numero_documento_visitante.setText(numeroDocumento);
                        pivjComboBoxget_apto.setSelectedItem(numApartamento);
                        pivtxtget_placa.setText(placaVehicular);
                        pivtxtget_motivo_visita.setText(motivoVisita);
                        piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);

                        // ... continuar con los demás campos
                    } else {
                        JOptionPane.showMessageDialog(null, "El visitante nunca ha ingresado");
                    }
                }
            }
        } catch (SQLException ex_buscarVisitantePorNumeroDocumento) {
            ex_buscarVisitantePorNumeroDocumento.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //METODO : VISITANTE > BUSCAR VISITANTE POR PLACA VEHICULAR (ARCHIVADO)
    /*public void buscarVisitantePorPlacaVehicular() {
        try {
            // Obtener la placa vehicular del JTextField
            String placaVehicular = pivtxtget_placa.getText();

            // Validar que se haya ingresado una placa vehicular
            if (placaVehicular.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una placa vehicular");
                return;
            }

            String consulta = "SELECT * FROM ta4_control_peatonal WHERE placa_vehicular = ?";
            try (PreparedStatement pst = cn.prepareStatement(consulta)) {
                pst.setString(1, placaVehicular);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                        // Por ejemplo:
                        String nombreVisitante = rs.getString("nombre_visitante");
                        String nombreDocumento = rs.getString("numero_documento_visitante");
                        String numeroApartamento = rs.getString("numero_apartamento");
//                    String placaVehicular = rs.getString("placa_vehicular");
                        String motivoVisita = rs.getString("motivo_visita");
                        String quienAutoriza = rs.getString("quien_autoriza");
                        // ... continuar con los demás campos

                        // Mostrar los datos en la UI, por ejemplo:
                        pivtxtget_nombre_visitante.setText(nombreVisitante);
                        pivtxtget_numero_documento_visitante.setText(nombreDocumento);
                        pivjComboBoxget_apto.setSelectedItem(numeroApartamento);
//                    pivtxtget_placa.setText(placaVehicular);
                        pivtxtget_motivo_visita.setText(motivoVisita);
                        piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);
                        // ... continuar con los demás campos
                    } else {
                        JOptionPane.showMessageDialog(null, "El visitante nunca ha ingresado");
                    }
                }
            }
        } catch (SQLException ex_10) {
            ex_10.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*//METODO: BUSCAR VISITANTE POR NOMBRE (ARCHIVADO)
    public void buscarVisitantePorNombre(String buscarNombres) {
        try {
            // Validar que se haya ingresado un nombre
            if (buscarNombres.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escriba el nombre");
                return;
            }

            String consulta_buscarVisitantePorNombre = "SELECT * FROM ta4_control_peatonal WHERE nombre_visitante LIKE ?";
            try (PreparedStatement pst = cn.prepareStatement(consulta_buscarVisitantePorNombre)) {
                // Utilizar '%' para buscar coincidencias parciales
                pst.setString(1, "%" + buscarNombres + "%");

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                        // Aquí puedes manejar múltiples resultados si lo deseas

                        // Mostrar los datos en la UI
                        String nombreVisitante = rs.getString("nombre_visitante");
                        String numeroDocumento = rs.getString("numero_documento_visitante");
                        String numeroApartamento = rs.getString("numero_apartamento");
                        String placaVehicular = rs.getString("placa_vehicular");
                        String motivoVisita = rs.getString("motivo_visita");
                        String quienAutoriza = rs.getString("quien_autoriza");
                        // ... continuar con los demás campos

                        // Mostrar los datos en la UI
                        pivtxtget_nombre_visitante.setText(nombreVisitante);
                        pivtxtget_numero_documento_visitante.setText(numeroDocumento);
                        pivjComboBoxget_apto.setSelectedItem(numeroApartamento);
                        pivtxtget_placa.setText(placaVehicular);
                        pivtxtget_motivo_visita.setText(motivoVisita);
                        piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);

                        // ... continuar con los demás campos
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontraron visitantes con ese nombre");
                    }
                }
            }
        } catch (SQLException ex_buscarVisitantePorNombre) {
            ex_buscarVisitantePorNombre.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*//METODO: BUSCAR VISITANTE POR ESTADO (ARCHIVADO)
    public void buscarVisitantePorEstado(String buscarEstado) {
        try {
            // Validar que se haya ingresado un Estado
            if (buscarEstado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escriba el estado del visitante");
                return;
            }

            String consulta_buscarVisitantePorEstado = "SELECT * FROM ta4_control_peatonal WHERE nombre_visitante";
            try (PreparedStatement pst = cn.prepareStatement(consulta_buscarVisitantePorEstado)) {
                pst.setString(1, buscarEstado);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                        // Aquí puedes manejar múltiples resultados si lo deseas

                        // Mostrar los datos en la UI
                        String nombreVisitante = rs.getString("nombre_visitante");
                        String numeroDocumento = rs.getString("numero_documento_visitante");
                        String numeroApartamento = rs.getString("numero_apartamento");
                        String placaVehicular = rs.getString("placa_vehicular");
                        String motivoVisita = rs.getString("motivo_visita");
                        String quienAutoriza = rs.getString("quien_autoriza");
                        // ... continuar con los demás campos

                        // Mostrar los datos en la UI
                        pivtxtget_nombre_visitante.setText(nombreVisitante);
                        pivtxtget_numero_documento_visitante.setText(numeroDocumento);
                        pivjComboBoxget_apto.setSelectedItem(numeroApartamento);
                        pivtxtget_placa.setText(placaVehicular);
                        pivtxtget_motivo_visita.setText(motivoVisita);
                        piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);

                        // ... continuar con los demás campos
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontraron visitantes con ese estado");
                    }
                }
            }
        } catch (SQLException ex_buscarVisitantePorEstado) {
            ex_buscarVisitantePorEstado.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*//METODO: BUSCAR VISITANTE POR FECHA (ARCHIVADO)

    public void buscarVisitantePorFecha(String buscarFecha) {
        try {
            // Validar que se haya ingresado una fecha
            if (buscarFecha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una fecha");
                return;
            }

            String consulta_buscarVisitantePorFecha = "SELECT * FROM ta4_control_peatonal WHERE fecha_hora_ingreso = ?";
            try (PreparedStatement pst = cn.prepareStatement(consulta_buscarVisitantePorFecha)) {
                pst.setString(1, buscarFecha);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El visitante está registrado, recuperar los datos y mostrarlos en la UI
                        String nombreVisitante = rs.getString("nombre_visitante");
                        String numeroDocumento = rs.getString("numero_documento_visitante");
                        String numeroApartamento = rs.getString("numero_apartamento");
                        String placaVehicular = rs.getString("placa_vehicular");
                        String motivoVisita = rs.getString("motivo_visita");
                        String quienAutoriza = rs.getString("quien_autoriza");
                        // ... continuar con los demás campos

                        // Mostrar los datos en la UI
                        pivtxtget_nombre_visitante.setText(nombreVisitante);
                        pivtxtget_numero_documento_visitante.setText(numeroDocumento);
                        pivjComboBoxget_apto.setSelectedItem(numeroApartamento);
                        pivtxtget_placa.setText(placaVehicular);
                        pivtxtget_motivo_visita.setText(motivoVisita);
                        piv_jComboBox_quien_autoriza.setSelectedItem(quienAutoriza);
                        // ... continuar con los demás campos
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontraron visitantes en la fecha especificada");
                    }
                }
            }
        } catch (SQLException ex_buscarVisitantePorFecha) {
            ex_buscarVisitantePorFecha.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void metBuscarCorrespondenciaPorNumeroGuia() {
        try {
            // Obtener el número de guía del JTextField
            String numeroGuiaCorrespondencia = pic_get_numero_guia.getText().trim();

            // Validar que se haya ingresado un número de guía
            if (numeroGuiaCorrespondencia.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese un número de guía");
                return;
            }

            // Crear la consulta SQL
            String consulta = "SELECT * FROM ta5_control_correspondencia WHERE numero_guia = ?";

            // Preparar la consulta y establecer el parámetro
            try (PreparedStatement pst = cn.prepareStatement(consulta)) {
                pst.setString(1, numeroGuiaCorrespondencia);

                // Ejecutar la consulta
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El número de guía existe, recuperar los datos y mostrarlos en la UI
                        String correspondenciaQuienTrae = rs.getString("quien_trae");
                        String correspondenciaTipoPaquete = rs.getString("tipo_paquete");
                        String correspondenciaApartamento = rs.getString("numero_apartamento");
                        String correspondenciaNombreResidente = rs.getString("a_nombre_de");
                        String correspondenciaQuienReclama = rs.getString("quien_reclama");
                        String correspondenciaObservacion = rs.getString("observacion");

                        // Asignar valores a los campos de texto en la UI
                        pic_get_quien_trae.setText(correspondenciaQuienTrae);
                        pic_get_tipo_paquete.setSelectedItem(correspondenciaTipoPaquete);
                        pic_get_apartamento.setSelectedItem(correspondenciaApartamento);
                        pic_get_nombre_residente.setSelectedItem(correspondenciaNombreResidente);
                        pic_get_quien_reclama.setText(correspondenciaQuienReclama);
                        pic_get_observacion.setText(correspondenciaObservacion);
                    } else {
                        JOptionPane.showMessageDialog(null, "El número de guía no se encuentra registrado");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo adecuado de la excepción (mostrar un mensaje de error al usuario)
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void metBuscarPorPlacaVehicularPropietario() {
        try {
            // Obtener la placa vehicular del JTextField
            String pipSalidaVehicularBPlaca = piptxtget_placa_vehicular.getText().trim();

            // Validar que se haya ingresado una placa vehicular
            if (pipSalidaVehicularBPlaca.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escriba una placa vehicular");
                return;
            }

            String consulta_sql_vehicular = "SELECT * FROM ta6_control_vehicular_propietarios WHERE placa_vehicular = ?";
            try (PreparedStatement pst = cn.prepareStatement(consulta_sql_vehicular)) {
                pst.setString(1, pipSalidaVehicularBPlaca);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // El propietario o visitante está registrado, recuperar los datos y mostrarlos en la UI
                        // Por ejemplo:
                        String pipSalidaVehicularBTipoDueño = rs.getString("tipo_dueño");
                        String pipSalidaVehicularBTipoVehiculo = rs.getString("tipo_vehiculo");
//                      String pipSalidaVehicularBPlaca  = rs.getString("placa_vehicular");
                        String pipSalidaVehicularBApto = rs.getString("numero_apartamento");
                        String pipSalidaVehicularBNombrePropietario = rs.getString("nombre_propietario");
                        String pipSalidaVehicularBNombreVisitante = rs.getString("nombre_visitante");
                        String pipSalidaVehicularBQuienAutoriza = rs.getString("visitante_autorizado_por");
                        String pipSalidaVehicularBEspejo = rs.getString("vehiculo_espejo");
                        String pipSalidaVehicularBEstrellado = rs.getString("vehiculo_estrellado");
                        String pipSalidaVehicularBRayado = rs.getString("vehiculo_rayado");
                        String pipSalidaVehicularBObservacion = rs.getString("vehiculo_observacion");
                        //...

                        // Mostrar los datos en la UI, por ejemplo:
                        piptxtget_tipo_dueño.setSelectedItem(pipSalidaVehicularBTipoDueño);
                        piptxtget_tipo_vehiculo.setSelectedItem(pipSalidaVehicularBTipoVehiculo);
                        piptxtget_get_apto.setSelectedItem(pipSalidaVehicularBApto);
//                      pivtxtget_placa.setText(placaVehicular);
                        piptxtget_nombre_propietario.setSelectedItem(pipSalidaVehicularBNombrePropietario);
                        piptxtget_nombre_visitante.setText(pipSalidaVehicularBNombreVisitante);
                        piptxtget_quien_autoriza.setSelectedItem(pipSalidaVehicularBQuienAutoriza);
                        piptxtget_vehicular_espejo.setSelectedItem(pipSalidaVehicularBEspejo);
                        piptxtget_vehicular_estrellado.setSelectedItem(pipSalidaVehicularBEstrellado);
                        piptxtget_vehicular_rayado.setSelectedItem(pipSalidaVehicularBRayado);
                        piptxtget_vehicular_observacion.setText(pipSalidaVehicularBObservacion);
                        // ... 
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe Registro de : " + pipSalidaVehicularBPlaca);
                    }
                }
            }
        } catch (SQLException ex_metBuscarPorPlacaVehicularPropietario) {
            // Manejo adecuado de la excepción
            System.out.println("Error metBuscarPorPlacaVehicularPropietario() " + ex_metBuscarPorPlacaVehicularPropietario);
            ex_metBuscarPorPlacaVehicularPropietario.printStackTrace();
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //(ventana control peatonal)[metodo salida peatonal]

    public void metRealizarSalidaVisitante() {
        try {
            // Obtener la fila seleccionada en la tabla
            int filaSeleccionada = JTableControlVisitantes.getSelectedRow();

            // Verificar si se ha seleccionado alguna fila
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un visitante de la tabla para realizar la salida");
                return;
            }

            // Obtener el ID del control peatonal de la fila seleccionada
            int idControlPeatonal = (int) JTableControlVisitantes.getValueAt(filaSeleccionada, 0);

            // Verificar si el visitante ya ha salido
            String consultaSalida = "SELECT fecha_hora_salida FROM ta4_control_peatonal WHERE id_control_peatonal = ?";
            try (PreparedStatement pstConsulta = cn.prepareStatement(consultaSalida)) {
                pstConsulta.setInt(1, idControlPeatonal);
                ResultSet rsConsulta = pstConsulta.executeQuery();

                if (rsConsulta.next()) {
                    // Si ya tiene fecha de salida, mostrar un mensaje y salir del método
                    if (rsConsulta.getString("fecha_hora_salida") != null) {
                        JOptionPane.showMessageDialog(null, "Este visitante ya ha salido");
                        return;
                    }
                } else {
                    // No se encontró el registro, mostrar un mensaje y salir del método
                    JOptionPane.showMessageDialog(null, "Error al consultar la salida del visitante");
                    return;
                }
            }

            // Obtener la fecha y hora actual del sistema
            LocalDateTime now = LocalDateTime.now();
            String fechaHoraSalida = now.toString();

            // Actualizar la salida del visitante en la base de datos
            String actualizarSalida = "UPDATE ta4_control_peatonal SET fecha_hora_salida = ?, estado_visitante = ? WHERE id_control_peatonal = ?";
            try (PreparedStatement pst = cn.prepareStatement(actualizarSalida)) {
                pst.setString(1, fechaHoraSalida);
                pst.setString(2, "Afuera");  // Cambiar el estado a "Afuera"
                pst.setInt(3, idControlPeatonal);

                int filasActualizadas = pst.executeUpdate();

                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(null, "Salida realizada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al realizar la salida");
                }
            }

            // Actualizar la tabla con los datos actualizados
            metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);

        } catch (SQLException ex_13) {
            ex_13.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //(ventana control correspondencia)[metodo reealizar salida correspondencia]

    public void metRealizarSalidaCorrespondencia() {
        try {
            // Obtener la fila seleccionada en la tabla
            int filaSeleccionada = JTableControlCorrespondencia.getSelectedRow();

            // Verificar si se ha seleccionado alguna fila
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una correspondencia para realizar la salida");
                return;
            }

            // Obtener el ID de control correspondencia de la fila seleccionada
            int idControlCorrespondencia = (int) JTableControlCorrespondencia.getValueAt(filaSeleccionada, 0);

            // Obtener el texto del JTextField para quien reclama la correspondencia
            String verificarQuienReclamaPaquete = pic_get_quien_reclama.getText().trim();

            // Comprobar si el JTextField está vacío
            if (verificarQuienReclamaPaquete.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escriba quien reclama la correspondencia");
                return;
            }

            // Verificar si el visitante ya ha salido
            String consultaSalidaCorrespondencia = "SELECT fecha_hora_salida FROM ta5_control_correspondencia WHERE id_control_correspondencia = ?";
            try (PreparedStatement pstConsulta = cn.prepareStatement(consultaSalidaCorrespondencia)) {
                pstConsulta.setInt(1, idControlCorrespondencia);
                ResultSet rsConsulta = pstConsulta.executeQuery();

                if (rsConsulta.next()) {
                    // Si ya tiene fecha de salida, mostrar un mensaje y salir del método
                    if (rsConsulta.getString("fecha_hora_salida") != null) {
                        JOptionPane.showMessageDialog(null, "Esta correspondencia ya ha sido entregada");
                        return;
                    }
                } else {
                    // No se encontró el registro, mostrar un mensaje y salir del método
                    JOptionPane.showMessageDialog(null, "Error al consultar la salida de la correspondencia");
                    return;
                }
            }

            // Obtener la fecha y hora actual del sistema
            LocalDateTime now = LocalDateTime.now();
            String fechaHoraSalidaCorrespondencia = now.toString();
            String quienReclamaPaquete = pic_get_quien_reclama.getText();
            String observacionPaquete = pic_get_observacion.getText().toUpperCase();

            // Actualizar la salida de la correspondencia en la base de datos
            String actualizarSalidaCorrespondencia = "UPDATE ta5_control_correspondencia SET fecha_hora_salida = ?, quien_reclama = ?, observacion = ?, estado_paquete = ? WHERE id_control_correspondencia = ?";
            try (PreparedStatement pst = cn.prepareStatement(actualizarSalidaCorrespondencia)) {
                pst.setString(1, fechaHoraSalidaCorrespondencia);
                pst.setString(2, quienReclamaPaquete);
                pst.setString(3, observacionPaquete);
                pst.setString(4, "Entregado");  // Actualiza estado "Pendiente" a "Entregado"
                pst.setInt(5, idControlCorrespondencia);

                int filasActualizadas = pst.executeUpdate();

                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(null, "Entregado");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al entregar");
                }
            }

            // Actualizar la tabla con los datos actualizados
            metJTableMostrarCorrespondenciaRegistrada(JTableControlCorrespondencia);

        } catch (SQLException ex_13) {
            ex_13.printStackTrace(); // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//(ventana control vehicular propietarios)[metodo realizar salida propietarios]

    private void metRealizarSalidaVehicularPropietarios() {
        // ... [Tu código previo para obtener los valores del formulario]
        // Obtener los valores del formulario INGRESO - SALIDA VEHICULAR PROPIETARIOS
        String pipSalidaVehicularTipoDueño = piptxtget_tipo_dueño.getSelectedItem().toString();
        String pipSalidaVehicularTipoVehiculo = piptxtget_tipo_vehiculo.getSelectedItem().toString();
        String pipSalidaVehicularPlaca = piptxtget_placa_vehicular.getText().toUpperCase();
        String pipSalidaVehicularApto = piptxtget_get_apto.getSelectedItem().toString();
        String pipSalidaNombrePropietario = piptxtget_nombre_propietario.getSelectedItem().toString();
        String pipSalidaVehicularNombreVisitane = piptxtget_nombre_visitante.getText().toUpperCase();
        String pipSalidaVehicularQuienAutoriza = piptxtget_quien_autoriza.getSelectedItem().toString();
        String pipSalidaVehicularEspejo = piptxtget_vehicular_espejo.getSelectedItem().toString();
        String pipSalidaVehicularEstrellado = piptxtget_vehicular_estrellado.getSelectedItem().toString();
        String pipSalidaVehicularRayado = piptxtget_vehicular_rayado.getSelectedItem().toString();
        String pipSalidaVehicularObservacion = piptxtget_vehicular_observacion.getText().toUpperCase();
        String pipSalidaVehicularNombreVigilante = piptxtget_vigilante.getText().toUpperCase();

        // obtener la fecha y hora actual del sistema
        LocalDateTime now = LocalDateTime.now();
        String pipSalidaHoraSalida = now.toString();

        // Establecer valores constantes
        final String pipEstado1 = "Afuera";

        // Manejo de errores y excepciones, conexion a base de datos y registro
        try {
            // Verificar si el vehículo ya está marcado como 'Afuera'
            String verificarEstadoVehiculo = "SELECT vehiculo_estado FROM ta6_control_vehicular_propietarios WHERE placa_vehicular = ? ORDER BY id_control_vehicular_propietarios DESC LIMIT 1";
            PreparedStatement pstVerificar = cn.prepareStatement(verificarEstadoVehiculo);
            pstVerificar.setString(1, pipSalidaVehicularPlaca);
            ResultSet rsVerificar = pstVerificar.executeQuery();

            if (rsVerificar.next() && "Afuera".equals(rsVerificar.getString("vehiculo_estado"))) {
                JOptionPane.showMessageDialog(null, "El vehículo ya salió, debe realizar el ingreso para poder realizar una nueva salida");
                return;
            }

            // ... [Tu código para insertar el registro de salida]
            String sqlRegistarSalidaVehicularPropietarios = "INSERT INTO ta6_control_vehicular_propietarios (fecha_hora_salida, tipo_dueño, tipo_vehiculo, placa_vehicular, numero_apartamento, nombre_propietario, nombre_visitante, visitante_autorizado_por, vehiculo_espejo, vehiculo_estrellado, vehiculo_rayado, vehiculo_observacion, vehiculo_estado, nombre_vigilante) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = cn.prepareStatement(sqlRegistarSalidaVehicularPropietarios);
            pst.setString(1, pipSalidaHoraSalida);
            pst.setString(2, pipSalidaVehicularTipoDueño);
            pst.setString(3, pipSalidaVehicularTipoVehiculo);
            pst.setString(4, pipSalidaVehicularPlaca);
            pst.setString(5, pipSalidaVehicularApto);
            pst.setString(6, pipSalidaNombrePropietario);
            pst.setString(7, pipSalidaVehicularNombreVisitane);
            pst.setString(8, pipSalidaVehicularQuienAutoriza);
            pst.setString(9, pipSalidaVehicularEspejo);
            pst.setString(10, pipSalidaVehicularEstrellado);
            pst.setString(11, pipSalidaVehicularRayado);
            pst.setString(12, pipSalidaVehicularObservacion);
            pst.setString(13, pipEstado1);
            pst.setString(14, pipSalidaVehicularNombreVigilante);
            pst.executeUpdate();

            // Actualiza la tabla para reflejar los cambios
            metJTableMostrarIngresoVehicularPropietarios(JTableControlVehicularPropietarios);

            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/Ico_bd_ok.png"));
            JOptionPane.showMessageDialog(null, pipSalidaVehicularTipoDueño + " " + pipSalidaNombrePropietario + " Salio", "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);

            // Enviar un correo al usuario
            // El código para enviar un correo, si es necesario
        } catch (SQLException ex_metSalidaVehicularPropietarios) {
            // ... [Tu manejo de excepciones]
            // Imprime la traza de la pila de llamadas en la consola
            System.out.println("Error metSalidaVehicularPropietarios() :" + ex_metSalidaVehicularPropietarios);
            ex_metSalidaVehicularPropietarios.printStackTrace();

            Icon halo2 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, pipSalidaVehicularTipoDueño + " " + pipSalidaNombrePropietario + "NO REGISTRADO -> ERROR -> " + ex_metSalidaVehicularPropietarios, "Atencion", JOptionPane.YES_NO_CANCEL_OPTION, halo2);
        }

        // ... [Tu código para limpiar los campos]
        // Establecer los campos en blanco
        piptxtget_placa_vehicular.setText("");
        piptxtget_get_apto.setSelectedItem(null);
        piptxtget_nombre_propietario.setSelectedItem(null);
        piptxtget_quien_autoriza.setSelectedItem(null);
        piptxtget_vehicular_observacion.setText("");
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void metCargarApartamentosVisitantes() {
        //Carga de apartamentos para panel ingreso salida visitantes
        try {
            // Consulta para obtener la lista de apartamentos ordenados numéricamente
            String consulta_cargarApartamentos_01 = "SELECT DISTINCT numero_apartamento FROM ta2_propietarios ORDER BY LENGTH(numero_apartamento), numero_apartamento;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta_cargarApartamentos_01);

            // Limpiar el ComboBox antes de agregar nuevos elementos
            pivjComboBoxget_apto.removeAllItems();

            // Llenar el ComboBox con los apartamentos obtenidos
            while (rs.next()) {
                String apartamentoVisitantes = rs.getString("numero_apartamento");
                pivjComboBoxget_apto.addItem(apartamentoVisitantes);
            }
        } catch (SQLException ex_consulta_cargarApartamentos_01) {
            ex_consulta_cargarApartamentos_01.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void metCargarApartamentosCorrespondencia() {
        //Carga de apartamentos para panel ingreso salida correspondencia
        try {
            // Consulta para obtener la lista de apartamentos ordenados numéricamente
            String consulta_cargarApartamentos_02 = "SELECT DISTINCT numero_apartamento FROM ta2_propietarios ORDER BY LENGTH(numero_apartamento), numero_apartamento;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta_cargarApartamentos_02);

            // Limpiar el ComboBox antes de agregar nuevos elementos
            pic_get_apartamento.removeAllItems();

            // Llenar el ComboBox con los apartamentos obtenidos
            while (rs.next()) {
                String apartamentoCorrespondencia = rs.getString("numero_apartamento");
                pic_get_apartamento.addItem(apartamentoCorrespondencia);
            }
        } catch (SQLException ex_consulta_cargarApartamentos_02) {
            ex_consulta_cargarApartamentos_02.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void metCargarApartamentosVehicularPropietarios() {
        //Carga de apartamentos para panel ingreso salida correspondencia
        try {
            // Consulta para obtener la lista de apartamentos ordenados numéricamente
            String consulta_cargarApartamentos_03 = "SELECT DISTINCT numero_apartamento FROM ta2_propietarios ORDER BY LENGTH(numero_apartamento), numero_apartamento;";

            // Crear el statement y ejecutar la consulta
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta_cargarApartamentos_03);

            // Limpiar el ComboBox antes de agregar nuevos elementos
            piptxtget_get_apto.removeAllItems();

            // Llenar el ComboBox con los apartamentos obtenidos
            while (rs.next()) {
                String apartamentoVehicularCorrespondencia = rs.getString("numero_apartamento");
                piptxtget_get_apto.addItem(apartamentoVehicularCorrespondencia);
            }
        } catch (SQLException ex_consulta_cargarApartamentos_03) {
            ex_consulta_cargarApartamentos_03.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void cargarPropietariosPorApartamentoVisitantes(String numeroApartamentoVisitantes) {
        //Carga de apartamentos para panel ingreso salida visitantes
        try {
            // Consulta para obtener los propietarios del apartamento seleccionado
            String consulta_cargarPropietariosApartamentos_01 = "SELECT DISTINCT nombre_propietario FROM ta2_propietarios WHERE numero_apartamento = ?;";

            // Preparar la declaración
            try (PreparedStatement pst = cn.prepareStatement(consulta_cargarPropietariosApartamentos_01)) {
                pst.setString(1, numeroApartamentoVisitantes);

                // Ejecutar la consulta
                ResultSet rs = pst.executeQuery();

                // Limpiar el ComboBox antes de agregar nuevos elementos
                piv_jComboBox_quien_autoriza.removeAllItems();

                // Llenar el ComboBox con los nombres de propietarios obtenidos
                while (rs.next()) {
                    String propietarioVisitantes = rs.getString("nombre_propietario");
                    piv_jComboBox_quien_autoriza.addItem(propietarioVisitantes);
                }
            }
        } catch (SQLException ex_consulta_cargarPropietariosApartamentos_01) {
            ex_consulta_cargarPropietariosApartamentos_01.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void cargarPropietariosPorApartamentoCorrespondencia(String numeroApartamentoCorrespondencia) {
        //Carga de apartamentos para panel ingreso salida correspondencia
        try {
            // Consulta para obtener los propietarios del apartamento seleccionado
            String consulta_cargarPropietariosApartamentos_02 = "SELECT DISTINCT nombre_propietario FROM ta2_propietarios WHERE numero_apartamento = ?;";

            // Preparar la declaración
            try (PreparedStatement pst = cn.prepareStatement(consulta_cargarPropietariosApartamentos_02)) {
                pst.setString(1, numeroApartamentoCorrespondencia);

                // Ejecutar la consulta
                ResultSet rs = pst.executeQuery();

                // Limpiar el ComboBox antes de agregar nuevos elementos
                pic_get_nombre_residente.removeAllItems();

                // Llenar el ComboBox con los nombres de propietarios obtenidos
                while (rs.next()) {
                    String propietarioCorrespondencia = rs.getString("nombre_propietario");
                    pic_get_nombre_residente.addItem(propietarioCorrespondencia);
                }
            }
        } catch (SQLException ex_consulta_cargarPropietariosApartamentos_02) {
            ex_consulta_cargarPropietariosApartamentos_02.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void cargarPropietariosPorApartamentoVehicularPropietarios(String numeroApartamentoVehicularPropietarios) {
        //Carga de apartamentos para panel ingreso salida correspondencia
        try {
            // Consulta para obtener los propietarios del apartamento seleccionado
            String consulta_cargarPropietariosApartamentos_03 = "SELECT DISTINCT nombre_propietario FROM ta2_propietarios WHERE numero_apartamento = ?;";

            // Preparar la declaración
            try (PreparedStatement pst = cn.prepareStatement(consulta_cargarPropietariosApartamentos_03)) {
                pst.setString(1, numeroApartamentoVehicularPropietarios);

                // Ejecutar la consulta
                ResultSet rs = pst.executeQuery();

                // Limpiar el ComboBox antes de agregar nuevos elementos
                piptxtget_nombre_propietario.removeAllItems();
                piptxtget_quien_autoriza.removeAllItems();

                // Llenar el ComboBox con los nombres de propietarios obtenidos
                while (rs.next()) {
                    String propietarioVehicular = rs.getString("nombre_propietario");
                    piptxtget_nombre_propietario.addItem(propietarioVehicular);
                    piptxtget_quien_autoriza.addItem(propietarioVehicular);
                }
            }
        } catch (SQLException ex_consulta_cargarPropietariosApartamentos_03) {
            ex_consulta_cargarPropietariosApartamentos_03.printStackTrace();
            // Manejo adecuado de la excepción (podría mostrar un mensaje de error al usuario)
        }
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]

    private void actualizarEstilosBotones() {
        if (estaVisitanteActivo) {
            panel_btn_visitante.setBackground(new Color(213, 219, 219));
        } else {
            panel_btn_visitante.setBackground(new Color(255, 255, 255));
        }
        // Repite para los otros botones
        if (estaCorrespondenciaActivo) {
            panel_btn_correspondencia.setBackground(new Color(213, 219, 219));
        } else {
            panel_btn_correspondencia.setBackground(new Color(255, 255, 255));
        }
        // Repite para los otros botones
        if (estaVehicularActivo) {
            panel_btn_vehicular.setBackground(new Color(213, 219, 219));
        } else {
            panel_btn_vehicular.setBackground(new Color(255, 255, 255));
        }
        // Repite para los otros botones
        if (estaBuscarActivo) {
            panel_btn_buscar.setBackground(new Color(213, 219, 219));
        } else {
            panel_btn_buscar.setBackground(new Color(255, 255, 255));
        }
        // Repite para los otros botones
        if (estaAcercaDeActivo) {
            panel_btn_acercade.setBackground(new Color(213, 219, 219));
        } else {
            panel_btn_acercade.setBackground(new Color(255, 255, 255));
        }
        // Repite para los otros botones
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  
/* ++++++++++++++++++++++++++++++++++++++++++++++ FINALIZA METODOS - LOGICA ++++++++++++++++++++++++++++++++++++++++++++++ */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p_i_v_btn_buscar_placa = new javax.swing.JButton();
        p_i_v_btb_buscar_cedula = new javax.swing.JButton();
        panel_left = new javax.swing.JPanel();
        panel_nombre_software = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panel_btn_visitante = new javax.swing.JPanel();
        btn_i_visitante = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        panel_btn_correspondencia = new javax.swing.JPanel();
        btn_i_correspondencia = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        panel_btn_vehicular = new javax.swing.JPanel();
        btn_i_vehicular = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        panel_btn_buscar = new javax.swing.JPanel();
        btn_buscar = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        panel_btn_acercade = new javax.swing.JPanel();
        btn_acercade = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        panel_info_usuario = new javax.swing.JPanel();
        txtU5MotrarNombreUsuario = new javax.swing.JLabel();
        jCPanel1 = new com.bolivia.panel.JCPanel();
        jSeparator15 = new javax.swing.JSeparator();
        panel_rigth = new javax.swing.JPanel();
        panel_m_i_visitante = new javax.swing.JPanel();
        panel_titulo = new javax.swing.JPanel();
        sub_panel_titulo_txt_titulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableControlVisitantes = new javax.swing.JTable();
        panel_form_botones = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pivtxtget_nombre_visitante = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        pivtxtget_numero_documento_visitante = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        pivjComboBoxget_apto = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pivtxtget_placa = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        pivtxtget_motivo_visita = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        pivtxtget_vigilante = new javax.swing.JTextField();
        piv_jComboBox_quien_autoriza = new javax.swing.JComboBox<>();
        jSeparator16 = new javax.swing.JSeparator();
        pivtxtget_filtro_opciones = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        p_i_v_btn_ingreso_visitante = new javax.swing.JButton();
        p_i_v_btn_actualizar_visitante = new javax.swing.JButton();
        p_i_v_btn_exit_visitante = new javax.swing.JButton();
        p_i_v_btn_exit_visitante1 = new javax.swing.JButton();
        p_i_v_btn_buscar_filtro = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        pivtxtget_filtro = new javax.swing.JTextField();
        panel_m_i_correspondencia = new javax.swing.JPanel();
        panel_titulo1 = new javax.swing.JPanel();
        sub_panel_titulo_txt_titulo1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTableControlCorrespondencia = new javax.swing.JTable();
        panel_form_ingreso = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pic_get_quien_trae = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pic_get_tipo_paquete = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        pic_get_numero_guia = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        pic_get_apartamento = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        pic_btn_registrar_ingreso = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        pic_get_nombre_residente = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        pic_get_observacion = new javax.swing.JTextField();
        pic_btn_registrar_salida = new javax.swing.JButton();
        pic_btn_buscar_apartamento = new javax.swing.JButton();
        pic_btn_buscar_guia = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        pictxtget_vigilante = new javax.swing.JTextField();
        pic_get_quien_reclama = new javax.swing.JTextField();
        panel_m_i_vehiculos = new javax.swing.JPanel();
        subpanel_titulo2 = new javax.swing.JPanel();
        sub_panel_titulo_txt_titulo2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        piptxtget_tipo_dueño = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        piptxtget_tipo_vehiculo = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        piptxtget_get_apto = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        piptxtget_nombre_propietario = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        piptxtget_vehicular_espejo = new javax.swing.JComboBox<>();
        piptxtget_vehicular_estrellado = new javax.swing.JComboBox<>();
        piptxtget_vehicular_rayado = new javax.swing.JComboBox<>();
        piptxtget_vehicular_observacion = new javax.swing.JTextField();
        piptxtget_nombre_visitante = new javax.swing.JTextField();
        piptxtget_placa_vehicular = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        piptxtget_quien_autoriza = new javax.swing.JComboBox<>();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        pip_btn_buscar_placa = new javax.swing.JButton();
        pip_btn_registrar_ingreso = new javax.swing.JButton();
        pip_btn_registrar_salida = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        piptxtget_vigilante = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        pip_btn_limpiar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTableControlVehicularPropietarios = new javax.swing.JTable();

        p_i_v_btn_buscar_placa.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_buscar_placa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_40px.png"))); // NOI18N
        p_i_v_btn_buscar_placa.setText("Buscar placa");
        p_i_v_btn_buscar_placa.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_buscar_placa.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_32px.png"))); // NOI18N
        p_i_v_btn_buscar_placa.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_37px.png"))); // NOI18N
        p_i_v_btn_buscar_placa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_buscar_placaActionPerformed(evt);
            }
        });

        p_i_v_btb_buscar_cedula.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btb_buscar_cedula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarcedula_40px.png"))); // NOI18N
        p_i_v_btb_buscar_cedula.setText("Buscar cedula");
        p_i_v_btb_buscar_cedula.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btb_buscar_cedula.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarcedula_32px.png"))); // NOI18N
        p_i_v_btb_buscar_cedula.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarcedula_37px.png"))); // NOI18N
        p_i_v_btb_buscar_cedula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_i_v_btb_buscar_cedulaMouseClicked(evt);
            }
        });
        p_i_v_btb_buscar_cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btb_buscar_cedulaActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setMinimumSize(new java.awt.Dimension(1366, 768));

        panel_left.setBackground(new java.awt.Color(255, 255, 255));
        panel_left.setMaximumSize(new java.awt.Dimension(220, 768));
        panel_left.setMinimumSize(new java.awt.Dimension(220, 768));
        panel_left.setPreferredSize(new java.awt.Dimension(220, 768));

        panel_nombre_software.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sicovp");

        javax.swing.GroupLayout panel_nombre_softwareLayout = new javax.swing.GroupLayout(panel_nombre_software);
        panel_nombre_software.setLayout(panel_nombre_softwareLayout);
        panel_nombre_softwareLayout.setHorizontalGroup(
            panel_nombre_softwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nombre_softwareLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_nombre_softwareLayout.setVerticalGroup(
            panel_nombre_softwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nombre_softwareLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_btn_visitante.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_visitante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_visitanteMouseClicked(evt);
            }
        });

        btn_i_visitante.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        btn_i_visitante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_adduser_x43.png"))); // NOI18N
        btn_i_visitante.setText("Visitantes");
        btn_i_visitante.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn_i_visitante.setContentAreaFilled(false);
        btn_i_visitante.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_i_visitante.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_i_visitante.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_adduser_x35.png"))); // NOI18N
        btn_i_visitante.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_adduser_x39.png"))); // NOI18N
        btn_i_visitante.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_i_visitanteMouseMoved(evt);
            }
        });
        btn_i_visitante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_i_visitanteMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_i_visitanteMouseExited(evt);
            }
        });
        btn_i_visitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_i_visitanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_btn_visitanteLayout = new javax.swing.GroupLayout(panel_btn_visitante);
        panel_btn_visitante.setLayout(panel_btn_visitanteLayout);
        panel_btn_visitanteLayout.setHorizontalGroup(
            panel_btn_visitanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_visitante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_btn_visitanteLayout.setVerticalGroup(
            panel_btn_visitanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_visitante)
        );

        panel_btn_correspondencia.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_correspondencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_correspondenciaMouseClicked(evt);
            }
        });

        btn_i_correspondencia.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        btn_i_correspondencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addpaquete_x43.png"))); // NOI18N
        btn_i_correspondencia.setText("Correspondencia");
        btn_i_correspondencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn_i_correspondencia.setContentAreaFilled(false);
        btn_i_correspondencia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_i_correspondencia.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_i_correspondencia.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addpaquete_x35.png"))); // NOI18N
        btn_i_correspondencia.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addpaquete_x39.png"))); // NOI18N
        btn_i_correspondencia.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_i_correspondenciaMouseMoved(evt);
            }
        });
        btn_i_correspondencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_i_correspondenciaMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_i_correspondenciaMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panel_btn_correspondenciaLayout = new javax.swing.GroupLayout(panel_btn_correspondencia);
        panel_btn_correspondencia.setLayout(panel_btn_correspondenciaLayout);
        panel_btn_correspondenciaLayout.setHorizontalGroup(
            panel_btn_correspondenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_correspondencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_btn_correspondenciaLayout.setVerticalGroup(
            panel_btn_correspondenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_correspondencia)
        );

        panel_btn_vehicular.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_vehicular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_vehicularMouseClicked(evt);
            }
        });

        btn_i_vehicular.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        btn_i_vehicular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addcar_x43.png"))); // NOI18N
        btn_i_vehicular.setText("Vehicular Propietario");
        btn_i_vehicular.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn_i_vehicular.setContentAreaFilled(false);
        btn_i_vehicular.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_i_vehicular.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_i_vehicular.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addcar_x35.png"))); // NOI18N
        btn_i_vehicular.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_addcar_x39.png"))); // NOI18N
        btn_i_vehicular.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_i_vehicularMouseMoved(evt);
            }
        });
        btn_i_vehicular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_i_vehicularMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_i_vehicularMouseExited(evt);
            }
        });
        btn_i_vehicular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_i_vehicularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_btn_vehicularLayout = new javax.swing.GroupLayout(panel_btn_vehicular);
        panel_btn_vehicular.setLayout(panel_btn_vehicularLayout);
        panel_btn_vehicularLayout.setHorizontalGroup(
            panel_btn_vehicularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_vehicular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_btn_vehicularLayout.setVerticalGroup(
            panel_btn_vehicularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_i_vehicular)
        );

        panel_btn_buscar.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_buscarMouseClicked(evt);
            }
        });

        btn_buscar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_x43.png"))); // NOI18N
        btn_buscar.setText("Consultar");
        btn_buscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn_buscar.setContentAreaFilled(false);
        btn_buscar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_buscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_buscar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_x35.png"))); // NOI18N
        btn_buscar.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_x39.png"))); // NOI18N
        btn_buscar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_buscarMouseMoved(evt);
            }
        });
        btn_buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_buscarMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_buscarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panel_btn_buscarLayout = new javax.swing.GroupLayout(panel_btn_buscar);
        panel_btn_buscar.setLayout(panel_btn_buscarLayout);
        panel_btn_buscarLayout.setHorizontalGroup(
            panel_btn_buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_btn_buscarLayout.setVerticalGroup(
            panel_btn_buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_buscar)
        );

        panel_btn_acercade.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_acercade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_acercadeMouseClicked(evt);
            }
        });

        btn_acercade.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        btn_acercade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_acercade_x43.png"))); // NOI18N
        btn_acercade.setText("Acerca de");
        btn_acercade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btn_acercade.setContentAreaFilled(false);
        btn_acercade.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_acercade.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_acercade.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_acercade_x35.png"))); // NOI18N
        btn_acercade.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_acercade_x39.png"))); // NOI18N
        btn_acercade.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_acercadeMouseMoved(evt);
            }
        });
        btn_acercade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_acercadeMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_acercadeMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panel_btn_acercadeLayout = new javax.swing.GroupLayout(panel_btn_acercade);
        panel_btn_acercade.setLayout(panel_btn_acercadeLayout);
        panel_btn_acercadeLayout.setHorizontalGroup(
            panel_btn_acercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_acercade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_btn_acercadeLayout.setVerticalGroup(
            panel_btn_acercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_acercade)
        );

        panel_info_usuario.setBackground(new java.awt.Color(0, 0, 0));

        txtU5MotrarNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtU5MotrarNombreUsuario.setText("txtU5MotrarNombreUsuario");

        jCPanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vigilante.png"))); // NOI18N

        javax.swing.GroupLayout jCPanel1Layout = new javax.swing.GroupLayout(jCPanel1);
        jCPanel1.setLayout(jCPanel1Layout);
        jCPanel1Layout.setHorizontalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
        jCPanel1Layout.setVerticalGroup(
            jCPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_info_usuarioLayout = new javax.swing.GroupLayout(panel_info_usuario);
        panel_info_usuario.setLayout(panel_info_usuarioLayout);
        panel_info_usuarioLayout.setHorizontalGroup(
            panel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_info_usuarioLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jCPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtU5MotrarNombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );
        panel_info_usuarioLayout.setVerticalGroup(
            panel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_info_usuarioLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panel_info_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_info_usuarioLayout.createSequentialGroup()
                        .addComponent(jCPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtU5MotrarNombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panel_leftLayout = new javax.swing.GroupLayout(panel_left);
        panel_left.setLayout(panel_leftLayout);
        panel_leftLayout.setHorizontalGroup(
            panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_nombre_software, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_visitante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_correspondencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_vehicular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_acercade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_info_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator10)
            .addComponent(jSeparator11)
            .addComponent(jSeparator12)
            .addComponent(jSeparator13)
            .addComponent(jSeparator14)
            .addComponent(jSeparator15)
        );
        panel_leftLayout.setVerticalGroup(
            panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_leftLayout.createSequentialGroup()
                .addComponent(panel_nombre_software, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_btn_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_btn_correspondencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_btn_vehicular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_btn_acercade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panel_info_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 413, Short.MAX_VALUE))
        );

        getContentPane().add(panel_left, java.awt.BorderLayout.LINE_START);

        panel_rigth.setMinimumSize(new java.awt.Dimension(1146, 768));
        panel_rigth.setPreferredSize(new java.awt.Dimension(1146, 768));
        panel_rigth.setLayout(new java.awt.CardLayout());

        panel_m_i_visitante.setBackground(new java.awt.Color(255, 255, 255));
        panel_m_i_visitante.setMinimumSize(new java.awt.Dimension(1161, 720));
        panel_m_i_visitante.setPreferredSize(new java.awt.Dimension(1161, 720));

        panel_titulo.setBackground(new java.awt.Color(51, 51, 51));
        panel_titulo.setPreferredSize(new java.awt.Dimension(100, 47));

        sub_panel_titulo_txt_titulo.setFont(new java.awt.Font("Century Gothic", 1, 15)); // NOI18N
        sub_panel_titulo_txt_titulo.setForeground(new java.awt.Color(255, 255, 255));
        sub_panel_titulo_txt_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sub_panel_titulo_txt_titulo.setText("INGRESO VISITANTES - SALIDA VISITANTES");

        javax.swing.GroupLayout panel_tituloLayout = new javax.swing.GroupLayout(panel_titulo);
        panel_titulo.setLayout(panel_tituloLayout);
        panel_tituloLayout.setHorizontalGroup(
            panel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_tituloLayout.setVerticalGroup(
            panel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JTableControlVisitantes.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        JTableControlVisitantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTableControlVisitantes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        JTableControlVisitantes.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTableControlVisitantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTableControlVisitantesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTableControlVisitantes);

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Nombre visitante");

        pivtxtget_nombre_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_nombre_visitante.setMaximumSize(new java.awt.Dimension(200, 26));
        pivtxtget_nombre_visitante.setMinimumSize(new java.awt.Dimension(200, 26));
        pivtxtget_nombre_visitante.setPreferredSize(new java.awt.Dimension(200, 26));
        pivtxtget_nombre_visitante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pivtxtget_nombre_visitanteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pivtxtget_nombre_visitanteFocusLost(evt);
            }
        });
        pivtxtget_nombre_visitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pivtxtget_nombre_visitanteActionPerformed(evt);
            }
        });
        pivtxtget_nombre_visitante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pivtxtget_nombre_visitanteKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("# Documento");

        pivtxtget_numero_documento_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_numero_documento_visitante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pivtxtget_numero_documento_visitanteKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Apartamento");

        pivjComboBoxget_apto.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Quien Autoriza");

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Placa");

        pivtxtget_placa.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_placa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pivtxtget_placaKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Motivo visita");

        pivtxtget_motivo_visita.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_motivo_visita.setMaximumSize(new java.awt.Dimension(200, 26));
        pivtxtget_motivo_visita.setMinimumSize(new java.awt.Dimension(200, 26));
        pivtxtget_motivo_visita.setPreferredSize(new java.awt.Dimension(200, 26));
        pivtxtget_motivo_visita.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pivtxtget_motivo_visitaKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Vigilante *");

        pivtxtget_vigilante.setEditable(false);
        pivtxtget_vigilante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        piv_jComboBox_quien_autoriza.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jSeparator16.setForeground(new java.awt.Color(0, 0, 0));

        pivtxtget_filtro_opciones.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_filtro_opciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "# Documento", "Placa Vehicular" }));

        jLabel30.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Filtrar por");

        p_i_v_btn_ingreso_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_ingreso_visitante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_ingresar_40px.png"))); // NOI18N
        p_i_v_btn_ingreso_visitante.setText("Registrar ing");
        p_i_v_btn_ingreso_visitante.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_ingreso_visitante.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_ingresar_32px.png"))); // NOI18N
        p_i_v_btn_ingreso_visitante.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_ingresar_37px.png"))); // NOI18N
        p_i_v_btn_ingreso_visitante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_i_v_btn_ingreso_visitanteMouseClicked(evt);
            }
        });
        p_i_v_btn_ingreso_visitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_ingreso_visitanteActionPerformed(evt);
            }
        });

        p_i_v_btn_actualizar_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_actualizar_visitante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_40px.png"))); // NOI18N
        p_i_v_btn_actualizar_visitante.setText("Actualizar");
        p_i_v_btn_actualizar_visitante.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_actualizar_visitante.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_32px.png"))); // NOI18N
        p_i_v_btn_actualizar_visitante.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_actualizar_37px.png"))); // NOI18N
        p_i_v_btn_actualizar_visitante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_i_v_btn_actualizar_visitanteMouseClicked(evt);
            }
        });
        p_i_v_btn_actualizar_visitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_actualizar_visitanteActionPerformed(evt);
            }
        });

        p_i_v_btn_exit_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_exit_visitante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_exit_40px.png"))); // NOI18N
        p_i_v_btn_exit_visitante.setText("Registrar sal");
        p_i_v_btn_exit_visitante.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_exit_visitante.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_exit_32px.png"))); // NOI18N
        p_i_v_btn_exit_visitante.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_exit_37px.png"))); // NOI18N
        p_i_v_btn_exit_visitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_exit_visitanteActionPerformed(evt);
            }
        });

        p_i_v_btn_exit_visitante1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_exit_visitante1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_40px.png"))); // NOI18N
        p_i_v_btn_exit_visitante1.setText("Limpiar");
        p_i_v_btn_exit_visitante1.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_exit_visitante1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_32px.png"))); // NOI18N
        p_i_v_btn_exit_visitante1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_37px.png"))); // NOI18N
        p_i_v_btn_exit_visitante1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_exit_visitante1ActionPerformed(evt);
            }
        });

        p_i_v_btn_buscar_filtro.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        p_i_v_btn_buscar_filtro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_vx40.png"))); // NOI18N
        p_i_v_btn_buscar_filtro.setText("Buscar");
        p_i_v_btn_buscar_filtro.setPreferredSize(new java.awt.Dimension(123, 38));
        p_i_v_btn_buscar_filtro.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_vx32.png"))); // NOI18N
        p_i_v_btn_buscar_filtro.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscar_vx37.png"))); // NOI18N
        p_i_v_btn_buscar_filtro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_i_v_btn_buscar_filtroMouseClicked(evt);
            }
        });
        p_i_v_btn_buscar_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_i_v_btn_buscar_filtroActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Buscar por");

        pivtxtget_filtro.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pivtxtget_filtro.setMaximumSize(new java.awt.Dimension(200, 26));
        pivtxtget_filtro.setMinimumSize(new java.awt.Dimension(200, 26));
        pivtxtget_filtro.setPreferredSize(new java.awt.Dimension(200, 26));
        pivtxtget_filtro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pivtxtget_filtroFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pivtxtget_filtroFocusLost(evt);
            }
        });
        pivtxtget_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pivtxtget_filtroActionPerformed(evt);
            }
        });
        pivtxtget_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pivtxtget_filtroKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panel_form_botonesLayout = new javax.swing.GroupLayout(panel_form_botones);
        panel_form_botones.setLayout(panel_form_botonesLayout);
        panel_form_botonesLayout.setHorizontalGroup(
            panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_form_botonesLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jSeparator16)
                .addGap(6, 6, 6))
            .addGroup(panel_form_botonesLayout.createSequentialGroup()
                .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_form_botonesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pivtxtget_filtro_opciones, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(pivtxtget_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_i_v_btn_buscar_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_i_v_btn_ingreso_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_i_v_btn_actualizar_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(p_i_v_btn_exit_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_i_v_btn_exit_visitante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_form_botonesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pivtxtget_nombre_visitante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(pivtxtget_numero_documento_visitante, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(pivjComboBoxget_apto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pivtxtget_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pivtxtget_motivo_visita, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(piv_jComboBox_quien_autoriza, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pivtxtget_vigilante))))
                .addContainerGap())
        );
        panel_form_botonesLayout.setVerticalGroup(
            panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_form_botonesLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pivtxtget_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(piv_jComboBox_quien_autoriza)
                    .addComponent(pivtxtget_motivo_visita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pivtxtget_placa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pivjComboBoxget_apto)
                    .addComponent(pivtxtget_numero_documento_visitante)
                    .addComponent(pivtxtget_nombre_visitante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_form_botonesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(p_i_v_btn_ingreso_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(p_i_v_btn_actualizar_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(p_i_v_btn_exit_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(p_i_v_btn_exit_visitante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(p_i_v_btn_buscar_filtro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7))
                    .addGroup(panel_form_botonesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(panel_form_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_form_botonesLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pivtxtget_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_form_botonesLayout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pivtxtget_filtro_opciones, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(22, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout panel_m_i_visitanteLayout = new javax.swing.GroupLayout(panel_m_i_visitante);
        panel_m_i_visitante.setLayout(panel_m_i_visitanteLayout);
        panel_m_i_visitanteLayout.setHorizontalGroup(
            panel_m_i_visitanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
            .addGroup(panel_m_i_visitanteLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panel_m_i_visitanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_form_botones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(3, 3, 3))
        );
        panel_m_i_visitanteLayout.setVerticalGroup(
            panel_m_i_visitanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_m_i_visitanteLayout.createSequentialGroup()
                .addComponent(panel_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(panel_form_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
        );

        panel_rigth.add(panel_m_i_visitante, "card2");

        panel_m_i_correspondencia.setBackground(new java.awt.Color(255, 255, 255));
        panel_m_i_correspondencia.setMinimumSize(new java.awt.Dimension(1161, 720));
        panel_m_i_correspondencia.setPreferredSize(new java.awt.Dimension(1161, 720));

        panel_titulo1.setBackground(new java.awt.Color(51, 51, 51));
        panel_titulo1.setPreferredSize(new java.awt.Dimension(100, 47));

        sub_panel_titulo_txt_titulo1.setFont(new java.awt.Font("Century Gothic", 1, 15)); // NOI18N
        sub_panel_titulo_txt_titulo1.setForeground(new java.awt.Color(255, 255, 255));
        sub_panel_titulo_txt_titulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sub_panel_titulo_txt_titulo1.setText("INGRESO CORRESPONDENCIA - SALIDA CORRESPONDENCIA");

        javax.swing.GroupLayout panel_titulo1Layout = new javax.swing.GroupLayout(panel_titulo1);
        panel_titulo1.setLayout(panel_titulo1Layout);
        panel_titulo1Layout.setHorizontalGroup(
            panel_titulo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_titulo1Layout.setVerticalGroup(
            panel_titulo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JTableControlCorrespondencia.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        JTableControlCorrespondencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTableControlCorrespondencia.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        JTableControlCorrespondencia.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTableControlCorrespondencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTableControlCorrespondenciaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(JTableControlCorrespondencia);

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("¿Quien trae?*");

        pic_get_quien_trae.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("¿Tipo Paquete?*");

        pic_get_tipo_paquete.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pic_get_tipo_paquete.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bolsa", "Caja", "Carta", "Documentos", "Electrodoméstico", "Factura Agua", "Factura Gas", "Factura Luz", "Factura Plan Datos", "Invitaciones o Tarjetas", "Libro", "Medicamentos", "Otros", "Paquete", "Periódico", "Revista", "Sobre", "Teléfono" }));

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("¿ Numero Guia?");

        pic_get_numero_guia.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Apartamento*");

        pic_get_apartamento.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("¿Nombre Residente?*");

        pic_btn_registrar_ingreso.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pic_btn_registrar_ingreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_agregar1_40px.png"))); // NOI18N
        pic_btn_registrar_ingreso.setText("Registrar ingreso");
        pic_btn_registrar_ingreso.setPreferredSize(new java.awt.Dimension(123, 38));
        pic_btn_registrar_ingreso.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_agregar1_32px.png"))); // NOI18N
        pic_btn_registrar_ingreso.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_agregar1_37px.png"))); // NOI18N
        pic_btn_registrar_ingreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pic_btn_registrar_ingresoMouseClicked(evt);
            }
        });
        pic_btn_registrar_ingreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pic_btn_registrar_ingresoActionPerformed(evt);
            }
        });

        pic_get_nombre_residente.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("¿Quien reclama?");

        jLabel16.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Observación");

        pic_get_observacion.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        pic_btn_registrar_salida.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pic_btn_registrar_salida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_salida1_40px.png"))); // NOI18N
        pic_btn_registrar_salida.setText("Registrar salida");
        pic_btn_registrar_salida.setPreferredSize(new java.awt.Dimension(123, 38));
        pic_btn_registrar_salida.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_salida1_32px.png"))); // NOI18N
        pic_btn_registrar_salida.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_salida1_37px.png"))); // NOI18N
        pic_btn_registrar_salida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pic_btn_registrar_salidaMouseClicked(evt);
            }
        });
        pic_btn_registrar_salida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pic_btn_registrar_salidaActionPerformed(evt);
            }
        });

        pic_btn_buscar_apartamento.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pic_btn_buscar_apartamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_40px.png"))); // NOI18N
        pic_btn_buscar_apartamento.setText("Limpiar");
        pic_btn_buscar_apartamento.setPreferredSize(new java.awt.Dimension(123, 38));
        pic_btn_buscar_apartamento.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_32px.png"))); // NOI18N
        pic_btn_buscar_apartamento.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_37px.png"))); // NOI18N
        pic_btn_buscar_apartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pic_btn_buscar_apartamentoActionPerformed(evt);
            }
        });

        pic_btn_buscar_guia.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pic_btn_buscar_guia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_buscar1_40px.png"))); // NOI18N
        pic_btn_buscar_guia.setText("Guia");
        pic_btn_buscar_guia.setPreferredSize(new java.awt.Dimension(123, 38));
        pic_btn_buscar_guia.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_buscar1_32px.png"))); // NOI18N
        pic_btn_buscar_guia.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_paquete_buscar1_37px.png"))); // NOI18N
        pic_btn_buscar_guia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pic_btn_buscar_guiaMouseClicked(evt);
            }
        });
        pic_btn_buscar_guia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pic_btn_buscar_guiaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Vigilante*");

        pictxtget_vigilante.setEditable(false);
        pictxtget_vigilante.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pictxtget_vigilante.setMinimumSize(new java.awt.Dimension(201, 24));

        pic_get_quien_reclama.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        javax.swing.GroupLayout panel_form_ingresoLayout = new javax.swing.GroupLayout(panel_form_ingreso);
        panel_form_ingreso.setLayout(panel_form_ingresoLayout);
        panel_form_ingresoLayout.setHorizontalGroup(
            panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_form_ingresoLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_form_ingresoLayout.createSequentialGroup()
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(pic_get_quien_reclama))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pic_get_observacion)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pictxtget_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pic_btn_registrar_salida, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pic_btn_buscar_apartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pic_btn_buscar_guia, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_form_ingresoLayout.createSequentialGroup()
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(pic_get_quien_trae))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pic_get_tipo_paquete, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(pic_get_numero_guia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(pic_get_apartamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pic_get_nombre_residente, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pic_btn_registrar_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_form_ingresoLayout.setVerticalGroup(
            panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_form_ingresoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pic_btn_registrar_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_form_ingresoLayout.createSequentialGroup()
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pic_get_quien_trae, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pic_get_tipo_paquete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pic_get_numero_guia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pic_get_apartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pic_get_nombre_residente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_form_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pic_get_observacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pic_btn_registrar_salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pic_btn_buscar_apartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pic_btn_buscar_guia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pictxtget_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pic_get_quien_reclama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout panel_m_i_correspondenciaLayout = new javax.swing.GroupLayout(panel_m_i_correspondencia);
        panel_m_i_correspondencia.setLayout(panel_m_i_correspondenciaLayout);
        panel_m_i_correspondenciaLayout.setHorizontalGroup(
            panel_m_i_correspondenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_titulo1, javax.swing.GroupLayout.DEFAULT_SIZE, 1161, Short.MAX_VALUE)
            .addGroup(panel_m_i_correspondenciaLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panel_m_i_correspondenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_form_ingreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        panel_m_i_correspondenciaLayout.setVerticalGroup(
            panel_m_i_correspondenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_m_i_correspondenciaLayout.createSequentialGroup()
                .addComponent(panel_titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(panel_form_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
        );

        panel_rigth.add(panel_m_i_correspondencia, "card2");

        panel_m_i_vehiculos.setBackground(new java.awt.Color(255, 255, 255));
        panel_m_i_vehiculos.setMinimumSize(new java.awt.Dimension(1161, 720));
        panel_m_i_vehiculos.setPreferredSize(new java.awt.Dimension(1161, 720));

        subpanel_titulo2.setBackground(new java.awt.Color(51, 51, 51));
        subpanel_titulo2.setPreferredSize(new java.awt.Dimension(100, 47));

        sub_panel_titulo_txt_titulo2.setFont(new java.awt.Font("Century Gothic", 1, 15)); // NOI18N
        sub_panel_titulo_txt_titulo2.setForeground(new java.awt.Color(255, 255, 255));
        sub_panel_titulo_txt_titulo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sub_panel_titulo_txt_titulo2.setText("INGRESO - SALIDA VEHICULAR PROPIETARIOS");

        javax.swing.GroupLayout subpanel_titulo2Layout = new javax.swing.GroupLayout(subpanel_titulo2);
        subpanel_titulo2.setLayout(subpanel_titulo2Layout);
        subpanel_titulo2Layout.setHorizontalGroup(
            subpanel_titulo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        subpanel_titulo2Layout.setVerticalGroup(
            subpanel_titulo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sub_panel_titulo_txt_titulo2, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(1160, 92));

        jLabel19.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Tipo Dueño *");

        piptxtget_tipo_dueño.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        piptxtget_tipo_dueño.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Propietario", "Visitante" }));

        jLabel21.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Tipo Vehiculo *");

        piptxtget_tipo_vehiculo.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        piptxtget_tipo_vehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carro", "Moto", "Bicicleta" }));

        jLabel22.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Placa");
        jLabel22.setMaximumSize(new java.awt.Dimension(66, 18));
        jLabel22.setMinimumSize(new java.awt.Dimension(66, 18));
        jLabel22.setPreferredSize(new java.awt.Dimension(66, 18));

        jLabel23.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Apartamento*");

        piptxtget_get_apto.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Nombre Propietario *");
        jLabel24.setMaximumSize(new java.awt.Dimension(200, 18));
        jLabel24.setMinimumSize(new java.awt.Dimension(200, 18));
        jLabel24.setPreferredSize(new java.awt.Dimension(200, 18));

        piptxtget_nombre_propietario.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Nombre Visitante *");
        jLabel25.setMaximumSize(new java.awt.Dimension(130, 18));
        jLabel25.setMinimumSize(new java.awt.Dimension(130, 18));
        jLabel25.setPreferredSize(new java.awt.Dimension(130, 18));

        jLabel26.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("¿Quien Autoriza *");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator2.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator2.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator2.setPreferredSize(new java.awt.Dimension(3, 46));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator3.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator3.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator3.setPreferredSize(new java.awt.Dimension(3, 46));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator4.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator4.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator4.setPreferredSize(new java.awt.Dimension(3, 46));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator5.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator5.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator5.setPreferredSize(new java.awt.Dimension(3, 46));

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Espejos *");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel17.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Estrellado *");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel18.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Rayado *");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel27.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Observación");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        piptxtget_vehicular_espejo.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        piptxtget_vehicular_espejo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Si", "No" }));

        piptxtget_vehicular_estrellado.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        piptxtget_vehicular_estrellado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));

        piptxtget_vehicular_rayado.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        piptxtget_vehicular_rayado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Si", "No" }));

        piptxtget_vehicular_observacion.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        piptxtget_nombre_visitante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        piptxtget_placa_vehicular.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator6.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator6.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator6.setPreferredSize(new java.awt.Dimension(3, 46));

        piptxtget_quien_autoriza.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator8.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator8.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator8.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator8.setPreferredSize(new java.awt.Dimension(3, 46));

        pip_btn_buscar_placa.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pip_btn_buscar_placa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_40px.png"))); // NOI18N
        pip_btn_buscar_placa.setText("Buscar Placa");
        pip_btn_buscar_placa.setMaximumSize(new java.awt.Dimension(140, 38));
        pip_btn_buscar_placa.setMinimumSize(new java.awt.Dimension(140, 38));
        pip_btn_buscar_placa.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_32px.png"))); // NOI18N
        pip_btn_buscar_placa.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_buscarplaca_37px.png"))); // NOI18N
        pip_btn_buscar_placa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pip_btn_buscar_placaActionPerformed(evt);
            }
        });

        pip_btn_registrar_ingreso.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pip_btn_registrar_ingreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_entrar_40px.png"))); // NOI18N
        pip_btn_registrar_ingreso.setText("Registrar Ingreso");
        pip_btn_registrar_ingreso.setMaximumSize(new java.awt.Dimension(140, 38));
        pip_btn_registrar_ingreso.setMinimumSize(new java.awt.Dimension(140, 38));
        pip_btn_registrar_ingreso.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_entrar_32px.png"))); // NOI18N
        pip_btn_registrar_ingreso.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_entrar_37px.png"))); // NOI18N
        pip_btn_registrar_ingreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pip_btn_registrar_ingresoMouseClicked(evt);
            }
        });

        pip_btn_registrar_salida.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pip_btn_registrar_salida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_salir_40px.png"))); // NOI18N
        pip_btn_registrar_salida.setText("Registrar Salida");
        pip_btn_registrar_salida.setMaximumSize(new java.awt.Dimension(140, 38));
        pip_btn_registrar_salida.setMinimumSize(new java.awt.Dimension(140, 38));
        pip_btn_registrar_salida.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_salir_32px.png"))); // NOI18N
        pip_btn_registrar_salida.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_vehiculo_salir_37px.png"))); // NOI18N
        pip_btn_registrar_salida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pip_btn_registrar_salidaMouseClicked(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Vigilante*");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        piptxtget_vigilante.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jSeparator9.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator9.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jSeparator9.setMaximumSize(new java.awt.Dimension(3, 46));
        jSeparator9.setMinimumSize(new java.awt.Dimension(3, 46));
        jSeparator9.setPreferredSize(new java.awt.Dimension(3, 46));

        pip_btn_limpiar.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        pip_btn_limpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_40px.png"))); // NOI18N
        pip_btn_limpiar.setText("Limpiar");
        pip_btn_limpiar.setPreferredSize(new java.awt.Dimension(123, 38));
        pip_btn_limpiar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_32px.png"))); // NOI18N
        pip_btn_limpiar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_clean_37px.png"))); // NOI18N
        pip_btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pip_btn_limpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(piptxtget_tipo_dueño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(piptxtget_tipo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(piptxtget_placa_vehicular))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(piptxtget_get_apto, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_nombre_propietario, 0, 208, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(piptxtget_nombre_visitante))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(piptxtget_quien_autoriza, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                            .addComponent(piptxtget_vehicular_espejo, 0, 0, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vehicular_estrellado, 0, 84, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel18)
                            .addComponent(piptxtget_vehicular_rayado, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator7)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vehicular_observacion, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(pip_btn_registrar_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pip_btn_registrar_salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pip_btn_buscar_placa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pip_btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(piptxtget_tipo_dueño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_tipo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_placa_vehicular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_get_apto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_nombre_propietario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_nombre_visitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vehicular_espejo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vehicular_estrellado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_vehicular_rayado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piptxtget_quien_autoriza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(1, 1, 1)
                                .addComponent(piptxtget_vehicular_observacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(1, 1, 1)
                                .addComponent(piptxtget_vigilante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(pip_btn_registrar_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pip_btn_registrar_salida, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pip_btn_buscar_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pip_btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
        );

        JTableControlVehicularPropietarios.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        JTableControlVehicularPropietarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTableControlVehicularPropietarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        JTableControlVehicularPropietarios.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane3.setViewportView(JTableControlVehicularPropietarios);

        javax.swing.GroupLayout panel_m_i_vehiculosLayout = new javax.swing.GroupLayout(panel_m_i_vehiculos);
        panel_m_i_vehiculos.setLayout(panel_m_i_vehiculosLayout);
        panel_m_i_vehiculosLayout.setHorizontalGroup(
            panel_m_i_vehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subpanel_titulo2, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
            .addGroup(panel_m_i_vehiculosLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panel_m_i_vehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(3, 3, 3))
        );
        panel_m_i_vehiculosLayout.setVerticalGroup(
            panel_m_i_vehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_m_i_vehiculosLayout.createSequentialGroup()
                .addComponent(subpanel_titulo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        panel_rigth.add(panel_m_i_vehiculos, "card2");

        getContentPane().add(panel_rigth, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_i_visitanteMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_visitanteMouseMoved
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaVisitanteActivo) {
            panel_btn_visitante.setBackground(new Color(213, 219, 219));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseMoved (Al mover el cursos set color personalizado)
//        panel_btn_visitante.setBackground(new Color(213, 219, 219));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_visitanteMouseMoved

    private void btn_i_visitanteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_visitanteMouseExited
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaVisitanteActivo) {
            panel_btn_visitante.setBackground(new Color(255, 255, 255));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseExited(Al quitar el cursos set color default)
//        panel_btn_visitante.setBackground(new Color(255, 255, 255));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_visitanteMouseExited

    private void btn_i_correspondenciaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_correspondenciaMouseMoved
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaCorrespondenciaActivo) {
            btn_i_correspondencia.setBackground(new Color(213, 219, 219));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseMoved (Al mover el cursos set color personalizado)
//        panel_btn_correspondencia.setBackground(new Color(213, 219, 219));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_correspondenciaMouseMoved

    private void btn_i_correspondenciaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_correspondenciaMouseExited
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaCorrespondenciaActivo) {
            btn_i_correspondencia.setBackground(new Color(255, 255, 255));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseExited(Al quitar el cursos set color default)
//        panel_btn_correspondencia.setBackground(new Color(255, 255, 255));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_correspondenciaMouseExited

    private void btn_i_vehicularMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_vehicularMouseMoved
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaVisitanteActivo) {
            btn_i_vehicular.setBackground(new Color(213, 219, 219));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseMoved (Al mover el cursos set color personalizado)
//        panel_btn_vehicular.setBackground(new Color(213, 219, 219));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_vehicularMouseMoved

    private void btn_i_vehicularMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_vehicularMouseExited
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (!estaVehicularActivo) {
            btn_i_vehicular.setBackground(new Color(255, 255, 255));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseExited(Al quitar el cursos set color default)
//        panel_btn_vehicular.setBackground(new Color(255, 255, 255));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_vehicularMouseExited

    private void btn_buscarMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseMoved
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (estaBuscarActivo) {
            btn_buscar.setBackground(new Color(213, 219, 219));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseMoved (Al mover el cursos set color personalizado)
//        panel_btn_buscar.setBackground(new Color(213, 219, 219));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_buscarMouseMoved

    private void btn_buscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseExited
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (estaBuscarActivo) {
            btn_buscar.setBackground(new Color(255, 255, 255));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseExited(Al quitar el cursos set color default)
//        panel_btn_buscar.setBackground(new Color(255, 255, 255));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_buscarMouseExited

    private void btn_acercadeMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_acercadeMouseMoved
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (estaAcercaDeActivo) {
            btn_acercade.setBackground(new Color(213, 219, 219));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseMoved (Al mover el cursos set color personalizado)
//        panel_btn_acercade.setBackground(new Color(213, 219, 219));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_acercadeMouseMoved

    private void btn_acercadeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_acercadeMouseExited
//──────────────────────────────────────────────────────────────────
//[Este código debería mantener el color del botón seleccionado hasta que otro botón sea activado]
        if (estaAcercaDeActivo) {
            btn_acercade.setBackground(new Color(255, 255, 255));
        }
//──────────────────────────────────────────────────────────────────
//Evento MauseExited(Al quitar el cursos set color default)
//        panel_btn_acercade.setBackground(new Color(255, 255, 255));
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_acercadeMouseExited

    private void btn_i_vehicularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_i_vehicularActionPerformed

    }//GEN-LAST:event_btn_i_vehicularActionPerformed

    private void btn_i_visitanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_visitanteMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = true;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;
// ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_visitante);
        panel_rigth.repaint();
        panel_rigth.revalidate();
        metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);
    }//GEN-LAST:event_btn_i_visitanteMouseClicked

    private void pivtxtget_nombre_visitanteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pivtxtget_nombre_visitanteFocusGained

    }//GEN-LAST:event_pivtxtget_nombre_visitanteFocusGained

    private void pivtxtget_nombre_visitanteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pivtxtget_nombre_visitanteFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_nombre_visitanteFocusLost

    private void btn_i_correspondenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_correspondenciaMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = true;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;

        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false
        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_correspondencia);
        panel_rigth.repaint();
        panel_rigth.revalidate();
        metJTableMostrarCorrespondenciaRegistrada(JTableControlCorrespondencia);
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_correspondenciaMouseClicked

    private void btn_i_vehicularMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_i_vehicularMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = true;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_vehiculos);
        panel_rigth.repaint();
        panel_rigth.revalidate();
        metJTableMostrarIngresoVehicularPropietarios(JTableControlVehicularPropietarios);
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_i_vehicularMouseClicked

    private void pivtxtget_nombre_visitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pivtxtget_nombre_visitanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_nombre_visitanteActionPerformed

    private void p_i_v_btn_ingreso_visitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_ingreso_visitanteActionPerformed

    }//GEN-LAST:event_p_i_v_btn_ingreso_visitanteActionPerformed

    private void pivtxtget_nombre_visitanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pivtxtget_nombre_visitanteKeyTyped
        /* ------------------------------------------------------------------------------------ */
        // Invocar LetterSpaceFilter
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) pivtxtget_nombre_visitante.getDocument();
        doc.setDocumentFilter(new LetterSpaceFilter());
        /* ------------------------------------------------------------------------------------ */
 /* ------------------------------------------------------------------------------------ */
        //Validador JtexField Cantidad de Caracteres deseados
        if (pivtxtget_nombre_visitante.getText().length() == 35) {
            evt.consume();
        }
        /* ------------------------------------------------------------------------------------ */
    }//GEN-LAST:event_pivtxtget_nombre_visitanteKeyTyped

    private void pivtxtget_numero_documento_visitanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pivtxtget_numero_documento_visitanteKeyTyped
        /* ------------------------------------------------------------------------------------ */
        //Invocar NumberOnlyFilter
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) pivtxtget_numero_documento_visitante.getDocument();
        doc.setDocumentFilter(new NumberOnlyFilter());
        /* ------------------------------------------------------------------------------------ */
        //Validador JtexField Cantidad de Caracteres deseados
        if (pivtxtget_numero_documento_visitante.getText().length() == 10) {
            evt.consume();
        }
        /* ------------------------------------------------------------------------------------ */
    }//GEN-LAST:event_pivtxtget_numero_documento_visitanteKeyTyped

    private void p_i_v_btn_buscar_placaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_buscar_placaActionPerformed
//        buscarVisitantePorPlacaVehicular();
    }//GEN-LAST:event_p_i_v_btn_buscar_placaActionPerformed

    private void p_i_v_btb_buscar_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btb_buscar_cedulaActionPerformed
//        buscarVisitantePorNumeroDocumento();
    }//GEN-LAST:event_p_i_v_btb_buscar_cedulaActionPerformed

    private void pivtxtget_placaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pivtxtget_placaKeyTyped
        /* ------------------------------------------------------------------------------------ */
        //Validador JtexField Cantidad de Caracteres deseados
        if (pivtxtget_placa.getText().length() == 6) {
            evt.consume();
        }
        /* ------------------------------------------------------------------------------------ */
    }//GEN-LAST:event_pivtxtget_placaKeyTyped

    private void pivtxtget_motivo_visitaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pivtxtget_motivo_visitaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_motivo_visitaKeyTyped

    private void p_i_v_btb_buscar_cedulaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_i_v_btb_buscar_cedulaMouseClicked
//        buscarVisitantePorNumeroDocumento();
    }//GEN-LAST:event_p_i_v_btb_buscar_cedulaMouseClicked

    private void p_i_v_btn_exit_visitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_exit_visitanteActionPerformed
        metRealizarSalidaVisitante();
    }//GEN-LAST:event_p_i_v_btn_exit_visitanteActionPerformed

    private void p_i_v_btn_actualizar_visitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_actualizar_visitanteActionPerformed

    }//GEN-LAST:event_p_i_v_btn_actualizar_visitanteActionPerformed

    private void JTableControlVisitantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTableControlVisitantesMouseClicked

    }//GEN-LAST:event_JTableControlVisitantesMouseClicked

    private void p_i_v_btn_actualizar_visitanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_i_v_btn_actualizar_visitanteMouseClicked
        metActualizarVisitante();
    }//GEN-LAST:event_p_i_v_btn_actualizar_visitanteMouseClicked

    private void p_i_v_btn_exit_visitante1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_exit_visitante1ActionPerformed
        // Establecer los campos en blanco
        pivtxtget_nombre_visitante.setText("");
        pivtxtget_numero_documento_visitante.setText("");
        pivjComboBoxget_apto.setSelectedItem(null);
        pivtxtget_placa.setText("");
        pivtxtget_motivo_visita.setText("");
        piv_jComboBox_quien_autoriza.setSelectedItem(null);
        pivtxtget_filtro.setText("");
        metJTableMostrarVisitantesRegistrados(JTableControlVisitantes);
    }//GEN-LAST:event_p_i_v_btn_exit_visitante1ActionPerformed

    private void JTableControlCorrespondenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTableControlCorrespondenciaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_JTableControlCorrespondenciaMouseClicked

    private void pic_btn_registrar_ingresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pic_btn_registrar_ingresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pic_btn_registrar_ingresoActionPerformed

    private void pic_btn_registrar_salidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pic_btn_registrar_salidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pic_btn_registrar_salidaActionPerformed

    private void pic_btn_buscar_apartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pic_btn_buscar_apartamentoActionPerformed
        // Establecer los campos en blanco
        pic_get_quien_trae.setText("");
        pic_get_numero_guia.setText("");
        pic_get_quien_reclama.setText("");
        pic_get_observacion.setText("");
        metJTableMostrarCorrespondenciaRegistrada(JTableControlCorrespondencia);
    }//GEN-LAST:event_pic_btn_buscar_apartamentoActionPerformed

    private void pic_btn_buscar_guiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pic_btn_buscar_guiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pic_btn_buscar_guiaActionPerformed

    private void pic_btn_registrar_ingresoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pic_btn_registrar_ingresoMouseClicked
        metRegistrarIngresoCorrespondencia();
    }//GEN-LAST:event_pic_btn_registrar_ingresoMouseClicked

    private void pic_btn_registrar_salidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pic_btn_registrar_salidaMouseClicked
        metRealizarSalidaCorrespondencia();
    }//GEN-LAST:event_pic_btn_registrar_salidaMouseClicked

    private void pic_btn_buscar_guiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pic_btn_buscar_guiaMouseClicked
        metBuscarCorrespondenciaPorNumeroGuia();
    }//GEN-LAST:event_pic_btn_buscar_guiaMouseClicked

    private void pip_btn_registrar_ingresoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pip_btn_registrar_ingresoMouseClicked
        metRegistroIngresoVehicularPropietarios(); //Invocar metodo
    }//GEN-LAST:event_pip_btn_registrar_ingresoMouseClicked

    private void pip_btn_registrar_salidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pip_btn_registrar_salidaMouseClicked
        metRealizarSalidaVehicularPropietarios(); //Invocar metodo
    }//GEN-LAST:event_pip_btn_registrar_salidaMouseClicked

    private void btn_i_visitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_i_visitanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_i_visitanteActionPerformed

    private void btn_buscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = true;
        estaAcercaDeActivo = false;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
//        panel_rigth.removeAll();
//        panel_rigth.add(panel_m_i_buscar);
//        panel_rigth.repaint();
//        panel_rigth.revalidate();

//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_buscarMouseClicked

    private void btn_acercadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_acercadeMouseClicked
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = true;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//        panel_rigth.removeAll();
//        panel_rigth.add(panel_m_i_acercade);
//        panel_rigth.repaint();
//        panel_rigth.revalidate();
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_btn_acercadeMouseClicked

    private void panel_btn_visitanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_visitanteMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = true;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;
// ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_visitante);
        panel_rigth.repaint();
        panel_rigth.revalidate();
    }//GEN-LAST:event_panel_btn_visitanteMouseClicked

    private void panel_btn_correspondenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_correspondenciaMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = true;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;

        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false
        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_correspondencia);
        panel_rigth.repaint();
        panel_rigth.revalidate();
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_panel_btn_correspondenciaMouseClicked

    private void panel_btn_vehicularMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_vehicularMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = true;
        estaBuscarActivo = false;
        estaAcercaDeActivo = false;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
        panel_rigth.removeAll();
        panel_rigth.add(panel_m_i_vehiculos);
        panel_rigth.repaint();
        panel_rigth.revalidate();
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_panel_btn_vehicularMouseClicked

    private void panel_btn_buscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_buscarMouseClicked
//──────────────────────────────────────────────────────────────────
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = true;
        estaAcercaDeActivo = false;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//Remover panel - añadir nuevo panel
//        panel_rigth.removeAll();
//        panel_rigth.add(panel_m_i_buscar);
//        panel_rigth.repaint();
//        panel_rigth.revalidate();

//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_panel_btn_buscarMouseClicked

    private void panel_btn_acercadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_acercadeMouseClicked
        // Activar el botón y desactivar los demás
        estaVisitanteActivo = false;
        estaCorrespondenciaActivo = false;
        estaVehicularActivo = false;
        estaBuscarActivo = false;
        estaAcercaDeActivo = true;
        // ... establece estaCorrespondenciaActiva, estaVehicularActivo, etc. a false

        actualizarEstilosBotones();
//──────────────────────────────────────────────────────────────────
//        panel_rigth.removeAll();
//        panel_rigth.add(panel_m_i_acercade);
//        panel_rigth.repaint();
//        panel_rigth.revalidate();
//──────────────────────────────────────────────────────────────────
    }//GEN-LAST:event_panel_btn_acercadeMouseClicked

    private void p_i_v_btn_ingreso_visitanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_i_v_btn_ingreso_visitanteMouseClicked
        //Invocar metodos
        metRegistrarIngresoVisitante();
    }//GEN-LAST:event_p_i_v_btn_ingreso_visitanteMouseClicked

    private void pip_btn_buscar_placaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pip_btn_buscar_placaActionPerformed
        //Incovar metodo
        metBuscarPorPlacaVehicularPropietario();
    }//GEN-LAST:event_pip_btn_buscar_placaActionPerformed

    private void pip_btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pip_btn_limpiarActionPerformed
        // Establecer los campos en blanco
        piptxtget_placa_vehicular.setText("");
        piptxtget_get_apto.setSelectedItem(null);
        piptxtget_nombre_propietario.setSelectedItem(null);
        piptxtget_nombre_visitante.setText("");
        piptxtget_quien_autoriza.setSelectedItem(null);
        piptxtget_vehicular_observacion.setText("");
        metJTableMostrarIngresoVehicularPropietarios(JTableControlVehicularPropietarios);
    }//GEN-LAST:event_pip_btn_limpiarActionPerformed

    private void pivtxtget_filtroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pivtxtget_filtroFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_filtroFocusGained

    private void pivtxtget_filtroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pivtxtget_filtroFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_filtroFocusLost

    private void pivtxtget_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pivtxtget_filtroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_filtroActionPerformed

    private void pivtxtget_filtroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pivtxtget_filtroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pivtxtget_filtroKeyTyped

    private void p_i_v_btn_buscar_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_i_v_btn_buscar_filtroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_i_v_btn_buscar_filtroActionPerformed

    private void p_i_v_btn_buscar_filtroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_i_v_btn_buscar_filtroMouseClicked

    }//GEN-LAST:event_p_i_v_btn_buscar_filtroMouseClicked

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
            java.util.logging.Logger.getLogger(U5Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(U5Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(U5Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(U5Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new U5Home().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable JTableControlCorrespondencia;
    private javax.swing.JTable JTableControlVehicularPropietarios;
    public static javax.swing.JTable JTableControlVisitantes;
    private javax.swing.JButton btn_acercade;
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_i_correspondencia;
    private javax.swing.JButton btn_i_vehicular;
    private javax.swing.JButton btn_i_visitante;
    private com.bolivia.panel.JCPanel jCPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    public static javax.swing.JButton p_i_v_btb_buscar_cedula;
    private javax.swing.JButton p_i_v_btn_actualizar_visitante;
    private javax.swing.JButton p_i_v_btn_buscar_filtro;
    private javax.swing.JButton p_i_v_btn_buscar_placa;
    private javax.swing.JButton p_i_v_btn_exit_visitante;
    private javax.swing.JButton p_i_v_btn_exit_visitante1;
    private javax.swing.JButton p_i_v_btn_ingreso_visitante;
    private javax.swing.JPanel panel_btn_acercade;
    private javax.swing.JPanel panel_btn_buscar;
    private javax.swing.JPanel panel_btn_correspondencia;
    private javax.swing.JPanel panel_btn_vehicular;
    private javax.swing.JPanel panel_btn_visitante;
    private javax.swing.JPanel panel_form_botones;
    private javax.swing.JPanel panel_form_ingreso;
    private javax.swing.JPanel panel_info_usuario;
    private javax.swing.JPanel panel_left;
    private javax.swing.JPanel panel_m_i_correspondencia;
    private javax.swing.JPanel panel_m_i_vehiculos;
    private javax.swing.JPanel panel_m_i_visitante;
    private javax.swing.JPanel panel_nombre_software;
    private javax.swing.JPanel panel_rigth;
    private javax.swing.JPanel panel_titulo;
    private javax.swing.JPanel panel_titulo1;
    private javax.swing.JButton pic_btn_buscar_apartamento;
    private javax.swing.JButton pic_btn_buscar_guia;
    private javax.swing.JButton pic_btn_registrar_ingreso;
    private javax.swing.JButton pic_btn_registrar_salida;
    private javax.swing.JComboBox<String> pic_get_apartamento;
    private javax.swing.JComboBox<String> pic_get_nombre_residente;
    private javax.swing.JTextField pic_get_numero_guia;
    private javax.swing.JTextField pic_get_observacion;
    private javax.swing.JTextField pic_get_quien_reclama;
    private javax.swing.JTextField pic_get_quien_trae;
    private javax.swing.JComboBox<String> pic_get_tipo_paquete;
    public static javax.swing.JTextField pictxtget_vigilante;
    private javax.swing.JButton pip_btn_buscar_placa;
    private javax.swing.JButton pip_btn_limpiar;
    private javax.swing.JButton pip_btn_registrar_ingreso;
    private javax.swing.JButton pip_btn_registrar_salida;
    private javax.swing.JComboBox<String> piptxtget_get_apto;
    private javax.swing.JComboBox<String> piptxtget_nombre_propietario;
    private javax.swing.JTextField piptxtget_nombre_visitante;
    private javax.swing.JTextField piptxtget_placa_vehicular;
    private javax.swing.JComboBox<String> piptxtget_quien_autoriza;
    private javax.swing.JComboBox<String> piptxtget_tipo_dueño;
    private javax.swing.JComboBox<String> piptxtget_tipo_vehiculo;
    private javax.swing.JComboBox<String> piptxtget_vehicular_espejo;
    private javax.swing.JComboBox<String> piptxtget_vehicular_estrellado;
    private javax.swing.JTextField piptxtget_vehicular_observacion;
    private javax.swing.JComboBox<String> piptxtget_vehicular_rayado;
    public static javax.swing.JTextField piptxtget_vigilante;
    private javax.swing.JComboBox<String> piv_jComboBox_quien_autoriza;
    private javax.swing.JComboBox<String> pivjComboBoxget_apto;
    private javax.swing.JTextField pivtxtget_filtro;
    private javax.swing.JComboBox<String> pivtxtget_filtro_opciones;
    private javax.swing.JTextField pivtxtget_motivo_visita;
    private javax.swing.JTextField pivtxtget_nombre_visitante;
    public static javax.swing.JTextField pivtxtget_numero_documento_visitante;
    private javax.swing.JTextField pivtxtget_placa;
    public static javax.swing.JTextField pivtxtget_vigilante;
    private javax.swing.JLabel sub_panel_titulo_txt_titulo;
    private javax.swing.JLabel sub_panel_titulo_txt_titulo1;
    private javax.swing.JLabel sub_panel_titulo_txt_titulo2;
    private javax.swing.JPanel subpanel_titulo2;
    public static javax.swing.JLabel txtU5MotrarNombreUsuario;
    // End of variables declaration//GEN-END:variables
}
