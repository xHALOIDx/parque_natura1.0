//=============================================================================================================
package com.code;

import static com.code.U5Home.piptxtget_vigilante;
import static com.code.U5Home.v2_txt_set_name_vigilante;
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
import static com.code.U5Home.pivtxtget_vigilante;
import static com.code.U5Home.pictxtget_vigilante;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
//=============================================================================================================

/**
 *
 * @author luise
 */
public class U1Login extends javax.swing.JFrame {

    /* |||||||||||||||||||||||||||||||||| Inicia Llamando conexion (conectar) |||||||||||||||||||||||||||||||||| */
    // Conexión a la base de datos  
    private conexion metodos_conectar = new conexion();
    private Connection metodos_conectar_bd = metodos_conectar.conexion();
    //// Variables para capturar nombre y apellido
    private String v1_capturar_nombre;
    private String v1_capturar_apellido;

    /**
     * Constructor de la clase v1_login. Inicializa los componentes y configura
     * la ventana.
     */

    /*||||||||||||||||||||||||||||||||||||| Finaliza Llamando conexion (conectar)|||||||||||||||||||||||||||||||||*/
    //=============================================================================================================
    public U1Login() {
        initComponents();
        //=============================================================================================================| 
        //METODO 01 Establece el título de la ventana principal
        this.setTitle("Sicovp Login");
        //=============================================================================================================|
        //METODO 02 centrar la ventana actual del programa
        this.setLocationRelativeTo(null);
        //=============================================================================================================|   
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
        //=============================================================================================================|
    }
    //=============================================================================================================

    /* ++++++++++++++++++++++++++++++++++++++++++++++ INICIO METODOS - LOGICA ++++++++++++++++++++++++++++++++++++++++++++++ */
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|    
    /* Método para obtener la imagen del icono */
// Este método sobrescribe el método getIconImage de la clase JFrame para establecer el icono de la aplicación.
    @Override
    public Image getIconImage() {
        return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("com/iconos/ico_barratareas_1.png"));
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|    

    /* Clase interna para filtrar los caracteres no numéricos en un JTextField. */
    public class NumberOnlyFilter extends DocumentFilter {
        // Método que se llama al momento de insertar un nuevo carácter

