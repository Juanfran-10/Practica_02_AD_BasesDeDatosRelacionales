package Controladores;

import Modelos.Alumno;
import Modelos.Curso;
import Modelos.Examen;
import Modelos.VistaMatricula;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CtrlConexion {

    //Atributos
    private Connection con;
    private String cadenaConexion;
    private String error;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    private ArrayList<Alumno> listaAlumnos = new ArrayList<>();
    private ArrayList<Curso> listaCursos = new ArrayList<>();
    private ArrayList<VistaMatricula> listaMatriculas = new ArrayList<>();
    private ArrayList<Examen> listaExamenes = new ArrayList<>();
    private String ins;
    private CallableStatement cl;

    //Método Conectar
    public Connection conectar(String servidor, String puerto, String sid, String usuario, String contraseña) {
        // Crea la cadena de conexión a la base de datos
        cadenaConexion = "jdbc:oracle:thin:@" + servidor.trim() + ":" + puerto.trim() + ":" + sid.trim();
        try {
            // Intenta establecer una conexión utilizando la cadena de conexión y las credenciales proporcionadas
            con = DriverManager.getConnection(cadenaConexion, usuario, contraseña);
        } catch (SQLException sql) {
            //En caso de excepción SQL, establece la conexión a null para indicar un error
            con = null;
        } finally {
            // Devuelve la conexión, que puede ser válida o null en caso de error
            return con;
        }
    }

    //Método para sacar a un ArrayList todos los alumnos
    public ArrayList<Alumno> readAlumnos() {
        listaAlumnos.removeAll(listaAlumnos);// Limpia la lista de Alumnos (elimina todos los elementos)
        try {
            // Verifica si la conexión no es nula y no está cerrada
            if (con != null && con.isClosed() == false) {
                ins = "SELECT * FROM ALUMNOS";
                st = con.createStatement();
                // Ejecuta la consulta y almacena los resultados en un conjunto de resultados (ResultSet)
                rs = st.executeQuery(ins);
                // Recorre los resultados y crea objetos Alumno con los datos de cada fila
                while (rs.next()) {
                    listaAlumnos.add(new Alumno(rs.getString("cCodAlu"), rs.getString("cNomAlu")));
                }
            }
            // Devolvemos la lista de cursos actualizada
            return listaAlumnos;
        } catch (SQLException sql) {
            // En caso de excepción SQL, devuelve null
            return listaAlumnos = null;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                this.listaAlumnos = null;
            } finally {
                // Devuelve la lista de alumnos (puede ser vacía o null en caso de error)
                return listaAlumnos;
            }

        }
    }

    //Método para sacar a un ArrayList todos los Cursos
    public ArrayList<Curso> readCursos() {
        listaCursos.removeAll(listaCursos);// Limpia la lista de cursos (elimina todos los elementos)
        try {
            // Verifica si la conexión no es nula y no está cerrada
            if (con != null && con.isClosed() == false) {
                ins = "SELECT * FROM CURSOS";
                st = con.createStatement();
                // Ejecuta la consulta y almacena los resultados en un conjunto de resultados (ResultSet)
                rs = st.executeQuery(ins);
                // Recorre los resultados y crea objetos Curso con los datos de cada fila
                while (rs.next()) {
                    listaCursos.add(new Curso(rs.getString("cCodCurso"), rs.getString("cNomCurso"), rs.getInt("nNumExa")));
                }
            }
            // Devolvemos la lista de cursos actualizada
            return listaCursos;
        } catch (SQLException sql) {
            // En caso de excepción SQL, devuelve null
            return listaCursos = null;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                this.listaCursos = null;
            } finally {
                // Devuelve la lista de cursos (puede ser vacía o null en caso de error)
                return listaCursos;
            }

        }
    }

    //Método para ejecutar el procedimientoAlmacenado 
    public int ejecutarProcedimientoAlmacenado(String idAlumno, String idCurso) {
        int error = 0;
        try {
            // Crear un objeto CallableStatement para ejecutar el procedimiento almacenado
            this.cl = this.con.prepareCall("{call sp_AltaMatricula(?, ?, ?)}");
            this.cl.setString(1, idAlumno);
            this.cl.setString(2, idCurso);
            this.cl.registerOutParameter(3, java.sql.Types.INTEGER);
            // Ejecutar el procedimiento almacenado
            this.cl.execute();

            error = cl.getInt(3);
        } catch (SQLException e) {
            error = -2;
        } finally {
            return error;
        }
    }

    //Método para sacar un ArrayList con los cursos en los que esta matriculados los alumnos
    public ArrayList<VistaMatricula> readV_Matriculas() {
        this.listaMatriculas.removeAll(listaMatriculas);// Limpia la lista de matriculas (elimina todos los elementos)
        try {
            // Verifica si la conexión no es nula y no está cerrada
            if (con != null && con.isClosed() == false) {
                ins = "SELECT * FROM V_MATRICULAS";
                st = con.createStatement();
                // Ejecuta la consulta y almacena los resultados en un conjunto de resultados (ResultSet)
                rs = st.executeQuery(ins);
                // Recorre los resultados y crea objetos VistaMatricula con los datos de cada fila
                while (rs.next()) {
                    this.listaMatriculas.add(new VistaMatricula(rs.getString("cCodAlu"), rs.getString("cNomAlu"),
                            rs.getString("cCodCurso"), rs.getString("cNomCurso"), rs.getInt("nNotaMedia")));
                }
            }
            // Devolvemos la lista de matriculas actualizada
            return this.listaMatriculas;
        } catch (SQLException sql) {
            // En caso de excepción SQL, devuelve null
            return this.listaMatriculas = null;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                this.listaMatriculas = null;
            } finally {
                // Devuelve la lista de matriculas (puede ser vacía o null en caso de error)
                return this.listaMatriculas;
            }

        }
    }

    //Método para sacar a un ArrayList todos los Cursos
    public ArrayList<Examen> readExamenes(String where) {
        this.listaExamenes.removeAll(listaExamenes);// Limpia la lista de examenes (elimina todos los elementos)
        // Creamos un formateador de fechas
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // Verifica si la conexión no es nula y no está cerrada
            if (con != null && con.isClosed() == false) {
                ins = "SELECT * FROM EXAMENES WHERE " + where;
                st = con.createStatement();
                // Ejecuta la consulta y almacena los resultados en un conjunto de resultados (ResultSet)
                rs = st.executeQuery(ins);
                // Recorre los resultados y crea objetos Examen con los datos de cada fila
                while (rs.next()) {
                    this.listaExamenes.add(new Examen(rs.getString("cCodAlu"), rs.getString("cCodCurso"),
                            rs.getInt("nNumExam"), rs.getDate("dFecExam"), rs.getInt("nNotaExam")));
                }
            }
            // Devolvemos la lista de exámenes actualizada
            return this.listaExamenes;
        } catch (SQLException sql) {
            // En caso de excepción SQL, devuelve null
            return this.listaExamenes = null;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                this.listaExamenes = null;
            } finally {
                // Devuelve la lista de exámenes (puede ser vacía o null en caso de error)
                return this.listaExamenes;
            }

        }
    }

    //Método para actualizar Fecha y la nota de los examenes
    public String updateExamenes(Examen examen) {
        error = "";
        java.sql.Date sqlDate = new java.sql.Date(0);
        try {
            // Verifica si la conexión no es nula y no está cerrada.
            if (con != null && con.isClosed() == false) {
                ins = "UPDATE EXAMENES SET DFECEXAM = ?, NNOTAEXAM = ? WHERE CCODALU = ? AND CCODCURSO = ? AND NNUMEXAM = ?";
                ps = con.prepareStatement(ins);
                // Se obtiene la fecha del objeto Examen.
                java.util.Date utilDate = examen.getdFecExam();
                // Se convierte la fecha utilDate a java.sql.Date.
                sqlDate = new java.sql.Date(utilDate.getTime());
                // Se establecen los valores de los parámetros en la declaración preparada.
                ps.setDate(1, sqlDate);
                ps.setDouble(2, examen.getnNotaExam());
                ps.setString(3, examen.getcCodAlu());
                ps.setString(4, examen.getcCodCurso());
                ps.setInt(5, examen.getnNumExam());
                // Ejecuta la actualización en la base de datos.
                ps.executeUpdate();
            }

        } catch (SQLException sql) {
            error = sql.getMessage();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                error = ex.getMessage();
            }
            return error;
        }
    }
}
