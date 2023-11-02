package Modelos;

import java.io.Serializable;
import java.util.Date;

public class Examen implements Serializable{
    //Atributos
    private String cCodAlu;
    private String cCodCurso;
    private int nNumExam;
    private Date dFecExam;
    private int nNotaExam;
    
    //Constructores
    
    public Examen() {
        
    }

    public Examen(String cCodAlu, String cCodCurso, int nNumExam, Date dFecExam, int nNotaExam) {
        this.cCodAlu = cCodAlu;
        this.cCodCurso = cCodCurso;
        this.nNumExam = nNumExam;
        this.dFecExam = dFecExam;
        this.nNotaExam = nNotaExam;
    }
    
    //GETTERS AND SETTERS
    public String getcCodAlu() {
        return cCodAlu;
    }

    public void setcCodAlu(String cCodAlu) {
        this.cCodAlu = cCodAlu;
    }

    public String getcCodCurso() {
        return cCodCurso;
    }

    public void setcCodCurso(String cCodCurso) {
        this.cCodCurso = cCodCurso;
    }

    public int getnNumExam() {
        return nNumExam;
    }

    public void setnNumExam(int nNumExam) {
        this.nNumExam = nNumExam;
    }

    public Date getdFecExam() {
        return dFecExam;
    }

    public void setdFecExam(Date dFecExam) {
        this.dFecExam = dFecExam;
    }

    public int getnNotaExam() {
        return nNotaExam;
    }

    public void setnNotaExam(int nNotaExam) {
        this.nNotaExam = nNotaExam;
    }
    
    //Método toString
    @Override
    public String toString() {
        return "Examen{" + "cCodAlu=" + cCodAlu + ", cCodCurso=" + cCodCurso 
                + ", nNumExam=" + nNumExam + ", dFecExam=" + dFecExam + ", nNotaExam=" + nNotaExam + '}';
    }
    
    
}