        @Override
        public void insertString(FilterBypass fb, int offset, String string,
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
        public void replace(FilterBypass fb, int offset, int length, String string,
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
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 

    /* Método para validar el inicio de sesión */
    /*  @return 1 si el usuario es válido, 0 en caso contrario. */
    
    public int metValidarInicioSesion() {
        /*01*//*usuario*/
        String v1_usuario = v1_txt_modulo_usuario.getText();
        /*02*//*contraseña*/
        String v1_contraseña = String.valueOf(v1_txt_modulo_contraseña.getPassword());
        int resultado = 0;
        /*CONSULTA BASE DE DATOS*/
        String consulta_01 = "SELECT nombres, apellidos FROM ta1_usuarios WHERE num_documento='" + v1_usuario + "' AND contraseña ='" + v1_contraseña + "'";
        Connection conect = null;
        try {
            Statement st = metodos_conectar_bd.createStatement();
            ResultSet rs = st.executeQuery(consulta_01);
            if (rs.next()) {
                v1_capturar_nombre = rs.getString("nombres");
                v1_capturar_apellido = rs.getString("apellidos");
                resultado = 1;
            }
        } catch (SQLException ex_01) {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Ha habido un error al intentar conectar con el servidor :" + ex_01,
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
        return resultado;
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|   

// Este método se encarga de cerrar el JFrame actual, y mostrar otro
    private void metSetV1AdmLogin() {
        U2LoginAdmon HALOID = new U2LoginAdmon();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 
    // Este método se encarga de cerrar el JFrame actual, y mostrar otro

    private void metSetV2Home() {
        U5Home HALOID = new U5Home();// Creamos una nueva instancia de la vista v1_adm_login  
        HALOID.setVisible(true);   // Hacemos visible la nueva vista
        this.dispose(); // Cerramos la vista actual
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++| 

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
        v1_txt_modulo_usuario = new javax.swing.JTextField();
        txt_modulo_iniciarsesion = new javax.swing.JLabel();
        jPanel_modulo_usuario1 = new javax.swing.JPanel();
        txt_modulo_usuario_info1 = new javax.swing.JLabel();
        v1_txt_modulo_contraseña = new javax.swing.JPasswordField();
        v1_btn_login = new javax.swing.JButton();
        v1_btn_administrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setMinimumSize(new java.awt.Dimension(500, 300));

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
            .addComponent(txt_fondo_baner, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
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
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel_modulo_usuario.setBackground(new java.awt.Color(0, 51, 51));
        jPanel_modulo_usuario.setPreferredSize(new java.awt.Dimension(250, 60));

        txt_modulo_usuario_info.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        txt_modulo_usuario_info.setForeground(new java.awt.Color(255, 255, 255));
        txt_modulo_usuario_info.setText("CEDULA");

        v1_txt_modulo_usuario.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        v1_txt_modulo_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                v1_txt_modulo_usuarioKeyTyped(evt);
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
            .addComponent(v1_txt_modulo_usuario)
        );
        jPanel_modulo_usuarioLayout.setVerticalGroup(
            jPanel_modulo_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuarioLayout.createSequentialGroup()
                .addComponent(txt_modulo_usuario_info, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_txt_modulo_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );

        txt_modulo_iniciarsesion.setBackground(new java.awt.Color(0, 0, 0));
        txt_modulo_iniciarsesion.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_modulo_iniciarsesion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_modulo_iniciarsesion.setText("INICIAR SESION");

        jPanel_modulo_usuario1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel_modulo_usuario1.setPreferredSize(new java.awt.Dimension(250, 60));

        txt_modulo_usuario_info1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        txt_modulo_usuario_info1.setForeground(new java.awt.Color(255, 255, 255));
        txt_modulo_usuario_info1.setText("CONTRASEÑA");

        v1_txt_modulo_contraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                v1_txt_modulo_contraseñaActionPerformed(evt);
            }
        });
        v1_txt_modulo_contraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                v1_txt_modulo_contraseñaKeyPressed(evt);
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
            .addComponent(v1_txt_modulo_contraseña)
        );
        jPanel_modulo_usuario1Layout.setVerticalGroup(
            jPanel_modulo_usuario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_modulo_usuario1Layout.createSequentialGroup()
                .addComponent(txt_modulo_usuario_info1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v1_txt_modulo_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        v1_btn_login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/Ico_add_aspirante_v3.png"))); // NOI18N
        v1_btn_login.setText("INGRESAR");
        v1_btn_login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v1_btn_loginMouseClicked(evt);
            }
        });
        v1_btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                v1_btn_loginActionPerformed(evt);
            }
        });

