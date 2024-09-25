package Model.fileManager;

//
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import Model.Pelicula;

public class PeliculasHandler extends DefaultHandler {
	private HashMap<Integer, Pelicula> peliculas = new HashMap<>();
	private Pelicula pelicula;
	private StringBuilder buffer = new StringBuilder();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case "pelicula":
			// Crear una nueva instancia de Pelicula cada vez que empieza un nuevo elemento
			// "pelicula"
			pelicula = new Pelicula(0, "", "", 0, "");
			break;

		case "id":
		case "titulo":
		case "director":
		case "anio":
		case "genero":
			// Limpiar el buffer para capturar el contenido del texto
			buffer.delete(0, buffer.length());
			break;
		}
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "id":
			// Asignar el valor del ID
			pelicula.setId(Integer.parseInt(buffer.toString().trim()));
			break;

		case "titulo":
			// Asignar el valor del título
			pelicula.setTitulo(buffer.toString().trim());
			break;

		case "director":
			// Asignar el valor del director
			pelicula.setDirector(buffer.toString().trim());
			break;

		case "anio":
			// Asignar el valor del año
			pelicula.setAnio(Integer.parseInt(buffer.toString().trim()));
			break;

		case "genero":
			// Asignar el valor del género
			pelicula.setGenero(buffer.toString().trim());
			break;

		case "pelicula":
			// Al finalizar un elemento "pelicula", añadirlo al HashMap usando el id como
			// clave
			peliculas.put(pelicula.getId(), pelicula);
			break;
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// Añadir el contenido del texto al buffer
		buffer.append(ch, start, length);
		super.characters(ch, start, length);
	}

	public HashMap<Integer, Pelicula> getPeliculas() {
		return peliculas;
	}
}
