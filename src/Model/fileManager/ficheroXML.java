package Model.fileManager;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Model.IAccesoDatos;
import Model.Pelicula;

public class ficheroXML implements IAccesoDatos {

	private File xmlFile;

	public ficheroXML(String filePath) {
		this.xmlFile = new File(filePath);
	}

	/* Método para añadir una nueva película al archivo XML
	 *  Se usa SAXBuilder para leer el XML
	 */
	@Override
	public void añadir(Pelicula pelicula) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(xmlFile);
			Element rootElement = document.getRootElement();

			// Crear el nuevo elemento "pelicula"
			Element peliculaElement = new Element("pelicula");
			peliculaElement.addContent(new Element("id").setText(String.valueOf(pelicula.getId())));
			peliculaElement.addContent(new Element("titulo").setText(pelicula.getTitulo()));
			peliculaElement.addContent(new Element("director").setText(pelicula.getDirector()));
			peliculaElement.addContent(new Element("anio").setText(String.valueOf(pelicula.getAnio())));
			peliculaElement.addContent(new Element("genero").setText(pelicula.getGenero()));

			// Añadir la nueva película al root
			rootElement.addContent(peliculaElement);

			// Guardar el documento modificado
			guardarDocumento(document);

			System.out.println("Película añadida: " + pelicula.getTitulo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 // Método para modificar una película en el archivo XML
	@Override
	public void modificar(Pelicula pelicula) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(xmlFile);
			Element rootElement = document.getRootElement();

			List<Element> peliculas = rootElement.getChildren("pelicula");

			 // Buscar la película por su ID y modificar sus datos
			for (Element peliculaElement : peliculas) {
				int peliculaId = Integer.parseInt(peliculaElement.getChildText("id"));
				if (peliculaId == pelicula.getId()) {
					peliculaElement.getChild("titulo").setText(pelicula.getTitulo());
					peliculaElement.getChild("director").setText(pelicula.getDirector());
					peliculaElement.getChild("anio").setText(String.valueOf(pelicula.getAnio()));
					peliculaElement.getChild("genero").setText(pelicula.getGenero());
					System.out.println("Película actualizada: " + pelicula.getTitulo());
					break;
				}
			}

			// Guardar el documento modificado
			guardarDocumento(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Método para leer todas las películas del archivo XML
	@Override
	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculasMap = new HashMap<>();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(xmlFile);
			Element rootElement = document.getRootElement();

			List<Element> peliculas = rootElement.getChildren("pelicula");
			for (Element peliculaElement : peliculas) {
				int id = Integer.parseInt(peliculaElement.getChildText("id"));
				String titulo = peliculaElement.getChildText("titulo");
				String director = peliculaElement.getChildText("director");
				int anio = Integer.parseInt(peliculaElement.getChildText("anio"));
				String genero = peliculaElement.getChildText("genero");

				Pelicula pelicula = new Pelicula(id, titulo, director, anio, genero);
				peliculasMap.put(id, pelicula);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peliculasMap;
	}

	 // Método para escribir todas las películas al archivo XML
	@Override
	public void escribirTodos(HashMap<Integer, Pelicula> listaPeliculas) {
		try {
			Element rootElement = new Element("peliculas");
			Document document = new Document(rootElement);

			for (Pelicula pelicula : listaPeliculas.values()) {
				Element peliculaElement = new Element("pelicula");

				peliculaElement.addContent(new Element("id").setText(String.valueOf(pelicula.getId())));
				peliculaElement.addContent(new Element("titulo").setText(pelicula.getTitulo()));
				peliculaElement.addContent(new Element("director").setText(pelicula.getDirector()));
				peliculaElement.addContent(new Element("anio").setText(String.valueOf(pelicula.getAnio())));
				peliculaElement.addContent(new Element("genero").setText(pelicula.getGenero()));

				rootElement.addContent(peliculaElement);
			}

			// Guardar el documento modificado
			guardarDocumento(document);

			System.out.println("Películas escritas en el archivo.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    // Método para borrar una película del archivo XML por su ID
	@Override
	public void borrar(int id) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(xmlFile);
			Element rootElement = document.getRootElement();

			List<Element> peliculas = rootElement.getChildren("pelicula");

			for (Element peliculaElement : peliculas) {
				int peliculaId = Integer.parseInt(peliculaElement.getChildText("id"));
				if (peliculaId == id) {
					peliculas.remove(peliculaElement); // Elimina la película
					System.out.println("Película eliminada: ID " + id);
					break;
				}
			}

			// Guardar el documento modificado
			guardarDocumento(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Método para guardar los cambios en el archivo XML
	private void guardarDocumento(Document document) {
		try {
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			xmlOutputter.output(document, new FileOutputStream(xmlFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Buscar la película por su ID y devolverla si se encuentra
	@Override
	public Pelicula buscar(int id) {
	    try {
	        SAXBuilder saxBuilder = new SAXBuilder();
	        Document document = saxBuilder.build(xmlFile);
	        Element rootElement = document.getRootElement();

	        List<Element> peliculas = rootElement.getChildren("pelicula");

	        for (Element peliculaElement : peliculas) {
	            int peliculaId = Integer.parseInt(peliculaElement.getChildText("id"));
	            if (peliculaId == id) {
	                String titulo = peliculaElement.getChildText("titulo");
	                String director = peliculaElement.getChildText("director");
	                int anio = Integer.parseInt(peliculaElement.getChildText("anio"));
	                String genero = peliculaElement.getChildText("genero");

	                return new Pelicula(peliculaId, titulo, director, anio, genero);// Devuelve la película encontrada
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Si no se encuentra la película, retorna null
	}
}
