package Controladores;

import Modelos.VistaMatricula;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

public class XMLGenerator {

    public String generateXMLFile(ArrayList<VistaMatricula> matriculas, String fileName) {
        String error = "";
        try {
            // Creamos una instancia de XMLOutputFactory que se utiliza para producir el XML
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // Creamos un XMLStreamWriter utilizando la instancia de XMLOutputFactory. Esto se utiliza para escribir el archivo XML
            // También creo un FileOutputStream para escribir en un archivo con el nombre proporcionado y con codificación UTF-8
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");

            // Escribo la cabecera XML
            writer.writeStartDocument("UTF-8", "1.0");
            // Abre una etiqueta raíz "matriculas" en el archivo XML
            writer.writeStartElement("matriculas");

            for (VistaMatricula matricula : matriculas) {
                // Abre una etiqueta "Alumno" para cada objeto VistaMatricula
                writer.writeStartElement("Alumno");

                // Escribimos los datos como elementos XML
                writer.writeStartElement("cCodAlu");
                writer.writeCharacters(matricula.getcCodAlu());
                writer.writeEndElement();

                writer.writeStartElement("cNomAlu");
                writer.writeCharacters(matricula.getcNomAlu());
                writer.writeEndElement();

                writer.writeStartElement("Curso");

                writer.writeStartElement("cCodCurso");
                writer.writeCharacters(matricula.getcCodCurso());
                writer.writeEndElement();

                writer.writeStartElement("cNomCurso");
                writer.writeCharacters(matricula.getcNomCurso());
                writer.writeEndElement();

                writer.writeStartElement("nNotaMedia");
                writer.writeCharacters(Integer.toString(matricula.getnNotaMedia()));
                writer.writeEndElement();

                writer.writeEndElement(); // Cerramos la etiqueta Curso
                writer.writeEndElement(); // Cerramos la etiqueta Alumno
            }

            writer.writeEndElement(); // Cierra la etiqueta "matriculas"
            writer.writeEndDocument();
            writer.close();
        } catch (Exception e) {
            error = e.getMessage();// Captura cualquier excepción y guarda el mensaje de error
        } finally {
            return error;// Devuelve la cadena de error, estará vacía si no hubo errores, o contendrá un mensaje de error en caso contrario
        }

    }
}
