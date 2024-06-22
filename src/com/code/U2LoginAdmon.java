package com.code;

import com.metodos.conexion;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static com.code.U4AdminAjustes.v1_ajuste_registro_name_vigilante;
import static com.code.U3AdminOpciones.txt_u4_nombre_vigilante;

/**
 *
 * @author luise
 */
public class U2LoginAdmon extends javax.swing.JFrame {

    /* |||||||||||||||||||||||||||||||||| Inicia Llamando conexion (conectar) |||||||||||||||||||||||||||||||||| */
//METODO 01 PUENTE DE CONEXION    
    conexion metodos_conectar = new conexion();
    Connection metodos_conectar_bd = metodos_conectar.conexion();
    //VARIABLE GET NOMBRE COMPLETO DATABASE
    String v1_adm_capturar_nombre;
    String v1_adm_capturar_apellido;

    /* |||||||||||||||||||||||||||||||||| Cierra Llamando conexion (conectar) |||||||||||||||||||||||||||||||||| */
    public U2LoginAdmon() {
        initComponents();
        //--------------------------------------------------------------------------------------------------------|
        //METODO 01 Establece el título de la ventana principal
        this.setTitle("Sicovp Login Admon");
        //--------------------------------------------------------------------------------------------------------|  
        //METODO 02 centrar la ventana actual del programa
        this.setLocationRelativeTo(null);
        //--------------------------------------------------------------------------------------------------------|   
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
    }

//--------------------------------------------------------------------------------------------------------|    
//METODO 04 ICONO BARRA DE TAREAS
// Este método sobrescribe el método getIconImage de la clase JFrame para establecer el icono de la aplicación.
// El icono se carga desde el archivo ico_barratareas_1.png ubicado en el paquete com.iconos
// y se establece como el icono predeterminado para la ventana.
    @Override
    public Image getIconImage() {
        // Obtener la imagen del icono usando el recurso del archivo ico_barratareas_1.png
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("com/iconos/ico_barratareas_1.png"));
        // Devolver la imagen del icono para ser usada como el icono predeterminado para la ventana
        return retValue;
    }
