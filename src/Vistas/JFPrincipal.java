package Vistas;

import Controladores.CtrlConexion;
import Controladores.XMLGenerator;
import Modelos.Alumno;
import Modelos.Curso;
import Modelos.Examen;
import Modelos.VistaMatricula;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class JFPrincipal extends javax.swing.JFrame {

    //Atributos
    private CtrlConexion conexion;
    private DefaultTableModel dtmAlumnos;
    private DefaultTableModel dtmCursos;
    private DefaultTableModel dtmMatriculas;
    private DefaultTableModel dtmExamenes;
    private ArrayList<Examen> examenes;

    public JFPrincipal() {
        // Creamos una instancia de la clase CtrlConexion para gestionar la conexión a la base de datos
        conexion = new CtrlConexion();

        // Comprobamos si la conexión a la base de datos ha sido exitosa
        if (conexion.conectar("localhost", "1521", "xe", "AD_TEMA02", "AD_TEMA02") == null) {
            // Si la conexión es nula, muestra un mensaje de error y cierra la aplicación
            JOptionPane.showMessageDialog(null, "Error 0 - No se ha podido conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Iniciamos los componentes
        initComponents();

        // Configuramos el formato de fecha del componente jDateChooser
        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        // Deshabilitamos la edición manual de la fecha
        jDateChooser1.getDateEditor().setEnabled(false);

        // Configuramos la selección de las tablas para que solo se pueda seleccionar una fila a la vez
        jTableAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableMatriculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableExamenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Creamos un modelo de tabla para la tabla de alumnos
        this.dtmAlumnos = new DefaultTableModel(new String[]{"Codigo Alumno", "Nombre"}, 0);
        jTableAlumnos.setModel(dtmAlumnos);

        // Llamamos al método listarAlumnos para llenar la tabla de alumnos con información de la base de datos
        listarAlumnos();

        // Creamos un modelo de tabla para la tabla de cursos
        this.dtmCursos = new DefaultTableModel(new String[]{"Codigo Curso", "Nombre Curso", "Nº Examenes"}, 0);
        jTableCursos.setModel(dtmCursos);

        // Llamamos al método listarCursos para llenar la tabla de cursos con información de la base de datos
        listarCursos();

        // Creamos un modelo de tabla para la tabla de matrículas
        this.dtmMatriculas = new DefaultTableModel(new String[]{"Codigo Alumno", "Nombre Alumno", "Codigo Curso", "Nombre Curso", "Nota Media"}, 0);
        jTableMatriculas.setModel(dtmMatriculas);

        // Creamos un modelo de tabla para la tabla de exámenes
        this.dtmExamenes = new DefaultTableModel(new String[]{"Numero Examen", "Fecha Examen", "Nota"}, 0);
        jTableExamenes.setModel(dtmExamenes);

        // Agregamos listeners a las tablas de alumnos, matrículas y exámenes para manejar la selección de filas
        listenerAlumnos();
        listenerMatriculas();
        listenerExamenes();

    }

    //Método para añadir los alumnos al jtableAlumnos
    private void listarAlumnos() {
        // Llamamos al método readAlumnos para obtener una lista de alumnos
        ArrayList<Alumno> alumnos = this.conexion.readAlumnos();
        // Verificamos si la lista de alumnos es nula
        if (alumnos == null) {
            // Si es nula indica un error en la lectura de la tabla
            JOptionPane.showMessageDialog(null, "Error al listar la tabla alumnos", "Error", JOptionPane.ERROR_MESSAGE);
            // En caso de error, crea una lista vacía para evitar problemas
            alumnos = new ArrayList<>();
        }

        // Recorremos la lista de alumnos y agrega cada alumno a la tabla
        for (Alumno alumno : alumnos) {
            this.dtmAlumnos.addRow(new Object[]{alumno.getcCodAlu(), alumno.getcNomAlu()});
        }
    }

    //Método para añadir los cursos al jtableCursos
    private void listarCursos() {
        // Llamamos al método readCursos para obtener una lista de cursos
        ArrayList<Curso> cursos = this.conexion.readCursos();
        // Verificamos si la lista de cursos es nula
        if (cursos == null) {
            // Si es nula indica un error en la lectura de la tabla
            JOptionPane.showMessageDialog(null, "Error al listar la tabla Cursos", "Error", JOptionPane.ERROR_MESSAGE);
            // En caso de error, crea una lista vacía para evitar problemas
            cursos = new ArrayList<>();
        }

        // Recorremos la lista de cursos y agrega cada curso a la tabla
        for (Curso curso : cursos) {
            this.dtmCursos.addRow(new Object[]{curso.getcCodCurso(), curso.getcNomCurso(), curso.getnNumExa()});
        }
    }

    //Método para añadir los matriculas al jtableMatriculas
    private void listarMatriculas(String cCodAlumno) {
        // Llamamos al método readV_Matriculas para obtener una lista de matriculas
        ArrayList<VistaMatricula> vMatriculas = this.conexion.readV_Matriculas();
        // Verificamos si la lista de vMatriculas es nula
        if (vMatriculas == null) {
            // Si es nula indica un error en la lectura de la tabla
            JOptionPane.showMessageDialog(null, "Error al listar la tabla VMatriculas", "Error", JOptionPane.ERROR_MESSAGE);
            // En caso de error, crea una lista vacía para evitar problemas
            vMatriculas = new ArrayList<>();
        }

        // Recorremos la lista de matriculas y agrega cada matricula a la tabla
        for (VistaMatricula matricula : vMatriculas) {
            // Comparamos el código de alumno con el código de alumno proporcionado como argumento
            if (cCodAlumno.equals(matricula.getcCodAlu())) {
                this.dtmMatriculas.addRow(new Object[]{matricula.getcCodAlu(), matricula.getcNomAlu(),
                    matricula.getcCodCurso(), matricula.getcNomCurso(), matricula.getnNotaMedia()});
            }
        }
    }

    //Método para añadir los exámenes al jtableExamenes
    private void listarExamenes(String cCodAlumno, String cCodCurso) {
        // Llamamos al método readExamenes para obtener una lista de examenes
        this.examenes = this.conexion.readExamenes("CCODALU = '" + cCodAlumno.trim() + "' "
                + "AND CCODCURSO = '" + cCodCurso.trim() + "'");
        // Verificamos si la lista de examenes es nula
        if (examenes == null) {
            // Si es nula indica un error en la lectura de la tabla
            JOptionPane.showMessageDialog(null, "Error al listar la tabla Examenes", "Error", JOptionPane.ERROR_MESSAGE);
            // En caso de error, crea una lista vacía para evitar problemas
            examenes = new ArrayList<>();
        }

        // Recorremos la lista de examenes y agrega cada examen a la examenes
        for (Examen examen : examenes) {
            this.dtmExamenes.addRow(new Object[]{examen.getnNumExam(), examen.getdFecExam(), examen.getnNotaExam()});
        }
    }

    private void listenerAlumnos() {
        // Agregamos un ListSelectionListener al modelo de selección de la tabla de alumnos
        jTableAlumnos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Este código se ejecuta cuando cambia la selección en la tabla alumnos
                    // Obtenemos la fila seleccionada en la tabla
                    int selectedRow = jTableAlumnos.getSelectedRow();
                    // Limpiamos el modelo de la tabla de matriculas para eliminar filas anteriores
                    dtmMatriculas.setRowCount(0);
                    if (selectedRow > -1) {
                        // Obtenemos el valor de la celda en la columna 0 (código de alumno)
                        String cCodAlumno = (String) jTableAlumnos.getValueAt(selectedRow, 0);
                        // Llamamos al método listarMatriculas con el código de alumno para mostrar las matriculas en la tabla matriculas
                        listarMatriculas(cCodAlumno);
                    }
                }
            }
        });
    }

    private void listenerMatriculas() {
        // Agregamos un ListSelectionListener al modelo de selección de la tabla de matriculas
        jTableMatriculas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Este código se ejecuta cuando cambia la selección en la tabla matriculas
                    // Obtenemos la fila seleccionada en la tabla
                    int selectedRow = jTableMatriculas.getSelectedRow();
                    // Limpiamos el modelo de la tabla de examenes para eliminar filas anteriores
                    dtmExamenes.setRowCount(0);
                    if (selectedRow > -1) {
                        // Obtenemos los valores de las celdas en las columnas 0 y 2 (código de alumno y código de curso)
                        String cCodAlumno = (String) jTableMatriculas.getValueAt(selectedRow, 0);
                        String cCodCurso = (String) jTableMatriculas.getValueAt(selectedRow, 2);
                        // Llamamos al método listarExamenes con el código de alumno y código de curso para mostrar los examenes en la tabla examenes
                        listarExamenes(cCodAlumno, cCodCurso);
                    }
                }
            }
        });
    }

    private void listenerExamenes() {
        // Agregamos un ListSelectionListener al modelo de selección de la tabla de examenes
        jTableExamenes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Este código se ejecuta cuando cambia la selección en la tabla examenes
                    // Obtenemos la fila seleccionada en la tabla
                    int selectedRow = jTableExamenes.getSelectedRow();
                    if (selectedRow > -1) {
                        // Obtenemos los valores de las celdas en las columnas 1 y 2 (fecha del examen y nota del examen)
                        Date fecha = (Date) jTableExamenes.getValueAt(selectedRow, 1);
                        int notaMedia = (int) jTableExamenes.getValueAt(selectedRow, 2);
                        // Establece la fecha y la nota del examen en el jDateChooser de la fecha y el TextField de la nota
                        jDateChooser1.setDate(fecha);
                        jtfNota.setText(String.valueOf(notaMedia));
                    }
                }
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAlumnos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCursos = new javax.swing.JTable();
        jbMatricularAlumno = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMatriculas = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableExamenes = new javax.swing.JTable();
        jlFechaExamen = new javax.swing.JLabel();
        jlNota = new javax.swing.JLabel();
        jtfNota = new javax.swing.JTextField();
        jbJSON = new javax.swing.JButton();
        jbXML = new javax.swing.JButton();
        jbActualizar = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tema_02_Practica_02_AD_BasesDeDatosRelacionales");

        jTableAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Alumno", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAlumnos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTableAlumnos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableAlumnos);

        jTableCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Curso", "Nombre Curso", "Nº Examenes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCursos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableCursos);
        if (jTableCursos.getColumnModel().getColumnCount() > 0) {
            jTableCursos.getColumnModel().getColumn(0).setResizable(false);
            jTableCursos.getColumnModel().getColumn(1).setResizable(false);
            jTableCursos.getColumnModel().getColumn(2).setResizable(false);
        }

        jbMatricularAlumno.setText("Matricular Alumno en Curso");
        jbMatricularAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMatricularAlumnoActionPerformed(evt);
            }
        });

        jTableMatriculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Alumno", "Nombre Alumno", "Codigo Curso", "Nombre Curso", "Nota Media"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMatriculas.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTableMatriculas);
        if (jTableMatriculas.getColumnModel().getColumnCount() > 0) {
            jTableMatriculas.getColumnModel().getColumn(0).setResizable(false);
            jTableMatriculas.getColumnModel().getColumn(1).setResizable(false);
            jTableMatriculas.getColumnModel().getColumn(2).setResizable(false);
            jTableMatriculas.getColumnModel().getColumn(3).setResizable(false);
            jTableMatriculas.getColumnModel().getColumn(4).setResizable(false);
        }

        jTableExamenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero Examen", "Fecha Examen", "Nota"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableExamenes.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTableExamenes);
        if (jTableExamenes.getColumnModel().getColumnCount() > 0) {
            jTableExamenes.getColumnModel().getColumn(0).setResizable(false);
            jTableExamenes.getColumnModel().getColumn(1).setResizable(false);
            jTableExamenes.getColumnModel().getColumn(2).setResizable(false);
        }

        jlFechaExamen.setText("Fecha Examen");

        jlNota.setText("Nota");

        jbJSON.setText("Boletín JSON");
        jbJSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbJSONActionPerformed(evt);
            }
        });

        jbXML.setText("Listado Matricula XML");
        jbXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbXMLActionPerformed(evt);
            }
        });

        jbActualizar.setText("Actualizar");
        jbActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(327, 327, 327)
                .addComponent(jbMatricularAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbJSON, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbXML, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jlFechaExamen)
                                        .addComponent(jlNota, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jtfNota, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jbActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jbMatricularAlumno)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlFechaExamen))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtfNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlNota))
                        .addGap(18, 18, 18)
                        .addComponent(jbActualizar)
                        .addGap(69, 69, 69)
                        .addComponent(jbJSON)
                        .addGap(18, 18, 18)
                        .addComponent(jbXML)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbMatricularAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMatricularAlumnoActionPerformed
        // Obtenemos el indice de la fila seleccionada en las tablas Alumnos y Cursos
        int rowIndexAlumno = this.jTableAlumnos.getSelectedRow();
        int rowIndexCurso = this.jTableCursos.getSelectedRow();
        // Si se han seleccionado filas en ambas tablas
        if (rowIndexAlumno != -1 && rowIndexCurso != -1) {
            String cCodAlumno = (String) this.jTableAlumnos.getValueAt(rowIndexAlumno, 0);
            String cCodCurso = (String) this.jTableCursos.getValueAt(rowIndexCurso, 0);
            
            // Ejecutamos un procedimiento almacenado en la base de datos con el código de alumno y curso
            int error = this.conexion.ejecutarProcedimientoAlmacenado(cCodAlumno, cCodCurso);

            // Si no hay errores en el procedimiento almacenado
            if (error == 0) {
                JOptionPane.showMessageDialog(null, "La matrícula y los exámenes se han insertado con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Limpiamos la tabla de matriculas y volvemos a listar las matriculas para el alumno seleccionado(Así simulamos que se actualizan en tiempo real)
                dtmMatriculas.setRowCount(0);
                listarMatriculas(cCodAlumno);
            // Si el procedimiento no encontró el curso especificado    
            } else if (error == -2) {
                JOptionPane.showMessageDialog(null, "No se encontró el curso especificado", "Error", JOptionPane.ERROR_MESSAGE);
            // En caso de otros errores    
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar la matrícula y los exámenes", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Mostramos un mensaje de error si no se seleccionaron filas en las tablas Alumnos y Cursos
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fila en las tablas de Alumnos y Cursos", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbMatricularAlumnoActionPerformed

    private void jbActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbActualizarActionPerformed
        // Obtenemos el indice de la fila seleccionada en la tabla examenes
        int selectedRow = jTableExamenes.getSelectedRow();
        // Obtenemos el indice de la fila seleccionada en la tabla matriculas
        int selectedRowM = jTableMatriculas.getSelectedRow();
        // Si se ha seleccionado una fila en la tabla examenes
        if (selectedRow != -1) {
            // Obtenemos los datos de las tablas que vamos a necesitar
            int numeroExamen = (int) jTableExamenes.getValueAt(selectedRow, 0);

            String idAlumno = (String) jTableMatriculas.getValueAt(selectedRowM, 0);
            String idCurso = (String) jTableMatriculas.getValueAt(selectedRowM, 2);

            try {
                int nuevaNota = Integer.parseInt(jtfNota.getText());
                if (nuevaNota >= 0 && nuevaNota <= 10) {
                    // Crea un objeto Examen con los datos recopilados
                    Examen examen = new Examen();
                    examen.setdFecExam(jDateChooser1.getDate());
                    examen.setnNotaExam(nuevaNota);
                    examen.setcCodAlu(idAlumno);
                    examen.setcCodCurso(idCurso);
                    examen.setnNumExam(numeroExamen);

                    // Llamamos al método updateExamenes para actualizar la información del examen
                    String error = this.conexion.updateExamenes(examen);

                    // Si no hay errores en la actualización
                    if (error.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El examen se actualizó correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        // Restablecemos el fondo del campo de nota a blanco
                        jtfNota.setBackground(Color.white);
                        // Limpiamos las tablas de matriculas y examenes
                        dtmMatriculas.setRowCount(0);
                        dtmExamenes.setRowCount(0);
                        // Volvemos a listar las matriculas para el alumno seleccionado
                        listarMatriculas(idAlumno);
                        // Volvemos a listar los examenes para el alumno y curso seleccionados
                        listarExamenes(idAlumno, idCurso);
                        // Restablecemos la selección en la tabla de matriculas
                        jTableMatriculas.setRowSelectionInterval(selectedRowM, selectedRowM);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar la fecha y la nota del exámen", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    jtfNota.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, "Debes introducir valores entre el 0 y el 10 incluidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                jtfNota.setBackground(Color.red);
                JOptionPane.showMessageDialog(null, "En el campo nota solo se deben introducir datos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Mostramos un mensaje de error si no se seleccionó una fila en la tabla examenes
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fila en la tabla exámenes", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbActualizarActionPerformed

    private void jbJSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbJSONActionPerformed
        // Obtenemos el indice de la fila seleccionada en la tabla matriculas
        int selectedRowM = jTableMatriculas.getSelectedRow();
        // Si se ha seleccionado una fila en la tabla matriculas
        if (selectedRowM != -1) {
            // Obtenemos los datos necesarios
            String cCodAlumno = (String) jTableMatriculas.getValueAt(selectedRowM, 0);
            String cCodCurso = (String) jTableMatriculas.getValueAt(selectedRowM, 2);
            // Creamos un objeto Gson para formatear el JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Convertimos la lista de examenes a formato JSON
            String json = gson.toJson(examenes);

            // Escribimos el JSON en un archivo llamado "boletín.json"
            try ( FileWriter fileWriter = new FileWriter("boletin.json")) {
                fileWriter.write(json);
                JOptionPane.showMessageDialog(null, "Boletín generado con éxito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ha habido un problema al generar el boletín", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla matriculas para generar el boletín", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbJSONActionPerformed

    private void jbXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbXMLActionPerformed
        // Obtenemos una lista de matriculas desde la base de datos
        ArrayList<VistaMatricula> vMatriculas = conexion.readV_Matriculas();
        // Verificamos si la lista de matriculas es nula
        if (vMatriculas == null) {
            JOptionPane.showMessageDialog(null, "Error al listar la tabla VMatriculas", "Error", JOptionPane.ERROR_MESSAGE);
            // Creamos una lista vacía para evitar problemas
            vMatriculas = new ArrayList<>();
        }
        
        // Creamos un objeto XMLGenerator para generar el archivo XML.
        XMLGenerator archivoXML = new XMLGenerator();
        // Llamamos al método generateXMLFile para generar el archivo XML con la lista de matriculas y nombre
        String error = archivoXML.generateXMLFile(vMatriculas, "listado_matriculas.xml");
        
        // Si la generación del archivo XML es correcta
        if(error.isEmpty()){
            JOptionPane.showMessageDialog(null, "Listado de las matrículas generado con éxito", "Exito", JOptionPane.INFORMATION_MESSAGE);
        // En caso de error   
        }else{
            JOptionPane.showMessageDialog(null, "Ha habido un problema al generar el listado de las matrículas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jbXMLActionPerformed

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
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableAlumnos;
    private javax.swing.JTable jTableCursos;
    private javax.swing.JTable jTableExamenes;
    private javax.swing.JTable jTableMatriculas;
    private javax.swing.JButton jbActualizar;
    private javax.swing.JButton jbJSON;
    private javax.swing.JButton jbMatricularAlumno;
    private javax.swing.JButton jbXML;
    private javax.swing.JLabel jlFechaExamen;
    private javax.swing.JLabel jlNota;
    private javax.swing.JTextField jtfNota;
    // End of variables declaration//GEN-END:variables
}
