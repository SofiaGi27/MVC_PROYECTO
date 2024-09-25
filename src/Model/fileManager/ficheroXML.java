package Model.fileManager;

import Model.IAccesoDatos;
import Model.Pelicula;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ficheroXML implements IAccesoDatos {
	private String rutaFichero;

	public ficheroXML(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse("Ficheros/peliculas.xml");
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("pelicula");

			if (nList.getLength() == 0) {
				System.out.println("No hay películas disponibles.");
				return peliculas; // Retorna un HashMap vacío
			}

			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
					String titulo = element.getElementsByTagName("titulo").item(0).getTextContent();
					String director = element.getElementsByTagName("director").item(0).getTextContent();
					int anio = Integer.parseInt(element.getElementsByTagName("anio").item(0).getTextContent());
					String genero = element.getElementsByTagName("genero").item(0).getTextContent();

					Pelicula pelicula = new Pelicula(id, titulo, director, anio, genero);
					peliculas.put(id, pelicula);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("El archivo no se encontró: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peliculas;
	}

	@Override
	public void añadir(Pelicula pelicula) {
		// Implementar la lógica para añadir película al XML
	}

	@Override
	public void modificar(Pelicula pelicula) {
		// Implementar la lógica para modificar película en el XML
	}

	@Override
	public void borrar(int id) {
		// Implementar la lógica para borrar película en el XML
	}

	@Override
	public void escribirTodos(HashMap<Integer, Pelicula> lista) {
		// TODO Auto-generated method stub

	}
}
