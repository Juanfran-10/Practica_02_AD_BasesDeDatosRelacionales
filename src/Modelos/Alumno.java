package Modelos;

import java.io.Serializable;

public class Alumno implements Serializable{
    //Atributos
    private String cCodAlu;
    private String cNomAlu;
    
    //Constructor
    public Alumno(String cCodAlu, String cNomAlu){
        this.cCodAlu = cCodAlu;
        this.cNomAlu = cNomAlu;
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
    
    //Método toString
    @Override
    public String toString() {
        return "Alumno{" + "cCodAlu=" + cCodAlu + ", cNomAlu=" + cNomAlu + '}';
    }
    
}
