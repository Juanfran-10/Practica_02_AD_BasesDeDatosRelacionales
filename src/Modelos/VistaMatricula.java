package Modelos;

import java.io.Serializable;

public class VistaMatricula implements Serializable{
    //Atributos
    private String cCodAlu;
    private String cNomAlu;
    private String cCodCurso;
    private String cNomCurso;
    private int nNotaMedia;
    
    //Constructor
    public VistaMatricula(String cCodAlu, String cNomAlu, String cCodCurso, String cNomCurso, int nNotaMedia){
        this.cCodAlu = cCodAlu;
        this.cNomAlu = cNomAlu;
        this.cCodCurso = cCodCurso;
        this.cNomCurso = cNomCurso;
        this.nNotaMedia = nNotaMedia;
    }

    //GETTERS AND SETTERS
    public String getcCodAlu() {
        return cCodAlu;
    }

    public void setcCodAlu(String cCodAlu) {
        this.cCodAlu = cCodAlu;
    }

    public String getcNomAlu() {
        return cNomAlu;
    }

    public void setcNomAlu(String cNomAlu) {
        this.cNomAlu = cNomAlu;
    }

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

    public int getnNotaMedia() {
        return nNotaMedia;
    }

    public void setnNotaMedia(int nNotaMedia) {
        this.nNotaMedia = nNotaMedia;
    }
    
       
    //Método toString
    @Override
    public String toString() {
        return "VistaMatricula{" + "cCodAlu=" + cCodAlu + ", cNomAlu=" + cNomAlu 
                + ", cCodCurso=" + cCodCurso + ", cNomCurso=" + cNomCurso + ", nNotaMedia=" + nNotaMedia + '}';
    }
       
}