        v1_btn_administrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/iconos/Ico_add_aspirante_v3.png"))); // NOI18N
        v1_btn_administrar.setText("ADMINISTRAR");
        v1_btn_administrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v1_btn_administrarMouseClicked(evt);
            }
        });

        jLabel1.setText("He olvidado mi contraseña.");

        javax.swing.GroupLayout jPanel_infoLayout = new javax.swing.GroupLayout(jPanel_info);
        jPanel_info.setLayout(jPanel_infoLayout);
        jPanel_infoLayout.setHorizontalGroup(
            jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_infoLayout.createSequentialGroup()
                .addComponent(jPanel_info_img, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_modulo_iniciarsesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_infoLayout.createSequentialGroup()
                        .addGroup(jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel_modulo_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel_modulo_usuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_infoLayout.createSequentialGroup()
                                .addComponent(v1_btn_login)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(v1_btn_administrar))
                            .addComponent(jLabel1))
                        .addGap(0, 23, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGroup(jPanel_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(v1_btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(v1_btn_administrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
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
            .addComponent(jCPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        jPanel_fondoLayout.setVerticalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void v1_btn_administrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v1_btn_administrarMouseClicked
        /* ----------------------------------------------- Ini Boton (Administrar)  ----------------------------------------------- */
        metSetV1AdmLogin();//Invocar metodo
        /* ----------------------------------------------- Fin Boton (Administrar)  ----------------------------------------------- */
    }//GEN-LAST:event_v1_btn_administrarMouseClicked


    private void v1_btn_loginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v1_btn_loginMouseClicked

    }//GEN-LAST:event_v1_btn_loginMouseClicked

    private void v1_txt_modulo_usuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v1_txt_modulo_usuarioKeyTyped
        /* ------------------------------------------- Ini Llamar metodo NumberOnlyFilter -------------------------------------------- */
        //Codigo para llamar clase NumberOnlyFilter la cual filtra que se escriba solo numero
        javax.swing.text.AbstractDocument doc
                = (javax.swing.text.AbstractDocument) v1_txt_modulo_usuario.getDocument();
        doc.setDocumentFilter(new NumberOnlyFilter());
        /* ------------------------------------------- Fin Llamar clase NumberOnlyFilter -------------------------------------------- */
 /* ------------------------------------------- Ini Validador (Cantidad Caracteres) ----------------------------------------------- */
        //Validador JtexField Cantidad de Caracteres deseados
        if (v1_txt_modulo_usuario.getText().length() == 10) {
            evt.consume();
        }
        /* -------------------------------------------- Fin Validador (Cantidad Caracteres) ----------------------------------------------- */
    }//GEN-LAST:event_v1_txt_modulo_usuarioKeyTyped

    private void v1_btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_v1_btn_loginActionPerformed
        //METODO LOGIN
        if (metValidarInicioSesion() == 1) {
            metSetV2Home();//Invocar metodo
            /*ENVIAR NONBRE COMPLETO A LA PROXIMA A VENTANA */
            v2_txt_set_name_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            pivtxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            pictxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            piptxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
        } else {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion [Usuario o contraseña incorrecta]",
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
    }//GEN-LAST:event_v1_btn_loginActionPerformed

    private void v1_txt_modulo_contraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_v1_txt_modulo_contraseñaActionPerformed
        //METODO LOGIN
        if (metValidarInicioSesion() == 1) {
            metSetV2Home();//Invocar metodo
            /*ENVIAR NONBRE COMPLETO A LA PROXIMA A VENTANA */
            v2_txt_set_name_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            pivtxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            pictxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
            piptxtget_vigilante.setText(v1_capturar_nombre + " " + v1_capturar_apellido);
        } else {
            Icon halo_v1 = new ImageIcon(getClass().getResource("/com/iconos/ico_bd_error.png"));
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion [Usuario o contraseña incorrecta]",
                    "                                              Atencion", JOptionPane.YES_NO_CANCEL_OPTION,
                    halo_v1);
        }
    }//GEN-LAST:event_v1_txt_modulo_contraseñaActionPerformed

    private void v1_txt_modulo_contraseñaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v1_txt_modulo_contraseñaKeyPressed

    }//GEN-LAST:event_v1_txt_modulo_contraseñaKeyPressed

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
    UIManager.setLookAndFeel( new FlatLightLaf() );
} catch( Exception ex ) {
    System.err.println( "Failed to initialize LaF" );
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new U1Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.bolivia.panel.JCPanel jCPanel_fondo;
    private com.bolivia.panel.JCPanel jCPanel_info_img;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JButton v1_btn_administrar;
    private javax.swing.JButton v1_btn_login;
    private javax.swing.JPasswordField v1_txt_modulo_contraseña;
    private javax.swing.JTextField v1_txt_modulo_usuario;
    // End of variables declaration//GEN-END:variables
}