//--------------------------------------------------------------------------------------------------------|
//--------------------------------------------------------------------------------------------------------|
//METODO 05 validador de jTextField que solo acepte números utilizando un DocumentFilter en Java

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
//METODO 06 VALIDAR USUARIO Y CONTRASEÑA

    public int metValidarInicioSesion() {
        /*01*//*usuario*/
        String v1_usuario = v1_txt_modulo_admusuario.getText();
        /*02*//*contraseña*/
        String v1_contraseña = String.valueOf(v1_txt_modulo_admcontraseña.getPassword());
        int resultado = 0;
        /*CONSULTA BASE DE DATOS*/
        String consulta_02 = "SELECT nombres, apellidos FROM ta1_usuarios WHERE num_documento='" + v1_usuario + "' AND contraseña ='" + v1_contraseña + "'";
        Connection conect = null;
        try {
            Statement st = metodos_conectar_bd.createStatement();
            ResultSet rs = st.executeQuery(consulta_02);
            if (rs.next()) {
                v1_adm_capturar_nombre = rs.getString("nombres");
                v1_adm_capturar_apellido = rs.getString("apellidos");
                resultado = 1;
            }
        } catch (SQLException a) {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Ha habido un error al intentar conectar con el servidor :" + a,
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
        return resultado;
    }
//--------------------------------------------------------------------------------------------------------|
    // Este método se encarga de cerrar el JFrame actual, y mostrar otro

    private void metSetV1Ajuste() {
        U3AdminOpciones HALOID = new U3AdminOpciones();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
//--------------------------------------------------------------------------------------------------------|
    // Este método se encarga de cerrar el JFrame actual, y mostrar otro

    private void metSetV1Login() {
        U1Login HALOID = new U1Login();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
//--------------------------------------------------------------------------------------------------------|
//--------------------------------------------------------------------------------------------------------| 

    /* +++++++++++++++++++++++++++++++++++++ Cierra DECLARACION DE METODOS +++++++++++++++++++++++++++++++++++++ */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_fondo = new javax.swing.JPanel();
        jCPanel_fondo = new com.bolivia.panel.JCPanel();
        jPanel_fondo_baner = new javax.swing.JPanel();
        txt_fondo_baner = new javax.swing.JLabel();
        jPanel_info = new javax.swing.JPanel();
        jPanel_info_img = new javax.swing.JPanel();
        txt_info_titulo = new javax.swing.JLabel();
        txt_info_descripcion = new javax.swing.JLabel();
        jCPanel_info_img = new com.bolivia.panel.JCPanel();
        jPanel_modulo_usuario = new javax.swing.JPanel();
        txt_modulo_usuario_info = new javax.swing.JLabel();
        v1_txt_modulo_admusuario = new javax.swing.JTextField();
        txt_modulo_iniciarsesion = new javax.swing.JLabel();
        jPanel_modulo_usuario1 = new javax.swing.JPanel();
        txt_modulo_usuario_info1 = new javax.swing.JLabel();
        v1_txt_modulo_admcontraseña = new javax.swing.JPasswordField();
        v1_btn_admlogin = new javax.swing.JButton();
        btn_regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());

        jPanel_fondo.setMinimumSize(new java.awt.Dimension(800, 500));
        jPanel_fondo.setPreferredSize(new java.awt.Dimension(900, 500));
        jPanel_fondo.setRequestFocusEnabled(false);

        jCPanel_fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/imagenes/v1_login_1_fondo_3.png"))); // NOI18N
        jCPanel_fondo.setVisibleLogo(false);

        jPanel_fondo_baner.setBackground(new java.awt.Color(51, 51, 51));

        txt_fondo_baner.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_fondo_baner.setForeground(new java.awt.Color(255, 255, 255));
        txt_fondo_baner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_fondo_baner.setText("EDIFICIO PARQUE NATURA");

        javax.swing.GroupLayout jPanel_fondo_banerLayout = new javax.swing.GroupLayout(jPanel_fondo_baner);
        jPanel_fondo_baner.setLayout(jPanel_fondo_banerLayout);
        jPanel_fondo_banerLayout.setHorizontalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        jPanel_fondo_banerLayout.setVerticalGroup(
            jPanel_fondo_banerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondo_banerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_fondo_baner)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_info.setPreferredSize(new java.awt.Dimension(594, 271));

        jPanel_info_img.setBackground(new java.awt.Color(0, 51, 51));

        txt_info_titulo.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_info_titulo.setForeground(new java.awt.Color(255, 255, 255));
        txt_info_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_info_titulo.setText("SICOVP");

        txt_info_descripcion.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txt_info_descripcion.setForeground(new java.awt.Color(255, 255, 255));
        txt_info_descripcion.setText("Sistema de control e ingreso vehicular y peatonal");

        jCPanel_info_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/edificio-de-oficinas.png"))); // NOI18N
        jCPanel_info_img.setVisibleLogo(false);

        javax.swing.GroupLayout jCPanel_info_imgLayout = new javax.swing.GroupLayout(jCPanel_info_img);
        jCPanel_info_img.setLayout(jCPanel_info_imgLayout);
        jCPanel_info_imgLayout.setHorizontalGroup(
            jCPanel_info_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jCPanel_info_imgLayout.setVerticalGroup(
            jCPanel_info_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel_info_imgLayout = new javax.swing.GroupLayout(jPanel_info_img);
        jPanel_info_img.setLayout(jPanel_info_imgLayout);
        jPanel_info_imgLayout.setHorizontalGroup(
            jPanel_info_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_info_imgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_info_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_info_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_info_descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_info_imgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCPanel_info_img, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );
        jPanel_info_imgLayout.setVerticalGroup(
            jPanel_info_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_info_imgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_info_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_info_descripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCPanel_info_img, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel_modulo_usuario.setBackground(new java.awt.Color(0, 51, 51));
        jPanel_modulo_usuario.setPreferredSize(new java.awt.Dimension(250, 60));

        txt_modulo_usuario_info.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        txt_modulo_usuario_info.setForeground(new java.awt.Color(255, 255, 255));
        txt_modulo_usuario_info.setText("CEDULA");

        v1_txt_modulo_admusuario.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        v1_txt_modulo_admusuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                v1_txt_modulo_admusuarioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel_modulo_usuarioLayout = new javax.swing.GroupLayout(jPanel_modulo_usuario);
        jPanel_modulo_usuario.setLayout(jPanel_modulo_usuarioLayout);
        jPanel_modulo_usuarioLayout.setHorizontalGroup(
            jPanel_modulo_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_modulo_usuario_info, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(v1_txt_modulo_admusuario)
        );
        jPanel_modulo_usuarioLayout.setVerticalGroup(
            jPanel_modulo_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuarioLayout.createSequentialGroup()
                .addComponent(txt_modulo_usuario_info, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_txt_modulo_admusuario, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );

        txt_modulo_iniciarsesion.setBackground(new java.awt.Color(0, 0, 0));
        txt_modulo_iniciarsesion.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_modulo_iniciarsesion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_modulo_iniciarsesion.setText("INICIAR SESION ADMON");

        jPanel_modulo_usuario1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel_modulo_usuario1.setPreferredSize(new java.awt.Dimension(250, 60));

        txt_modulo_usuario_info1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        txt_modulo_usuario_info1.setForeground(new java.awt.Color(255, 255, 255));
        txt_modulo_usuario_info1.setText("CONTRASEÑA");

        v1_txt_modulo_admcontraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                v1_txt_modulo_admcontraseñaActionPerformed(evt);
            }
        });
        v1_txt_modulo_admcontraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                v1_txt_modulo_admcontraseñaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel_modulo_usuario1Layout = new javax.swing.GroupLayout(jPanel_modulo_usuario1);
        jPanel_modulo_usuario1.setLayout(jPanel_modulo_usuario1Layout);
        jPanel_modulo_usuario1Layout.setHorizontalGroup(
            jPanel_modulo_usuario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuario1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_modulo_usuario_info1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(v1_txt_modulo_admcontraseña)
        );
        jPanel_modulo_usuario1Layout.setVerticalGroup(
            jPanel_modulo_usuario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuario1Layout.createSequentialGroup()
                .addComponent(txt_modulo_usuario_info1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_txt_modulo_admcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        v1_btn_admlogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/Ico_add_aspirante_v3.png"))); // NOI18N
        v1_btn_admlogin.setText("INGRESAR");
        v1_btn_admlogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v1_btn_admloginMouseClicked(evt);
            }
        });
        v1_btn_admlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                v1_btn_admloginActionPerformed(evt);
            }
        });

        btn_regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x24.png"))); // NOI18N
        btn_regresar.setText("Regresar");
        btn_regresar.setToolTipText("Regresar");
        btn_regresar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x20.png"))); // NOI18N
        btn_regresar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/ico_home_x22.png"))); // NOI18N
        btn_regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_regresarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel_infoLayout = new javax.swing.GroupLayout(jPanel_info);
        jPanel_info.setLayout(jPanel_infoLayout);
        jPanel_infoLayout.setHorizontalGroup(
            jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_infoLayout.createSequentialGroup()
                .addComponent(jPanel_info_img, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_modulo_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_modulo_usuario1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(v1_btn_admlogin)
                    .addComponent(btn_regresar)
                    .addComponent(txt_modulo_iniciarsesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel_infoLayout.setVerticalGroup(
            jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_info_img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_modulo_iniciarsesion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_modulo_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel_modulo_usuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_btn_admlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_regresar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jCPanel_fondoLayout = new javax.swing.GroupLayout(jCPanel_fondo);
        jCPanel_fondo.setLayout(jCPanel_fondoLayout);
        jCPanel_fondoLayout.setHorizontalGroup(
            jCPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCPanel_fondoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jCPanel_fondoLayout.setVerticalGroup(
            jCPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCPanel_fondoLayout.createSequentialGroup()
                .addComponent(jPanel_fondo_baner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(jPanel_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_fondoLayout = new javax.swing.GroupLayout(jPanel_fondo);
        jPanel_fondo.setLayout(jPanel_fondoLayout);
        jPanel_fondoLayout.setHorizontalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        jPanel_fondoLayout.setVerticalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void v1_btn_admloginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v1_btn_admloginMouseClicked
        //METODO LOGIN
        if (metValidarInicioSesion() == 1) {
            metSetV1Ajuste();//Invocar metodo
            /*ENVIAR NONBRE COMPLETO A LA PROXIMA A VENTANA v1_login_admon_ajuste*/
            txt_u4_nombre_vigilante.setText(v1_adm_capturar_nombre + " " + v1_adm_capturar_apellido);
            v1_ajuste_registro_name_vigilante.setText(v1_adm_capturar_nombre + " " + v1_adm_capturar_apellido);
            
            
        } else {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion [Usuario o contraseña incorrecta]",
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
    }//GEN-LAST:event_v1_btn_admloginMouseClicked

    private void v1_txt_modulo_admusuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v1_txt_modulo_admusuarioKeyTyped
        /* ------------------------------------------- Ini Llamar clase NumberOnlyFilter -------------------------------------------- */
        //Codigo para llamar clase NumberOnlyFilter la cual filtra que se escriba solo numero
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) v1_txt_modulo_admusuario.getDocument();
        doc.setDocumentFilter(new NumberOnlyFilter());
        /* ------------------------------------------- Fin Llamar clase NumberOnlyFilter -------------------------------------------- */

 /* ----------------------------------------------- Ini Validador (Cantidad Caracteres)  ----------------------------------------------- */
        //Validador JtexField Cantidad de Caracteres deseados
        if (v1_txt_modulo_admusuario.getText().length() == 10) {
            evt.consume();
        }
        /* ----------------------------------------------- Fin Validador (Cantidad Caracteres)  ----------------------------------------------- */
    }//GEN-LAST:event_v1_txt_modulo_admusuarioKeyTyped

    private void v1_txt_modulo_admcontraseñaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v1_txt_modulo_admcontraseñaKeyTyped
        /* ----------------------------------------------- Ini Validador (Cantidad Caracteres)  ----------------------------------------------- */
        //Validador Jpassword Cantidad de Caracteres deseados
        if (v1_txt_modulo_admcontraseña.getText().length() == 30) {
            evt.consume();
        }
        /* ----------------------------------------------- Fin Validador (Cantidad Caracteres)  ----------------------------------------------- */
    }//GEN-LAST:event_v1_txt_modulo_admcontraseñaKeyTyped

    private void btn_regresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_regresarMouseClicked
        /* ----------------------------------------------- Ini Boton (home)  ----------------------------------------------- */
        metSetV1Login();
        /* ----------------------------------------------- Fin Boton (home)  ----------------------------------------------- */
    }//GEN-LAST:event_btn_regresarMouseClicked

    private void v1_btn_admloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_v1_btn_admloginActionPerformed
        //METODO LOGIN
        if (metValidarInicioSesion() == 1) {
            metSetV1Ajuste();//Invocar metodo
            /*ENVIAR NONBRE COMPLETO A LA PROXIMA A VENTANA */
            txt_u4_nombre_vigilante.setText(v1_adm_capturar_nombre + " " + v1_adm_capturar_apellido);
        } else {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion [Usuario o contraseña incorrecta]",
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
    }//GEN-LAST:event_v1_btn_admloginActionPerformed

    private void v1_txt_modulo_admcontraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_v1_txt_modulo_admcontraseñaActionPerformed
        //METODO LOGIN
        if (metValidarInicioSesion() == 1) {
            metSetV1Ajuste();//Invocar metodo
            /*ENVIAR NONBRE COMPLETO A LA PROXIMA A VENTANA */
            txt_u4_nombre_vigilante.setText(v1_adm_capturar_nombre + " " + v1_adm_capturar_apellido);
        } else {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion [Usuario o contraseña incorrecta]",
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
    }//GEN-LAST:event_v1_txt_modulo_admcontraseñaActionPerformed

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
            java.util.logging.Logger.getLogger(U2LoginAdmon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(U2LoginAdmon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(U2LoginAdmon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(U2LoginAdmon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new U2LoginAdmon().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_regresar;
    private com.bolivia.panel.JCPanel jCPanel_fondo;
    private com.bolivia.panel.JCPanel jCPanel_info_img;
    private javax.swing.JPanel jPanel_fondo;
    private javax.swing.JPanel jPanel_fondo_baner;
    private javax.swing.JPanel jPanel_info;
    private javax.swing.JPanel jPanel_info_img;
    private javax.swing.JPanel jPanel_modulo_usuario;
    private javax.swing.JPanel jPanel_modulo_usuario1;
    private javax.swing.JLabel txt_fondo_baner;
    private javax.swing.JLabel txt_info_descripcion;
    private javax.swing.JLabel txt_info_titulo;
    private javax.swing.JLabel txt_modulo_iniciarsesion;
    private javax.swing.JLabel txt_modulo_usuario_info;
    private javax.swing.JLabel txt_modulo_usuario_info1;
    private javax.swing.JButton v1_btn_admlogin;
    public static javax.swing.JPasswordField v1_txt_modulo_admcontraseña;
    public static javax.swing.JTextField v1_txt_modulo_admusuario;
    // End of variables declaration//GEN-END:variables
}
