package Modelos;

import java.io.Serializable;

public class Curso implements Serializable{
    //Atributos
    private String cCodCurso;
    private String cNomCurso;
    private int nNumExa;
    
    //Constructor
    public Curso(String cCodCurso, String cNomCurso, int nNumExa){
        this.cCodCurso = cCodCurso;
        this.cNomCurso = cNomCurso;
        this.nNumExa = nNumExa;
    }
    
    //GETTERS AND SETTERS
    public String getcCodCurso() {
        return cCodCurso;
    }

    public void setcCodCurso(String cCodCurso) {
        this.cCodCurso = cCodCurso;
    }

    public String getcNomCurso() {
        return cNomCurso;
    }

    public void setcNomCurso(String cNomCurso) {
        this.cNomCurso = cNomCurso;
    }

    public int getnNumExa() {
        return nNumExa;
    }

    public void setnNumExa(int nNumExa) {
        this.nNumExa = nNumExa;
    }
    
    //Método toString
    @Override
    public String toString() {
        return "Curso{" + "cCodCurso=" + cCodCurso + ", cNomCurso=" + cNomCurso + ", nNumExa=" + nNumExa + '}';
    }
    
    
}
