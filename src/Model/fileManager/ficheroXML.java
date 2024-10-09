package Model.fileManager;

import Model.IAccesoDatos;
import Model.Pelicula;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ficheroXML implements IAccesoDatos {
    private String rutaFichero;

    public ficheroXML(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    @Override
    public HashMap<Integer, Pelicula> leerTodos() {
        // Crear un HashMap para almacenar las películas
        HashMap<Integer, Pelicula> peliculas = new HashMap<>();

        try {
            // Crear una fábrica para el parser de SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // Crear un objeto SAXParser
            SAXParser saxParser = factory.newSAXParser();

            // Crear una instancia de tu handler
            PeliculasHandler handler = new PeliculasHandler();

            // Usar el parser para analizar el archivo XML y pasar el control al handler
            saxParser.parse(rutaFichero, handler);

            // Obtener las películas procesadas por el handler
            peliculas = handler.getPeliculas();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retornar el HashMap con las películas
        return peliculas;
    }

    // Métodos añadir, modificar, borrar y escribirTodos aquí...
    
    @Override
    public void añadir(Pelicula pelicula) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            File archivo = new File(rutaFichero);
            if (archivo.exists()) {
                doc = dBuilder.parse(archivo);
            } else {
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("peliculas");
                doc.appendChild(rootElement);
            }

            Element peliculaElement = doc.createElement("pelicula");
            doc.getDocumentElement().appendChild(peliculaElement);

            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(String.valueOf(pelicula.getId())));
            peliculaElement.appendChild(idElement);

            Element tituloElement = doc.createElement("titulo");
            tituloElement.appendChild(doc.createTextNode(pelicula.getTitulo()));
            peliculaElement.appendChild(tituloElement);

            Element directorElement = doc.createElement("director");
            directorElement.appendChild(doc.createTextNode(pelicula.getDirector()));
            peliculaElement.appendChild(directorElement);

            Element anioElement = doc.createElement("anio");
            anioElement.appendChild(doc.createTextNode(String.valueOf(pelicula.getAnio())));
            peliculaElement.appendChild(anioElement);

            Element generoElement = doc.createElement("genero");
            generoElement.appendChild(doc.createTextNode(pelicula.getGenero()));
            peliculaElement.appendChild(generoElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);
            transformer.transform(source, result);

            System.out.println("Película añadida con éxito: " + pelicula.getTitulo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificar(Pelicula pelicula) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(rutaFichero);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("pelicula");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());

                    if (id == pelicula.getId()) {
                        element.getElementsByTagName("titulo").item(0).setTextContent(pelicula.getTitulo());
                        element.getElementsByTagName("director").item(0).setTextContent(pelicula.getDirector());
                        element.getElementsByTagName("anio").item(0).setTextContent(String.valueOf(pelicula.getAnio()));
                        element.getElementsByTagName("genero").item(0).setTextContent(pelicula.getGenero());

                        System.out.println("Película modificada con éxito: " + pelicula.getTitulo());
                        break;
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(rutaFichero);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void borrar(int id) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(rutaFichero);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("pelicula");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int peliculaId = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());

                    if (peliculaId == id) {
                        element.getParentNode().removeChild(element);
                        System.out.println("Película eliminada con éxito: " + peliculaId);
                        break;
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(rutaFichero);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void escribirTodos(HashMap<Integer, Pelicula> lista) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("peliculas");
            doc.appendChild(rootElement);

            for (Pelicula pelicula : lista.values()) {
                Element peliculaElement = doc.createElement("pelicula");

                Element idElement = doc.createElement("id");
                idElement.appendChild(doc.createTextNode(String.valueOf(pelicula.getId())));
                peliculaElement.appendChild(idElement);

                Element tituloElement = doc.createElement("titulo");
                tituloElement.appendChild(doc.createTextNode(pelicula.getTitulo()));
                peliculaElement.appendChild(tituloElement);

                Element directorElement = doc.createElement("director");
                directorElement.appendChild(doc.createTextNode(pelicula.getDirector()));
                peliculaElement.appendChild(directorElement);

                Element anioElement = doc.createElement("anio");
                anioElement.appendChild(doc.createTextNode(String.valueOf(pelicula.getAnio())));
                peliculaElement.appendChild(anioElement);

                Element generoElement = doc.createElement("genero");
                generoElement.appendChild(doc.createTextNode(pelicula.getGenero()));
                peliculaElement.appendChild(generoElement);

                rootElement.appendChild(peliculaElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(rutaFichero);
            transformer.transform(source, result);
            System.out.println("Todas las películas han sido escritas en el archivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
